package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.utils.SimpleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class ArcaneAshBlock extends SimpleBlock {

    public static final IntProperty AGE = Properties.AGE_3;

    private final int min;
    private final int max;

    public ArcaneAshBlock(int min, int max, Settings settings) {
        super(settings.ticksRandomly().dropsNothing());

        this.min = min;
        this.max = max;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.scheduledTick(state, world, pos, random);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        int age = 4 - state.get(AGE);
        world.getBlockTickScheduler().schedule(pos, this, MathHelper.nextInt(world.random, age * min, age * max));
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);

        if( age == 3 ) {
            // TODO: play some effects
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), this.soundGroup.getBreakSound(), SoundCategory.BLOCKS, 0.8f, 1.0f);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }else{
            int h = 4 - age;
            world.getBlockTickScheduler().schedule(pos, this, MathHelper.nextInt(random, h * min, h * max));
            world.setBlockState(pos, state.with(AGE, age + 1));
        }
    }

}
