package net.darktree.stylishoccult.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {

    public static final FoodComponent FLESH_FOOD = new FoodComponent.Builder()
            .hunger(3)
            .saturationModifier(0.1F)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.91f)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 0), 0.85f)
            .meat()
            .build();

    public static final FoodComponent BLOOD = new FoodComponent.Builder()
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 1)
            .build();

}
