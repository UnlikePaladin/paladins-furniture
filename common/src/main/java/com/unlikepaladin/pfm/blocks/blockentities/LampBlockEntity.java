package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

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
    public void fromTag(BlockState state, NbtCompound nbt) {
        if (nbt.contains("color", 8)) {
            this.color = DyeColor.byName(nbt.getString("color"), DyeColor.WHITE);
        }
        if (nbt.contains("variant", 8)) {
            String variantName = nbt.getString("variant");
            if (WoodVariantRegistry.getVariant(Identifier.tryParse(variantName)) != null)
                this.variant = WoodVariantRegistry.getVariant(Identifier.tryParse(variantName));
            else {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't find variant for lamp: {}", variantName);
                this.variant = WoodVariantRegistry.OAK;
            }
        }
        super.fromTag(state, nbt);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("color", color.asString());
        nbt.putString("variant", variant.getIdentifier().toString());
        return nbt;
    }


    public NbtCompound writeColorAndVariant(NbtCompound nbt) {
        NbtCompound newNBT = writeColor(nbt);
        newNBT.putString("variant", variant.getIdentifier().toString());
        return newNBT;
    }

    public NbtCompound writeColor(NbtCompound nbt) {
        nbt.putString("color", color.asString());
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
