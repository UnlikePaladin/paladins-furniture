package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.irisshaders.iris.api.v0.IrisApi;

import java.lang.reflect.Field;

public class PaladinFurnitureModClientImpl {
    private static volatile Boolean cachedShaderState = null;
    private static long lastCheckTime = 0;
    private static final long CACHE_DURATION_MS = 1000; // Cache for 1 second

    // Optifine reflection fields - cached to avoid repeated lookups
    private static Field optifineShaderPackLoadedField = null;
    private static boolean optifineReflectionFailed = false;

    public static boolean areShadersOn() {
        long currentTime = System.currentTimeMillis();

        // Return cached result if it's still valid
        if (cachedShaderState != null && (currentTime - lastCheckTime) < CACHE_DURATION_MS) {
            return cachedShaderState;
        }

        boolean result = checkShadersInternal();

        // Update cache
        cachedShaderState = result;
        lastCheckTime = currentTime;

        return result;
    }

    private static boolean checkShadersInternal() {
        // Check Iris/Oculus first (most common on newer versions)
        if (PaladinFurnitureMod.getModList().contains("oculus") || PaladinFurnitureMod.getModList().contains("iris")) {
            try {
                return IrisApi.getInstance().isShaderPackInUse();
            } catch (Exception e) {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Failed to check Iris/Oculus shader state: " + e.getMessage());
                // Fall through to Optifine check
            }
        }

        // Check Optifine
        if (!PaladinFurnitureMod.isOptifineLoaded()) {
            return false;
        }

        return checkOptifineShaders();
    }

    private static boolean checkOptifineShaders() {
        // Skip if reflection already failed
        if (optifineReflectionFailed) {
            return false;
        }

        // Cache the reflection field lookup
        if (optifineShaderPackLoadedField == null) {
            try {
                Class<?> shadersClass = Class.forName("net.optifine.shaders.Shaders");
                optifineShaderPackLoadedField = shadersClass.getField("shaderPackLoaded");
                optifineShaderPackLoadedField.setAccessible(true); // Just in case
            } catch (ClassNotFoundException | NoSuchFieldException e) {
                PaladinFurnitureMod.GENERAL_LOGGER.info("Optifine shaders class not found or field missing: " + e.getMessage());
                optifineReflectionFailed = true;
                return false;
            }
        }

        // Get the shader state
        try {
            return optifineShaderPackLoadedField.getBoolean(null);
        } catch (IllegalAccessException e) {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't access Optifine shaders field: " + e.getMessage());
            optifineReflectionFailed = true;
            return false;
        }
    }

    public static void refreshShaderCache() {
        cachedShaderState = null;
        lastCheckTime = 0;
    }
}
