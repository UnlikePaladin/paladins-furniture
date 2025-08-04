package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class LampBlockEntity extends BlockEntity implements DyeableFurnitureBlockEntity<LampBlockEntity> {
    protected WoodVariant variant;
    protected DyeColor color;

    public LampBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LAMP_BLOCK_ENTITY, pos, state);
        color = DyeColor.WHITE;
        variant = WoodVariantRegistry.OAK;
    }

    @Override
    protected void readData(ReadView view) {
        this.color = DyeColor.byId(view.getString("color", "white"), DyeColor.WHITE);
        view.getOptionalString("variant").ifPresent((variantName) -> {
            WoodVariant woodVariant = WoodVariantRegistry.getVariant(Identifier.tryParse(variantName));
            if (woodVariant != null)
                this.variant = woodVariant;
            else {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't find variant for lamp: {}", variantName);
                this.variant = WoodVariantRegistry.OAK;
            }
        });
        super.readData(view);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putString("color", color.asString());
        view.putString("variant", variant.getIdentifier().toString());
    }


    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(PFMComponents.VARIANT_COMPONENT, this.variant.identifier);
        componentMapBuilder.add(PFMComponents.COLOR_COMPONENT, this.color);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        Identifier variantName = components.getOrDefault(PFMComponents.VARIANT_COMPONENT, WoodVariantRegistry.OAK.identifier);
        if (WoodVariantRegistry.getVariant(variantName) != null)
            this.variant = WoodVariantRegistry.getVariant(variantName);
        else {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Couldn't find variant for lamp: {}", variantName);
            this.variant = WoodVariantRegistry.OAK;
        }
        this.color = components.getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE);
    }

    @Override
    public void removeFromCopiedStackData(WriteView view) {
        super.removeFromCopiedStackData(view);
        view.remove("color");
        view.remove("variant");
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
    public static BlockEntityType.BlockEntityFactory<? extends LampBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
