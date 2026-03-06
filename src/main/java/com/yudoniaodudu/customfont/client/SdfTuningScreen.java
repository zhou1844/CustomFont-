package com.yudoniaodudu.customfont.client;

import com.yudoniaodudu.customfont.CustomFontMod;
import com.yudoniaodudu.customfont.client.render.ClientShaders;
import com.yudoniaodudu.customfont.client.sdf.SdfSettings;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public final class SdfTuningScreen extends Screen {
    private static final ResourceLocation CUSTOM_FONT = new ResourceLocation(CustomFontMod.MODID, "custom");
    private final Screen parent;

    public SdfTuningScreen(Screen parent) {
        super(Component.translatable("screen.customfont.sdf_tuning"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int topY = this.height / 2 - 40;
        double sliderValue = normalize(SdfSettings.weight());
        this.addRenderableWidget(new WeightSlider(centerX - 110, topY, 220, 20, sliderValue));
        this.addRenderableWidget(Button.builder(Component.translatable("controls.reset"), b -> {
            SdfSettings.setWeight(0.0f);
            SdfSettings.save();
            ClientShaders.applySdfUniforms();
            this.rebuildWidgets();
        }).pos(centerX - 110, topY + 32).size(106, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> this.onClose())
                .pos(centerX + 4, topY + 32).size(106, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 64, 0xFFFFFF);
        Component preview = Component.translatable("screen.customfont.preview_text")
                .withStyle(style -> style.withFont(CUSTOM_FONT));
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(2.2f, 2.2f, 1.0f);
        guiGraphics.drawCenteredString(
                this.font,
                preview,
                (int) ((this.width / 2.0f) / 2.2f),
                (int) ((this.height / 2.0f + 62.0f) / 2.2f),
                0xFFFFFFFF
        );
        guiGraphics.pose().popPose();
    }

    @Override
    public void onClose() {
        SdfSettings.save();
        this.minecraft.setScreen(parent);
    }

    private static double normalize(float value) {
        float min = SdfSettings.minWeight();
        float max = SdfSettings.maxWeight();
        return (value - min) / (max - min);
    }

    private static float denormalize(double value) {
        float min = SdfSettings.minWeight();
        float max = SdfSettings.maxWeight();
        return (float) (min + (max - min) * value);
    }

    private static final class WeightSlider extends AbstractSliderButton {
        private WeightSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Component.empty(), value);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int percent = (int) Math.round(this.value * 100.0);
            this.setMessage(Component.literal(String.format(Locale.ROOT, "%s: %d%%", Component.translatable("screen.customfont.weight").getString(), percent)));
        }

        @Override
        protected void applyValue() {
            SdfSettings.setWeight(denormalize(this.value));
            ClientShaders.applySdfUniforms();
        }
    }
}
