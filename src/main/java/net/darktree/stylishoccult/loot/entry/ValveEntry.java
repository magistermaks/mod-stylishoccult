package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class ValveEntry extends AbstractEntry {

    public static ArrayList<ItemStack> open(ArrayList<ItemStack> arr, Random rng, LootContext ctx) {
        return arr;
    }

    public static ArrayList<ItemStack> singular(ArrayList<ItemStack> arr, Random rng, LootContext ctx) {
        if( !arr.isEmpty() ){
            ItemStack stack = arr.get(0);
            arr.clear();
            arr.add( stack );
        }

        return arr;
    }

    public static ArrayList<ItemStack> fortune(ArrayList<ItemStack> arr, Random rng, LootContext ctx) {
        int level = ctx.toolGetEnchantment(Enchantments.FORTUNE);
        if( level > 0 ) {
            for( ItemStack stack : arr ) {
                stack.setCount( stack.getCount() * getFortuneMultiplier( rng, level ) );
            }
        }

        return arr;
    }

    private static int getFortuneMultiplier(Random random, int level) {
        // based on table found on Minecraft Wiki
        // minecraft.gamepedia.com/Fortune

        return switch (level) {
            case 1 -> RandUtils.getBool(66.6f, random) ? 1 : 2;
            case 2 -> RandUtils.getBool(50.0f, random) ? 1 : (RandUtils.getBool(50.0f, random) ? 2 : 3);
            case 3 -> RandUtils.getBool(40.0f, random) ? 1 : (RandUtils.getBool(33.3f, random) ? 2 : (RandUtils.getBool(50.0f, random) ? 3 : 4));
            default -> 1;
        };
    }

    private final Valve valve;
    private final LootTable table;

    public ValveEntry( Valve valve ) {
        this.valve = valve;
        this.table = new LootTable();
    }

    public LootTable getTable() {
        return this.table;
    }

    @Override
    public ArrayList<ItemStack> getLoot(Random random, LootContext context) {
        return valve.call( table.getLoot(random, context), random, context );
    }

    public interface Valve {
        ArrayList<ItemStack> call(ArrayList<ItemStack> stacks, Random random, LootContext context);
    }
}