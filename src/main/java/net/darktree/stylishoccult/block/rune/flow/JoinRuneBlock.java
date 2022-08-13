package net.darktree.stylishoccult.block.rune.flow;

import net.darktree.stylishoccult.block.entity.rune.RuneBlockEntity;
import net.darktree.stylishoccult.block.rune.DirectionalRuneBlock;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.script.engine.Stack;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class JoinRuneBlock extends DirectionalRuneBlock {

	public JoinRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		RuneBlockEntity entity = getEntity(world, pos);

		if (entity.hasMeta()) {
			Stack stack = new Stack(32);
			stack.readNbt(entity.getMeta());
			stack.reset(script.stack::push);
			entity.setMeta(null);
		} else {
			entity.setMeta(script.stack.writeNbt(new NbtCompound()));
			script.stack.reset(element -> {});
		}
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, Script script) {
		return getEntity(world, pos).hasMeta() ? Directions.NONE : Directions.of(getFacing(world, pos));
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.isOf(newState.getBlock())) {
			return;
		}

		RuneBlockEntity entity = getEntity(world, pos);

		if (entity.hasMeta()) {
			Stack stack = new Stack(32);
			stack.readNbt(entity.getMeta());
			stack.reset(element -> element.drop(world, pos));
			entity.setMeta(null);
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

}
