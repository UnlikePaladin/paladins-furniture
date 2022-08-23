package com.unlikepaladin.pfm.compat;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PaladinFurnitureModConfig {

    @ExpectPlatform
    public static boolean doTablesOfDifferentMaterialsConnect() {
       return false;
    }

    @ExpectPlatform
    public static boolean doChairsFacePlayer() {
        return true;
    }

    @ExpectPlatform
    public static boolean doCountersOfDifferentMaterialsConnect() {
        return false;
    }

    @ExpectPlatform
    public static boolean doesFoodPopOffStove() {
        return false;
    }

}
