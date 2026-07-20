package com.mrpineapple.fletcher.core.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record FletchingRecipeInput(ItemStack arrowIn, ItemStack potionIn) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return switch(index) {
            case 0 -> arrowIn;
            case 1 -> potionIn;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
