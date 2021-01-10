package net.darktree.stylishoccult.items;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.WallCandelabraBlock;
import net.darktree.stylishoccult.enums.CandleHolderMaterial;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;

public class WallCandelabraItem extends BlockItem {

    CandleHolderMaterial material;
    private static final Map<CandleHolderMaterial, WallCandelabraItem> itemMap = new HashMap<>();

    public WallCandelabraItem(Settings settings, CandleHolderMaterial material) {
        super(ModBlocks.WALL_CANDELABRA, settings);
        this.material = material;
    }

    public static Item register(CandleHolderMaterial material ) {
        WallCandelabraItem item = new WallCandelabraItem(
                new Item.Settings().group(ModItems.Groups.STYLISH_OCCULT),
                material
        );

        itemMap.put( material, item );
        String name = (material.toString() + "_wall_candelabra");

        return RegUtil.item(name, item);
    }

    public static Item getItem( CandleHolderMaterial material) {
        return itemMap.get( material );
    }

    @Override
    protected BlockState getPlacementState(ItemPlacementContext context) {
        Direction d = context.getPlacementDirections()[0];
        if (d.getAxis() == Direction.Axis.Y) return null;

        BlockState state = getBlock().getDefaultState().with(WallCandelabraBlock.HORIZONTAL_FACING, d);

        if( canPlace(context, state) ) {
            return state.with(WallCandelabraBlock.MATERIAL, material);
        }

        return null;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add( new ItemStack( this ) );
        }
    }

}
