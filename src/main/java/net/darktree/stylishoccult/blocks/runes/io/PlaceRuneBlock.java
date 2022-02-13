package net.darktree.stylishoccult.blocks.runes.io;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.blocks.runes.ActorRuneBlock;
import net.darktree.stylishoccult.script.components.RuneException;
import net.darktree.stylishoccult.script.components.RuneExceptionType;
import net.darktree.stylishoccult.script.elements.ItemElement;
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
        int x = (int) Math.round( script.pull(world, pos).value() );
        int y = (int) Math.round( script.pull(world, pos).value() );
        int z = (int) Math.round( script.pull(world, pos).value() );

        ItemElement element = script.stack.pull().cast(ItemElement.class);
        BlockPos target = pos.add(x, y, z);

        if( !target.isWithinDistance(pos, range) ) {
            throw RuneException.of(RuneExceptionType.INVALID_ARGUMENT);
        }

        if( element.stack.getItem() instanceof BlockItem blockItem ) {
            try {
                if(!blockItem.place(new AutomaticItemPlacementContext(world, target, Direction.UP, element.stack, Direction.UP)).isAccepted()) {
                    // make sure not to lose any items, even when operation fails
                    script.ring.push(element, world, pos);
                }
            }catch (Exception e) {
                StylishOccult.LOGGER.warn("place error!");
            }
        }else{
            StylishOccult.LOGGER.warn("not a block item!");
        }

        super.apply(script);
    }

}
