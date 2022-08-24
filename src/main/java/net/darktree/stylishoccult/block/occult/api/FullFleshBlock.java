package net.darktree.stylishoccult.block.occult.api;

import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class FullFleshBlock extends SimpleBlock {

	public FullFleshBlock(Settings settings) {
		super(settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(40) == 0) {
			double d = random.nextDouble();
			double f = random.nextDouble();
			world.addParticle(Particles.BLOOD_DRIPPING, pos.getX() + d, pos.getY() - 0.1, pos.getZ() + f, 0.0, 0.0, 0.0);
		}
	}

}
