package com.unlikepaladin.pfm.blocks.models.chairClassic.forge;

import com.unlikepaladin.pfm.blocks.ClassicChairBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.util.math.random.Random;

public class ForgeChairClassicModel extends PFMForgeBakedModel {
    public ForgeChairClassicModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    public static ModelProperty<Boolean> TUCKED = new ModelProperty<>();

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof ClassicChairBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);
            data = data.derive().with(TUCKED, state.get(ClassicChairBlock.TUCKED)).build();
            return data;
        }
        return tileData;
    }

    @Override
    public void collectParts(Random random, List<BlockModelPart> dest, ModelData extraData, @Nullable BlockRenderLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && extraData.get(TUCKED) != null) {
            int tucked = Boolean.TRUE.equals(extraData.get(TUCKED)) ? 1 : 0;
            List<Sprite> spriteList = getSpriteList(state);
            BlockModelPart part = getTemplateBakedModels().get(tucked);
            dest.add(getQuadsWithTexture(part, ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getTemplateBakedModels().get(0).getQuads(face);
        return getQuadsWithTextureInner(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
