package com.mrpineapple.fletcher.compat.jei;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record JEIFletchingRecipe(
        Ingredient baseIngredient,
        Ingredient modifierIngredient,
        ItemStack baseStack,
        ItemStack modifierStack,
        ItemStack result) {

    public static JEIFletchingRecipe normal(Ingredient base, Ingredient modifier, ItemStack result) {
        return new JEIFletchingRecipe(base, modifier, ItemStack.EMPTY, ItemStack.EMPTY, result);
    }

    public static JEIFletchingRecipe concrete(ItemStack base, ItemStack modifier, ItemStack result) {
        return new JEIFletchingRecipe(null, null, base, modifier, result);
    }

    public boolean hasConcreteInputs() {
        return !baseStack.isEmpty() && !modifierStack.isEmpty();
    }
}
