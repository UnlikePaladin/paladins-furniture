package com.unlikepaladin.pfm.client.model;


import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PFMBakedModelGetQuadsExtension {
    List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random);

    List<BakedQuad> getQuadsCached(@Nullable Direction face, RandomSource random);
}
