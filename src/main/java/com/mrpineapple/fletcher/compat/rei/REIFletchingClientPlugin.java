package com.mrpineapple.fletcher.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.level.block.Blocks;

public class REIFletchingClientPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new REIFletchingCategory());
        registry.addWorkstations(REIFletchingDisplay.CATEGORY, EntryStacks.of(Blocks.FLETCHING_TABLE));
    }
}
