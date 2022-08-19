package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.rune.ActorRuneBlock;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PlaceRuneBlock extends ActorRuneBlock {

	private final int range;

	public PlaceRuneBlock(String name, int range) {
		super(name);
		this.range = range;
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		int x = (int) Math.round(script.pull(world, pos).value());
		int y = (int) Math.round(script.pull(world, pos).value());
		int z = (int) Math.round(script.pull(world, pos).value());

		ItemElement element = script.pull(world, pos).cast(ItemElement.class);
		BlockPos target = pos.add(x, y, z);
		boolean successful = false;

		if (!target.isWithinDistance(pos, range)) {
			throw RuneException.of(RuneExceptionType.OUT_OF_RANGE);
		}

		ItemStack stack = element.stack;

		if (stack.getCount() > 0) {
			try {
				AutomaticItemPlacementContext context = new AutomaticItemPlacementContext(world, target, Direction.UP, stack, Direction.UP);
				successful = stack.useOnBlock(context).isAccepted();
			} catch (Exception e) {
				StylishOccult.LOGGER.warn("Item usage operation interrupted for reasons unknown!", e);
			}
		} else {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_TYPE);
		}

		Criteria.TRIGGER.trigger(world, pos, this, successful);
	}

}
