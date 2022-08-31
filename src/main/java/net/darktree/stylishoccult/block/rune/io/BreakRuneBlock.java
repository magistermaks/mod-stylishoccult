package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.entity.rune.RuneBlockAttachment;
import net.darktree.stylishoccult.block.rune.TimedRuneBlock;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

// TODO cleanup
public class BreakRuneBlock extends TimedRuneBlock implements TargetingRune {

	// FIXME :tiny_potato:
	private int breakProgress = 0;
	private int maxProgress = 0;
	private BlockState state = Blocks.AIR.getDefaultState();

	public BreakRuneBlock(String name) {
		super(RuneType.ACTOR, name);
	}

	protected int getMaxProgress() {
		return this.maxProgress;
	}

	@Override
	protected void onDelayEnd(World world, BlockPos pos) {
		onTimeoutEnd(world, pos);
	}

	@Override
	protected void onTimeoutEnd(World world, BlockPos pos) {
		RuneBlockAttachment attachment = getEntity(world, pos).getAttachment();

		if (attachment.getNbt() != null) {
			Script script = attachment.getScript();
			BlockPos target = BlockPos.fromLong(attachment.getNbt().getLong("pos"));

			boolean removed = tickBlockBreak(world, pos, target, script, () -> {
				propagateTo(world, pos, script.drops(true), Directions.of(script.direction), null);
				attachment.clear();
				breakProgress = 0;
			});

			Criteria.TRIGGER.trigger(world, pos, this, removed);
		}
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		BlockPos target = getTarget(script, world, pos);
		BlockState state = world.getBlockState(target);
		RuneBlockAttachment attachment = getEntity(world, pos).getAttachment();

		NbtCompound nbt = new NbtCompound();
		nbt.putLong("pos", target.asLong());
		attachment.setNbt(nbt);
		attachment.setScript(script.drops(false));

		this.maxProgress = (int) (state.getHardness(world, target) * 7);
		this.state = state;
	}

	private boolean tickBlockBreak(World world, BlockPos pos, BlockPos target, Script script, Runnable notifier) {
		BlockState state = world.getBlockState(target);

		if (state != this.state) {
			this.state = state;
			breakProgress = 0;
			notifier.run();
			return false;
		}

		this.breakProgress ++;

		int i = (int) ((float)this.breakProgress / this.getMaxProgress() * 10);

		// FIXME
		world.setBlockBreakingInfo(16492, target, i);

		if (this.breakProgress >= this.getMaxProgress()) {
			return breakBlock(script, world, target, world.getBlockState(target), notifier);
		}

		setTimeout(world, pos, 1);
		return false;
	}

	private boolean breakBlock(Script script, World world, BlockPos pos, BlockState state, Runnable notifier) {
		if (state.isAir()) {
			return false;
		}

		if (!(state.getBlock() instanceof AbstractFireBlock)) {
			world.syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
		}

		if (world instanceof ServerWorld server) {
			BlockEntity entity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
			getDroppedStacks(state, server, pos, entity, null, ItemStack.EMPTY).forEach(stack -> script.ring.push(new ItemElement(stack), world, pos));
			state.onStacksDropped(server, pos, ItemStack.EMPTY);
			notifier.run();
		}

		boolean replaced = world.setBlockState(pos, world.getFluidState(pos).getBlockState(), 3, 512);

		if (replaced) {
			world.emitGameEvent(null, GameEvent.BLOCK_DESTROY, pos);
		}

		return replaced;
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, BlockState state, Script script, RuneBlockAttachment attachment) {
		return Directions.NONE;
	}
}
