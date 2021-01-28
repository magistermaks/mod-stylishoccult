package net.darktree.stylishoccult.loot;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootManager {

    private static final Random random = new Random();

    public static LootTable create() {
        return new LootTable();
    }

    public static ArrayList<ItemStack> getEmpty() {
        return new ArrayList<>();
    }

    public static ArrayList<ItemStack> getLoot(BakedLootTable table, LootContext context) {
        return table == null ? getEmpty() : table.table().getLoot( random, context );
    }

    public static List<ItemStack> dispatch(BlockState state, net.minecraft.loot.context.LootContext.Builder builder, Identifier vanilla, BakedLootTable custom) {

        net.minecraft.loot.context.LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
        ServerWorld serverWorld = lootContext.getWorld();
        net.minecraft.loot.LootTable lootTable = serverWorld.getServer().getLootManager().getTable(vanilla);

        if( lootTable == net.minecraft.loot.LootTable.EMPTY ) {
            return LootManager.getLoot(custom, getLootContext( state, builder ));
        }else{
            return serverWorld.getServer().getLootManager().getTable(vanilla).generateLoot(lootContext);
        }

    }

    private static LootContext getLootContext( BlockState state, net.minecraft.loot.context.LootContext.Builder builder ) {
        return new LootContext.Builder(builder.getWorld(), new BlockPos(builder.get(LootContextParameters.ORIGIN)))
                .setBlockState(state)
                .setTool(builder.get(LootContextParameters.TOOL))
                .setEntity(builder.getNullable(LootContextParameters.THIS_ENTITY))
                .setBlockEntity(builder.getNullable(LootContextParameters.BLOCK_ENTITY))
                .build();
    }

    public static ArrayList<ItemStack> getAsArray( ItemStack stack ) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        stacks.add( stack );
        return stacks;
    }

}
