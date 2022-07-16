package net.darktree.stylishoccult.block.occult;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.occult.api.FullFleshBlock;
import net.darktree.stylishoccult.block.occult.api.ImpureBlock;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.loot.LootTables;
import net.darktree.stylishoccult.utils.BlockUtils;
import net.darktree.stylishoccult.utils.OccultHelper;
import net.darktree.stylishoccult.utils.RandUtils;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class FossilizedFleshBlock extends FullFleshBlock implements ImpureBlock {

    public static final BooleanProperty STABLE = BooleanProperty.of("stable");

    public FossilizedFleshBlock() {
        super(RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.STONE, 1.0F, 1.0F, true ).slipperiness(0.7f).requiresTool().ticksRandomly());
        setDefaultState( getDefaultState().with(STABLE, false) );
    }

    public static boolean isPosValid(BlockView world, BlockPos origin) {
        for( Direction direction : Direction.values() ){
            BlockState state = world.getBlockState( origin.offset( direction ) );
            Block block = state.getBlock();
            if( block != ModBlocks.DEFAULT_FLESH && block != ModBlocks.BONE_FLESH ) return false;
        }
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STABLE);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(STABLE);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if( RandUtils.getBool(33.0f) ) {
            if( BlockUtils.countInArea(world, pos, FossilizedFleshBlock.class, 4) < 5 ) {
                BlockPos target = pos.offset( RandUtils.getEnum(Direction.class) );
                BlockState targetState = world.getBlockState( target );

                if( targetState.getBlock() == ModBlocks.DEFAULT_FLESH && isPosValid(world, target) ) {
                    world.setBlockState(target, state);
                }
            }else{
                world.setBlockState(pos, state.cycle(STABLE));
            }
        }
    }

    @Override
    public void cleanse(World world, BlockPos pos, BlockState state) {
        OccultHelper.cleanseFlesh(world, pos, state);
    }

    @Override
    public int impurityLevel(BlockState state) {
        return 16;
    }

    @Override
    public LootTable getInternalLootTableId() {
        return LootTables.BONE_FLESH;
    }
}
