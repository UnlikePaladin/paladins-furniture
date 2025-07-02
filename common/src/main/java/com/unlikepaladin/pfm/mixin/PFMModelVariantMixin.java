package com.unlikepaladin.pfm.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.client.model.PFMModelVariantExtension;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ModelVariant.class)
public class PFMModelVariantMixin implements PFMModelVariantExtension {

    @Mutable
    @Shadow @Final public static MapCodec<ModelVariant> MAP_CODEC;
    @Mutable
    @Shadow @Final public static Codec<ModelVariant> CODEC;
    @Unique
    private Optional<Identifier> pfm$type = Optional.empty();
    @Override
    public Optional<Identifier> pfm$getCustomType() {
        return pfm$type;
    }

    @Override
    public void pfm$setCustomType(Identifier customType) {
        if (customType != null) {
            this.pfm$type = Optional.of(customType);
        }
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void pfm$redefineCodecs(CallbackInfo ci) {
        MAP_CODEC = RecordCodecBuilder.mapCodec(modelVariantInstance -> modelVariantInstance.group(
                Identifier.CODEC.optionalFieldOf(PFMFileUtil.pfm$getTypeFieldName()).forGetter(p -> ((PFMModelVariantExtension)(Object)p).pfm$getCustomType()), Identifier.CODEC.fieldOf("model").forGetter(ModelVariant::modelId), ModelVariant.ModelState.CODEC.forGetter(ModelVariant::modelState)
        ).apply(modelVariantInstance, (type, model, modelState) -> {
            ModelVariant variant = new ModelVariant(model, modelState);
            ((PFMModelVariantExtension)(Object)variant).pfm$setCustomType(type.orElse(null));
            return variant;
        }));
        CODEC = MAP_CODEC.codec();
    }
}
