package net.darktree.stylishoccult.items;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.enums.CandleHolderMaterial;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.RuneUtils;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;

import java.util.ArrayList;

public class ModItems {

    public static final ArrayList<Item> RUNESTONES = new ArrayList<>();

    static public class Groups {
        public static final ItemGroup STYLISH_OCCULT = FabricItemGroupBuilder.create(
                new ModIdentifier("stylish_occult_main"))
                .icon(() -> new ItemStack( ModItems.CANDLE ))
                .build();

        public static final ItemGroup RUNES = FabricItemGroupBuilder.create(
                new ModIdentifier("stylish_occult_runes"))
                .icon(() -> new ItemStack( ModItems.BLOOD_BOTTLE ))
                .build();
    }

    public static final Item LAVA_STONE = RegUtil.item( "lava_stone", ModBlocks.LAVA_STONE, Groups.STYLISH_OCCULT );
    public static final Item WAX = RegUtil.item( "wax", Groups.STYLISH_OCCULT );
    public static final Item LAVA_HEART = RegUtil.item( "lava_heart", new Item.Settings().group( Groups.STYLISH_OCCULT ).rarity( Rarity.UNCOMMON ).fireproof() );
    public static final Item EXTINGUISHED_CANDLE = RegUtil.item( "extinguished_candle", new CandleItem( new Item.Settings().group(Groups.STYLISH_OCCULT), ModBlocks.EXTINGUISHED_CANDLE) );
    public static final Item CANDLE = RegUtil.item( "candle", new CandleItem( new Item.Settings().group(Groups.STYLISH_OCCULT), ModBlocks.CANDLE) );
    public static final Item WAX_BLOCK = RegUtil.item( "wax_block", ModBlocks.WAX_BLOCK, Groups.STYLISH_OCCULT );
    public static final Item GRANITE_PEDESTAL = RegUtil.item( "granite_pedestal", ModBlocks.GRANITE_PEDESTAL, Groups.STYLISH_OCCULT );
    public static final Item ANDESITE_PEDESTAL = RegUtil.item( "andesite_pedestal", ModBlocks.ANDESITE_PEDESTAL, Groups.STYLISH_OCCULT );
    public static final Item DIORITE_PEDESTAL = RegUtil.item( "diorite_pedestal", ModBlocks.DIORITE_PEDESTAL, Groups.STYLISH_OCCULT );
    public static final Item FIERY_LANTERN = RegUtil.item( "fiery_lantern", ModBlocks.FIERY_LANTERN, new Item.Settings().group(Groups.STYLISH_OCCULT).rarity(Rarity.UNCOMMON).fireproof() );
    public static final Item URN = RegUtil.item( "urn", new BlockItem( ModBlocks.URN, new Item.Settings().group( Groups.STYLISH_OCCULT ).rarity( Rarity.UNCOMMON ) ) );
    public static final Item FLESH = RegUtil.item( "flesh", new Item.Settings().group( Groups.STYLISH_OCCULT ).food( ModFoodComponents.FLESH_FOOD ) );
    public static final Item VEINS = RegUtil.item( "veins", new Item.Settings().group( Groups.STYLISH_OCCULT ).food( ModFoodComponents.VEINS ) );
    public static final Item GROWTH = RegUtil.item( "growth", ModBlocks.GROWTH, Groups.STYLISH_OCCULT );
    public static final Item BLOOD_BOTTLE = RegUtil.item("blood_bottle", new BottleItem(new Item.Settings().group(Groups.STYLISH_OCCULT).recipeRemainder(Items.GLASS_BOTTLE).food(ModFoodComponents.BLOOD).maxCount(16)) );
    public static final Item SPARK_VENT = RegUtil.item("spark_vent", ModBlocks.SPARK_VENT, Groups.STYLISH_OCCULT );
    public static final Item RUNESTONE = RegUtil.item( "runestone", ModBlocks.RUNESTONE, Groups.STYLISH_OCCULT );
    public static final Item RUNE_ERROR_REPORT = RegUtil.item( "error_tablet", new ErrorReportItem( new Item.Settings().group( Groups.STYLISH_OCCULT ).maxCount(1) ) );

