package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.occult.PassiveFleshBlock;
import net.darktree.stylishoccult.blocks.occult.ThinFleshBlock;
import net.darktree.stylishoccult.blocks.runes.*;
import net.darktree.stylishoccult.sounds.Sounds;
import net.darktree.stylishoccult.utils.BuildingBlock;
import net.darktree.stylishoccult.utils.RegUtil;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;

public class ModBlocks {

    public static final ArrayList<Block> RUNESTONES = new ArrayList<>();

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
    public static final Block GROWTH = RegUtil.block( "growth", new ThinFleshBlock() );
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
    public static final Block RUNESTONE = RegUtil.block( "runestone", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 2.0f, 6.0f, true ).materialColor(MaterialColor.BLACK).requiresTool() ) );
    public static final Block ARCANE_ASH = RegUtil.block( "arcane_ash", new ArcaneAshBlock( 100, 500, 0.8f, RegUtil.settings( Material.SOIL, BlockSoundGroup.SAND, 2.0f, 6.0f, true ).requiresTool().breakByTool(FabricToolTags.SHOVELS).materialColor(MaterialColor.BLACK) ) );
    public static final Block CRYSTALLINE_BLACKSTONE = RegUtil.block( "crystalline_blackstone", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.GILDED_BLACKSTONE, 2.0f, 6.0f, true ).materialColor(MaterialColor.BLACK).requiresTool() ) );

    // runes - 'latin' names, do you have a better name? I'm open for suggestions.
    public static final Block BROKEN_RUNE_BLOCK = RegUtil.rune( new BrokenRuneBlock( "damaged" ) );
    public static final Block DEBUG_RUNE_BLOCK = RegUtil.rune( new DebugRuneBlock( "debug" ) );
    public static final Block CLICK_RUNE_BLOCK = RegUtil.rune( new ClickRuneBlock( "tactus" ) );
    public static final Block NOOP_RUNE_BLOCK = RegUtil.rune( new TransferRuneBlock( "vanitas" ) );
    public static final Block REDSTONE_DIGITAL_OUT_RUNE_BLOCK = RegUtil.rune( new RedstoneDigitalOutputRuneBlock( "exitus" ) );
    public static final Block SCATTER_RUNE_BLOCK = RegUtil.rune( new ScatterRuneBlock( "dispergat" ) );
    public static final Block DIRECTION_RUNE_BLOCK = RegUtil.rune( new RedirectRuneBlock( "directio" ) );
    public static final Block RANDOM_UPDATE_RUNE_BLOCK = RegUtil.rune( new RandomRuneBlock( "fortuitus" ) );
    public static final Block FORK_RUNE_BLOCK = RegUtil.rune( new ForkRuneBlock( "furca" ) );
    public static final Block MINUS_RUNE_BLOCK = RegUtil.rune( new NumberRuneBlock( "numerus_minus", '-' ) );
    public static final Block ZERO_RUNE_BLOCK = RegUtil.rune( new NumberRuneBlock( "numerus_nihil", '0' ) );
    public static final Block ONE_RUNE_BLOCK = RegUtil.rune( new NumberRuneBlock( "numerus_unus", '1' ) );
    public static final Block TWO_RUNE_BLOCK = RegUtil.rune( new NumberRuneBlock( "numerus_duo", '2' ) );
    public static final Block THREE_RUNE_BLOCK = RegUtil.rune( new NumberRuneBlock( "numerus_tres", '3' ) );
    public static final Block FOUR_RUNE_BLOCK = RegUtil.rune( new NumberRuneBlock( "numerus_quattuor", '4' ) );
    public static final Block FIVE_RUNE_BLOCK = RegUtil.rune( new NumberRuneBlock( "numerus_quinque", '5' ) );
    public static final Block PUSH_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "paello", LogicRuneBlock.Functions.PUSH ) );
    public static final Block PULL_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "divexo", LogicRuneBlock.Functions.PULL ) );
    public static final Block EXCHANGE_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "cambitas", LogicRuneBlock.Functions.EXCHANGE ) );
    public static final Block DUPLICATE_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "effingo", LogicRuneBlock.Functions.DUPLICATE ) );
    public static final Block OR_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "vel", LogicRuneBlock.Functions.OR ) );
    public static final Block ADD_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "adaugeo", LogicRuneBlock.Functions.ADD ) );
    public static final Block NOT_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "ni", LogicRuneBlock.Functions.NOT ) );
    public static final Block MULTIPLY_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "multiplico", LogicRuneBlock.Functions.MULTIPLY ) );
    public static final Block INVERT_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "inverto", LogicRuneBlock.Functions.INVERT ) );
    public static final Block RECIPROCAL_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "mutuus", LogicRuneBlock.Functions.RECIPROCAL ) );
    public static final Block EQUALS_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "pares", LogicRuneBlock.Functions.EQUALS ) );
    public static final Block LESS_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "minus", LogicRuneBlock.Functions.LESS ) );
    public static final Block MORE_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "magis", LogicRuneBlock.Functions.MORE ) );
    public static final Block IF_RUNE_BLOCK = RegUtil.rune( new IfRuneBlock( "si" ) );
    public static final Block PLACE_RUNE_BLOCK = RegUtil.rune( new PlaceRuneBlock( "locus", 256, PlaceRuneBlock.ARCANE_ASH_PLACER ) );
    public static final Block INCREMENT_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "incrementum", LogicRuneBlock.Functions.INCREMENT ) );
    public static final Block DECREMENT_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "decrementum", LogicRuneBlock.Functions.DECREMENT ) );
    public static final Block REDSTONE_ANALOG_OUT_RUNE_BLOCK = RegUtil.rune( new RedstoneAnalogOutputRuneBlock( "scribo" ) );
    public static final Block PROXIMITY_SENSOR_RUNE_BLOCK = RegUtil.rune( new PlayerRuneBlock( "propinquitas", 8 ) );
    public static final Block SINE_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "sine", LogicRuneBlock.Functions.SINE ) );
    public static final Block SPLIT_RUNE_BLOCK = RegUtil.rune( new SplitRuneBlock( "scindo" ) );
    public static final Block REDSTONE_DIGITAL_IN_RUNE_BLOCK = RegUtil.rune( new RedstoneEntryRuneBlock( "evigilo" ) );
    public static final Block REDSTONE_ANALOG_IN_RUNE_BLOCK = RegUtil.rune( new RedstoneAnalogInputRuneBlock( "sensum" ) );
    public static final Block CLOCK_RUNE_BLOCK = RegUtil.rune( new ClockRuneBlock( "horologium", 5 ) );
    public static final Block RANDOM_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "temere", LogicRuneBlock.Functions.RANDOM ) );
    public static final Block GATEWAY_RUNE_BLOCK = RegUtil.rune( new RedstoneGateRuneBlock( "porta" ) );
    public static final Block POP_RUNE_BLOCK = RegUtil.rune( new LogicRuneBlock( "absumo", LogicRuneBlock.Functions.POP ) );

    public static void init() {
        // load class
    }

    public static void clientInit() {
        Block[] runestones = ModBlocks.RUNESTONES.toArray( new Block[0] );

        BlockRenderLayerMap.INSTANCE.putBlock(CHANDELIER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LAVA_STONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LAVA_DEMON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FIERY_LANTERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NETHER_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NETHER_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GROWTH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), runestones);

        ColorProviderRegistry.BLOCK.register( RuneBlock.COLOR_PROVIDER, runestones );
    }

}
