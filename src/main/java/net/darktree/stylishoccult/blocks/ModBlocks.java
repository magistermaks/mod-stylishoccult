package net.darktree.stylishoccult.blocks;

import net.darktree.stylishoccult.blocks.occult.*;
import net.darktree.stylishoccult.blocks.runes.*;
import net.darktree.stylishoccult.blocks.runes.flow.*;
import net.darktree.stylishoccult.blocks.runes.io.*;
import net.darktree.stylishoccult.blocks.runes.trigger.ClickRuneBlock;
import net.darktree.stylishoccult.blocks.runes.trigger.ClockRuneBlock;
import net.darktree.stylishoccult.blocks.runes.trigger.RandomRuneBlock;
import net.darktree.stylishoccult.blocks.runes.trigger.RedstoneEntryRuneBlock;
import net.darktree.stylishoccult.utils.RegUtil;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;

public class ModBlocks {

    public static final ArrayList<Block> RUNESTONES = new ArrayList<>();

    public static final Block URN = RegUtil.block( "urn", new UrnBlock() );
    public static final Block LAVA_DEMON = RegUtil.block( "lava_demon", new LavaDemonBlock() );
    public static final Block FIERY_LANTERN = RegUtil.block( "fiery_lantern", new FieryLanternBlock() );
    public static final Block SPARK_VENT = RegUtil.block( "spark_vent", new SparkVentBlock() );
    public static final Block GROWTH = RegUtil.block( "growth", new ThinFleshBlock() );
    public static final Block NETHER_GRASS = RegUtil.block( "nether_grass", new NetherGrassBlock( FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.RED).requiresTool().noCollision().breakInstantly().sounds(BlockSoundGroup.NETHER_SPROUTS)) );
    public static final Block NETHER_FERN = RegUtil.block( "nether_fern", new NetherFernBlock( FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.RED).requiresTool().noCollision().breakInstantly().sounds(BlockSoundGroup.NETHER_SPROUTS)) );

    // flesh and friends
    public static final Block DEFAULT_FLESH = RegUtil.block( "flesh_default", new FleshBlock() );
    public static final Block LEAVES_FLESH = RegUtil.block( "flesh_leaves", new LeavesFleshBlock() );
    public static final Block SOIL_FLESH = RegUtil.block( "flesh_soil", new SoilFleshBlock() );
    public static final Block BONE_FLESH = RegUtil.block( "flesh_bone", new FossilizedFleshBlock() );
    public static final Block GOO_FLESH = RegUtil.block( "flesh_goo", new GooFleshBlock() );
    public static final Block GLOW_FLESH = RegUtil.block( "flesh_glow", new GlowFleshBlock() );
    public static final Block TENTACLE = RegUtil.block( "flesh_tentacle", new TentacleBlock() );
    public static final Block EYE_FLESH = RegUtil.block( "flesh_eye", new EyeBlock() );
    public static final Block EYES_FLESH = RegUtil.block( "flesh_eyes", new EyesBlock() );
    public static final Block SPORE_VENT = RegUtil.block( "flesh_vent", new VentBlock() );
    public static final Block WARTS_FLESH = RegUtil.block( "flesh_warts", new EyesBlock() );
    public static final Block WORMS_FLESH = RegUtil.block( "flesh_worms", new WormsBlock() );
    public static final Block EYES_BLOCK = RegUtil.block( "eyes_block", new BuildingBlock( RegUtil.settings( Material.ORGANIC_PRODUCT, BlockSoundGroup.HONEY, 0.8F, 0.8F, false ) ) );
    public static final Block FLESH_PASSIVE = RegUtil.block( "flesh_passive", new PassiveFleshBlock() );

    // simple building blocks
    public static final Block STONE_FLESH = RegUtil.block( "flesh_stone", new FleshStone( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 2.5f, 6.5f, true ).requiresTool() ) );
    public static final Block LAVA_STONE = RegUtil.block( "lava_stone", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 12.0f, 8.0f, true ).luminance(3).requiresTool() ) );
    public static final Block RUNESTONE = RegUtil.block( "runestone", new BuildingBlock( RegUtil.settings( Material.STONE, BlockSoundGroup.STONE, 2.0f, 6.0f, true ).mapColor(MapColor.BLACK).requiresTool() ) );
    public static final Block ARCANE_ASH = RegUtil.block( "arcane_ash", new ArcaneAshBlock( 100, 500, 0.4f, RegUtil.settings( Material.SOIL, BlockSoundGroup.SAND, 2.0f, 6.0f, true ).mapColor(MapColor.BLACK) ) );
    public static final Block CRYSTALLINE_BLACKSTONE = RegUtil.block( "crystalline_blackstone", new CrystallineBlackstone( RegUtil.settings( Material.STONE, BlockSoundGroup.GILDED_BLACKSTONE, 2.0f, 6.0f, true ).mapColor(MapColor.BLACK).requiresTool() ) );

    // runes
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
    public static final Block JOIN_RUNE_BLOCK = RegUtil.rune( new JoinRuneBlock( "adiungo" ) );

    public static void init() {
        // load class
    }

    public static void clientInit() {
        Block[] runestones = ModBlocks.RUNESTONES.toArray( new Block[0] );
        BlockColorProvider runeTintProvider = (state, world, pos, tintIndex) -> ((RuneBlock) state.getBlock()).getTint(state);

        BlockRenderLayerMap.INSTANCE.putBlock(LAVA_STONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LAVA_DEMON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FIERY_LANTERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NETHER_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(NETHER_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GROWTH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(WORMS_FLESH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GOO_FLESH, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), runestones);

        ColorProviderRegistry.BLOCK.register( runeTintProvider, runestones );
    }

}
