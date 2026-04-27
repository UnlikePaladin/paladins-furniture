package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;

public class LampBlockEntity extends BlockEntity implements DyeableFurnitureBlockEntity {
    protected WoodVariant variant;
    protected DyeColor color;

    public LampBlockEntity() {
        super(BlockEntities.LAMP_BLOCK_ENTITY);
        color = DyeColor.WHITE;
        variant = WoodVariantRegistry.OAK;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        if (nbt.contains("color", 8)) {
            this.color = DyeColor.byName(nbt.getString("color"), DyeColor.WHITE);
        }
        if (nbt.contains("variant", 8)) {
            String variantName = nbt.getString("variant");
            if (WoodVariantRegistry.getVariant(ResourceLocation.tryParse(variantName)) != null)
                this.variant = WoodVariantRegistry.getVariant(ResourceLocation.tryParse(variantName));
            else {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't find variant for lamp: {}", variantName);
                this.variant = WoodVariantRegistry.OAK;
            }
        }
        super.load(state, nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putString("color", color.getSerializedName());
        nbt.putString("variant", variant.getIdentifier().toString());
        return nbt;
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
    public BlockEntity getEntity() {
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
    public static Supplier<? extends LampBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
