package com.unlikepaladin.pfm.mixin.fabric;

import net.fabricmc.fabric.impl.resource.loader.GroupResourcePack;
import net.minecraft.server.packs.PackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GroupResourcePack.class)
public interface PFMGroupResourcePackAccessor {
    @Accessor("packs")
    List<? extends PackResources> getPacks();
}
