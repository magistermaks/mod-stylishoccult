package net.darktree.stylishoccult.items;

import net.darktree.stylishoccult.blocks.CandelabraBlock;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.enums.CandelabraType;
import net.darktree.stylishoccult.enums.CandleHolderMaterial;
import net.darktree.stylishoccult.utils.Pair;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;

public class CandelabraItem extends BlockItem {

    BlockState state;
    private static final HashMap<Pair<CandleHolderMaterial,Integer>,Item> itemMap = new HashMap<>();

    public CandelabraItem(Settings settings, BlockState state) {
        super(state.getBlock(), settings);
        this.state = state;
    }

    public static Item register(CandleHolderMaterial material, int count ) {

        CandelabraType type = CandelabraType.getByCount( count );

        CandelabraItem item = new CandelabraItem(
                new Item.Settings().group(ModItems.Groups.STYLISH_OCCULT),
                ModBlocks.CANDELABRA.getDefaultState()
                        .with(CandelabraBlock.TYPE, type)
                        .with(CandelabraBlock.MATERIAL, material)
        );

        itemMap.put( new Pair<>( material, count ), item );
        String name = (material.toString() + "_candelabra_" + count);

        return RegUtil.item(name, item);
    }

    public static Item getItem(CandleHolderMaterial material, int count ) {
        return itemMap.get( new Pair<>( material, count ) );
    }

    @Override
    protected BlockState getPlacementState(ItemPlacementContext context) {
        if( state != null && this.canPlace(context, state) ) {
            CandelabraType type = state.get(CandelabraBlock.TYPE);
            if( type.requiresRotation() ) {
                type = type.applyRotation( context.getPlayer().getHorizontalFacing() );
            }

            return state.with(CandelabraBlock.TYPE, type);
        }else{
            return null;
        }
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add( new ItemStack( this ) );
        }
    }

}
