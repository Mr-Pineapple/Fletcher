package com.mrpineapple.fletcher.core;

import com.mrpineapple.fletcher.Fletcher;
import com.mrpineapple.fletcher.recipe.FletchingRecipe;
import com.mrpineapple.fletcher.screen.FletchingTableMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRegistry {
    public static MenuType<FletchingTableMenu> FLETCHING_MENU;
    public static RecipeSerializer<FletchingRecipe> FLETCHING_RECIPE_RECIPE_SERIALIZER;
    public static RecipeType<FletchingRecipe> FLETCHING_RECIPE_TYPE;

    public static void register() {
        FLETCHING_MENU = Registry.register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "fletching_table_menu"),
                new MenuType<>(FletchingTableMenu::new, FeatureFlags.VANILLA_SET)
        );

        FLETCHING_RECIPE_RECIPE_SERIALIZER = Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "fletching"),
                new RecipeSerializer<>(FletchingRecipe.CODEC, FletchingRecipe.STREAM_CODEC)
        );

        FLETCHING_RECIPE_TYPE = Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "fletching"),
                new RecipeType<FletchingRecipe>() { }
        );

    }
}
