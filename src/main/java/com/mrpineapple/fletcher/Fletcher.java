package com.mrpineapple.fletcher;

import com.mrpineapple.fletcher.core.InteractionEvent;
import com.mrpineapple.fletcher.core.ModRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.resources.Identifier;

public class Fletcher implements ModInitializer {
	public static final String MOD_ID = "fletcher";

	@Override
	public void onInitialize() {
		System.out.println("RegisteredMain");
		ModRegistry.register();
		InteractionEvent.blockInteraction();
		RecipeSynchronization.synchronizeRecipeSerializer(ModRegistry.FLETCHING_RECIPE_RECIPE_SERIALIZER);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
