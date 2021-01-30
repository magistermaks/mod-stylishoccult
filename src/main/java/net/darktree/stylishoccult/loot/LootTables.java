package net.darktree.stylishoccult.loot;

import net.darktree.stylishoccult.blocks.AbstractCandleHolderBlock;
import net.darktree.stylishoccult.blocks.CandleBlock;
import net.darktree.stylishoccult.blocks.LavaDemonBlock;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.AbstractCandleHolderBlockEntity;
import net.darktree.stylishoccult.blocks.entities.PedestalBlockEntity;
import net.darktree.stylishoccult.enums.LavaDemonPart;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.loot.entry.ValveEntry;
import net.darktree.stylishoccult.utils.Tag;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class LootTables {

    public static final BakedLootTable SIMPLE = LootManager.create()
            .addGenerator( (rng, ctx) -> LootManager.getAsArray( ctx.getBlockItem() ) )
            .build();

    public static final BakedLootTable SIMPLE_RESISTANT = LootManager.create()
            .ignoreExplosions()
            .addTable( SIMPLE.table() )
            .build();

    // TODO INVALID LOTUS LIB USAGE - PURGE
    public static final BakedLootTable PEDESTAL = LootManager.create()
            .applyForCreative()
            .addGenerator( (rng, ctx) -> {
                ArrayList<ItemStack> stacks = new ArrayList<>();

                if( ctx.shouldDrop() ) {
                    stacks.add(ctx.getBlockItem());
                }

                stacks.add( ctx.getBlockEntity(PedestalBlockEntity.class).drop() );
                return stacks;
            } )
            .build();

    public static final BakedLootTable URN = LootManager.create()
            .addCondition( (rng, ctx) -> ctx.toolHasEnchantment( Enchantments.SILK_TOUCH ) )
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
                .addItem( ModItems.WAX, 3.2f )
                .dropExperience(2, 10)
                .pop()
            .minimum(1)
            .build();

    public static final BakedLootTable LAVA_DEMON = LootManager.create()
            .addCondition( (rng, ctx) -> ctx.getBlock() == ModBlocks.LAVA_DEMON && ctx.getState().get(LavaDemonBlock.PART) == LavaDemonPart.HEAD )
                .addItem( ModItems.LAVA_HEART )
                .pop()
            .addElse()
                .addCondition( (rng, ctx) -> ctx.toolHasEnchantment( Enchantments.SILK_TOUCH ) )
                    .addItem( ModItems.LAVA_STONE)
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

    // TODO INVALID LOTUS LIB USAGE - PURGE
    public static final BakedLootTable CANDLE_CONTAINER = LootManager.create()
            .applyForCreative()
            .addGenerator( (rng, ctx) -> {
                ArrayList<ItemStack> stacks = new ArrayList<>();

                AbstractCandleHolderBlockEntity entity = ctx.getBlockEntity(AbstractCandleHolderBlockEntity.class);
                if( entity != null ) {
                    stacks = entity.dropAll();

                    if( ctx.shouldDrop() ) {
                        AbstractCandleHolderBlock block = ctx.getBlock(AbstractCandleHolderBlock.class);
                        if( block != null ) {
                            stacks.add( block.getMainStack(ctx.getState()) );
                        }
                    }
                }

                return stacks;
            } )
            .build();

    public static final BakedLootTable CANDLE = LootManager.create()
            .addGenerator( (rng, ctx) -> {
                ArrayList<ItemStack> stacks = new ArrayList<>();
                Tag tag = new Tag();
                int layers = ctx.getState().get(CandleBlock.LAYERS);

                if( layers != 1 ) {
                    String l = String.valueOf( ctx.getState().get(CandleBlock.LAYERS) );
                    tag.newTag( "BlockStateTag" ).putString( "layers", l ).pop();
                    ItemStack stack = ItemStack.EMPTY;

                    if( ctx.getBlock() == ModBlocks.CANDLE ) {
                        stack = new ItemStack( ModItems.CANDLE );
                    }else if( ctx.getBlock() == ModBlocks.EXTINGUISHED_CANDLE ) {
                        stack = new ItemStack( ModItems.EXTINGUISHED_CANDLE );
                    }

                    stack.setTag( tag.getSimpleTag() );
                    stacks.add(stack);
                }

                return stacks;
            } )
            .build();

    public static final BakedLootTable PASSIVE_FLESH = LootManager.create()
            .addCondition( (rng, ctx) -> ctx.toolHasEnchantment( Enchantments.SILK_TOUCH ) )
                .addItem( ModItems.FLESH_BLOCK )
                .pop()
            .addElse()
                .addItem( new ItemStack( ModItems.FLESH ), 100.0f, 1, 4 )
                .pop()
            .build();

    public static final BakedLootTable SPARK_VENT = LootManager.create()
            .addCondition( (rng, ctx) -> ctx.toolHasEnchantment( Enchantments.SILK_TOUCH ) )
                .addItem( ModItems.SPARK_VENT )
                .pop()
            .addElse()
                .addItem( Items.NETHERRACK )
                .pop()
            .build();

    public static final BakedLootTable PLANT = LootManager.create()
            .addGenerator( (rng, ctx) -> {
                ArrayList<ItemStack> stacks = new ArrayList<>();

                if( ctx.getTool().getItem() == Items.SHEARS ) {
                    stacks.add(ctx.getBlockItem());
                }

                return stacks;
            } )
            .build();

    public static void init() {
        // load class
    }

}
