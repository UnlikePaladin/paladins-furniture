package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;

public class ParticleProviderRegistryFabric {

    public static void registerParticleFactories() {
        ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register(((atlasTexture, registry) -> {
            registry.register(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "particle/shower_water"));
        }));

        ParticleFactoryRegistry.getInstance().register(ParticleIDs.WATER_DROP, DripParticle.WaterFallProvider::new);
    }
}
