package net.darktree.stylishoccult.entity.goal;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.entity.SporeEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class FollowLanternGoal extends Goal {

	private final SporeEntity spore;
	private final World world;

	private Vec3d target;

	public FollowLanternGoal(SporeEntity spore) {
		this.spore = spore;
		this.world = spore.world;
	}

	@Override
	public boolean canStart() {
		this.target = findLanternBlock();

		return this.target != null;
	}

	private Vec3d findLanternBlock() {
		Random random = this.spore.getRandom();
		BlockPos pos = this.spore.getBlockPos();

		for (int i = 0; i < 10; ++i) {
			BlockPos target = pos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

			if (this.world.getBlockState(target).getBlock() == ModBlocks.FIERY_LANTERN) {
				return Vec3d.ofCenter(target);
			}
		}

		return null;
	}

	@Override
	public void start() {
		this.spore.setFixedTarget(target);
	}

	@Override
	public boolean shouldContinue() {
		return true;
	}

}
