package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.darktree.stylishoccult.loot.LootManager;
import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ExperienceEntry extends AbstractEntry {

	private final float chance;
	private final int amountMin;
	private final int amountMax;

	public ExperienceEntry(float chance, int amountMin, int amountMax) {
		this.chance = chance;
		this.amountMin = amountMin;
		this.amountMax = amountMax;
	}

	protected void dropExperience(World world, BlockPos pos, int size) {
		if (world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
			while(size > 0) {
				int i = ExperienceOrbEntity.roundToOrbSize(size);
				size -= i;
				world.spawnEntity(new ExperienceOrbEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, i));
			}
		}
	}

	@Override
	public List<ItemStack> getLoot(Random random, LootContext context) {
		if (RandUtils.nextBool(this.chance, random)) {
			int size = random.nextInt((amountMax - amountMin) + 1) + amountMin;
			dropExperience(context.getWorld(), context.getPos(), size);
		}

		return LootManager.getEmpty();
	}

}
