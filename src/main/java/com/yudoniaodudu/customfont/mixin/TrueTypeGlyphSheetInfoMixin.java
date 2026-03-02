package com.yudoniaodudu.customfont.mixin;

import com.yudoniaodudu.customfont.client.sdf.SdfGenerator;
import com.yudoniaodudu.customfont.client.sdf.SdfSettings;
import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.mojang.blaze3d.font.TrueTypeGlyphProvider$Glyph$1")
public abstract class TrueTypeGlyphSheetInfoMixin {
    @org.spongepowered.asm.mixin.Unique
    private static boolean customfont$loggedOnce = false;

    @Redirect(
            method = "upload",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;upload(IIIIIIIZZ)V")
    )
    private void customfont$upload(NativeImage image, int a, int xOffset, int yOffset, int d, int e, int w, int h, boolean blur, boolean clamp) {
        if (SdfSettings.enabled()) {
            try {
                long start = System.nanoTime();
                SdfGenerator.convertRegionLuminanceToSdfInPlace(image, 0, 0, w, h, SdfSettings.pixelRange());
                if (!customfont$loggedOnce) {
                    System.out.println("CustomFont: SDF generation successful for glyph. Time: " + (System.nanoTime() - start) + "ns");
                    customfont$loggedOnce = true;
                }
            } catch (Exception ex) {
                System.err.println("CustomFont: SDF generation failed!");
                ex.printStackTrace();
            }
        }
        image.upload(a, xOffset, yOffset, d, e, w, h, blur, clamp);
    }
}