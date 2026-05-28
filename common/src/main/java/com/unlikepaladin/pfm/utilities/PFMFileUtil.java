package com.unlikepaladin.pfm.utilities;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ARGB;
import net.minecraft.util.StringRepresentable;
import org.spongepowered.asm.mixin.Unique;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class PFMFileUtil {

    @ExpectPlatform
    public static Path getGamePath() {
        throw new RuntimeException();
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    @ExpectPlatform
    public static List<PackResources> getSubPacks(PackResources pack) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ModLoader getModLoader() {
        throw new AssertionError();
    }

    public enum ModLoader implements StringRepresentable {
        MINECRAFTFORGE("minecraftforge"),
        FABRIC("fabric"),
        NEOFORGE("neoforge"),
        INVALID("");

        private String loader;
        ModLoader(String loader) {
            this.loader = loader;
        }

        public static ModLoader get(String modLoader) {
            for (ModLoader value : ModLoader.values()) {
                if (value.loader.equals(modLoader)) {
                    return value;
                }
            }
            return INVALID;
        }

        @Override
        public String getSerializedName() {
            return loader;
        }
    }

    @ExpectPlatform
    public static MinecraftServer getCurrentServer() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String pfm$getTypeFieldName() {
        throw new AssertionError();
    }

    public static int adjustColor(int argbColor) {
        return (argbColor & -67108864) == 0 ? ARGB.opaque(argbColor) : argbColor;
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Optional<String> getVersion(String modId) {
        throw new AssertionError();
    }
}
