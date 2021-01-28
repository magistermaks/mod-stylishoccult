package net.darktree.stylishoccult.loot.entry;

import net.darktree.stylishoccult.loot.LootContext;
import net.darktree.stylishoccult.loot.LootTable;
import net.darktree.stylishoccult.utils.RandUtils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class ValveEntry extends AbstractEntry {

    public static ArrayList<ItemStack> nop( ArrayList<ItemStack> arr, Random rng, LootContext ctx ) {
        return arr;
    }

    public static ArrayList<ItemStack> singular( ArrayList<ItemStack> arr, Random rng, LootContext ctx ) {
        if( !arr.isEmpty() ){
            ItemStack stack = arr.get(0);
            arr.clear();
            arr.add( stack );
        }
        return arr;
    }

    public static ArrayList<ItemStack> fortune( ArrayList<ItemStack> arr, Random rng, LootContext ctx ) {
        int level = ctx.toolGetEnchantment(Enchantments.FORTUNE);
        if( level > 0 ) {
            for( ItemStack stack : arr ) {
                stack.setCount( stack.getCount() * getFortuneMultiplier( rng, level ) );
            }
        }
        return arr;
    }

    private static int getFortuneMultiplier( Random rng, int level ) {
        // based on table found on Minecraft Wiki
        // minecraft.gamepedia.com/Fortune
        switch( level ) {
            case 1: return RandUtils.getBool( 66.6f, rng ) ? 1 : 2;
            case 2: return RandUtils.getBool( 50.0f, rng ) ? 1 : ( RandUtils.getBool( 50.0f, rng ) ? 2 : 3 );
            case 3: return RandUtils.getBool( 40.0f, rng ) ? 1 : ( RandUtils.getBool( 33.3f, rng ) ? 2 : ( RandUtils.getBool( 50.0f, rng ) ? 3 : 4 ) );
            default: return 1;
        }
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