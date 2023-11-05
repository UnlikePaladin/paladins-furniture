package com.unlikepaladin.pfm.compat.imm_ptl.forge;

import com.unlikepaladin.pfm.compat.PFMClientModCompatibility;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.client.PFMImmersivePortalsClient;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.entity.PFMMirrorEntity;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import java.util.Optional;

public class PFMImmersivePortalsImpl implements PFMModCompatibility {
    private PFMClientModCompatibility clientModCompatibility;
    public static final EntityType<PFMMirrorEntity> MIRROR = EntityType.Builder.create(PFMMirrorEntity::new, SpawnGroup.MISC).setDimensions(0.0F, 0.0F).makeFireImmune().disableSummon().build("mirror_entity");

    @Override
    public void registerEntityTypes() {
        EntityRegistry.registerEntityType("mirror_entity", MIRROR);
    }

    @Override
    public void createBlocks() {
        PaladinFurnitureModBlocksItems.WHITE_MIRROR = new PFMMirrorBlockIP(AbstractBlock.Settings.of(Material.STONE, MapColor.WHITE).nonOpaque());
        PaladinFurnitureModBlocksItems.GRAY_MIRROR = new PFMMirrorBlockIP(AbstractBlock.Settings.of(Material.STONE, MapColor.GRAY).nonOpaque());
    }

    @Override
    public String getModId() {
        return "immersive_portals";
    }

    @Override
    public Optional<PFMClientModCompatibility> getClientModCompatiblity() {
        if (clientModCompatibility == null)
            clientModCompatibility = new PFMImmersivePortalsClient(this);
        return Optional.of(clientModCompatibility);
    }
    public static PFMModCompatibility getInstance() {
        return new PFMImmersivePortalsImpl();
    }
}
