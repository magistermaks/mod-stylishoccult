package net.darktree.stylishoccult.loot;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.darktree.stylishoccult.StylishOccult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LootManager {

    private static final Random random = new Random();
    private static final HashMap<Identifier, LootTable> lootTableHashMap = new HashMap<>();

    public static LootTable create() {
        return new LootTable();
    }

    public static void addLootTable( Identifier name, LootTable table ) {
        lootTableHashMap.put(name, table);
    }

    public static LootTable getLootTable( Identifier name ) {
        return lootTableHashMap.get(name);
    }

    public static ArrayList<ItemStack> getEmpty() {
        return new ArrayList<>();
    }

    public static ArrayList<ItemStack> getLoot(Identifier name, LootContext context) {
        LootTable lt = getLootTable(name);

        if( lt != null ) {
            return lt.getLoot( random, context );
        }else{
            StylishOccult.LOGGER.warn( "Non-existent entry '" + name + "' requested from Loot Manager!" );
        }

        return getEmpty();
    }

    public static ArrayList<ItemStack> getLoot(BakedLootTable table, LootContext context) {
        return table == null ? getEmpty() : table.table().getLoot( random, context );
    }

    public static void dropLoot( Identifier name, LootContext context ) {
        ArrayList<ItemStack> itemStacks = LootManager.getLoot( name, context );
        for( ItemStack itemStack : itemStacks ) {
            dropStack( context.getWorld(), context.getPos(), itemStack );
        }
    }

    public static void dropStack(World world, BlockPos pos, ItemStack stack) {
        Block.dropStack(world, pos, stack);
    }

    public static List<ItemStack> dispatch(BlockState state, net.minecraft.loot.context.LootContext.Builder builder, Identifier vanilla, Identifier custom) {
        if( vanilla == LootTables.EMPTY || vanilla == null ) {

            if( custom == LootTables.EMPTY || custom == null ) {
                return LootManager.getEmpty();
            }

            return LootManager.getLoot(custom, getLootContext( state, builder ));

        }

        return getVanilla( state, builder, vanilla );
    }

    public static List<ItemStack> dispatch(BlockState state, net.minecraft.loot.context.LootContext.Builder builder, Identifier vanilla, BakedLootTable custom) {
        if( vanilla == LootTables.EMPTY || vanilla == null ) {
            return LootManager.getLoot(custom, getLootContext( state, builder ));
        }

        return getVanilla( state, builder, vanilla );
    }

    private static LootContext getLootContext( BlockState state, net.minecraft.loot.context.LootContext.Builder builder ) {
        return new LootContext.Builder(builder.getWorld(), new BlockPos(builder.get(LootContextParameters.ORIGIN)))
                .setBlockState(state)
                .setTool(builder.get(LootContextParameters.TOOL))
                .setEntity(builder.getNullable(LootContextParameters.THIS_ENTITY))
                .setBlockEntity(builder.getNullable(LootContextParameters.BLOCK_ENTITY))
                .build();
    }

    private static List<ItemStack> getVanilla( BlockState state, net.minecraft.loot.context.LootContext.Builder builder, Identifier vanilla ) {
        net.minecraft.loot.context.LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
        ServerWorld serverWorld = lootContext.getWorld();
        net.minecraft.loot.LootTable lootTable = serverWorld.getServer().getLootManager().getTable(vanilla);
        return lootTable.generateLoot(lootContext);
    }

}
