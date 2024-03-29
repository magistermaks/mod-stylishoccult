package net.darktree.stylishoccult.block;

import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ArcaneAshBlock extends SimpleBlock {

	public static final IntProperty AGE = Properties.AGE_3;
	public static final BooleanProperty PERSISTENT = Properties.PERSISTENT;

	public ArcaneAshBlock(float hardness, Settings settings) {
		super(settings.ticksRandomly().strength(hardness, hardness));
		setDefaultState(getDefaultState().with(PERSISTENT, true).with(AGE, 0));
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);
		if (!world.isClient) {
			Network.ASH.send(pos, (ServerWorld) world);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, PERSISTENT);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getDefaultState().with(PERSISTENT, !(ctx instanceof AutomaticItemPlacementContext || ctx.getPlayer() == null));
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!state.get(PERSISTENT)) {
			world.getBlockTickScheduler().schedule(pos, this, 120 + world.random.nextInt(40));

			if (!world.isClient) {
				Network.ASH.send(pos, (ServerWorld) world);
			}
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int age = state.get(AGE);

		if (age == 3) {
			Network.ASH.send( pos, world );
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), this.soundGroup.getBreakSound(), SoundCategory.BLOCKS, 0.8f, 1.0f);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		} else {
			world.setBlockState(pos, state.with(AGE, age + 1));
			// no need to schedule an update here, onBlockAdded is called every time the state changes
		}
	}

	@Override
	public LootTable getDefaultLootTable() {
		return LootTables.ASH;
	}

}
