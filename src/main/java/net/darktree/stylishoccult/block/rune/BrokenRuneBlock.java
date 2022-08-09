package net.darktree.stylishoccult.block.rune;

import net.darktree.stylishoccult.StylishOccult;
import net.darktree.stylishoccult.script.component.RuneException;
import net.darktree.stylishoccult.script.component.RuneExceptionType;
import net.darktree.stylishoccult.script.component.RuneType;
import net.darktree.stylishoccult.script.engine.Script;
import net.darktree.stylishoccult.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrokenRuneBlock extends RuneBlock {

    public BrokenRuneBlock(String name) {
        super(RuneType.TRANSFER, name);
    }

    @Override
    public void apply(Script script) {
        throw RuneException.of(RuneExceptionType.BROKEN);
    }

    @Override
    public String getTranslationKey() {
        return "block." + StylishOccult.NAMESPACE + ".damaged_runestone";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add( Utils.tooltip( "damaged_rune" ) );
    }

}
