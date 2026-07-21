package com.mrpineapple.fletcher.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record FletchingRecipeInput(ItemStack baseItem, ItemStack modifierItem) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> this.baseItem;
            case 1 -> this.modifierItem;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
