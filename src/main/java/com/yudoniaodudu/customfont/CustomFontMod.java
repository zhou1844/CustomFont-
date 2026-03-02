package com.yudoniaodudu.customfont;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.yudoniaodudu.customfont.client.pack.ExternalFontPackResources;

@Mod(CustomFontMod.MODID)
public final class CustomFontMod
{
    public static final String MODID = "customfont";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CustomFontMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void onAddPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() != PackType.CLIENT_RESOURCES) {
                return;
            }

            event.addRepositorySource((packConsumer) -> {
                Pack pack = Pack.readMetaAndCreate(
                        "customfont_external_font",
                        Component.literal("Custom Font"),
                        true,
                        id -> new ExternalFontPackResources(id, true),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.DEFAULT
                );

                if (pack != null) {
                    packConsumer.accept(pack);
                }
            });
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("CustomFontMod common setup");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("CustomFontMod client setup");
        }
    }
}
