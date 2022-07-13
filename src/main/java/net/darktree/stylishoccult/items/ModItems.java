package net.darktree.stylishoccult.items;

import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.entities.ModEntities;
import net.darktree.stylishoccult.items.material.TwistedBoneArmorMaterial;
import net.darktree.stylishoccult.items.material.TwistedBoneToolMaterial;
import net.darktree.stylishoccult.items.tools.AxeItem;
import net.darktree.stylishoccult.items.tools.HoeItem;
import net.darktree.stylishoccult.items.tools.PickaxeItem;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.darktree.stylishoccult.utils.RegUtil;
import net.darktree.stylishoccult.utils.RuneUtils;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;

import java.util.ArrayList;

public class ModItems {

    public static final ArrayList<Item> RUNESTONES = new ArrayList<>();

    static public class Groups {
        public static final ItemGroup STYLISH_OCCULT = FabricItemGroupBuilder.create(
                new ModIdentifier("stylish_occult_main"))
                .icon(() -> new ItemStack( ModItems.LAVA_HEART ))
                .build();

        public static final ItemGroup RUNES = FabricItemGroupBuilder.create(
                new ModIdentifier("stylish_occult_runes"))
                .icon(() -> new ItemStack( ModBlocks.CLICK_RUNE_BLOCK.asItem() ))
                .build();
    }

