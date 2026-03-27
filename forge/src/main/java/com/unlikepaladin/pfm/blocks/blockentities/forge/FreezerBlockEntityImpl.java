package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.FreezerBlockEntityBalm;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FreezerBlockEntityImpl extends FreezerBlockEntity {

    private final LazyOptional<IItemHandlerModifiable>[] handlers;

    public FreezerBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends FreezerBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? FreezerBlockEntityBalm::new : FreezerBlockEntityImpl::new;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP)
                return handlers[0].cast();
            else if (side == Direction.DOWN) {
                return handlers[1].cast();
            } else {
                return handlers[2].cast();
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<IItemHandlerModifiable> handler : this.handlers) {
            handler.invalidate();
        }
    }
}
