package com.unlikepaladin.pfm.menus.fabric;

import com.unlikepaladin.pfm.networking.SyncRecipesPayload;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class WorkbenchScreenHandlerImpl {
    public static void sendSyncRecipesPayload(Player player, Level world, ArrayList<FurnitureRecipe> recipes) {
        ServerPlayNetworking.send((ServerPlayer) player, new SyncRecipesPayload(recipes));
    }
}
