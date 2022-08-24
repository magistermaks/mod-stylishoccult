package net.darktree.stylishoccult.entity.damage;

import net.minecraft.entity.damage.DamageSource;

public class ModDamageSource extends DamageSource {

	public static final DamageSource SHOCK = new ModDamageSource("stylish_shock").setBypassesArmor().setUsesMagic();
	public static final DamageSource CORRUPTION = new ModDamageSource("stylish_corruption").setBypassesArmor();
	public static final DamageSource SACRIFICE = new ModDamageSource("stylish_sacrifice").setBypassesArmor();
	public static final DamageSource FADING = new ModDamageSource("stylish_fading").setBypassesArmor();
	public static final DamageSource REBOUND = new ModDamageSource("stylish_rebound").setBypassesArmor();

	protected ModDamageSource(String name) {
		super(name);
	}

}
