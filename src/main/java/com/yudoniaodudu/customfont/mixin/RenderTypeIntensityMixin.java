package com.yudoniaodudu.customfont.mixin;

import com.yudoniaodudu.customfont.client.render.SdfRenderTypes;
import com.yudoniaodudu.customfont.client.sdf.SdfSettings;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public abstract class RenderTypeIntensityMixin {
    @Inject(method = "textIntensity(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;", at = @At("HEAD"), cancellable = true)
    private static void customfont$textIntensity(ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        if (SdfSettings.enabled()) {
            cir.setReturnValue(SdfRenderTypes.textIntensity(texture));
        }
    }

    @Inject(method = "textIntensityPolygonOffset(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;", at = @At("HEAD"), cancellable = true)
    private static void customfont$textIntensityPolygonOffset(ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        if (SdfSettings.enabled()) {
            cir.setReturnValue(SdfRenderTypes.textIntensityPolygonOffset(texture));
        }
    }

    @Inject(method = "textIntensitySeeThrough(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;", at = @At("HEAD"), cancellable = true)
    private static void customfont$textIntensitySeeThrough(ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        if (SdfSettings.enabled()) {
            cir.setReturnValue(SdfRenderTypes.textIntensitySeeThrough(texture));
        }
    }
}