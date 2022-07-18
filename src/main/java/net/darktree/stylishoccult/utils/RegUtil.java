package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.block.ModBlocks;
import net.darktree.stylishoccult.block.rune.RuneBlock;
import net.darktree.stylishoccult.item.ModItems;
import net.darktree.stylishoccult.item.RuneBlockItem;
import net.darktree.stylishoccult.script.component.RuneRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class RegUtil {

    public static Block block(String name, Block block) {
        return Registry.register(Registry.BLOCK, new ModIdentifier(name), block);
    }

    public static BlockItem item(String id, Block block, ItemGroup group) {
        return (BlockItem) item(id, new BlockItem(block, new Item.Settings().group(group)));
    }

    public static Item item(String id, Item.Settings settings) {
        return item(id, new Item(settings));
    }

    public static Item item(String id, Item item) {
        return Registry.register( Registry.ITEM, new ModIdentifier(id), item);
    }

    public static FlowableFluid fluid(String name, FlowableFluid fluid) {
        return Registry.register(Registry.FLUID, new ModIdentifier(name), fluid);
    }

    public static Block rune( RuneBlock block ) {
        String name = "rune_" + block.name;
        Item item = item(name, new RuneBlockItem(block, new FabricItemSettings().group(ModItems.Groups.RUNES)));
        ModItems.RUNESTONES.add(item);
        ModBlocks.RUNESTONES.add(block);

        if (block.getInstance() != null) {
            RuneRegistry.putRune(block.name, block);
        }

        return block(name, block);
    }

    public static FabricBlockSettings settings(Material material, BlockSoundGroup sounds, float hardness, float resistance, boolean opaque) {
        FabricBlockSettings settings = FabricBlockSettings.of(material);
        if( !opaque ) settings.nonOpaque();
        settings.strength(hardness, resistance);
        settings.sounds(sounds);

        return settings;
    }

}
