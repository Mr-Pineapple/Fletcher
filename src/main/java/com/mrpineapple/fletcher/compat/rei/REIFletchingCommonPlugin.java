package com.mrpineapple.fletcher.compat.rei;

import com.mrpineapple.fletcher.core.ModRegistry;
import com.mrpineapple.fletcher.recipe.FletchingRecipe;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;

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
                .fill(recipe -> new REIFletchingDisplay(
                        List.of(
                                EntryIngredients.ofIngredient(recipe.value().getBaseItem()),
                                EntryIngredients.ofIngredient(recipe.value().getModifierItem())
                        ),
                        List.of(
                                EntryIngredients.of(recipe.value().getResult().create())
                        )
                ));
    }
}
