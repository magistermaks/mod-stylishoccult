package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;

public class ModIdentifier extends net.minecraft.util.Identifier {

    public ModIdentifier(String name) {
        super(StylishOccult.NAMESPACE, name);
    }

}
