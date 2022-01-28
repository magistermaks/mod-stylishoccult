package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class BlockEntities {

    public static final BlockEntityType<LavaDemonBlockEntity> LAVA_DEMON = FabricBlockEntityTypeBuilder.create(
            LavaDemonBlockEntity::new, ModBlocks.LAVA_DEMON).build(null);

    public static final BlockEntityType<RuneBlockEntity> RUNESTONE = FabricBlockEntityTypeBuilder.create(
            RuneBlockEntity::new, ModBlocks.RUNESTONES.toArray(new Block[0]) ).build(null);

    public static void init() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("lava_demon"), LAVA_DEMON);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("runestone"), RUNESTONE); }

}
