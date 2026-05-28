package com.unlikepaladin.pfm.menus.neoforge;

import com.unlikepaladin.pfm.networking.SyncRecipesPayload;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

public class WorkbenchScreenHandlerImpl {

    public static void sendSyncRecipesPayload(Player player, Level world, ArrayList<FurnitureRecipe> recipes) {
        SyncRecipesPayload syncRecipesPacket = new SyncRecipesPayload(recipes);
        PacketDistributor.sendToPlayer((ServerPlayer) player, syncRecipesPacket);
    }
}
