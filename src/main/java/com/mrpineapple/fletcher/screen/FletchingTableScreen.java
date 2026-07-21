package com.mrpineapple.fletcher.screen;

import com.mrpineapple.fletcher.Fletcher;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FletchingTableScreen extends AbstractContainerScreen<@NotNull FletchingTableMenu> {
    private static final Identifier FLETCHING_TABLE_LOCATION = Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "textures/gui/fletching.png");

    private static final Identifier EMPTY_SLOT_GLOWSTONE = Identifier.withDefaultNamespace("container/slot/redstone_dust");
    private static final Identifier EMPTY_SLOT_POTION = Identifier.withDefaultNamespace("container/slot/potion");
    private static final Identifier EMPTY_SLOT_STRING = Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "container/empty_string");

    private static final Identifier EMPTY_SLOT_ARROW = Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "container/empty_arrow");
    private static final Identifier EMPTY_SLOT_BOW = Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "container/empty_bow");
    private static final Identifier EMPTY_SLOT_CROSSBOW = Identifier.fromNamespaceAndPath(Fletcher.MOD_ID, "container/empty_crossbow");

    private int recipeHint = 0;
    private int recipeHintTicks = 0;

    public FletchingTableScreen(@NotNull FletchingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, FLETCHING_TABLE_LOCATION, x, y, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);

        renderSlotHints(graphics);
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        if(++recipeHintTicks >= 30) {
            recipeHintTicks = 0;
            recipeHint = (recipeHint + 1) % 4;
        }
    }

    private void renderSlotHints(GuiGraphicsExtractor graphics) {
        ItemStack base = this.menu.getSlot(0).getItem();
        ItemStack modifier = this.menu.getSlot(1).getItem();

        Identifier baseSprite = null;
        Identifier modifierSprite = null;

        if(base.isEmpty() && modifier.isEmpty()) {
            switch (recipeHint) {
                case 0 -> {
                    baseSprite = EMPTY_SLOT_BOW;
                    modifierSprite = EMPTY_SLOT_STRING;
                }
                case 1 -> {
                    baseSprite = EMPTY_SLOT_CROSSBOW;
                    modifierSprite = EMPTY_SLOT_STRING;
                }
                case 2 -> {
                    baseSprite = EMPTY_SLOT_ARROW;
                    modifierSprite = EMPTY_SLOT_POTION;
                }
                case 3 -> {
                    baseSprite = EMPTY_SLOT_ARROW;
                    modifierSprite = EMPTY_SLOT_GLOWSTONE;
                }
            }
        }
        else if(!base.isEmpty() && modifier.isEmpty()) {
            if(base.is(Items.BOW) || base.is(Items.CROSSBOW)) {
                modifierSprite = EMPTY_SLOT_STRING;
            }
            else if(base.is(Items.ARROW)) {
                modifierSprite = (recipeHint % 2 == 0) ? EMPTY_SLOT_POTION : EMPTY_SLOT_GLOWSTONE;
            }
        }
        else if(base.isEmpty() && !modifier.isEmpty()) {
            if(modifier.is(Items.STRING)) {
                baseSprite = (recipeHint % 2 == 0) ? EMPTY_SLOT_BOW : EMPTY_SLOT_CROSSBOW;
            }
            else if(modifier.is(Items.POTION) || modifier.is(Items.GLOWSTONE_DUST)) {
                baseSprite = EMPTY_SLOT_ARROW;
            }
        }

        if(baseSprite != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, baseSprite, this.leftPos + 27, this.topPos + 35, 16, 16);
        }
        if(modifierSprite != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, modifierSprite, this.leftPos + 76, this.topPos + 35, 16, 16);
        }
    }
}
