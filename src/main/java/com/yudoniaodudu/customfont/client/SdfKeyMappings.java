package com.yudoniaodudu.customfont.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.yudoniaodudu.customfont.CustomFontMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CustomFontMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SdfKeyMappings {
    public static final KeyMapping OPEN_TUNING = new KeyMapping(
            "key.customfont.open_tuning",
            InputConstants.UNKNOWN.getValue(),
            "key.categories.customfont"
    );

    private SdfKeyMappings() {
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_TUNING);
    }
}
