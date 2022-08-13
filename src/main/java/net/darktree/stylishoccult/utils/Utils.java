package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;

public class Utils {

    public static Box box(float x1, float y1, float z1, float x2, float y2, float z2) {
        return new Box(x1 / 16d, y1 / 16d, z1 / 16d, x2 / 16d, y2 / 16d, z2 / 16d);
    }

    public static MutableText tooltip(String text, Object... args) {
        return new TranslatableText("tooltip." + StylishOccult.NAMESPACE + "." + text, args).formatted(Formatting.GRAY);
    }

}