    // decorative
    public static final Item FLESH_BLOCK = RegUtil.item( "flesh_block", ModBlocks.FLESH, Groups.STYLISH_OCCULT );
    public static final Item PASSIVE_FLESH_BLOCK = RegUtil.item( "passive_flesh_block", ModBlocks.FLESH_BLOCK, Groups.STYLISH_OCCULT );
    public static final Item OLD_BRICKS = RegUtil.item( "old_bricks", ModBlocks.OLD_BRICKS, Groups.STYLISH_OCCULT );
    public static final Item CRACKED_BRICKS = RegUtil.item( "cracked_bricks", ModBlocks.CRACKED_BRICKS, Groups.STYLISH_OCCULT );
    public static final Item SMALL_STONE_BRICKS_1 = RegUtil.item( "small_stone_bricks_1", ModBlocks.SMALL_STONE_BRICKS_1, Groups.STYLISH_OCCULT );
    public static final Item SMALL_STONE_BRICKS_2 = RegUtil.item( "small_stone_bricks_2", ModBlocks.SMALL_STONE_BRICKS_2, Groups.STYLISH_OCCULT );
    public static final Item SMALL_STONE_BRICKS_3 = RegUtil.item( "small_stone_bricks_3", ModBlocks.SMALL_STONE_BRICKS_3, Groups.STYLISH_OCCULT );
    public static final Item GRANITE_SLIM_PILLAR = RegUtil.item( "granite_slim_pillar", ModBlocks.GRANITE_SLIM_PILLAR, Groups.STYLISH_OCCULT );
    public static final Item ANDESITE_SLIM_PILLAR = RegUtil.item( "andesite_slim_pillar",  ModBlocks.ANDESITE_SLIM_PILLAR, Groups.STYLISH_OCCULT );
    public static final Item DIORITE_SLIM_PILLAR = RegUtil.item( "diorite_slim_pillar", ModBlocks.DIORITE_SLIM_PILLAR, Groups.STYLISH_OCCULT );
    public static final Item NETHER_GRASS = RegUtil.item( "nether_grass", ModBlocks.NETHER_GRASS, Groups.STYLISH_OCCULT );
    public static final Item NETHER_FERN = RegUtil.item( "nether_fern", ModBlocks.NETHER_FERN, Groups.STYLISH_OCCULT );
    public static final Item ARCANE_ASH = RegUtil.item( "arcane_ash", ModBlocks.ARCANE_ASH, Groups.STYLISH_OCCULT );
    public static final Item CRYSTALLINE_BLACKSTONE = RegUtil.item( "crystalline_blackstone", ModBlocks.CRYSTALLINE_BLACKSTONE, Groups.STYLISH_OCCULT );

