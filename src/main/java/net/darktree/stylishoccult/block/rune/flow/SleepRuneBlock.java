package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.entity.rune.RuneBlockEntity;
import net.darktree.stylishoccult.block.rune.TimedRuneBlock;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class SleepRuneBlock extends TimedRuneBlock {

	public SleepRuneBlock(String name) {
		super(RuneType.TRANSFER, name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		RuneBlockEntity entity = getEntity(world, pos);
		int time = (int) script.pull(world, pos).value();

		if (!entity.hasMeta()) {
			NbtCompound nbt = new NbtCompound();
			script.writeNbt(nbt);
			script.stack.reset(element -> {});
			script.ring.reset(element -> {});
			nbt.putInt("time", time);
			entity.setMeta(nbt);
		}
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, Script script) {
		return Directions.NONE;
	}

	@Override
	protected void onDelayEnd(World world, BlockPos pos) {
		RuneBlockEntity entity = getEntity(world, pos);

		if (entity.hasMeta()) {
			setTimeout(world, pos, entity.getMeta().getInt("time"));
		}
	}

	@Override
	protected void onTimeoutEnd(World world, BlockPos pos) {
		RuneBlockEntity entity = getEntity(world, pos);
		Script script = Script.fromNbt(entity.getMeta());
		entity.setMeta(null);
		propagateTo(world, pos, script, Directions.of(script.direction));
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(TimedRuneBlock.WAITING)) {

			for (Direction direction : Directions.ALL) {
				BlockPos target = pos.offset(direction);

				if (!world.getBlockState(target).isOpaqueFullCube(world, target)){
					Direction.Axis axis = direction.getAxis();
					double e = axis == Direction.Axis.X ? 0.5 + 0.5625 * (double) direction.getOffsetX() : (double) random.nextFloat();
					double f = axis == Direction.Axis.Y ? 0.5 + 0.5625 * (double) direction.getOffsetY() : (double) random.nextFloat();
					double g = axis == Direction.Axis.Z ? 0.5 + 0.5625 * (double) direction.getOffsetZ() : (double) random.nextFloat();

					world.addParticle(DustParticleEffect.DEFAULT, (double) pos.getX() + e, (double) pos.getY() + f, (double) pos.getZ() + g, 0.0, 0.0, 0.0);
				}
			}
		}
	}
}

