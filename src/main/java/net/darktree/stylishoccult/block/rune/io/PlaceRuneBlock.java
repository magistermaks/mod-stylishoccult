package net.darktree.stylishoccult.block.rune.io;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.advancement.ModCriteria;
import net.darktree.stylishoccult.block.rune.ActorRuneBlock;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.element.ItemElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PlaceRuneBlock extends ActorRuneBlock implements TargetingRune {

	public PlaceRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		ItemElement element = script.stack.pull().cast(ItemElement.class);

		BlockPos target = getTarget(script, world, pos);
		boolean successful = false;

		ItemStack stack = element.stack;
		script.ring.push(element, world, pos);

		if (stack.getCount() > 0) {
			try {
				AutomaticItemPlacementContext context = new AutomaticItemPlacementContext(world, target, Direction.UP, stack, Directions.UNDECIDED);
				successful = stack.useOnBlock(context).isAccepted();
			} catch (Throwable throwable) {
				// Food BlockItem's throw NPE on failed placement if the context is automatic
				if (!(stack.isFood() && stack.getItem() instanceof BlockItem)) {
					StylishOccult.LOGGER.warn("Item usage operation interrupted for reasons unknown for item: " + stack.getItem().toString());

					if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
						StylishOccult.LOGGER.error(throwable);
					}
				}
			}
		} else {
			throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT);
		}

		ModCriteria.TRIGGER.trigger(world, pos, this, successful);
	}

}
