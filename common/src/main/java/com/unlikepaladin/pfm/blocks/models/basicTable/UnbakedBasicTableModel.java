package com.unlikepaladin.pfm.blocks.models.basicTable;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.materials.BlockType;
import com.unlikepaladin.pfm.blocks.materials.MaterialEnum;
import com.unlikepaladin.pfm.blocks.materials.StoneVariant;
import com.unlikepaladin.pfm.blocks.materials.WoodVariant;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.bed.BakedBedModel;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedBasicTableModel implements UnbakedModel {
    public static final List<String> BASIC_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/table_basic/table_basic_base");
            add("block/table_basic/table_basic_north_east");
            add("block/table_basic/table_basic_north_west");
            add("block/table_basic/table_basic_south_east");
            add("block/table_basic/table_basic_south_west");
            add("block/table_basic/table_basic_north_south_east_top");
            add("block/table_basic/table_basic_north_south_west_top");
            add("block/table_basic/table_basic_east_west_north");
            add("block/table_basic/table_basic_east_west_south");
            add("block/table_basic/table_basic_north_south_east_bottom");
            add("block/table_basic/table_basic_north_south_west_bottom");
            add("block/table_basic/table_basic_north_south_east");
            add("block/table_basic/table_basic_north_south_west");
            add("block/table_basic/table_basic_north_east_corner");
            add("block/table_basic/table_basic_north_west_corner");
            add("block/table_basic/table_basic_south_east_corner");
            add("block/table_basic/table_basic_south_west_corner");
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> TABLE_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_table_basic"));
            }
            for(WoodVariant variant : WoodVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/stripped_" + variant.asString() + "_table_basic"));
            }
            for(StoneVariant variant : StoneVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_table_basic"));
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : BASIC_MODEL_PARTS_BASE) {
                    String newPart = part;
                    if (!variant.equals(WoodVariant.OAK))
                         newPart = part.replace("table", variant.asString() + "_table");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : BASIC_MODEL_PARTS_BASE) {
                    String newPart = part.replace("table", "stripped_" + variant.asString() + "_table");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(StoneVariant variant : StoneVariant.values()){
                for (String part : BASIC_MODEL_PARTS_BASE) {
                    String newPart = part.replace("table", variant.asString() + "_table");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
        }
    };

    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;

    public UnbakedBasicTableModel(MaterialEnum variant, List<String> modelParts, BlockType type) {
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(type));
        for(String modelPartName : BASIC_MODEL_PARTS_BASE){
            String s = modelPartName;
            if (!variant.equals(WoodVariant.OAK))
                s = s.replace("table", variant.asString() + "_table");
            if (type == BlockType.STRIPPED_LOG) {
                s = s.replace(variant.asString(), "stripped_" + variant.asString());
            }
            modelParts.add(s);
        }
        this.MODEL_PARTS = modelParts;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(PARENT);
    }
    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        List<SpriteIdentifier> list = new ArrayList<>(2);
        list.add(frameTex);
        return list;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Map<String,BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPart : MODEL_PARTS) {
            bakedModels.put(modelPart, loader.bake(new Identifier(PaladinFurnitureMod.MOD_ID, modelPart), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), rotationContainer, bakedModels, MODEL_PARTS);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Sprite frame, ModelBakeSettings settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        return new BakedBasicTableModel(frame, settings, bakedModels);
    }
}
