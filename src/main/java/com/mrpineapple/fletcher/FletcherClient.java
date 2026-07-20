package com.mrpineapple.fletcher;

import com.mrpineapple.fletcher.core.ModRegistry;
import com.mrpineapple.fletcher.screen.FletchingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class FletcherClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModRegistry.FLETCHING_MENU, FletchingTableScreen::new);
        System.out.println("RegisteredClient");
    }
}