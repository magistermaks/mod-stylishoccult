package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.script.components.RuneException;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RuneUtils {

    public static final int COLOR_0 = 0x4d0000;
    public static final int COLOR_1 = 0x660000;
    public static final int COLOR_2 = 0x800000;
    public static final int COLOR_3 = 0x990000;

    public static void createErrorReport(RuneException exception, World world, BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putString("error", exception.getMessage());
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());

        ItemStack stack = new ItemStack(ModItems.RUNE_ERROR_REPORT, 1);
        stack.setTag(tag);
        Block.dropStack(world, pos, stack);
    }

    public static int getTint( int index ) {
        switch( index ) {
            case 0: return COLOR_0;
            case 1: return COLOR_1;
            case 2: return COLOR_2;
            case 3: return COLOR_3;
        }

        return 0;
    }

}
