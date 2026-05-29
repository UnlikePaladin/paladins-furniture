package com.unlikepaladin.pfm.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.client.model.PFMModelVariantExtension;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Variant.class)
public class PFMModelVariantMixin implements PFMModelVariantExtension {

    @Mutable
    @Shadow @Final public static MapCodec<Variant> MAP_CODEC;
    @Mutable
    @Shadow @Final public static Codec<Variant> CODEC;
    @Unique
    private Optional<ResourceLocation> pfm$type = Optional.empty();
    @Override
    public Optional<ResourceLocation> pfm$getCustomType() {
        return pfm$type;
    }

    @Override
    public void pfm$setCustomType(ResourceLocation customType) {
        if (customType != null) {
            this.pfm$type = Optional.of(customType);
        }
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void pfm$redefineCodecs(CallbackInfo ci) {
        MAP_CODEC = RecordCodecBuilder.mapCodec(modelVariantInstance -> modelVariantInstance.group(
                ResourceLocation.CODEC.optionalFieldOf(PFMFileUtil.pfm$getTypeFieldName()).forGetter(p -> ((PFMModelVariantExtension)(Object)p).pfm$getCustomType()), ResourceLocation.CODEC.fieldOf("model").forGetter(Variant::modelLocation), Variant.SimpleModelState.MAP_CODEC.forGetter(Variant::modelState)
        ).apply(modelVariantInstance, (type, model, modelState) -> {
            Variant variant = new Variant(model, modelState);
            ((PFMModelVariantExtension)(Object)variant).pfm$setCustomType(type.orElse(null));
            return variant;
        }));
        CODEC = MAP_CODEC.codec();
    }
}
