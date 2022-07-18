package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.item.ThrownItemEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class Utils {

    public static VoxelShape shape(float x1, float y1, float z1, float x2, float y2, float z2) {
        // make sure that the x1, y1, z1 given to VoxelShapes.cuboid are
        // smaller than x2, y2, z2, required my minecraft >=1.17
        return VoxelShapes.cuboid(
                Math.min(x1, x2) / 16d,
                Math.min(y1, y2) / 16d,
                Math.min(z1, z2) / 16d,
                Math.max(x1, x2) / 16d,
                Math.max(y1, y2) / 16d,
                Math.max(z1, z2) / 16d
        );
    }

    public static Box box(float x1, float y1, float z1, float x2, float y2, float z2) {
        return new Box(x1 / 16d, y1 / 16d, z1 / 16d, x2 / 16d, y2 / 16d, z2 / 16d);
    }

    public static MutableText tooltip(String text, Object... args) {
        return new TranslatableText("tooltip." + StylishOccult.NAMESPACE + "." + text, args).formatted(Formatting.GRAY);
    }

    public static MutableText guiText(String text, Object... args) {
        return new TranslatableText("gui." + StylishOccult.NAMESPACE + "." + text, args);
    }

    public static void ejectStack(World world, float x, float y, float z, ItemStack stack, float vx, float vy, float vz) {
        if (!world.isClient && !stack.isEmpty()) {
            ItemEntity itemEntity = new ThrownItemEntity(world, x, y, z, stack, vx, vy, vz, 20);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
    }

}
