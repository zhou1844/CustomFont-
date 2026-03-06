package com.yudoniaodudu.customfont.client;

import com.yudoniaodudu.customfont.CustomFontMod;
import com.yudoniaodudu.customfont.client.render.ClientShaders;
import com.yudoniaodudu.customfont.client.sdf.SdfSettings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CustomFontMod.MODID, value = Dist.CLIENT)
public final class SdfClientControls {
    private static boolean initialized;

    private SdfClientControls() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (!initialized) {
            SdfSettings.load();
            ClientShaders.applySdfUniforms();
            initialized = true;
        }
        ClientShaders.applySdfUniforms();
        if (mc.screen == null && SdfKeyMappings.OPEN_TUNING.consumeClick()) {
            mc.setScreen(new SdfTuningScreen(null));
        }
    }
}
