package com.unlikepaladin.pfm.blocks.models.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.forge.PFMBakedModelGetQuadsExtension;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class PFMForgeBakedModel extends AbstractBakedModel implements PFMBakedModelGetQuadsExtension {
    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        return getQuads(state, face, random);
    }

    public PFMForgeBakedModel(ModelBakeSettings settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }
    public static ModelProperty<BlockState> STATE = new ModelProperty<>();

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        tileData.setData(STATE, state);
        return tileData;
    }

    Map<Identifier, List<BakedQuad>> separatedQuads = new ConcurrentHashMap<>();
    public List<BakedQuad> getQuadsWithTexture(List<BakedQuad> quads, List<Sprite> toReplace, List<Sprite> replacements) {
        if (quads == null)
            return Collections.emptyList();

        if (replacements == null || toReplace == null || toReplace.size() != replacements.size()) {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Replacement list is not the same size or was null, skipping transformation");
            return quads;
        }
        if (toReplace.equals(replacements))
            return quads;

        for (BakedQuad quad : quads) {
            Identifier sprite = quad.func_187508_a().getId();
            if (separatedQuads.containsKey(sprite)) {
                if (!separatedQuads.get(sprite).contains(quad)) {
                    List<BakedQuad> newQuadList = new ArrayList<>(separatedQuads.get(sprite));
                    newQuadList.add(quad);
                    separatedQuads.put(sprite, newQuadList);
                }
                continue;
            }
            List<BakedQuad> list = new ArrayList<>();
            list.add(quad);
            separatedQuads.put(sprite, list);
        }

        List<BakedQuad> transformedQuads = new ArrayList<>(quads.size());
        for (Map.Entry<Identifier, List<BakedQuad>> entry : separatedQuads.entrySet()) {
            Identifier keyId = entry.getKey();
            int index = IntStream.range(0, toReplace.size())
                    .filter(i -> keyId.equals(toReplace.get(i).getId()))
                    .findFirst()
                    .orElse(-1);

            if (index != -1) {
                Sprite replacement = replacements.get(index);
                transformedQuads.addAll(getQuadsWithTexture(entry.getValue().stream().filter(quads::contains).collect(Collectors.toList()), replacement));
            } else {
                transformedQuads.addAll(entry.getValue().stream().filter(quads::contains).collect(Collectors.toList()));
            }
        }
        return transformedQuads;
    }

    Map<Pair<Identifier, BakedQuad>, BakedQuad> quadToTransformedQuad = new ConcurrentHashMap<>();
    public List<BakedQuad> getQuadsWithTexture(List<BakedQuad> quads, Sprite sprite) {
        List<BakedQuad> transformedQuads = new ArrayList<>(quads.size());

        // UV Element index
        int uvVertexIndx = findVertexElement(VertexFormatElement.Type.UV, 0);

        quads.forEach(quad -> {
            Pair<Identifier, BakedQuad> quadKey = new Pair<>(sprite.getId(), quad);
            if (quad.func_187508_a().getId() == sprite.getId() && !quadToTransformedQuad.containsKey(quadKey)) {
                quadToTransformedQuad.put(quadKey, quad);
                transformedQuads.add(quad);
            }
            else if (quadToTransformedQuad.containsKey(quadKey)) {
                transformedQuads.add(quadToTransformedQuad.get(quadKey));
            }
            else {
                int[] vertexData = new int[quad.getVertexData().length];
                System.arraycopy(quad.getVertexData(), 0, vertexData, 0, vertexData.length);
                float[][] uv = new float[4][2];
                for (int vertexIndx = 0; vertexIndx < 4; vertexIndx++) {
                    LightUtil.unpack(vertexData, uv[vertexIndx], VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, vertexIndx, uvVertexIndx);
                    Sprite originalSprite = quad.func_187508_a();
                    float frameU = ModelHelper.method_35804(originalSprite, uv[vertexIndx][0]);
                    float frameV = ModelHelper.method_35805(originalSprite, uv[vertexIndx][1]);
                    uv[vertexIndx][0] = sprite.getFrameU(frameU);
                    uv[vertexIndx][1] = sprite.getFrameV(frameV);
                    LightUtil.pack(uv[vertexIndx], vertexData, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, vertexIndx, uvVertexIndx);
                }
                BakedQuad transformedQuad = new BakedQuad(vertexData, quad.getColorIndex(), quad.getFace(), quad.func_187508_a(), quad.hasShade());
                quadToTransformedQuad.put(quadKey, transformedQuad);
                transformedQuads.add(transformedQuad);
            }
        });
        return transformedQuads;
    }

    private static final Map<Pair<VertexFormatElement.Type, Integer>, Integer> ELEMENT_INTEGER_MAP = new ConcurrentHashMap<>();
    public static int findVertexElement(VertexFormatElement.Type type, int index) {
        Pair<VertexFormatElement.Type, Integer> pairToFind = new Pair<>(type, index);
        if (ELEMENT_INTEGER_MAP.containsKey(pairToFind))
            return ELEMENT_INTEGER_MAP.get(pairToFind);

        int id = 0;
        for (VertexFormatElement element1 : VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getElements())
        {
            if (element1.getType() == type && element1.getTextureIndex() == index)
                break;
            id++;
        }
        ELEMENT_INTEGER_MAP.put(pairToFind, id);
        return id;
    }

    @Override
    public Sprite getParticleTexture(@NotNull IModelData data) {
        if (data.hasProperty(STATE) && data.getData(STATE) != null)
            return getSpriteList(data.getData(STATE)).get(0);
        return super.getParticleTexture(data);
    }

    @Override
    public Sprite getSprite() {
        return getTemplateBakedModels().get(0).getSprite();
    }

    public void appendProperties(ModelDataMap.Builder builder) {
        builder.withProperty(STATE);
    }
}
