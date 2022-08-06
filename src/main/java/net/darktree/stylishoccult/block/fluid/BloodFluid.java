package net.darktree.stylishoccult.block.fluid;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

/**
 * Copied from Fabric Wiki
 * @see <a href="https://fabricmc.net/wiki/tutorial:fluids">Fluid Tutorial</a>
 */
public abstract class BloodFluid extends BloodFlowableFluid {

	@Override
	public Fluid getStill() {
		return ModFluids.STILL_BLOOD;
	}

	@Override
	public Fluid getFlowing() {
		return ModFluids.FLOWING_BLOOD;
	}

	@Override
	public Item getBucketItem() {
		return ModItems.BLOOD_BUCKET;
	}

	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return ModBlocks.BLOOD.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}

	@Override
	public ParticleEffect getParticle() {
		return Particles.BLOOD_DRIPPING;
	}

	@Override
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		if ((state.isStill() || state.get(FALLING)) && RandUtils.getBool(20f, random)) {
			world.addParticle(Particles.UNDER_BLOOD, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
		}
	}

	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
	}

	public static class Flowing extends BloodFluid {

		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}

	}

	public static class Still extends BloodFluid {

		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}

	}
}