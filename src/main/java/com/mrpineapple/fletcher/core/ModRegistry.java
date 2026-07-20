package com.mrpineapple.fletcher.core;

import com.mrpineapple.fletcher.Fletcher;
import com.mrpineapple.fletcher.screen.FletchingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModRegistry {
    public static MenuType<FletchingTableMenu> FLETCHING_MENU;

    public static void register() {
        FLETCHING_MENU = Registry.register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "fletching_table_menu"),
                new MenuType<>((syncId, inventory) -> new FletchingTableMenu(syncId, inventory, BlockPos.ZERO), FeatureFlags.VANILLA_SET)
        );
    }
}
