package com.unlikepaladin.pfm.menus.forge;

import com.unlikepaladin.pfm.networking.SyncRecipesPayload;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;

public class WorkbenchScreenHandlerImpl {

    public static void sendSyncRecipesPayload(Player player, Level world, ArrayList<FurnitureRecipe> recipes) {
        SyncRecipesPayload syncRecipesPacket = new SyncRecipesPayload(recipes);
        NetworkRegistryForge.PFM_CHANNEL.send(syncRecipesPacket, PacketDistributor.PLAYER.with((ServerPlayer) player));
    }
}
