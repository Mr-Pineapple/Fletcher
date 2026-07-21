package com.mrpineapple.fletcher.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public class FadingSlotIcon {
    private Identifier current;
    private Identifier previous;

    private int fadeTicks = 0;
    private static final int FADE_TIME = 4;

    public void set(Identifier sprite) {
        if(!Objects.equals(current, sprite)) {
            previous = current;
            current = sprite;
            fadeTicks = FADE_TIME;
        }
    }

    public void tick() {
        if(fadeTicks > 0) {
            fadeTicks--;
        }
    }

    public void render(GuiGraphicsExtractor graphics, int x, int y) {
        if (current == null) {
            return;
        }

        float progress = 1.0F - (fadeTicks / (float) FADE_TIME);

        if(previous != null && progress < 0.5) {
            float oldProgress = 1.0F - (progress * 2.0F);
            int alpha = (int)(255 * oldProgress);
            int color = (alpha << 24) | 0xFFFFFF;

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, previous, x, y, 16, 16, color);
        }

        if(progress >= 0.5F) {
            float newProgress = (progress - 0.5F) * 2.0F;
            int alpha = (int)(255 * newProgress);
            int color = (alpha << 24) | 0xFFFFFF;

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, current, x, y, 16, 16, color);
        }
    }
}
