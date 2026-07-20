package com.mrpineapple.fletcher.core;

import com.mrpineapple.fletcher.Fletcher;
import com.mrpineapple.fletcher.core.recipe.FletchingTableRecipe;
import com.mrpineapple.fletcher.screen.FletchingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRegistry {
    public static MenuType<FletchingTableMenu> FLETCHING_MENU;
    public static RecipeType<FletchingTableRecipe> FLETCHING_RECIPE_TYPE;
    public static RecipeSerializer<FletchingTableRecipe> FLETCHING_RECIPE_SERIALIZER;

    public static void register() {
        FLETCHING_MENU = Registry.register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "fletching_table_menu"),
                new MenuType<>((syncId, inventory) -> new FletchingTableMenu(syncId, inventory, BlockPos.ZERO), FeatureFlags.VANILLA_SET)
        );

        FLETCHING_RECIPE_TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, Fletcher.MOD_ID, new RecipeType<FletchingTableRecipe>() {
            @Override
            public String toString() {
                return "fletching_table";
            }
        });

        FLETCHING_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Fletcher.MOD_ID, FletchingTableRecipe.SERIALIZER);

    }
}
