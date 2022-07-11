package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.parts.AltarRingItemStack;
import net.darktree.stylishoccult.blocks.runes.VerticalRuneLink;
import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.script.elements.ItemElement;
import net.darktree.stylishoccult.sounds.SoundManager;
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
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AltarPlateBlockEntity extends SimpleBlockEntity {

	private final Box box;

	List<Item> ingredients = new ArrayList<>();
	List<BlockPos> pillars = new ArrayList<>();
	boolean active = false;
	int pillar = 0;
	int ritual = 0;
	int pickup = 0;

	List<AltarRingItemStack> candles = new ArrayList<>();
	ItemStack center = ItemStack.EMPTY;

	public AltarPlateBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.ALTAR_PLATE, pos, state);
		this.box = Utils.box(2, 2, 2, 14, 3, 14).offset(pos);
	}

	public boolean use(ItemStack stack) {
		if (stack.getItem() == ModItems.OCCULT_STAFF) {
			activate();
			return true;
		}

		if (world != null && stack.isEmpty()) {
			if (!center.isEmpty()) {
				playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_HIT);
				pickup = 40;
				Block.dropStack(world, pos, center);
				center = ItemStack.EMPTY;
				markDirty();
				return true;
			}

			if (!candles.isEmpty()) {
				ItemStack taken = candles.remove(0).stack;
				pickup = 40;
				Block.dropStack(world, pos, taken);
				playSound(SoundEvents.BLOCK_CANDLE_BREAK);
				markDirty();
				return true;
			}

			return false;
		}

		if (stack.isIn(ItemTags.CANDLES)) {
			candles.add(AltarRingItemStack.create(stack.split(1)));
			playSound(SoundEvents.BLOCK_CANDLE_PLACE);
			markDirty();
			return true;
		}

		if (center.isEmpty()) {
			center = stack.split(1);
			playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE);

			markDirty();
			return true;
		}

		return false;
	}

	/**
	 * Called when the altar is activated,
	 * The ritual will now commence!
	 */
	public boolean activate() {

		if (active || world == null || world.isClient || candles.size() < 3 || this.center.isEmpty()) {
			return false;
		}

		ingredients.clear();
		pillars.clear();
		SoundManager.playSound(world, pos, "boom");

		BlockPos.Mutable pos = this.pos.mutableCopy();
		System.out.println("[ALTAR] Catalyst placed at: " + BlockUtils.posToString(pos));

		for (int x = -7; x <= 7; x ++) {
			for (int z = -7; z <= 7; z ++) {
				if (x == 0 && z == 0) continue;

				pos.setX(this.pos.getX() + x);
				pos.setZ(this.pos.getZ() + z);

				if (this.world.getBlockState(pos).getBlock() == ModBlocks.ALTAR_PLATE) {
					AltarPlateBlockEntity plate = BlockUtils.getEntity(AltarPlateBlockEntity.class, world, pos);

					if (plate != null) {
						pillars.add(new BlockPos(pos));
						System.out.println("[ALTAR] Found altar pillar at: " + BlockUtils.posToString(pos));
					}
				}
			}
		}

		Collections.shuffle(pillars);
		active = true;
		pillar = 0;
		ritual = 20;

		return true;

	}

	public void tick() {

		if (world.isClient) {
			return;
		}

		if (pickup <= 0) {
			tryPickupItems();
		}else{
			pickup --;
		}

		if (active) {
			ritual ++;

			world.getServer().getPlayerManager().sendToAround(null, pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, 128, world.getRegistryKey(), new ParticleS2CPacket(ParticleTypes.SMOKE, false, this.pos.getX() + 0.45, this.pos.getY() + 0.1, this.pos.getZ() + 0.45, 0.1f, 0, 0.1f, 0.01f, 1));

			if (ritual >= 40) {
				if (pillar >= pillars.size()) {
					AltarRitual ritual = ResourceLoaders.ALTAR_RITUALS.find(center.getItem(), ingredients);

					if (ritual == null) {
						System.out.println("[ALTAR] No ritual found!");
					}else {
						if (world.getBlockState(pos).getBlock() instanceof VerticalRuneLink pillar) {
							ItemStack products = new ItemStack(ritual.product, ritual.count);

							if (!pillar.sendDown(world, pos, new ItemElement(products))) {
								Block.dropStack(world, pos, products);
							}

							SoundManager.playSound(world, pos, "transmute");
						}
					}

					active = false;
				}else{
					boolean item = false;

					while (!item && pillar < pillars.size()) {
						item = tryStealItem();
					}
				}

				ritual = 0;
			}

		}else{
			ritual = 0;
		}

	}

	private void tryPickupItems() {
		if (this.center.isEmpty() && world.getTime() % 4 == 0) {
			List<ItemEntity> entities = world.getEntitiesByType(EntityType.ITEM, this.box, item -> true);

			for (ItemEntity item : entities) {
				ItemStack stack = item.getStack();

				if (stack.getCount() >= 0) {
					this.center = stack.split(1);

					markDirty();
					sync();
					break;
				}
			}
		}
	}

	private boolean tryStealItem() {
		BlockPos pos = pillars.get(pillar ++);
		AltarPlateBlockEntity plate = BlockUtils.getEntity(AltarPlateBlockEntity.class, world, pos);

		if (plate != null) {
			ItemStack stack = plate.center;

			if (!stack.isEmpty()) {
				ingredients.add(stack.getItem());
				plate.center = ItemStack.EMPTY;
				plate.sync();
				plate.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_HIT);

				Network.ARC.send(pos, (ServerWorld) world,
						pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5,
						this.pos.getX() + 0.5, this.pos.getY() + 0.1, this.pos.getZ() + 0.5
				);

				SoundManager.playSound(world, pos, "arc");
				return true;
			}
		}

		return false;
	}

	private void playSound(SoundEvent event) {
		world.playSound(null, pos, event, SoundCategory.BLOCKS, 1, 1 + world.random.nextFloat()/2);
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("c", this.center.writeNbt(new NbtCompound()));
		nbt.putInt("l", this.candles.size());

		for (int i = 0; i < this.candles.size(); i ++) {
			nbt.put("i" + i, this.candles.get(i).writeNbt(new NbtCompound()));
		}

		return super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		try {
			candles.clear();
			this.center = ItemStack.fromNbt(nbt.getCompound("c"));

			int l = nbt.getInt("l");

			for (int i = 0; i < l; i ++) {
				candles.add(AltarRingItemStack.fromNbt(nbt.getCompound("i" + i)));
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		super.readNbt(nbt);
	}

	public ItemStack getCenter() {
		return center;
	}

	public List<AltarRingItemStack> getCandles() {
		return candles;
	}

}
