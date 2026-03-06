package com.yudoniaodudu.customfont.client.sdf;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SdfSettings {
    private static final Gson GSON = new Gson();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("customfont_sdf.json");
    private static final float MIN_WEIGHT = -0.12f;
    private static final float MAX_WEIGHT = 0.08f;
    private static volatile float weight = 0.0f;

    private SdfSettings() {
    }

    public static boolean enabled() {
        return true;
    }

    public static int pixelRange() {
        return 12;
    }

    public static float weight() {
        return weight;
    }

    public static void setWeight(float value) {
        weight = clamp(value, MIN_WEIGHT, MAX_WEIGHT);
    }

    public static float minWeight() {
        return MIN_WEIGHT;
    }

    public static float maxWeight() {
        return MAX_WEIGHT;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            return;
        }
        try {
            String json = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
            Data data = GSON.fromJson(json, Data.class);
            if (data != null) {
                setWeight(data.weight);
            }
        } catch (IOException | JsonSyntaxException ignored) {
        }
    }

    public static void save() {
        Data data = new Data();
        data.weight = weight();
        String json = GSON.toJson(data);
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, json, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static final class Data {
        private float weight;
    }
}
