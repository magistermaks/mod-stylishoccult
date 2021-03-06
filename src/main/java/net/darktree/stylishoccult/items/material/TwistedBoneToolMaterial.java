package net.darktree.stylishoccult.items.material;

import net.darktree.stylishoccult.items.ModItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class TwistedBoneToolMaterial implements ToolMaterial {

    public static final TwistedBoneToolMaterial INSTANCE = new TwistedBoneToolMaterial();

    @Override
    public int getDurability() {
        return 200;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 12f;
    }

    @Override
    public float getAttackDamage() {
        return 1.5f;
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
