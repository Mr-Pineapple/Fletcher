package com.mrpineapple.fletcher.compat.rei;

import com.mrpineapple.fletcher.core.ModRegistry;
import com.mrpineapple.fletcher.recipe.FletchingRecipe;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.ArrayList;
import java.util.List;

public class REIFletchingCommonPlugin implements REICommonPlugin {
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(REIFletchingDisplay.CATEGORY.getIdentifier(), REIFletchingDisplay.SERIALIZER);
    }

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(FletchingRecipe.class)
                .filterType(ModRegistry.FLETCHING_RECIPE_TYPE)
                .fillMultiple(recipeHolder -> createDisplays(recipeHolder.value()));
    }

    private static List<REIFletchingDisplay> createDisplays(FletchingRecipe recipe) {
        List<REIFletchingDisplay> displays = new ArrayList<>();
        ItemStack arrowStack = new ItemStack(Items.ARROW);
        ItemStack potionStack = new ItemStack(Items.POTION);
        boolean isPotionArrowRecipe = recipe.getBaseItem().test(arrowStack) && recipe.getModifierItem().test(potionStack);

        if(isPotionArrowRecipe) {
            addPotionDisplays(recipe, displays);
            return displays;
        }

        displays.add(new REIFletchingDisplay(List.of(
                EntryIngredients.ofIngredient(recipe.getBaseItem()),
                EntryIngredients.ofIngredient(recipe.getModifierItem())
        ), List.of(
                EntryIngredients.of(recipe.getResult().create())
        )));
        return displays;
    }

    private static void addPotionDisplays(FletchingRecipe recipe, List<REIFletchingDisplay> displays) {
        RegistryAccess registryAccess = BasicDisplay.registryAccess();
        Registry<Potion> potionRegistry = registryAccess.lookupOrThrow(Registries.POTION);
        potionRegistry.listElements().forEach(potionHolder -> {
            ItemStack inputPotion = PotionContents.createItemStack(Items.POTION, potionHolder);
            ItemStack output = recipe.getResult().create();
            PotionContents contents = inputPotion.get(DataComponents.POTION_CONTENTS);

            if(contents != null) {
                output.set(DataComponents.POTION_CONTENTS, contents);
            }

            displays.add(new REIFletchingDisplay(List.of(
                    EntryIngredients.of(Items.ARROW),
                    EntryIngredients.of(inputPotion)
            ), List.of(
                    EntryIngredients.of(output)
            )));
        });
    }

}
