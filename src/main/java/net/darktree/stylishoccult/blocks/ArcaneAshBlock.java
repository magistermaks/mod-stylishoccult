package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.network.Network;
import net.darktree.stylishoccult.utils.SimpleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class ArcaneAshBlock extends SimpleBlock {

    public static final IntProperty AGE = Properties.AGE_3;
    public static final BooleanProperty PERSISTENT = Properties.PERSISTENT;

    private final int min;
    private final int max;

    public ArcaneAshBlock(int min, int max, float hardness, Settings settings) {
        super(settings.ticksRandomly().strength(hardness, hardness));
        setDefaultState( getDefaultState().with(PERSISTENT, true).with(AGE, 0) );

        this.min = min;
        this.max = max;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( random.nextInt(20) == 0 && !state.get(PERSISTENT) ) {
            this.scheduledTick(state, world, pos, random);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if( !world.isClient ) {
            Network.ASH_PACKET.send(pos, (ServerWorld) world);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, PERSISTENT);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if( !state.get(PERSISTENT) ) {
            int age = 4 - state.get(AGE);
            world.getBlockTickScheduler().schedule(pos, this, MathHelper.nextInt(world.random, age * min, age * max));
            if( !world.isClient ) {
                Network.ASH_PACKET.send(pos, (ServerWorld) world);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);

        if( age == 3 ) {
            Network.ASH_PACKET.send( pos, world );
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), this.soundGroup.getBreakSound(), SoundCategory.BLOCKS, 0.8f, 1.0f);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }else{
            int h = 4 - age;
            world.getBlockTickScheduler().schedule(pos, this, MathHelper.nextInt(random, h * min, h * max));
            world.setBlockState(pos, state.with(AGE, age + 1));
        }
    }

    @Override
    public net.darktree.stylishoccult.loot.LootTable getInternalLootTableId() {
        return LootTables.ASH;
    }

}
