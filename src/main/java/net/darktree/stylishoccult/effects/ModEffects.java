package net.darktree.stylishoccult.effects;

import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.registry.Registry;

public class ModEffects {

    public static final StatusEffect CORRUPTED_BLOOD = new CorruptedBloodEffect();

    public static void init() {
        Registry.register( Registry.STATUS_EFFECT, new ModIdentifier("corrupted_blood"), CORRUPTED_BLOOD );
    }

}
