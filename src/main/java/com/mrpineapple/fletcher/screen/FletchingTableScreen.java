package com.mrpineapple.fletcher.screen;

import com.mrpineapple.fletcher.Fletcher;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FletchingTableScreen extends AbstractContainerScreen<@NotNull FletchingTableMenu> {
    private static final Identifier FLETCHING_TABLE_LOCATION = Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "textures/gui/fletching.png");
    private static final Identifier EMPTY_SLOT_GLOWSTONE = Identifier.withDefaultNamespace("container/slot/redstone_dust");
    private static final Identifier EMPTY_SLOT_POTION = Identifier.withDefaultNamespace("container/slot/potion");
    private static final List<Identifier> EMPTY_MODIFIER_SLOTS = List.of(EMPTY_SLOT_GLOWSTONE, EMPTY_SLOT_POTION);
    private final CyclingSlotBackground modifierIcon = new CyclingSlotBackground(1);

    public FletchingTableScreen(@NotNull FletchingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, FLETCHING_TABLE_LOCATION, x, y, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
        this.modifierIcon.extractRenderState(this.menu, graphics, delta, this.leftPos, this.topPos);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.modifierIcon.tick(EMPTY_MODIFIER_SLOTS);
    }
}
