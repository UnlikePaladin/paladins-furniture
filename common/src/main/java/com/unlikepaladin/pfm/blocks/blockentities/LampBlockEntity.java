package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class LampBlockEntity extends BlockEntity implements DyeableFurnitureBlockEntity<LampBlockEntity> {
    protected WoodVariant variant;
    protected DyeColor color;

    public LampBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LAMP_BLOCK_ENTITY, pos, state);
        color = DyeColor.WHITE;
        variant = WoodVariantRegistry.OAK;
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        if (nbt.contains("color", Tag.TAG_STRING)) {
            this.color = DyeColor.byName(nbt.getString("color"), DyeColor.WHITE);
        }
        if (nbt.contains("variant", Tag.TAG_STRING)) {
            String variantName = nbt.getString("variant");
            if (WoodVariantRegistry.getVariant(ResourceLocation.tryParse(variantName)) != null)
                this.variant = WoodVariantRegistry.getVariant(ResourceLocation.tryParse(variantName));
            else {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't find variant for lamp: {}", variantName);
                this.variant = WoodVariantRegistry.OAK;
            }
        }
        super.loadAdditional(nbt, registryLookup);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putString("color", color.getSerializedName());
        nbt.putString("variant", variant.getIdentifier().toString());
    }


    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
        componentMapBuilder.set(PFMComponents.VARIANT_COMPONENT, this.variant.identifier);
        componentMapBuilder.set(PFMComponents.COLOR_COMPONENT, this.color);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput components) {
        super.applyImplicitComponents(components);
        ResourceLocation variantName = components.getOrDefault(PFMComponents.VARIANT_COMPONENT, WoodVariantRegistry.OAK.identifier);
        if (WoodVariantRegistry.getVariant(variantName) != null)
            this.variant = WoodVariantRegistry.getVariant(variantName);
        else {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't find variant for lamp: {}", variantName);
            this.variant = WoodVariantRegistry.OAK;
        }
        this.color = components.getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag nbt) {
        super.removeComponentsFromTag(nbt);
        nbt.remove("color");
        nbt.remove("variant");
    }

    public CompoundTag writeColorAndVariant(CompoundTag nbt) {
        CompoundTag newNBT = writeColor(nbt);
        newNBT.putString("variant", variant.getIdentifier().toString());
        return newNBT;
    }

    public CompoundTag writeColor(CompoundTag nbt) {
        nbt.putString("color", color.getSerializedName());
        return nbt;
    }

    @Override
    public LampBlockEntity getEntity() {
        return this;
    }

    public DyeColor getPFMColor() {
        return color;
    }

    public WoodVariant getVariant() {
        return variant;
    }

    public void setPFMColor(DyeColor color) {
        this.color = color;
    }

    public void setVariant(WoodVariant variant) {
        this.variant = variant;
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends LampBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
