package net.darktree.stylishoccult.block.property;

import net.minecraft.util.StringIdentifiable;

public enum LavaDemonPart implements StringIdentifiable {
	BODY("body"),
	HEAD("head"),
	EMITTER("emitter");

	private final String name;

	LavaDemonPart(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String asString() {
		return this.name;
	}
}
