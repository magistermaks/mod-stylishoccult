package net.darktree.stylishoccult.block.entity.altar;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.block.OccultCauldronBlock;
import net.darktree.stylishoccult.block.entity.cauldron.OccultCauldronBlockEntity;
import net.darktree.stylishoccult.block.fluid.ModFluids;
import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AltarRitualState {

	private final List<Item> ingredients = new ArrayList<>();
	private final List<BlockPos> pillars = new ArrayList<>();
	private final List<BlockPos> cauldrons = new ArrayList<>();

	private int pillar = 0;
	private int tick = 0;
	private int blood = 0;

	public void reset() {
		ingredients.clear();
		pillars.clear();
		cauldrons.clear();
		blood = 0;
	}

	public void addIngredient(ItemStack stack) {
		ingredients.add(stack.getItem());
	}

	public List<Item> getIngredients() {
		return ingredients;
	}

	public AltarRitual getRitual(ItemStack catalyst) {
		return ResourceLoaders.ALTAR_RITUALS.find(catalyst.getItem(), ingredients);
	}

	public void addPillar(BlockPos.Mutable pos) {
		pillars.add(pos.toImmutable());
	}

	public void addCauldron(World world, BlockPos.Mutable pos) {
		OccultCauldronBlockEntity cauldron = BlockUtils.getEntity(OccultCauldronBlockEntity.class, world, pos);

		if (cauldron != null) {
			cauldrons.add(pos.toImmutable());
			OccultCauldronBlock.set(world, pos, true, true);
			blood += cauldron.getStorage().getAmount();
		}
	}

	public void begin() {
		Collections.shuffle(pillars);
		Collections.shuffle(cauldrons);
		pillar = 0;
		tick = 20;
	}

	public boolean hasNextPillar() {
		return pillar < pillars.size();
	}

	public BlockPos getNextPillar() {
		if (hasNextPillar()) {
			return pillars.get(pillar ++);
		}

		return null;
	}

	public boolean tick(boolean active) {
		if (active) {
			tick ++;
		}else{
			tick = 0;
		}

		if (tick >= 40) {
			tick = 0;
			return true;
		}

		return false;
	}

	public void writeNbt(NbtCompound nbt) {
		nbt.putInt("pillar", pillar);
		nbt.putInt("tick", tick);
		nbt.putInt("blood", blood);

		NbtList itemList = new NbtList();
		for (Item item : ingredients) {
			itemList.add(NbtString.of(Registry.ITEM.getId(item).toString()));
		}

		NbtList pillarList = new NbtList();
		for (BlockPos pillar : pillars) {
			pillarList.add(NbtLong.of(pillar.asLong()));
		}

		NbtList cauldronList = new NbtList();
		for (BlockPos cauldron : cauldrons) {
			cauldronList.add(NbtLong.of(cauldron.asLong()));
		}

		nbt.put("items", itemList);
		nbt.put("pillars", pillarList);
		nbt.put("cauldrons", cauldronList);
	}

	public void readNbt(NbtCompound nbt) {
		this.reset();

		this.pillar = nbt.getInt("pillar");
		this.tick = nbt.getInt("tick");
		this.blood = nbt.getInt("blood");

		NbtList itemList = nbt.getList("items", NbtElement.STRING_TYPE);
		NbtList pillarList = nbt.getList("pillars", NbtElement.LONG_TYPE);
		NbtList cauldronList = nbt.getList("cauldrons", NbtElement.LONG_TYPE);

		for (NbtElement item : itemList) {
			this.ingredients.add(Registry.ITEM.get(new Identifier(item.asString())));
		}

		for (NbtElement pillar : pillarList) {
			this.pillars.add(BlockPos.fromLong(((NbtLong) pillar).longValue()));
		}

		for (NbtElement cauldron : cauldronList) {
			this.cauldrons.add(BlockPos.fromLong(((NbtLong) cauldron).longValue()));
		}
	}

	public void notifyCauldrons(World world) {
		for (BlockPos pos : cauldrons) {
			OccultCauldronBlock.set(world, pos, false, false);
		}
	}

	public boolean getBlood(World world, int blood) {
		StylishOccult.LOGGER.debug("(Altar) Trying to fetch: " + blood + " units of blood, detected: " + this.blood);

		if (blood <= this.blood) {
			try (Transaction transaction = Transaction.openOuter()) {
				for (BlockPos pos : cauldrons) {
					OccultCauldronBlockEntity cauldron = BlockUtils.getEntity(OccultCauldronBlockEntity.class, world, pos);

					if (cauldron != null) {
						blood -= cauldron.getStorage().extract(ModFluids.BLOOD_VARIANT, blood, transaction);
					}
				}

				StylishOccult.LOGGER.debug("(Altar) Amount missing: " + blood);

				if (blood > 0) {
					transaction.abort();
				} else {
					transaction.commit();
				}
			}
		}

		return blood == 0;
	}

}
