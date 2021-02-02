package net.darktree.stylishoccult.items;

import net.darktree.stylishoccult.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.List;

public class CandleItem extends BlockItem {

    public CandleItem(Settings settings, Block block) {
        super(block, settings);
    }

    @Override
    protected BlockState getPlacementState(ItemPlacementContext context) {

        BlockState blockPlacementState;
        WorldView worldView = context.getWorld();
        BlockPos pos = context.getBlockPos();

        for (Direction direction : context.getPlacementDirections()) {
            blockPlacementState = direction == Direction.DOWN ? ( this.getBlock() != null ? this.getBlock().getPlacementState(context) : null ) : null;

            if ( blockPlacementState != null && blockPlacementState.canPlaceAt(worldView, pos) && worldView.canPlace(blockPlacementState, pos, ShapeContext.absent()) ) {
                return blockPlacementState;
            }

        }

        return null;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            ItemStack stack = new ItemStack( this );
            CompoundTag tag1 = new CompoundTag();
            CompoundTag tag2 = new CompoundTag();
            tag2.putString( "layers", "8" );
            tag1.put( "BlockStateTag", tag2 );
            stack.setTag( tag1 );
            stacks.add( stack );
        }
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        String layers = String.valueOf( getLayers( stack ) - 1 );
        tooltip.add( Utils.tooltip( "candle", layers ) );
    }

    public static short getLayers( ItemStack stack ) {
        try{
            return Short.parseShort( stack.getTag().getCompound("BlockStateTag").getString("layers") );
        }catch(Exception ignored) {
            return 8;
        }
    }

}
