package com.mrpineapple.fletcher.compat.jei;

import com.mrpineapple.fletcher.Fletcher;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class JEIFletchingCategory extends AbstractRecipeCategory<JEIFletchingRecipe> {
    public static final IRecipeType<JEIFletchingRecipe> TYPE = IRecipeType.create(Fletcher.MOD_ID, "fletching", JEIFletchingRecipe.class);
    private final IDrawableStatic arrow;

    public JEIFletchingCategory(IGuiHelper guiHelper) {
        super(TYPE, Component.translatable("rei.fletcher.fletching"), guiHelper.createDrawableItemStack(new ItemStack(Items.FLETCHING_TABLE)), 116, 54);
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public void draw(JEIFletchingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 61, (getHeight() - arrow.getHeight()) / 2);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JEIFletchingRecipe recipe, IFocusGroup focuses) {
        var baseSlot = builder.addInputSlot(1, 19).setStandardSlotBackground();
        var modifierSlot = builder.addInputSlot(37, 19).setStandardSlotBackground();

        if(recipe.hasConcreteInputs()) {
            baseSlot.add(recipe.baseStack());
            modifierSlot.add(recipe.modifierStack());
        } else {
            baseSlot.add(recipe.baseIngredient());
            modifierSlot.add(recipe.modifierIngredient());
        }

        builder.addOutputSlot(95, 19).setOutputSlotBackground().add(recipe.result());
    }
}