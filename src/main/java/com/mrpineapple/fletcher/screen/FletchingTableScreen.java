package com.mrpineapple.fletcher.screen;

import com.mrpineapple.fletcher.Fletcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FletchingTableScreen extends AbstractContainerScreen<@NotNull FletchingTableMenu> {
    public static final Identifier FLETCHING_TABLE_LOCATION = Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "textures/gui/fletching.png");

    public FletchingTableScreen(@NotNull FletchingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, FLETCHING_TABLE_LOCATION, x, y, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }
}
