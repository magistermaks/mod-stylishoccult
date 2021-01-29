package net.darktree.stylishoccult.blocks.entities;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.SimpleBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

public class BlockEntities {

    public static final BlockEntityType<LavaDemonBlockEntity> LAVA_DEMON = BlockEntityType.Builder.create(
            LavaDemonBlockEntity::new, ModBlocks.LAVA_DEMON ).build(null);

    public static final BlockEntityType<CandelabraBlockEntity> CANDELABRA = BlockEntityType.Builder.create(
            CandelabraBlockEntity::new, ModBlocks.CANDELABRA ).build(null);

    public static final BlockEntityType<ChandelierBlockEntity> CHANDELIER = BlockEntityType.Builder.create(
            ChandelierBlockEntity::new, ModBlocks.CHANDELIER ).build(null);

    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL = BlockEntityType.Builder.create(
            PedestalBlockEntity::new, ModBlocks.ANDESITE_PEDESTAL, ModBlocks.GRANITE_PEDESTAL, ModBlocks.DIORITE_PEDESTAL ).build(null);

    public static final BlockEntityType<WallCandelabraBlockEntity> WALL_CANDELABRA = BlockEntityType.Builder.create(
            WallCandelabraBlockEntity::new, ModBlocks.WALL_CANDELABRA ).build(null);

    public static final BlockEntityType<RuneBlockEntity> RUNESTONE = BlockEntityType.Builder.create(
            RuneBlockEntity::new, ModBlocks.RUNESTONES.toArray( new Block[0] ) ).build(null);

    public static void init() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("lava_demon"), LAVA_DEMON);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("candelabra"), CANDELABRA);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("wall_candelabra"), WALL_CANDELABRA);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("chandelier"), CHANDELIER);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("pedestal"), PEDESTAL);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ModIdentifier("runestone"), RUNESTONE); }

    public static void clientInit() {
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.WALL_CANDELABRA, SimpleBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.CANDELABRA, SimpleBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.CHANDELIER, SimpleBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.PEDESTAL, SimpleBlockEntityRenderer::new);
    }

}
