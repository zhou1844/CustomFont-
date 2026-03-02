package com.yudoniaodudu.customfont.client;

import com.yudoniaodudu.customfont.CustomFontMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CustomFontMod.MODID, value = Dist.CLIENT)
public final class FontVerifyToast {
    private static boolean sent;

    private FontVerifyToast() {
    }

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        if (sent) {
            return;
        }
        sent = true;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        mc.player.displayClientMessage(
                Component.literal("字体测试：LOLITA 123 你好").withStyle(s -> s.withFont(new ResourceLocation(CustomFontMod.MODID, "custom"))),
                false
        );
    }
}