    // tools and armors
    public static final Item TWISTED_SWORD = RegUtil.item( "twisted_sword", new SwordItem(TwistedBoneToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item TWISTED_SHOVEL = RegUtil.item( "twisted_shovel", new ShovelItem(TwistedBoneToolMaterial.INSTANCE, 1.5F, -3.0F, new Item.Settings().group(ItemGroup.TOOLS)));
    public static final Item TWISTED_PICKAXE = RegUtil.item( "twisted_pickaxe", new PickaxeItem(TwistedBoneToolMaterial.INSTANCE, 1, -2.8F, new Item.Settings().group(ItemGroup.TOOLS)));
    public static final Item TWISTED_AXE = RegUtil.item( "twisted_axe", new AxeItem(TwistedBoneToolMaterial.INSTANCE, 7.0F, -3.2F, new Item.Settings().group(ItemGroup.TOOLS)));
    public static final Item TWISTED_HOE = RegUtil.item( "twisted_hoe", new HoeItem(TwistedBoneToolMaterial.INSTANCE, -1, -2.0F, new Item.Settings().group(ItemGroup.TOOLS)));

    public static final Item TWISTED_HELMET = RegUtil.item( "twisted_helmet", new ArmorItem(TwistedBoneArmorMaterial.INSTANCE, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item TWISTED_CHESTPLATE = RegUtil.item( "twisted_chestplate", new ArmorItem(TwistedBoneArmorMaterial.INSTANCE, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item TWISTED_LEGGINGS = RegUtil.item( "twisted_leggings", new ArmorItem(TwistedBoneArmorMaterial.INSTANCE, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item TWISTED_BOOTS = RegUtil.item( "twisted_boots", new ArmorItem(TwistedBoneArmorMaterial.INSTANCE, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT)));

    public static final Item LAVA_STONE = RegUtil.item("lava_stone", ModBlocks.LAVA_STONE, Groups.STYLISH_OCCULT);
    public static final Item LAVA_HEART = RegUtil.item("lava_heart", new Item.Settings().group(Groups.STYLISH_OCCULT).rarity(Rarity.UNCOMMON).fireproof());
    public static final Item LAVA_SHARD = RegUtil.item("lava_shard", new Item.Settings().group(Groups.STYLISH_OCCULT).rarity(Rarity.UNCOMMON).fireproof());
    public static final Item BLOOD_SHARD = RegUtil.item("blood_shard", new Item.Settings().group(Groups.STYLISH_OCCULT).rarity(Rarity.UNCOMMON));
    public static final Item BLAZING_LAVA_SHARD = RegUtil.item("blazing_lava_shard", new Item.Settings().group( Groups.STYLISH_OCCULT ).rarity( Rarity.UNCOMMON ).fireproof() );
    public static final Item FIERY_LANTERN = RegUtil.item("fiery_lantern", ModBlocks.FIERY_LANTERN, new Item.Settings().group(Groups.STYLISH_OCCULT).rarity(Rarity.UNCOMMON).fireproof() );
    public static final Item URN = RegUtil.item("urn", new BlockItem( ModBlocks.URN, new Item.Settings().group( Groups.STYLISH_OCCULT ).rarity( Rarity.UNCOMMON ) ) );
    public static final Item FLESH = RegUtil.item("flesh", new Item.Settings().group(Groups.STYLISH_OCCULT).food(ModFoodComponents.FLESH_FOOD));
    public static final Item VEINS = RegUtil.item("veins", new Item.Settings().group( Groups.STYLISH_OCCULT ).food( ModFoodComponents.VEINS));
    public static final Item COOKED_VEINS = RegUtil.item("cooked_veins", new Item.Settings().group( Groups.STYLISH_OCCULT).food(ModFoodComponents.COOKED_VEINS));
    public static final Item TWISTED_BONE = RegUtil.item("twisted_bone", new Item.Settings().group( Groups.STYLISH_OCCULT));
    public static final Item GLOWGROWTH_SHARD = RegUtil.item("glowgrowth_shard", new Item.Settings().group( Groups.STYLISH_OCCULT));
    public static final Item GROWTH = RegUtil.item("growth", ModBlocks.GROWTH, Groups.STYLISH_OCCULT);
    public static final Item BLOOD_BOTTLE = RegUtil.item("blood_bottle", new BottleItem(new Item.Settings().group(Groups.STYLISH_OCCULT).recipeRemainder(Items.GLASS_BOTTLE).food(ModFoodComponents.BLOOD).maxCount(16)) );
    public static final Item SPARK_VENT = RegUtil.item("spark_vent", ModBlocks.SPARK_VENT, Groups.STYLISH_OCCULT);
    public static final Item RUNESTONE = RegUtil.item("runestone", ModBlocks.RUNESTONE, Groups.STYLISH_OCCULT);
    public static final Item RUNE_ERROR_REPORT = RegUtil.item("error_tablet", new ErrorReportItem( new Item.Settings().group( Groups.STYLISH_OCCULT ).maxCount(1) ) );
    public static final Item RUNESTONE_SLAB = RegUtil.item("runestone_slab", ModBlocks.RUNESTONE_SLAB, Groups.STYLISH_OCCULT);
    public static final Item RUNESTONE_STAIR = RegUtil.item("runestone_stairs", ModBlocks.RUNESTONE_STAIR, Groups.STYLISH_OCCULT);
    public static final Item RUNESTONE_TABLE = RegUtil.item("runestone_table", ModBlocks.RUNESTONE_TABLE, Groups.STYLISH_OCCULT);
    public static final Item RUNESTONE_ALTAR_PLATE = RegUtil.item("runestone_altar_plate", ModBlocks.ALTAR_PLATE, Groups.STYLISH_OCCULT);
    public static final Item BLOOD_BUCKET = RegUtil.item("blood_bucket", new BucketItem(ModBlocks.STILL_BLOOD, new Item.Settings().group(ItemGroup.MISC).recipeRemainder(Items.BUCKET).maxCount(1)));

    // "decorative"
    public static final Item EYES_BLOCK = RegUtil.item( "eyes_block", ModBlocks.EYES_BLOCK, Groups.STYLISH_OCCULT );
    public static final Item WORMS_FLESH_BLOCK = RegUtil.item( "flesh_worms", ModBlocks.WORMS_FLESH, Groups.STYLISH_OCCULT );
    public static final Item EYES_FLESH_BLOCK = RegUtil.item( "flesh_eyes", ModBlocks.EYES_FLESH, Groups.STYLISH_OCCULT );
    public static final Item WARTS_FLESH_BLOCK = RegUtil.item( "flesh_warts", ModBlocks.WARTS_FLESH, Groups.STYLISH_OCCULT );
    public static final Item DEFAULT_FLESH_BLOCK = RegUtil.item( "flesh_default", ModBlocks.DEFAULT_FLESH, Groups.STYLISH_OCCULT );
    public static final Item LEAVES_FLESH_BLOCK = RegUtil.item( "flesh_leaves", ModBlocks.LEAVES_FLESH, Groups.STYLISH_OCCULT );
    public static final Item SOIL_FLESH_BLOCK = RegUtil.item( "flesh_soil", ModBlocks.SOIL_FLESH, Groups.STYLISH_OCCULT );
    public static final Item BONE_FLESH_BLOCK = RegUtil.item( "flesh_bone", ModBlocks.BONE_FLESH, Groups.STYLISH_OCCULT );
    public static final Item GOO_FLESH_BLOCK = RegUtil.item( "flesh_goo", ModBlocks.GOO_FLESH, Groups.STYLISH_OCCULT );
    public static final Item GLOW_FLESH_BLOCK = RegUtil.item( "flesh_glow", ModBlocks.GLOW_FLESH, Groups.STYLISH_OCCULT );
    public static final Item STONE_FLESH_BLOCK = RegUtil.item( "flesh_stone", ModBlocks.STONE_FLESH, Groups.STYLISH_OCCULT );
    public static final Item TENTACLE_FLESH_BLOCK = RegUtil.item( "flesh_tentacle", ModBlocks.TENTACLE, Groups.STYLISH_OCCULT );
    public static final Item EYE_FLESH_BLOCK = RegUtil.item( "flesh_eye", ModBlocks.EYE_FLESH, Groups.STYLISH_OCCULT );
    public static final Item PASSIVE_FLESH_BLOCK = RegUtil.item( "flesh_passive", ModBlocks.FLESH_PASSIVE, Groups.STYLISH_OCCULT );
    public static final Item NETHER_GRASS = RegUtil.item( "nether_grass", ModBlocks.NETHER_GRASS, Groups.STYLISH_OCCULT );
    public static final Item NETHER_FERN = RegUtil.item( "nether_fern", ModBlocks.NETHER_FERN, Groups.STYLISH_OCCULT );
    public static final Item ARCANE_ASH = RegUtil.item( "arcane_ash", ModBlocks.ARCANE_ASH, Groups.STYLISH_OCCULT );
    public static final Item CRYSTALLINE_BLACKSTONE = RegUtil.item( "crystalline_blackstone", ModBlocks.CRYSTALLINE_BLACKSTONE, Groups.STYLISH_OCCULT );
    public static final Item SPORE_VENT = RegUtil.item( "spore_vent", ModBlocks.SPORE_VENT, Groups.STYLISH_OCCULT );

    // spawn eggs
    public static final Item SPARK_SPAWN_EGG = RegUtil.item("spark_spawn_egg", new SpawnEggItem(ModEntities.SPARK, 0xff5454, 0xff0000, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item SPORE_SPAWN_EGG = RegUtil.item("spore_spawn_egg", new SpawnEggItem(ModEntities.SPORE, 0x620d0d, 0x4e0909, new Item.Settings().group(ItemGroup.MISC)));

    // staff
    public static final Item OCCULT_STAFF = RegUtil.item("staff", new Item(new Item.Settings().group(Groups.STYLISH_OCCULT)));

    public static void init() {
        // Load this class
    }

    public static void clientInit() {
        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> RuneUtils.COLOR_1,
                ModItems.RUNESTONES.toArray( new Item[0] )
        );
    }

}
