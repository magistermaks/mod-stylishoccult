package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.entity.rune.RuneBlockAttachment;
import net.darktree.stylishoccult.block.rune.TimedRuneBlock;
import net.darktree.stylishoccult.particles.Particles;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Random;
import java.util.function.Consumer;

// TODO cleanup
public class BreakRuneBlock extends TimedRuneBlock implements TargetingRune {

	public BreakRuneBlock(String name) {
		super(RuneType.ACTOR, name);
	}

	@Override
	protected void onDelayEnd(World world, BlockPos pos) {
		onTimeoutEnd(world, pos);
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, BlockState state, Script script, RuneBlockAttachment attachment) {
		return Directions.NONE;
	}

	@Override
	protected void onTimeoutEnd(World world, BlockPos pos) {
		RuneBlockAttachment attachment = getEntity(world, pos).getAttachment();

		if (attachment.getNbt() != null) {
			Script script = attachment.getScript();
			Progress progress = Progress.fromNbt(world, attachment.getNbt());

			boolean completed = tickBlockBreak(world, pos, progress, script, (removed) -> {
				propagateTo(world, pos, script.drops(true), Directions.of(script.direction), null);
				attachment.clear();
				Criteria.TRIGGER.trigger(world, pos, this, removed);
			});

			if (!completed) {
				attachment.setNbt(progress.toNbt());
				spawnWorkParticles(world, pos, world.random);
			}
		}
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		BlockPos target = getTarget(script, world, pos);
		RuneBlockAttachment attachment = getEntity(world, pos).getAttachment();

		Progress progress = new Progress(world, target);
		attachment.setNbt(progress.toNbt());
		attachment.setScript(script.drops(false));
	}

	private boolean tickBlockBreak(World world, BlockPos pos, Progress progress, Script script, Consumer<Boolean> callback) {
		progress.progress ++;

		int i = (int) ((float)progress.progress / progress.hardness * 10);

		world.setBlockBreakingInfo((int) pos.asLong(), progress.pos, i);

		if (progress.progress >= progress.hardness) {
			breakBlock(script, world, progress.pos, progress.state, callback);
			return true;
		}

		setTimeout(world, pos, 1);
		return false;
	}

	private void breakBlock(Script script, World world, BlockPos pos, BlockState state, Consumer<Boolean> callback) {
		if (state.isAir()) {
			callback.accept(false);
			spawnMissParticles(world, pos, world.random);
			return;
		}

		if (!(state.getBlock() instanceof AbstractFireBlock)) {
			world.syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
		}

		if (world instanceof ServerWorld server) {
			BlockEntity entity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
			getDroppedStacks(state, server, pos, entity, null, ItemStack.EMPTY).forEach(stack -> script.ring.push(new ItemElement(stack), world, pos));
			state.onStacksDropped(server, pos, ItemStack.EMPTY);
		}

		boolean replaced = world.setBlockState(pos, world.getFluidState(pos).getBlockState(), 3, 512);

		if (replaced) {
			world.emitGameEvent(null, GameEvent.BLOCK_DESTROY, pos);
		}

		callback.accept(replaced);
	}

	static final class Progress {

		public short progress;
		public short hardness;
		public BlockState state;
		public BlockPos pos;

		public Progress(World world, BlockPos pos) {
			this.progress = 0;
			this.state = world.getBlockState(pos);
			this.hardness = getHardness(world, pos, this.state);
			this.pos = pos;
		}

		public Progress(short progress, float hardness, BlockState state, BlockPos pos) {
			this.progress = progress;
			this.hardness = (short) hardness;
			this.state = state;
			this.pos = pos;
		}

		public static short getHardness(World world, BlockPos pos, BlockState state) {
			return (short) (state.getHardness(world, pos) * 5);
		}

		public static Progress fromNbt(World world, NbtCompound nbt) {
			BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
			short progress = nbt.getShort("p");

			BlockState state = world.getBlockState(pos);
			BlockState expected = Block.getStateFromRawId(nbt.getInt("s"));

			if (state != expected) {
				progress = 0;
			}

			return new Progress(progress, getHardness(world, pos, state), state, pos);
		}

		public NbtCompound toNbt() {
			NbtCompound nbt = new NbtCompound();
			nbt.putLong("pos", pos.asLong());
			nbt.putInt("s", Block.getRawIdFromState(state));
			nbt.putShort("p", progress);

			return nbt;
		}

	}

	private void spawnWorkParticles(World world, BlockPos pos, Random random) {
		Direction direction = RandUtils.pickFromEnum(Direction.class, random);
		BlockPos target = pos.offset(direction);

		if (!world.getBlockState(target).isOpaqueFullCube(world, target)) {
			Direction.Axis axis = direction.getAxis();
			double x = axis == Direction.Axis.X ? 0.5 + 0.5625 * direction.getOffsetX() : random.nextFloat();
			double y = axis == Direction.Axis.Y ? 0.5 + 0.5625 * direction.getOffsetY() : random.nextFloat();
			double z = axis == Direction.Axis.Z ? 0.5 + 0.5625 * direction.getOffsetZ() : random.nextFloat();

			Particles.spawn(world, DustParticleEffect.DEFAULT, pos.getX() + x, pos.getY() + y, pos.getZ() + z, 1);
		}
	}

	private void spawnMissParticles(World world, BlockPos pos, Random random) {
		double x = pos.getX() + 0.5f;
		double y = pos.getY() + 0.5f;
		double z = pos.getZ() + 0.5f;

		Particles.spawn(world, ParticleTypes.SMOKE, x, y, z, 3);
	}

}
