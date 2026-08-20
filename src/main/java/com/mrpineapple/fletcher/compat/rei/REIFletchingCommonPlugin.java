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
import net.minecraft.world.item.Item;
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
        ItemStack bow = new ItemStack(Items.BOW);
        ItemStack crossBow = new ItemStack(Items.CROSSBOW);
        ItemStack string = new ItemStack(Items.STRING);
        boolean isBowRepairRecipe = (recipe.getBaseItem().test(bow) || recipe.getBaseItem().test(crossBow)) && recipe.getModifierItem().test(string);
        boolean isPotionArrowRecipe = recipe.getBaseItem().test(arrowStack) && recipe.getModifierItem().test(potionStack);

        if(isBowRepairRecipe) {
            addBowRepairDisplays(recipe, displays);
            return displays;
        }

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

    private static void addBowRepairDisplays(FletchingRecipe recipe, List<REIFletchingDisplay> displays) {
        ItemStack bow = new ItemStack(Items.BOW);
        ItemStack crossBow = new ItemStack(Items.CROSSBOW);
        if(recipe.getBaseItem().test(bow)) {
            addRepairDisplay(Items.BOW, 0.75f, 1, displays);
            addRepairDisplay(Items.BOW, 0.75f, 4, displays);
        }
        if(recipe.getBaseItem().test(crossBow)) {
            addRepairDisplay(Items.CROSSBOW, 0.75f, 1, displays);
            addRepairDisplay(Items.CROSSBOW, 0.75f, 4, displays);
        }
    }

    private static void addRepairDisplay(Item item, float damagePercentage, int stringCount, List<REIFletchingDisplay> displays) {
        ItemStack damaged = new ItemStack(item);
        int maxDamage = damaged.getMaxDamage();;

        damaged.setDamageValue((int) (maxDamage * damagePercentage));

        ItemStack strings = new ItemStack(Items.STRING, stringCount);
        ItemStack repaired = damaged.copy();
        int repairAmount = (maxDamage / 8) * stringCount;

        repaired.setDamageValue(Math.max(0, damaged.getDamageValue() - repairAmount));

        displays.add(new REIFletchingDisplay(List.of(
                EntryIngredients.of(damaged),
                EntryIngredients.of(strings)
        ), List.of(
                EntryIngredients.of(repaired)
        )));
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
