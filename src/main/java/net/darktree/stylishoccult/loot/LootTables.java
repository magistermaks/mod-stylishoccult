package net.darktree.stylishoccult.loot;

import net.darktree.stylishoccult.blocks.AbstractCandleHolderBlock;
import net.darktree.stylishoccult.blocks.CandleBlock;
import net.darktree.stylishoccult.blocks.LavaDemonBlock;
import net.darktree.stylishoccult.blocks.ModBlocks;
import net.darktree.stylishoccult.blocks.entities.AbstractCandleHolderBlockEntity;
import net.darktree.stylishoccult.blocks.occult.EyesBlock;
import net.darktree.stylishoccult.blocks.occult.ThinFleshBlock;
import net.darktree.stylishoccult.enums.LavaDemonPart;
import net.darktree.stylishoccult.items.ModItems;
import net.darktree.stylishoccult.loot.entry.ConditionEntry;
import net.darktree.stylishoccult.loot.entry.ValveEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
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
                .addItem( ModItems.WAX, 3.2f )
                .dropExperience(2, 10)
                .pop()
            .minimum(1)
            .build();

    public static final LootTable GROWTH = LootManager.create()
            .addValve( (arr, rng, ctx) -> {
                int size = ctx.getState().get(ThinFleshBlock.SIZE);
                int sides = Integer.bitCount( ThinFleshBlock.getBitfield(ctx.getState()) );
                int multiplier = Math.min( rng.nextInt( size + sides ), sides );

                for( ItemStack stack : arr ) stack.setCount( multiplier );
                return arr;
            } )
                .addItem( ModItems.VEINS, 77.7f )
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

    public static final LootTable CANDLE_CONTAINER = LootManager.create()
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

    public static final LootTable CANDLE = LootManager.create()
            .addGenerator( (rng, ctx) -> {
                ArrayList<ItemStack> stacks = new ArrayList<>();
                CompoundTag tag1 = new CompoundTag();
                CompoundTag tag2 = new CompoundTag();
                int layers = ctx.getState().get(CandleBlock.LAYERS);

                if( layers != 1 ) {
                    String l = String.valueOf( ctx.getState().get(CandleBlock.LAYERS) );
                    tag2.putString( "layers", l );
                    tag1.put( "BlockStateTag", tag2 );
                    ItemStack stack = ItemStack.EMPTY;

                    if( ctx.getBlock() == ModBlocks.CANDLE ) {
                        stack = new ItemStack( ModItems.CANDLE );
                    }else if( ctx.getBlock() == ModBlocks.EXTINGUISHED_CANDLE ) {
                        stack = new ItemStack( ModItems.EXTINGUISHED_CANDLE );
                    }

                    stack.setTag( tag1 );
                    stacks.add(stack);
                }

                return stacks;
            } )
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
