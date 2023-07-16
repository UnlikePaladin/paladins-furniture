package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.fabric.sandwichable.PFMSandwichableCompat;
import net.minecraft.item.ItemStack;

public class PFMToasterBlockImpl {
    public static boolean isSandwich(ItemStack stack) {
        if (PaladinFurnitureMod.getModList().contains("sandwichable")) {
            return PFMSandwichableCompat.isSandwich(stack);
        }
        return false;
    }
}
