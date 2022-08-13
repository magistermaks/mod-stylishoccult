package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.advancement.Criteria;
import net.darktree.stylishoccult.block.rune.ActorRuneBlock;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
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

		ItemElement element = script.stack.pull().cast(ItemElement.class);
		BlockPos target = pos.add(x, y, z);
		boolean placed = false;

		if (!target.isWithinDistance(pos, range)) {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT);
		}

		if (element.stack.getItem() instanceof BlockItem blockItem) {
			try {
				placed = blockItem.place(new AutomaticItemPlacementContext(world, target, Direction.UP, element.stack, Direction.UP)).isAccepted();

				// make sure not to lose any items, even when operation fails
				if (!placed) {
					script.ring.push(element, world, pos);
				}
			} catch (Exception e) {
				StylishOccult.LOGGER.warn("Failed to place block for unknown reason!", e);
			}
		} else {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT_TYPE);
		}

		Criteria.TRIGGER.trigger(world, pos, this, placed);
	}

}
