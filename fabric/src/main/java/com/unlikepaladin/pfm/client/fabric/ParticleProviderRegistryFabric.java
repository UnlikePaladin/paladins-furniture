package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;

public class ParticleProviderRegistryFabric {

    public static void registerParticleFactories() {
        ParticleFactoryRegistry.getInstance().register(ParticleIDs.WATER_DROP, provider -> {
            return (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
                TextureSheetParticle spriteBillboardParticle = DripParticle.createWaterFallParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ);
                if (provider != null) {
                    spriteBillboardParticle.setSprite(provider);
                }
                return spriteBillboardParticle;
            };
        });
    }
}
