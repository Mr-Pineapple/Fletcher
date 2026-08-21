package com.mrpineapple.fletcher.compat.jei;

import com.mrpineapple.fletcher.Fletcher;
import com.mrpineapple.fletcher.core.ModRegistry;
import com.mrpineapple.fletcher.recipe.FletchingRecipe;
import com.mrpineapple.fletcher.screen.FletchingTableMenu;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIFletchingPlugin implements IModPlugin {

    @Override
    public Identifier getPluginUid() {
        return Fletcher.id("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIFletchingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.level == null) return;

        List<JEIFletchingRecipe> jeiRecipes = new ArrayList<>();
        var recipes = minecraft.level.recipeAccess().getSynchronizedRecipes().getAllOfType(ModRegistry.FLETCHING_RECIPE_TYPE);

        for(RecipeHolder<FletchingRecipe> holder : recipes) {
            createJeirecipes(minecraft, holder.value(), jeiRecipes);
        }

        addRepairRecipe(Items.BOW, jeiRecipes);
        addRepairRecipe(Items.CROSSBOW, jeiRecipes);
        registration.addRecipes(JEIFletchingCategory.TYPE, jeiRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(JEIFletchingCategory.TYPE, Items.FLETCHING_TABLE);
    }

    private void createJeirecipes(Minecraft minecraft, FletchingRecipe recipe, List<JEIFletchingRecipe> displays) {
        ItemStack arrow = new ItemStack(Items.ARROW);
        ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
        ItemStack potion = new ItemStack(Items.POTION);

        boolean acceptsArrow = recipe.getBaseItem().test(arrow);
        boolean spectralRecipe = acceptsArrow && recipe.getModifierItem().test(glowstone);
        boolean tippedRecipe = acceptsArrow && recipe.getModifierItem().test(potion);

        if(spectralRecipe) {
            addSpectralArrowRecipes(recipe, displays);
            return;
        }

        if(tippedRecipe) {
            addTippedArrowRecipes(minecraft, displays);
            return;
        }

        displays.add(JEIFletchingRecipe.normal(recipe.getBaseItem(), recipe.getModifierItem(), recipe.getResult().create()));
    }

    private void addSpectralArrowRecipes(FletchingRecipe recipe, List<JEIFletchingRecipe> displays) {
        for(int count = 1; count <= 8; count++) {
            ItemStack arrows = new ItemStack(Items.ARROW, count);
            ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST, 1);
            ItemStack result = recipe.getResult().create();
            result.setCount(count);
            displays.add(JEIFletchingRecipe.concrete(arrows, glowstone, result));
        }
    }

    private void addTippedArrowRecipes(Minecraft minecraft, List<JEIFletchingRecipe> displays) {
        if(minecraft.level == null) return;
        var potionRegistry = minecraft.level.registryAccess().lookupOrThrow(Registries.POTION);
        potionRegistry.listElements().forEach(potion -> {
            ItemStack potionStack = PotionContents.createItemStack(Items.POTION, potion);

            for(int count = 1; count <= 8; count++) {
                ItemStack arrows = new ItemStack(Items.ARROW, count);
                ItemStack tippedArrows = PotionContents.createItemStack(Items.TIPPED_ARROW, potion);
                tippedArrows.setCount(count);
                displays.add(JEIFletchingRecipe.concrete(arrows, potionStack.copy(), tippedArrows));
            }
        });
    }

    private void addRepairRecipe(Item item, List<JEIFletchingRecipe> displays) {
        ItemStack damaged = new ItemStack(item);
        int maxDamage = damaged.getMaxDamage();
        int startingDamage = maxDamage / 2;
        damaged.setDamageValue(startingDamage);
        ItemStack repaired = damaged.copy();
        int repairAmount = maxDamage / 8;
        repaired.setDamageValue(Math.max(0, startingDamage - repairAmount));
        displays.add(JEIFletchingRecipe.concrete(damaged, new ItemStack(Items.STRING), repaired));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(FletchingTableMenu.class, ModRegistry.FLETCHING_MENU, JEIFletchingCategory.TYPE, 0, 2, 3, 36);
    }
}
