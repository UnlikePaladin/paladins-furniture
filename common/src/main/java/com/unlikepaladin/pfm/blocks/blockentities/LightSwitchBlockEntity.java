package com.unlikepaladin.pfm.blocks.blockentities;

import com.mojang.serialization.Codec;
import com.unlikepaladin.pfm.blocks.PowerableBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class LightSwitchBlockEntity extends BlockEntity {
    private final List<BlockPos> lights;
    public LightSwitchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY, pos, state);
        lights = DefaultedList.of();
    }


    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        WriteView.ListAppender<Long> listAppender = view.getListAppender("Items", Codec.LONG);
        lights.forEach(blockPos -> listAppender.add(blockPos.asLong()));
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        view.getOptionalTypedListView("lights", Codec.LONG).ifPresent((longs) -> {
            lights.clear();
            longs.forEach(this::addLight);
        });
    }
    public void addLight(long pos)
    {
        BlockPos lightPos = BlockPos.fromLong(pos);
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
                BlockState state = world.getBlockState(this.pos.subtract(offset));
                return !(state.getBlock() instanceof PowerableBlock);
            });
            lights.forEach(offset ->
            {
                BlockPos actualPos = this.pos.subtract(offset);
                BlockState state = world.getBlockState(actualPos);
                ((PowerableBlock) state.getBlock()).setPowered(world, actualPos, powered);

            });

        }
    }

}
