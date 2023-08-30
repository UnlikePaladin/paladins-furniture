package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.client;

import com.unlikepaladin.pfm.compat.PFMClientModCompatibility;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveBlockEntityRendererBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

import java.util.function.Function;

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


    public static <E extends BlockEntity> Function<BlockEntityRenderDispatcher, BlockEntityRenderer<? super E>> getStoveRenderer() {
        return blockEntityRenderDispatcher -> (BlockEntityRenderer<? super E>) new StoveBlockEntityRendererBalm(blockEntityRenderDispatcher);
    }
}
