package net.darktree.stylishoccult.utils;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class RegUtil {

    public static Block block( String name, Block block ) {
        return Registry.register(Registry.BLOCK, new ModIdentifier(name), block);
    }

    public static BlockItem item( String name, Block block, ItemGroup group ) {
        return (BlockItem) item( name, new BlockItem( block, new Item.Settings().group( group ) ) );
    }

    public static BlockItem item( String name, Block block, Item.Settings settings ) {
        return (BlockItem) item( name, new BlockItem( block, settings ) );
    }

    public static Item item( String name, ItemGroup group ) {
        return item( name, new Item( new Item.Settings().group( group ) ) );
    }

    public static Item item( String name, Item.Settings settings ) {
        return item( name, new Item( settings ) );
    }

    public static Item item( String name, Item item ) {
        return Registry.register( Registry.ITEM, new ModIdentifier( name ), item );
    }

    public static FabricBlockSettings settings(Material material, BlockSoundGroup sounds, float hardness, float resistance, boolean opaque ) {
        FabricBlockSettings settings = FabricBlockSettings.of( material );
        if( !opaque ) settings.nonOpaque();
        settings.strength( hardness, resistance );
        settings.sounds( sounds );
        return settings;
    }

}
