package net.darktree.stylishoccult.block.rune.flow;

import com.google.common.collect.Lists;
import net.darktree.stylishoccult.block.entity.rune.RuneBlockAttachment;
import net.darktree.stylishoccult.block.rune.DirectionalRuneBlock;
import net.darktree.stylishoccult.script.element.StackElement;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Directions;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class JoinRuneBlock extends DirectionalRuneBlock {

	public JoinRuneBlock(String name) {
		super(name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		RuneBlockAttachment attachment = getEntity(world, pos).getAttachment();

		if (attachment.getScript() == null) {
			attachment.setScript(script.drops(false));
			attachment.getScript().ring.reset(element -> element.drop(world, pos));
		} else {
			List<StackElement> elements = new ArrayList<>();
			attachment.getScript().stack.reset(elements::add);

			for (StackElement element : Lists.reverse(elements)) {
				script.stack.push(element);
			}

			attachment.setScript(null);
		}
	}

	@Override
	public Direction[] getDirections(World world, BlockPos pos, BlockState state, Script script, RuneBlockAttachment attachment) {
		return attachment.getScript() != null ? Directions.NONE : Directions.of(getFacing(state));
	}

	@Override
	protected void onRuneBroken(World world, BlockPos pos, RuneBlockAttachment attachment) {
		if (attachment.getScript() != null) {
			attachment.getScript().stack.reset(element -> element.drop(world, pos));
			attachment.clear();
		}
	}

}
