package net.darktree.stylishoccult.items;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.ChandelierBlock;
import net.darktree.stylishoccult.enums.CandleHolderMaterial;
import net.darktree.stylishoccult.utils.RegUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Map;

public class ChandelierItem extends BlockItem {

    BlockState state;
    private static final Map<CandleHolderMaterial, ChandelierItem> itemMap = new HashMap<>();

    public ChandelierItem(Settings settings, BlockState state) {
        super(state.getBlock(), settings);
        this.state = state;
    }

    public static Item register(CandleHolderMaterial material ) {
        ChandelierItem item = new ChandelierItem(
                new Item.Settings().group(ModItems.Groups.STYLISH_OCCULT),
                ModBlocks.CHANDELIER.getDefaultState()
                        .with(ChandelierBlock.MATERIAL, material)
        );

        itemMap.put( material, item );
        String name = (material.toString() + "_chandelier");

        return RegUtil.item(name, item);
    }

    public static Item getItem( CandleHolderMaterial material ) {
        return itemMap.get( material );
    }

    protected BlockState getPlacementState(ItemPlacementContext context) {
        return state != null && this.canPlace(context, state) ? state : null;
    }

    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add( new ItemStack( this ) );
        }
    }
}