    // candle holding madness
    public static final Item OAK_CANDELABRA_5 = CandelabraItem.register( CandleHolderMaterial.OAK, 5 );
    public static final Item OAK_CANDELABRA_4 = CandelabraItem.register( CandleHolderMaterial.OAK, 4 );
    public static final Item OAK_CANDELABRA_3 = CandelabraItem.register( CandleHolderMaterial.OAK, 3 );
    public static final Item OAK_CANDELABRA_2 = CandelabraItem.register( CandleHolderMaterial.OAK, 2 );
    public static final Item OAK_CANDELABRA_1 = CandelabraItem.register( CandleHolderMaterial.OAK, 1 );
    public static final Item DARK_OAK_CANDELABRA_5 = CandelabraItem.register( CandleHolderMaterial.DARK_OAK, 5 );
    public static final Item DARK_OAK_CANDELABRA_4 = CandelabraItem.register( CandleHolderMaterial.DARK_OAK, 4 );
    public static final Item DARK_OAK_CANDELABRA_3 = CandelabraItem.register( CandleHolderMaterial.DARK_OAK, 3 );
    public static final Item DARK_OAK_CANDELABRA_2 = CandelabraItem.register( CandleHolderMaterial.DARK_OAK, 2 );
    public static final Item DARK_OAK_CANDELABRA_1 = CandelabraItem.register( CandleHolderMaterial.DARK_OAK, 1 );
    public static final Item ACACIA_CANDELABRA_5 = CandelabraItem.register( CandleHolderMaterial.ACACIA, 5 );
    public static final Item ACACIA_CANDELABRA_4 = CandelabraItem.register( CandleHolderMaterial.ACACIA, 4 );
    public static final Item ACACIA_CANDELABRA_3 = CandelabraItem.register( CandleHolderMaterial.ACACIA, 3 );
    public static final Item ACACIA_CANDELABRA_2 = CandelabraItem.register( CandleHolderMaterial.ACACIA, 2 );
    public static final Item ACACIA_CANDELABRA_1 = CandelabraItem.register( CandleHolderMaterial.ACACIA, 1 );
    public static final Item BIRCH_CANDELABRA_5 = CandelabraItem.register( CandleHolderMaterial.BIRCH, 5 );
    public static final Item BIRCH_CANDELABRA_4 = CandelabraItem.register( CandleHolderMaterial.BIRCH, 4 );
    public static final Item BIRCH_CANDELABRA_3 = CandelabraItem.register( CandleHolderMaterial.BIRCH, 3 );
    public static final Item BIRCH_CANDELABRA_2 = CandelabraItem.register( CandleHolderMaterial.BIRCH, 2 );
    public static final Item BIRCH_CANDELABRA_1 = CandelabraItem.register( CandleHolderMaterial.BIRCH, 1 );
    public static final Item JUNGLE_CANDELABRA_5 = CandelabraItem.register( CandleHolderMaterial.JUNGLE, 5 );
    public static final Item JUNGLE_CANDELABRA_4 = CandelabraItem.register( CandleHolderMaterial.JUNGLE, 4 );
    public static final Item JUNGLE_CANDELABRA_3 = CandelabraItem.register( CandleHolderMaterial.JUNGLE, 3 );
    public static final Item JUNGLE_CANDELABRA_2 = CandelabraItem.register( CandleHolderMaterial.JUNGLE, 2 );
    public static final Item JUNGLE_CANDELABRA_1 = CandelabraItem.register( CandleHolderMaterial.JUNGLE, 1 );
    public static final Item SPRUCE_CANDELABRA_5 = CandelabraItem.register( CandleHolderMaterial.SPRUCE, 5 );
    public static final Item SPRUCE_CANDELABRA_4 = CandelabraItem.register( CandleHolderMaterial.SPRUCE, 4 );
    public static final Item SPRUCE_CANDELABRA_3 = CandelabraItem.register( CandleHolderMaterial.SPRUCE, 3 );
    public static final Item SPRUCE_CANDELABRA_2 = CandelabraItem.register( CandleHolderMaterial.SPRUCE, 2 );
    public static final Item SPRUCE_CANDELABRA_1 = CandelabraItem.register( CandleHolderMaterial.SPRUCE, 1 );
    public static final Item OAK_CHANDELIER = ChandelierItem.register( CandleHolderMaterial.OAK );
    public static final Item DARK_OAK_CHANDELIER = ChandelierItem.register( CandleHolderMaterial.DARK_OAK );
    public static final Item ACACIA_CHANDELIER = ChandelierItem.register( CandleHolderMaterial.ACACIA );
    public static final Item BIRCH_CHANDELIER = ChandelierItem.register( CandleHolderMaterial.BIRCH );
    public static final Item JUNGLE_CHANDELIER = ChandelierItem.register( CandleHolderMaterial.JUNGLE );
    public static final Item SPRUCE_CHANDELIER = ChandelierItem.register( CandleHolderMaterial.SPRUCE );
    public static final Item OAK_WALL_CANDELABRA = WallCandelabraItem.register( CandleHolderMaterial.OAK );
    public static final Item DARK_OAK_WALL_CANDELABRA = WallCandelabraItem.register( CandleHolderMaterial.DARK_OAK );
    public static final Item ACACIA_WALL_CANDELABRA = WallCandelabraItem.register( CandleHolderMaterial.ACACIA );
    public static final Item BIRCH_WALL_CANDELABRA = WallCandelabraItem.register( CandleHolderMaterial.BIRCH );
    public static final Item JUNGLE_WALL_CANDELABRA = WallCandelabraItem.register( CandleHolderMaterial.JUNGLE );
    public static final Item SPRUCE_WALL_CANDELABRA = WallCandelabraItem.register( CandleHolderMaterial.SPRUCE );

    public static void init() {
        // Load this class
    }

    public static void clientInit() {
        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> RuneUtils.COLOR_0,
                ModItems.RUNESTONES.toArray( new Item[0] )
        );
    }

}
