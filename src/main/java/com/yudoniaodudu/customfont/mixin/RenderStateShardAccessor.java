package com.yudoniaodudu.customfont.mixin;

import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderStateShard.class)
public interface RenderStateShardAccessor {
    @Accessor("TRANSLUCENT_TRANSPARENCY")
    static RenderStateShard.TransparencyStateShard getTRANSLUCENT_TRANSPARENCY() {
        throw new AssertionError();
    }

    @Accessor("LIGHTMAP")
    static RenderStateShard.LightmapStateShard getLIGHTMAP() {
        throw new AssertionError();
    }

    @Accessor("POLYGON_OFFSET_LAYERING")
    static RenderStateShard.LayeringStateShard getPOLYGON_OFFSET_LAYERING() {
        throw new AssertionError();
    }

    @Accessor("NO_DEPTH_TEST")
    static RenderStateShard.DepthTestStateShard getNO_DEPTH_TEST() {
        throw new AssertionError();
    }

    @Accessor("COLOR_WRITE")
    static RenderStateShard.WriteMaskStateShard getCOLOR_WRITE() {
        throw new AssertionError();
    }
}
