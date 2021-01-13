package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.BuildingBlock;
import net.darktree.stylishoccult.utils.RegUtil;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

    public static final Block CHANDELIER = RegUtil.block( "chandelier", new ChandelierBlock() );
    public static final Block CANDELABRA = RegUtil.block( "candelabra", new CandelabraBlock() );
    public static final Block WALL_CANDELABRA = RegUtil.block( "wall_candelabra", new WallCandelabraBlock() );
    public static final Block EXTINGUISHED_CANDLE = RegUtil.block( "extinguished_candle", new ExtinguishedCandleBlock() );
    public static final Block CANDLE = RegUtil.block( "candle", new CandleBlock() );
    public static final Block URN = RegUtil.block( "urn", new UrnBlock() );
    public static final Block LAVA_DEMON = RegUtil.block( "lava_demon", new LavaDemonBlock() );
    public static final Block GRANITE_PEDESTAL = RegUtil.block( "granite_pedestal", new PedestalBlock() );
    public static final Block ANDESITE_PEDESTAL = RegUtil.block( "andesite_pedestal", new PedestalBlock() );
    public static final Block DIORITE_PEDESTAL = RegUtil.block( "diorite_pedestal", new PedestalBlock() );
    public static final Block FIERY_LANTERN = RegUtil.block( "fiery_lantern", new FieryLanternBlock() );
    public static final Block SPARK_VENT = RegUtil.block( "spark_vent", new SparkVentBlock() );
    public static final Block NETHER_GRASS = RegUtil.block( "nether_grass", new NetherGrassBlock( AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT, MaterialColor.RED).noCollision().breakInstantly().sounds(BlockSoundGroup.NETHER_SPROUTS)) );
    public static final Block NETHER_FERN = RegUtil.block( "nether_fern", new NetherFernBlock( AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT, MaterialColor.RED).noCollision().breakInstantly().sounds(BlockSoundGroup.NETHER_SPROUTS)) );

    // simple building blocks
    public static final Block LAVA_STONE = RegUtil.block( "lava_stone", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 12.0f, 8.0f, true ).requiresTool() ) );
    public static final Block WAX_BLOCK = RegUtil.block( "wax_block", new BuildingBlock( RegUtil.settings( Material.ORGANIC_PRODUCT, Sounds.CANDLE, 0.8F, 0.8F, true ) ) );
    public static final Block FLESH_BLOCK = RegUtil.block( "flesh_block", new PassiveFleshBlock() );
    public static final Block CRACKED_BRICKS = RegUtil.block( "cracked_bricks", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 2.0f, 6.0f, true ).materialColor(MaterialColor.RED).requiresTool() ) );
    public static final Block OLD_BRICKS = RegUtil.block( "old_bricks", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 2.0f, 4.0f, true ).materialColor(MaterialColor.RED).requiresTool() ) );
    public static final Block SMALL_STONE_BRICKS_1 = RegUtil.block( "small_stone_bricks_1", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 2.0f, 6.0f, true ).requiresTool() ) );
    public static final Block SMALL_STONE_BRICKS_2 = RegUtil.block( "small_stone_bricks_2", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 2.0f, 6.0f, true ).requiresTool() ) );
    public static final Block SMALL_STONE_BRICKS_3 = RegUtil.block( "small_stone_bricks_3", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 2.0f, 6.0f, true ).requiresTool() ) );
    public static final Block GRANITE_SLIM_PILLAR = RegUtil.block( "granite_slim_pillar", new SlimPillarBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 1.5f, 6.0f, true ).requiresTool() ) );
    public static final Block ANDESITE_SLIM_PILLAR = RegUtil.block( "andesite_slim_pillar", new SlimPillarBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 1.5f, 6.0f, true ).requiresTool() ) );
    public static final Block DIORITE_SLIM_PILLAR = RegUtil.block( "diorite_slim_pillar", new SlimPillarBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 1.5f, 6.0f, true ).requiresTool() ) );

    public static void clientInit() {
        BlockRenderLayerMap.INSTANCE.putBlock(CHANDELIER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LAVA_STONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LAVA_DEMON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FIERY_LANTERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NETHER_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NETHER_FERN, RenderLayer.getCutout());
    }

}
