package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.parts.AltarRingItemStack;
import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.sounds.SoundManager;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.SimpleBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AltarPlateBlockEntity extends SimpleBlockEntity {

	List<Item> ingredients = new ArrayList<>();
	List<BlockPos> pillars = new ArrayList<>();
	boolean active = false;
	int pillar = 0;
	int ritual = 0;

	List<AltarRingItemStack> candles = new ArrayList<>();
	ItemStack center = ItemStack.EMPTY;

	public AltarPlateBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntities.ALTAR_PLATE, pos, state);
	}

	public boolean use(ItemStack stack) {
		if (world != null && stack.isEmpty()) {
			if (!center.isEmpty()) {
				world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.BLOCKS, 1, 1 + world.random.nextFloat()/2);
				Block.dropStack(world, pos, center);
				center = ItemStack.EMPTY;
				markDirty();
				return true;
			}

			if (!candles.isEmpty()) {
				ItemStack taken = candles.remove(0).stack;
				Block.dropStack(world, pos, taken);
				world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_BREAK, SoundCategory.BLOCKS, 1, 1 + world.random.nextFloat()/2);
				markDirty();
				return true;
			}

			return false;
		}

		if (stack.isIn(ItemTags.CANDLES)) {
			candles.add(AltarRingItemStack.create(stack.split(1)));
			world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_PLACE, SoundCategory.BLOCKS, 1, 1 + world.random.nextFloat()/2);
			markDirty();
			return true;
		}

		if (center.isEmpty()) {
			center = stack.split(1);
			world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE, SoundCategory.BLOCKS, 1, 1 + world.random.nextFloat()/2);

			System.out.println("[ALTAR] Checking...");
			if (candles.size() > 0) {
				onCatalystPlaced();
			}

			markDirty();
			return true;
		}

		return false;
	}

	/**
	 * Called when the catalyst is placed,
	 * The ritual will now commence!
	 */
	private void onCatalystPlaced() {

		if (world.isClient) {
			return;
		}

		ingredients.clear();
		pillars.clear();

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

	}

	public void tick() {

		if (world.isClient) {
			return;
		}

		if (active) {
			ritual ++;

			if (ritual >= 40) {
				if (pillar >= pillars.size()) {
					AltarRitual ritual = ResourceLoaders.ALTAR_RITUALS.find(center.getItem(), ingredients);

					if (ritual == null) {
						System.out.println("[ALTAR] No ritual found!");
					}else {
						Block.dropStack(world, pos, new ItemStack(ritual.product, ritual.count));
					}

					active = false;
				}else{
					BlockPos pos = pillars.get(pillar ++);
					System.out.println("[ALTAR] Targeting pillar: " + BlockUtils.posToString(pos));
					AltarPlateBlockEntity plate = BlockUtils.getEntity(AltarPlateBlockEntity.class, world, pos);

					if (plate != null) {
						ingredients.add(plate.center.getItem());
						plate.center = ItemStack.EMPTY;
						plate.sync();

						Network.ARC.send(pos, (ServerWorld) world,
								pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5,
								this.pos.getX() + 0.5, this.pos.getY() + 0.1, this.pos.getZ() + 0.5
						);

						SoundManager.playSound(world, pos, "spark");
					}
				}

				ritual = 0;
			}

		}else{
			ritual = 0;
		}

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
