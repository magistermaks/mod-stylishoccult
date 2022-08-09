package net.darktree.stylishoccult.item.material;

import net.darktree.stylishoccult.item.ModItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class TwistedBoneToolMaterial implements ToolMaterial {

    public static final TwistedBoneToolMaterial INSTANCE = new TwistedBoneToolMaterial();

    @Override
    public int getDurability() {
        return 210;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 12f;
    }

    @Override
    public float getAttackDamage() {
        return 1.6f;
    }

    @Override
    public int getMiningLevel() {
        return 2;
    }

    @Override
    public int getEnchantability() {
        return 12;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ModItems.TWISTED_BONE);
    }

}
