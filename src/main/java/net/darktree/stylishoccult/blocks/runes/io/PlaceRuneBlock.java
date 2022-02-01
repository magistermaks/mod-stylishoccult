package net.darktree.stylishoccult.blocks.runes.io;

import net.darktree.stylishoccult.blocks.ArcaneAshBlock;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.runes.ActorRuneBlock;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.engine.Script;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceRuneBlock extends ActorRuneBlock {

    public interface PlaceFunction {
        void place( World world, BlockPos pos, BlockState state );
    }

    public static final PlaceFunction ARCANE_ASH_PLACER = (world, pos, state) -> {
        if( (state.isAir() || state.getMaterial().isReplaceable()) && (state.getBlock() != ModBlocks.ARCANE_ASH || state.get(ArcaneAshBlock.AGE) != 0) ) {
            world.setBlockState(pos, ModBlocks.ARCANE_ASH.getDefaultState().with(ArcaneAshBlock.PERSISTENT, false));
        }
    };

    private final int range;
    private final PlaceFunction placeFunction;

    public PlaceRuneBlock(String name, int range, PlaceFunction placeFunction) {
        super(name);
        this.range = range;
        this.placeFunction = placeFunction;
    }

    @Override
    public void apply(Script script, World world, BlockPos pos) {
        try {
            int x = (int) Math.round( script.pull(world, pos).value() );
            int y = (int) Math.round( script.pull(world, pos).value() );
            int z = (int) Math.round( script.pull(world, pos).value() );

            BlockPos target = pos.add(x, y, z);

            if( !target.isWithinDistance(pos, range) ) {
                throw RuneExceptionType.INVALID_ARGUMENT.get();
            }

            placeFunction.place(world, target, world.getBlockState(target));
        }catch (Exception exception) {
            throw RuneExceptionType.INVALID_ARGUMENT_COUNT.get();
        }

        super.apply(script);
    }


}
