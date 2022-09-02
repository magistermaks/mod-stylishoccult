package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.entity.rune.RuneBlockAttachment;
import net.darktree.stylishoccult.block.rune.TimedRuneBlock;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

	protected Direction getTargetDirection(Script script) {
		return script.direction;
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		RuneBlockAttachment attachment = getEntity(world, pos).getAttachment();
		int time = (int) script.pull(world, pos).value();

		if (attachment.getNbt() == null) {
			attachment.setScript(script.drops(false));

			NbtCompound nbt = new NbtCompound();
			nbt.putInt("time", time);
			attachment.setNbt(nbt);
		}
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, BlockState state, Script script, RuneBlockAttachment attachment) {
		return Directions.NONE;
	}

	@Override
	protected void onDelayEnd(World world, BlockPos pos) {
		NbtCompound nbt = getEntity(world, pos).getAttachment().getNbt();

		if (nbt != null) {
			setTimeout(world, pos, nbt.getInt("time"));
		}
	}

	@Override
	protected void onTimeoutEnd(World world, BlockPos pos) {
		RuneBlockAttachment attachment = getEntity(world, pos).getAttachment();
		Script script = attachment.getScript().drops(true);
		propagateTo(world, pos, script, Directions.of(getTargetDirection(script)), null);
		attachment.clear();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(TimedRuneBlock.WAITING)) {

			for (Direction direction : Directions.ALL) {
				BlockPos target = pos.offset(direction);

				if (!world.getBlockState(target).isOpaqueFullCube(world, target)){
					Direction.Axis axis = direction.getAxis();
					double x = axis == Direction.Axis.X ? 0.5 + 0.5625 * direction.getOffsetX() : random.nextFloat();
					double y = axis == Direction.Axis.Y ? 0.5 + 0.5625 * direction.getOffsetY() : random.nextFloat();
					double z = axis == Direction.Axis.Z ? 0.5 + 0.5625 * direction.getOffsetZ() : random.nextFloat();

					world.addParticle(DustParticleEffect.DEFAULT, pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0, 0, 0);
				}
			}
		}
	}

	@Override
	protected void onRuneBroken(World world, BlockPos pos, RuneBlockAttachment attachment) {
		if (attachment.getScript() != null) {
			attachment.getScript().drops(true).reset(world, pos);
			attachment.clear();
		}
	}

}

