package com.yudoniaodudu.customfont.client.render;

import com.yudoniaodudu.customfont.client.sdf.SdfSettings;
import com.yudoniaodudu.customfont.CustomFontMod;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = CustomFontMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientShaders {
    public static ShaderInstance SDF_TEXT;
    public static ShaderInstance SDF_TEXT_OFFSET;

    private ClientShaders() {
    }

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), new ResourceLocation(CustomFontMod.MODID, "sdf_text"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP),
                shader -> {
                    SDF_TEXT = shader;
                    applySdfUniforms();
                    System.out.println("CustomFont: Registered sdf_text shader: " + shader);
                }
        );
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), new ResourceLocation(CustomFontMod.MODID, "sdf_text_offset"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP),
                shader -> {
                    SDF_TEXT_OFFSET = shader;
                    applySdfUniforms();
                    System.out.println("CustomFont: Registered sdf_text_offset shader: " + shader);
                }
        );
    }

    public static void applySdfUniforms() {
        applySdfUniforms(SDF_TEXT);
        applySdfUniforms(SDF_TEXT_OFFSET);
    }

    private static void applySdfUniforms(ShaderInstance shader) {
        if (shader == null) {
            return;
        }
        if (shader.getUniform("SdfWeight") != null) {
            shader.getUniform("SdfWeight").set(SdfSettings.weight());
        }
    }
}
