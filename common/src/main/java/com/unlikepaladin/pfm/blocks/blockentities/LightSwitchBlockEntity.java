package com.unlikepaladin.pfm.blocks.blockentities;

import com.mojang.serialization.Codec;
import com.unlikepaladin.pfm.blocks.PowerableBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

public class LightSwitchBlockEntity extends BlockEntity {
    private final List<BlockPos> lights;
    public LightSwitchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY, pos, state);
        lights = NonNullList.create();
    }


    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        ValueOutput.TypedOutputList<Long> listAppender = view.list("Items", Codec.LONG);
        lights.forEach(blockPos -> listAppender.add(blockPos.asLong()));
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        view.list("lights", Codec.LONG).ifPresent((longs) -> {
            lights.clear();
            longs.forEach(this::addLight);
        });
    }
    public void addLight(long pos)
    {
        BlockPos lightPos = BlockPos.of(pos);
        if(!lights.contains(lightPos))
        {
            lights.add(lightPos);
        }
    }

    public void setState(boolean powered)
    {
        if(!lights.isEmpty()) {
            lights.removeIf(offset ->
            {
                BlockState state = level.getBlockState(this.worldPosition.subtract(offset));
                return !(state.getBlock() instanceof PowerableBlock);
            });
            lights.forEach(offset ->
            {
                BlockPos actualPos = this.worldPosition.subtract(offset);
                BlockState state = level.getBlockState(actualPos);
                ((PowerableBlock) state.getBlock()).setPowered(level, actualPos, powered);

            });

        }
    }

}
