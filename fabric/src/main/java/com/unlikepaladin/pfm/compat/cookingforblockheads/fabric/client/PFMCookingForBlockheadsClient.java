package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.client;

import com.unlikepaladin.pfm.compat.PFMClientModCompatibility;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.menu.StoveBlockEntityRendererBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.menu.StoveScreenBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public class PFMCookingForBlockheadsClient implements PFMClientModCompatibility {
    private final PFMModCompatibility parent;

    public PFMCookingForBlockheadsClient(PFMModCompatibility parent) {
        this.parent = parent;
    }

    @Override
    public PFMModCompatibility getCompatiblity() {
        return parent;
    }

    public static <T extends ScreenHandler, J extends Screen & ScreenHandlerProvider<T>> TriFunc<T, PlayerInventory, Text,J> getStoveScreen() {
        return (t, playerInventory, text) -> (J) new StoveScreenBalm((StoveScreenHandlerBalm) t, playerInventory, text);
    }

    public static BlockEntityRendererFactory getStoveRenderer() {
        return StoveBlockEntityRendererBalm::new;
    }

}
