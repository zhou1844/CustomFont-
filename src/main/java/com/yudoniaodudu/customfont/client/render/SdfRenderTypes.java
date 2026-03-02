package com.yudoniaodudu.customfont.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import com.yudoniaodudu.customfont.mixin.RenderStateShardAccessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SdfRenderTypes {
    private static final Map<ResourceLocation, RenderType> TEXT_INTENSITY = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, RenderType> TEXT_INTENSITY_SEE_THROUGH = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, RenderType> TEXT_INTENSITY_POLYGON_OFFSET = new ConcurrentHashMap<>();
    private static final RenderStateShard.ShaderStateShard SHADER = new RenderStateShard.ShaderStateShard(() -> {
        if (ClientShaders.SDF_TEXT == null) {
            System.err.println("CustomFont: SDF_TEXT shader is null! This will likely cause a crash.");
        }
        return ClientShaders.SDF_TEXT;
    });

    private SdfRenderTypes() {
    }

    public static RenderType textIntensity(ResourceLocation texture) {
        return TEXT_INTENSITY.computeIfAbsent(texture, t -> RenderType.create(
                "customfont_sdf_text_intensity",
                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(t, true, false))
                        .setTransparencyState(RenderStateShardAccessor.getTRANSLUCENT_TRANSPARENCY())
                        .setLightmapState(RenderStateShardAccessor.getLIGHTMAP())
                        .createCompositeState(false)
        ));
    }

    public static RenderType textIntensityPolygonOffset(ResourceLocation texture) {
        return TEXT_INTENSITY_POLYGON_OFFSET.computeIfAbsent(texture, t -> RenderType.create(
                "customfont_sdf_text_intensity_polygon_offset",
                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(t, true, false))
                        .setTransparencyState(RenderStateShardAccessor.getTRANSLUCENT_TRANSPARENCY())
                        .setLightmapState(RenderStateShardAccessor.getLIGHTMAP())
                        .setLayeringState(RenderStateShardAccessor.getPOLYGON_OFFSET_LAYERING())
                        .createCompositeState(false)
        ));
    }

    public static RenderType textIntensitySeeThrough(ResourceLocation texture) {
        return TEXT_INTENSITY_SEE_THROUGH.computeIfAbsent(texture, t -> RenderType.create(
                "customfont_sdf_text_intensity_see_through",
                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(t, true, false))
                        .setTransparencyState(RenderStateShardAccessor.getTRANSLUCENT_TRANSPARENCY())
                        .setLightmapState(RenderStateShardAccessor.getLIGHTMAP())
                        .setDepthTestState(RenderStateShardAccessor.getNO_DEPTH_TEST())
                        .setWriteMaskState(RenderStateShardAccessor.getCOLOR_WRITE())
                        .createCompositeState(false)
        ));
    }

    // Removed get() method as fields are public in Mojang mappings or accessible via AT if needed.
    // In Mojang mappings (which are used here), these fields are public static final.
}