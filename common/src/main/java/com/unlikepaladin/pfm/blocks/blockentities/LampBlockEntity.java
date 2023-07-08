package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DyeableFurniture;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LampBlockEntity extends BlockEntity {
    protected WoodVariant variant;
    protected DyeColor color;

    public LampBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LAMP_BLOCK_ENTITY, pos, state);
        color = DyeColor.WHITE;
        variant = WoodVariantRegistry.OAK;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("color", NbtElement.STRING_TYPE)) {
            this.color = DyeColor.byName(nbt.getString("color"), DyeColor.WHITE);
        }
        if (nbt.contains("variant", NbtElement.STRING_TYPE)) {
            String variantName = nbt.getString("variant");
            if (WoodVariantRegistry.getVariant(Identifier.tryParse(variantName)) != null)
                this.variant = WoodVariantRegistry.getVariant(Identifier.tryParse(variantName));
            else {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't find variant for lamp: {}", variantName);
                this.variant = WoodVariantRegistry.OAK;
            }
        }
        super.readNbt(nbt);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("color", color.asString());
        nbt.putString("variant", variant.getIdentifier().toString());
        return nbt;
    }


    public NbtCompound writeColorAndVariant(NbtCompound nbt) {
        nbt.putString("color", color.asString());
        nbt.putString("variant", variant.getIdentifier().toString());
        return nbt;
    }

    public DyeColor getColor() {
        return color;
    }

    public WoodVariant getVariant() {
        return variant;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public void setVariant(WoodVariant variant) {
        this.variant = variant;
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntityFactory<? extends LampBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
