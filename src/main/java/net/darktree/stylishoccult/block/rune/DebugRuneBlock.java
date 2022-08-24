package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRuneBlock extends RuneBlock {

	public DebugRuneBlock( String name ) {
		super(RuneType.ACTOR, name);
	}

	@Override
	public void apply(Script script, World world, BlockPos pos) {
		getEntity(world, pos).getAttachment().setNbt(script.writeNbt(new NbtCompound()));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			NbtCompound script = getEntity(world, pos).getAttachment().getNbt();

			if (script != null) {
				Network.DEBUG.send(player, pos, script);
			} else {
				player.sendMessage(new TranslatableText("tooltip." + StylishOccult.NAMESPACE + ".debug_unavailable"), false);
			}
		}

		return ActionResult.SUCCESS;
	}

}
