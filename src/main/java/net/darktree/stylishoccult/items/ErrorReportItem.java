package net.darktree.stylishoccult.items;

import net.darktree.stylishoccult.utils.Utils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class ErrorReportItem extends Item {

    public ErrorReportItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tag = stack.getNbt();
        try{
            if( tag != null && !tag.isEmpty() ) {
                String x = String.valueOf( tag.getInt("x") );
                String y = String.valueOf( tag.getInt("y") );
                String z = String.valueOf( tag.getInt("z") );

                tooltip.add( Utils.tooltip( "error_tablet.error." + tag.getString("error") ) );
                tooltip.add( Utils.tooltip( "error_tablet.location", x, y, z ) );
                return;
            }
        }catch(Exception ignore) {}
        tooltip.add( Utils.tooltip( "error_tablet.success") );
    }

}
