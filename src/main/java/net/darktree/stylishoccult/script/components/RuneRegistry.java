package net.darktree.stylishoccult.script.components;

import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.darktree.stylishoccult.blocks.runes.RuneBlock;
import net.darktree.stylishoccult.script.elements.NumericElement;
import net.darktree.stylishoccult.script.elements.StackElement;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class RuneRegistry {

    private static final HashMap<String, RuneBlock> runes = new HashMap<>();
    private static final Object2ShortOpenHashMap<Class<?>> classToId = new Object2ShortOpenHashMap<>();
    private static final ArrayList<StackElement.Factory> idToFactory = new ArrayList<>();

    public static RuneBlock putRune(String name, RuneBlock rune ) {
        return runes.put(name, rune);
    }

    public static RuneBlock getRune(String name) {
        return runes.get(name);
    }

    public static <T extends StackElement> void registerElement(Class<T> clazz, StackElement.Factory factory) {
        short id = (short) idToFactory.size();
        idToFactory.add(factory);
        classToId.put(clazz, id);
    }

    @ApiStatus.Internal
    public static StackElement.Factory getElementFactory(int id) {
        return idToFactory.get(id);
    }

    @ApiStatus.Internal
    public static <T extends StackElement> short getElementIdentifier(Class<T> clazz) {
        return classToId.getShort(clazz);
    }

    static {
        registerElement(NumericElement.class, NumericElement::new);
    }

}
