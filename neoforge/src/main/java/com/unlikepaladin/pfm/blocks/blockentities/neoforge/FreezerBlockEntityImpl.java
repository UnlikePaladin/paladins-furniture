package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.FreezerBlockEntityBalm;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FreezerBlockEntityImpl extends FreezerBlockEntity {
    public FreezerBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends FreezerBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? FreezerBlockEntityBalm::new : FreezerBlockEntityImpl::new;
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        world.invalidateCapabilities(pos);
    }
}
