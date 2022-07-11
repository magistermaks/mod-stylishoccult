package net.darktree.stylishoccult.blocks.runes;

import net.darktree.stylishoccult.script.elements.StackElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface VerticalRuneLink {

	default boolean sendDown(World world, BlockPos pos, @Nullable StackElement element) {
		return forward(world, pos, Direction.DOWN, element);
	}

	default boolean sendUp(World world, BlockPos pos, @Nullable StackElement element) {
		return forward(world, pos, Direction.UP, element);
	}

	boolean onEndReached(World world, BlockPos pos, StackElement element);

	private boolean forward(World world, BlockPos pos, Direction direction, StackElement element) {
		BlockPos next = pos.offset(direction);

		if (world.getBlockState(next).getBlock() instanceof VerticalRuneLink pillar) {
			return pillar.forward(world, next, direction, element);
		}else{
			return onEndReached(world, pos, element);
		}
	}

}
