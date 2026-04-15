package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.client;

import com.unlikepaladin.pfm.compat.PFMClientModCompatibility;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.menu.StoveBlockEntityRendererBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.menu.StoveScreenBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;

public class PFMCookingForBlockheadsClient implements PFMClientModCompatibility {
    private final PFMModCompatibility parent;

    public PFMCookingForBlockheadsClient(PFMModCompatibility parent) {
        this.parent = parent;
    }

    @Override
    public PFMModCompatibility getCompatiblity() {
        return parent;
    }

    public static <T extends AbstractContainerMenu, J extends Screen & MenuAccess<T>> TriFunc<T, Inventory, Component,J> getStoveScreen() {
        return (t, playerInventory, text) -> (J) new StoveScreenBalm((StoveScreenHandlerBalm) t, playerInventory, text);
    }

    public static BlockEntityRendererProvider getStoveRenderer() {
        return StoveBlockEntityRendererBalm::new;
    }

}
