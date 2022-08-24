package net.darktree.stylishoccult.utils;

import net.darktree.stylishoccult.StylishOccult;

public class ModIdentifier extends net.minecraft.util.Identifier {

	public ModIdentifier(String path) {
		super(StylishOccult.NAMESPACE, path);
	}

	public static ModIdentifier of(String path) {
		return new ModIdentifier(path);
	}

}
