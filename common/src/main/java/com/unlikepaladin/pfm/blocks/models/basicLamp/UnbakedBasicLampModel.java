package com.unlikepaladin.pfm.blocks.models.basicLamp;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import dev.architectury.injectables.annotations.ExpectPlatform;
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

public class UnbakedBasicLampModel implements UnbakedModel {

    public UnbakedBasicLampModel() {
    }

    public static final List<Identifier> LAMP_MODEL_IDS = new ArrayList<Identifier>() {
        {
            add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_lamp"));
            add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/basic_lamp"));
        }
    };

    public static Identifier getItemModelId() {
        return PaladinFurnitureMod.getLoader() == PaladinFurnitureMod.Loader.FORGE ? new Identifier("builtin/entity") : LAMP_MODEL_IDS.get(1);
    }

    public static final List<String> MODEL_PARTS_BASE = new ArrayList<String>() {{
       add("block/basic_lamp/template_basic_lamp/template_basic_lamp_bottom");
       add("block/basic_lamp/template_basic_lamp/template_basic_lamp_middle");
       add("block/basic_lamp/template_basic_lamp/template_basic_lamp_single");
        add("block/basic_lamp/template_basic_lamp/template_basic_lamp_top");
    }};

    public static final List<String> STATIC_PARTS = new ArrayList<String>() {{
        add("block/basic_lamp/basic_lamp_shade");
        add("block/basic_lamp/basic_lamp_light_bulb_off");
        add("block/basic_lamp/basic_lamp_light_bulb_on");
    }};
    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<Identifier>() {
        {
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                for (String part : MODEL_PARTS_BASE) {
                    String newPart = part.replaceAll("template", variant.asString());
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for (String part : STATIC_PARTS) {
                add(new Identifier(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };


    Map<WoodVariant, SpriteIdentifier> textureMap = new HashMap<>();
    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
            textureMap.put(variant, new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.STRIPPED_LOG)));
        }
        return textureMap.values();
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.singleton(PARENT);
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Map<WoodVariant, Map<String, BakedModel>> bakedModels = new LinkedHashMap<>();
        List<String> parts = new ArrayList<>(MODEL_PARTS_BASE);
        parts.addAll(STATIC_PARTS);
        for (WoodVariant woodVariant : WoodVariantRegistry.getVariants()) {
            bakedModels.put(woodVariant, new LinkedHashMap<>());
            for (String part : parts) {
                bakedModels.get(woodVariant).put(part, loader.bake(new Identifier(PaladinFurnitureMod.MOD_ID, part.replaceAll("template", woodVariant.asString())), rotationContainer));
            }
        }
        Map<WoodVariant, Sprite> spriteMap = new HashMap<>();
        for (Map.Entry<WoodVariant, SpriteIdentifier> spriteIdentifierEntry : textureMap.entrySet()) {
            spriteMap.put(spriteIdentifierEntry.getKey(), textureGetter.apply(spriteIdentifierEntry.getValue()));
        }
        return getBakedModel(spriteMap, rotationContainer, bakedModels, parts);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Map<WoodVariant, Sprite> textures, ModelBakeSettings settings, Map<WoodVariant, Map<String, BakedModel>> variantToModelMap, List<String> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}
