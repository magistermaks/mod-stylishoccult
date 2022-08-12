package net.darktree.stylishoccult.block.entity.altar;

import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.entity.BlockEntities;
import net.darktree.stylishoccult.block.rune.VerticalRuneLink;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AltarPlateBlockEntity extends SimpleBlockEntity {

	private final Box box;
	private final AltarRitualState state = new AltarRitualState();
	private final List<AltarRingItemStack> candles = new ArrayList<>();

	private boolean active = false;
	private int pickup = 0;
	private ItemStack catalyst = ItemStack.EMPTY;

	public AltarPlateBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.ALTAR_PLATE, pos, state);
		this.box = Utils.box(2, 2, 2, 14, 3, 14).offset(pos);
	}

	public boolean use(ItemStack stack, Random random) {
		if (stack.getItem() == ModItems.OCCULT_STAFF) {
			activate();
			return true;
		}

		if (world != null && stack.isEmpty()) {
			if (!catalyst.isEmpty()) {
				playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_HIT);
				pickup = 40;
				Block.dropStack(world, pos, catalyst);
				catalyst = ItemStack.EMPTY;
				update();
				return true;
			}

			if (!candles.isEmpty()) {
				ItemStack taken = candles.remove(0).stack;
				pickup = 40;
				Block.dropStack(world, pos, taken);
				playSound(SoundEvents.BLOCK_CANDLE_BREAK);
				update();
				return true;
			}

			return false;
		}

		if (stack.isIn(ItemTags.CANDLES)) {
			if (candles.size() < 64) {
				candles.add(AltarRingItemStack.create(stack.split(1), random));
				playSound(SoundEvents.BLOCK_CANDLE_PLACE);
				update();
			}

			return true;
		}

		if (catalyst.isEmpty()) {
			catalyst = stack.split(1);
			playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE);
			update();
			return true;
		}

		return false;
	}

	/**
	 * Called when the altar is activated,
	 * The ritual will now commence!
	 */
	public boolean activate() {

		if (active || world == null || world.isClient || candles.size() < 3 || this.catalyst.isEmpty()) {
			return false;
		}

		state.reset();
		Sounds.BOOM.play(world, pos);
		BlockPos.Mutable pos = this.pos.mutableCopy();

		for (int x = -6; x <= 6; x ++) {
			for (int z = -6; z <= 6; z ++) {
				if (x == 0 && z == 0) continue;

				pos.setX(this.pos.getX() + x);
				pos.setY(this.pos.getY());
				pos.setZ(this.pos.getZ() + z);

				if (this.world.getBlockState(pos).getBlock() == ModBlocks.ALTAR_PLATE) {
					AltarPlateBlockEntity plate = BlockUtils.getEntity(AltarPlateBlockEntity.class, world, pos);

					if (plate != null) {
						state.addPillar(pos);
					}
				}

				for (int y = -6; y <= 6; y ++) {
					pos.setY(this.pos.getY() + y);

					if (this.world.getBlockState(pos).getBlock() == ModBlocks.OCCULT_CAULDRON) {
						state.addCauldron(world, pos);
					}
				}
			}
		}

		markDirty();
		state.begin();
		active = true;

		return true;
	}

	public void tick(World world, BlockPos pos, BlockState blockState) {
		if (world == null || world.isClient || blockState.getBlock() != ModBlocks.ALTAR_PLATE) {
			return;
		}

		if (pickup <= 0) {
			tryPickupItems();
		}else{
			pickup --;
		}

		// update counters, returns true when
		// it's time to steal another item
		boolean next = state.tick(active);

		if (active) {
			world.getServer().getPlayerManager().sendToAround(null, pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, 128, world.getRegistryKey(), new ParticleS2CPacket(ParticleTypes.SMOKE, false, this.pos.getX() + 0.45, this.pos.getY() + 0.1, this.pos.getZ() + 0.45, 0.1f, 0, 0.1f, 0.01f, 1));

			if (next) {

				// there are some pillars left to check
				if (state.hasNextPillar()) {
					boolean item = false;

					// keep checking until we find an items or run out of pillars
					while (!item && state.hasNextPillar()) {
						item = tryStealItem();
					}
				} else {

					// get matching recipe
					AltarRitual ritual = state.getRitual(this.catalyst);

					if (ritual == null || !state.getBlood(world, ritual.blood)) {
						ejectItems(state.getIngredients());
						playSound(SoundEvents.ENTITY_GENERIC_EXPLODE);
					} else {
						applyTransmutation(ritual);
					}

					// reset altar state
					state.notifyCauldrons(world);
					state.reset();
					active = false;
					update();
				}
			}
		}
	}

	/**
	 * Finalizes the ritual and fulfils the given altar recipe
	 * by activating a rune or spawning the items in the world
	 */
	private void applyTransmutation(AltarRitual ritual) {
		if (world.getBlockState(pos).getBlock() instanceof VerticalRuneLink pillar) {
			ItemElement products = new ItemElement(new ItemStack(ritual.product, ritual.count));

			if (ritual.consume) {
				this.catalyst.decrement(1);
			}

			if (!pillar.sendDown(world, pos, products)) {
				ejectItems(Collections.nCopies(ritual.count, ritual.product));
			}
		}

		Criteria.RITUAL.trigger(world, pos, new ItemStack(ritual.catalyst), new ItemStack(ritual.product));
		Sounds.TRANSMUTE.play(world, pos);
	}

	/**
	 * Throw the items in a circle around the altar
	 */
	private void ejectItems(List<Item> items) {
		int count = items.size();

		if (count < 3) {
			for(Item item : items) {
				float x = (pos.getX() + 0.5f) + MathHelper.nextFloat(world.random, -0.25f, 0.25f);
				float y = (pos.getY() + 0.5f) + MathHelper.nextFloat(world.random, -0.25f, 0.25f) - EntityType.ITEM.getHeight() / 2.0f;
				float z = (pos.getZ() + 0.5f) + MathHelper.nextFloat(world.random, -0.25f, 0.25f);

				float vx = world.random.nextFloat() * 0.2f - 0.1f;
				float vz = world.random.nextFloat() * 0.2f - 0.1f;

				Utils.ejectStack(world, x, y, z,new ItemStack(item), vx, 0.2f, vz);
			}
		} else {
			float angle = MathHelper.TAU / count;

			for (int i = 0; i < count; i ++) {
				float x = (float) Math.sin(angle * i) * 0.12f;
				float z = (float) Math.cos(angle * i) * 0.12f;

				Utils.ejectStack(world, pos.getX() + 0.5f, pos.getY() + 0.25f, pos.getZ() + 0.5f,new ItemStack(items.get(i)), x, 0.25f, z);
			}
		}
	}

	/**
	 * Try picking up items from the world
	 */
	private void tryPickupItems() {
		if (this.catalyst.isEmpty() && world.getTime() % 4 == 0) {
			List<ItemEntity> entities = world.getEntitiesByType(EntityType.ITEM, this.box, item -> true);

			for (ItemEntity item : entities) {
				ItemStack stack = item.getStack();

				if (stack.getCount() >= 0) {
					this.catalyst = stack.split(1);

					update();
					break;
				}
			}
		}
	}

	/**
	 * Tries stealing one item from connected pillars
	 */
	private boolean tryStealItem() {
		BlockPos pos = state.getNextPillar();
		AltarPlateBlockEntity plate = BlockUtils.getEntity(AltarPlateBlockEntity.class, world, pos);

		if (plate != null) {
			ItemStack stack = plate.catalyst;

			if (!stack.isEmpty()) {
				state.addIngredient(stack);
				plate.catalyst = ItemStack.EMPTY;
				plate.update();

				Network.ARC.send(pos, (ServerWorld) world,
						this.pos.getX() + 0.5, this.pos.getY() + 0.1, this.pos.getZ() + 0.5,
						pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5
				);

				plate.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_HIT);
				Sounds.ARC.play(world, pos);
				Sounds.VOICE.play(world, pos.down(1));
				return true;
			}
		}

		return false;
	}

	private void playSound(SoundEvent event) {
		world.playSound(null, pos, event, SoundCategory.BLOCKS, 1, 1 + world.random.nextFloat() / 2.0f);
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("catalyst", this.catalyst.writeNbt(new NbtCompound()));
		nbt.putInt("pickup", this.pickup);
		nbt.putBoolean("active", this.active);

		if (this.active) {
			NbtCompound stateNbt = new NbtCompound();
			nbt.put("state", stateNbt);

			state.writeNbt(stateNbt);
		}

		NbtList ring = new NbtList();

		for (AltarRingItemStack candle : this.candles) {
			ring.add(candle.writeNbt(new NbtCompound()));
		}

		nbt.put("ring", ring);

		return super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		try {
			candles.clear();
			this.catalyst = ItemStack.fromNbt(nbt.getCompound("catalyst"));
			this.pickup = nbt.getInt("pickup");
			this.active = nbt.getBoolean("active");

			if (this.active) {
				state.readNbt(nbt.getCompound("state"));
			}

			for (NbtElement element : nbt.getList("ring", NbtElement.COMPOUND_TYPE)) {
				candles.add(AltarRingItemStack.fromNbt((NbtCompound) element));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		super.readNbt(nbt);
	}

	public ItemStack getCatalyst() {
		return catalyst;
	}

	public List<AltarRingItemStack> getCandles() {
		return candles;
	}

	public void onBlockRemoved() {
		for (AltarRingItemStack candle : candles) {
			Block.dropStack(world, pos, candle.stack);
		}

		Block.dropStack(world, pos, catalyst);
		ejectItems(state.getIngredients());

		if (active) {
			state.notifyCauldrons(world);
			playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE);
		}
	}

}
