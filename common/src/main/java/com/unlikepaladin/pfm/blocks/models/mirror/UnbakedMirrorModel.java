package com.unlikepaladin.pfm.blocks.models.mirror;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedMirrorModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedMirrorModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedMirrorModel::variant))
                            .apply(instance, UnbakedMirrorModel::new));

    public static final Codec<UnbakedMirrorModel> CODEC = MAP_CODEC.codec();

    public static final String[] BASE_MODEL_PARTS = new String[] {"block/mirror/mirror_base", "block/mirror/mirror_top", "block/mirror/mirror_bottom", "block/mirror/mirror_left","block/mirror/mirror_right", "block/mirror/mirror_right_top", "block/mirror/mirror_left_top", "block/mirror/mirror_right_bottom", "block/mirror/mirror_left_bottom"};
    public static final Identifier[] DEFAULT_TEXTURES = new Identifier[] {Identifier.fromNamespaceAndPath("minecraft","block/white_concrete"), Identifier.fromNamespaceAndPath("minecraft","block/glass"), Identifier.fromNamespaceAndPath("pfm","block/mirror")};
    public static final Identifier[] MIRROR_MODEL_IDS = {Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/white_mirror"), Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/gray_mirror")};
    public static final Identifier MIRROR_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/mirror");

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for (String part : BASE_MODEL_PARTS) {
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, part));
            }
            for (String part : BASE_MODEL_PARTS) {
                part = part.replace("mirror", "gray_mirror");
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();

        Map<String,BlockModelPart> bakedModels = new LinkedHashMap<>();
        for (String modelPartName: BASE_MODEL_PARTS) {
            String part = modelPartName.replace("mirror", "gray_mirror");
            bakedModels.put(modelPartName, SimpleModelWrapper.bake(baker, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, part), settings));
        }

        return getBakedModel(settings, bakedModels, bakedModels.keySet().stream().toList());
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(ModelState settings, Map<String,BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (Identifier c : ALL_MODEL_IDS)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
