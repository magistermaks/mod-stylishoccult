package net.darktree.stylishoccult.loot;

import net.darktree.stylishoccult.blocks.LavaDemonBlock;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.occult.EyesBlock;
import net.darktree.stylishoccult.enums.LavaDemonPart;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.loot.entry.ConditionEntry;
import net.darktree.stylishoccult.loot.entry.ValveEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LootTables {

    public static final LootTable SIMPLE = LootManager.create()
            .addBlockItem()
            .build();

    public static final LootTable SIMPLE_RESISTANT = LootManager.create()
            .ignoreExplosions()
            .addBlockItem()
            .build();

    public static final LootTable URN = LootManager.create()
            .addCondition( ConditionEntry::hasSilkTouch )
                .addItem( ModItems.URN )
                .pop()
            .addElse()
                .addItem( Items.DIAMOND, 5.7f )
                .addItem( Items.GOLD_NUGGET, 35.3f )
                .addItem( Items.GOLD_INGOT, 12.1f )
                .addItem( Items.EMERALD, 23.4f )
                .addItem( Items.LAPIS_LAZULI, 11.0f )
                .addItem( Items.GUNPOWDER, 29.6f )
                .addItem( Items.REDSTONE, 12.0f )
                .addItem( Items.IRON_INGOT, 11.0f )
                .addItem( Items.STRING, 3.2f )
                .addItem( Items.ROTTEN_FLESH, 3.2f )
                .addItem( Items.BONE, 4.2f )
                .dropExperience(2, 10)
                .pop()
            .minimum(1)
            .build();

    public static final LootTable CRYSTALLINE_BLACKSTONE = LootManager.create()
            .addCondition( ConditionEntry::hasFortune )
                .addValve( ValveEntry::fortune )
                    .addItem(Items.QUARTZ)
                    .dropExperience(1, 3)
                    .pop()
                .pop()
            .addElse()
                .addBlockItem()
                .pop()
            .build();

    public static final LootTable GROWTH = LootManager.create()
            .addCondition(ConditionEntry::hasSilkTouch)
                .addBlockItem()
                .pop()
            .addElse()
                .addValve(ValveEntry::fortune)
                    .addItem(ModItems.VEINS, 77.0f)
                    .addItem(ModItems.VEINS, 23.0f)
                    .addItem(ModItems.VEINS, 10.0f)
                    .minimum(1)
                    .pop()
                .pop()
            .build();

    public static final LootTable LAVA_DEMON = LootManager.create()
            .addCondition( (rng, ctx) -> ctx.getBlock() == ModBlocks.LAVA_DEMON && ctx.getState().get(LavaDemonBlock.PART) == LavaDemonPart.HEAD )
                .addItem(ModItems.LAVA_HEART)
                .pop()
            .addElse()
                .addCondition( ConditionEntry::hasSilkTouch )
                    .addItem(ModItems.LAVA_STONE)
                    .pop()
                .addElse()
                    .addValve( ValveEntry::fortune )
                        .addValve( ValveEntry::singular )
                            .addItem( Items.DIAMOND, 11.7f )
                            .addItem( Items.EMERALD, 19.0f )
                            .addItem( Items.LAPIS_LAZULI, 1.0f )
                            .addItem( Items.COAL, 30.4f )
                            .minimum(1)
                            .pop()
                        .pop()
                    .pop()
                .pop()
            .build();

    public static final LootTable STONE_FLESH = LootManager.create()
            .addCondition( ConditionEntry::hasSilkTouch )
                .addBlockItem()
                .pop()
            .addElse()
                .addItem(Items.COBBLESTONE)
                .addValve(ValveEntry::fortune)
                    .addItem(new ItemStack(ModItems.VEINS), 100, 1, 3)
                    .pop()
                .pop()
            .build();

    public static final LootTable GENERIC_FLESH = LootManager.create()
            .addCondition( ConditionEntry::hasSilkTouch )
                .addBlockItem()
                .pop()
            .addElse()
                .addItem( new ItemStack( ModItems.FLESH ), 100.0f, 1, 4 )
                .pop()
            .build();

    public static final LootTable BONE_FLESH = LootManager.create()
            .addCondition( ConditionEntry::hasSilkTouch )
                .addBlockItem()
                .pop()
            .addElse()
                .addItem( ModItems.FLESH )
                .addItem( new ItemStack( ModItems.TWISTED_BONE ), 100.0f, 1, 4 )
                .dropExperience( 0, 3 )
                .pop()
            .build();

    public static final LootTable GLOW_FLESH = LootManager.create()
            .addCondition( ConditionEntry::hasSilkTouch )
                .addBlockItem()
                .pop()
            .addElse()
                .addItem( ModItems.FLESH )
                .addItem( new ItemStack(ModItems.GLOWGROWTH_SHARD), 100.0f, 1, 2 )
            .pop()
            .build();

    public static final LootTable EYES_BlOCK = LootManager.create()
            .addValve( (arr, rng, ctx) -> {
                int size = ctx.getState().get(EyesBlock.SIZE);
                for( ItemStack stack : arr ) stack.setCount( size );
                return arr;
            } )
                .addBlockItem()
                .pop()
            .build();

    public static final LootTable SPARK_VENT = LootManager.create()
            .addCondition( ConditionEntry::hasSilkTouch )
                .addItem( ModItems.SPARK_VENT )
                .pop()
            .addElse()
                .addItem( Items.NETHERRACK )
                .pop()
            .build();

    public static void init() {
        // load class
    }

}
