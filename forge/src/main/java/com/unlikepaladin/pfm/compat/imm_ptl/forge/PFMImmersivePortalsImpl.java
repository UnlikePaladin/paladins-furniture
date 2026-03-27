package com.unlikepaladin.pfm.compat.imm_ptl.forge;

import com.unlikepaladin.pfm.compat.PFMClientModCompatibility;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.client.PFMImmersivePortalsClient;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.entity.PFMMirrorEntity;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.Optional;

public class PFMImmersivePortalsImpl implements PFMModCompatibility {
    private PFMClientModCompatibility clientModCompatibility;
    public static EntityType<PFMMirrorEntity> MIRROR;

    @Override
    public void registerEntityTypes() {
        MIRROR = EntityType.Builder.of(PFMMirrorEntity::new, MobCategory.MISC).sized(0.0F, 0.0F).fireImmune().noSummon().build("mirror_entity");
        EntityRegistry.registerEntityType("mirror_entity", MIRROR);
    }

    @Override
    public void createBlocks() {
        PaladinFurnitureModBlocksItems.WHITE_MIRROR = new PFMMirrorBlockIP(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).noOcclusion());
        PaladinFurnitureModBlocksItems.GRAY_MIRROR = new PFMMirrorBlockIP(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).noOcclusion());
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
