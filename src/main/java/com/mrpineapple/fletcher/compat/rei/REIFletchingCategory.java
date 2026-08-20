package com.mrpineapple.fletcher.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class REIFletchingCategory implements DisplayCategory<REIFletchingDisplay> {
    @Override
    public CategoryIdentifier<? extends REIFletchingDisplay> getCategoryIdentifier() {
        return REIFletchingDisplay.CATEGORY;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.fletcher.fletching");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.FLETCHING_TABLE);
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public List<Widget> setupDisplay(REIFletchingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int x = bounds.getCenterX();
        int y = bounds.getCenterY();

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createSlot(new Point(x - 50, y - 9)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(x - 25, y - 9)).entries(display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createArrow(new Point(x + 2, y - 8)));
        widgets.add(Widgets.createSlot(new Point(x + 40, y - 9)).entries(display.getOutputEntries().get(0)).markOutput());

        return widgets;
    }
}
