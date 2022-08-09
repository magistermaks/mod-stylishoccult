package net.darktree.stylishoccult.script.component;

public enum SafeMode {
	SCHEDULED,
	ENABLED,
	DISABLED;

	public static SafeMode from(int ordinal) {
		return values()[ordinal];
	}

	public SafeMode advance() {
		return this == SCHEDULED ? ENABLED : DISABLED;
	}
}
