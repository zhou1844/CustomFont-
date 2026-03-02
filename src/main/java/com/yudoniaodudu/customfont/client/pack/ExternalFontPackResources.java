package com.yudoniaodudu.customfont.client.pack;

import com.yudoniaodudu.customfont.CustomFontMod;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.minecraft.server.packs.PackResources.ResourceOutput;

public final class ExternalFontPackResources extends AbstractPackResources {
    private static final String OVERRIDE_FONT_PATH = "font/mr.ttf";

    public ExternalFontPackResources(String packId, boolean isBuiltin) {
        super(packId, isBuiltin);
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... paths) {
        if (paths.length == 1 && "pack.mcmeta".equals(paths[0])) {
            byte[] bytes = buildPackMcmetaJson().getBytes(StandardCharsets.UTF_8);
            return () -> new ByteArrayInputStream(bytes);
        }
        return null;
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput resourceOutput) {
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        Set<String> set = new HashSet<>();
        if (type == PackType.CLIENT_RESOURCES) {
            set.add(CustomFontMod.MODID);
        }
        return set;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        if (type != PackType.CLIENT_RESOURCES) {
            return null;
        }
        if (!CustomFontMod.MODID.equals(location.getNamespace())) {
            return null;
        }
        // Accept any path ending in mr.ttf to handle potential path prefixing issues
        if (!location.getPath().endsWith("mr.ttf")) {
            return null;
        }

        Path userFont = findUserFontFile();
        if (userFont != null) {
            return IoSupplier.create(userFont);
        }

        return () -> {
            InputStream in = ExternalFontPackResources.class.getResourceAsStream("/assets/" + CustomFontMod.MODID + "/" + OVERRIDE_FONT_PATH);
            if (in == null) {
                throw new FileNotFoundException("Missing bundled font resource: " + CustomFontMod.MODID + ":" + OVERRIDE_FONT_PATH);
            }
            return in;
        };
    }

    @Override
    public void close() {
    }

    @Nullable
    private static Path findUserFontFile() {
        Path dir = FMLPaths.GAMEDIR.get().resolve("font");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            return null;
        }

        try {
            return pickBestFontFile(dir);
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private static Path pickBestFontFile(Path dir) throws IOException {
        try (var stream = Files.list(dir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String n = p.getFileName().toString().toLowerCase(Locale.ROOT);
                        return n.endsWith(".ttf") || n.endsWith(".otf");
                    })
                    .sorted(Comparator.comparingInt(ExternalFontPackResources::fontFilePriority)
                            .thenComparing(p -> p.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                    .findFirst()
                    .orElse(null);
        }
    }

    private static int fontFilePriority(Path p) {
        String n = p.getFileName().toString().toLowerCase(Locale.ROOT);
        if (n.equals("font.ttf") || n.equals("default.ttf")) {
            return 0;
        }
        if (n.endsWith(".ttf")) {
            return 1;
        }
        return 2;
    }

    private static String buildPackMcmetaJson() {
        int packFormat = SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES);
        return "{\"pack\":{\"pack_format\":" + packFormat + ",\"description\":\"Custom Font Mod External Font\"}}";
    }
}
