package com.yudoniaodudu.customfont.client.render;

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

    private ClientShaders() {
    }

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), new ResourceLocation(CustomFontMod.MODID, "sdf_text"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP),
                shader -> {
                    SDF_TEXT = shader;
                    System.out.println("CustomFont: Registered sdf_text shader: " + shader);
                }
        );
    }
}