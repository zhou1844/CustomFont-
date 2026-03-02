package com.yudoniaodudu.customfont.mixin;

import com.yudoniaodudu.customfont.client.sdf.SdfSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenSdfIndicatorMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void customfont$renderSdfIndicator(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!SdfSettings.enabled()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        int x = 4;
        int y = 4;
        guiGraphics.drawString(minecraft.font, "SDF 已开启", x, y, 0xFFFFFFFF, true);
    }
}
