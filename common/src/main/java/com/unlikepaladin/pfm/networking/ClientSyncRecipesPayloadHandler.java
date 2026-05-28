package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.menus.WorkbenchScreenHandler;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.List;

public class ClientSyncRecipesPayloadHandler {
    public static void handlePacket(List<FurnitureRecipe> recipes) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.level() != null)
                WorkbenchScreenHandler.setAllRecipes(player.level(), recipes);
        });
    }
}
