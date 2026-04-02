package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.PowerableBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;

import java.util.List;

public class LightSwitchBlockEntity extends BlockEntity {
    private final List<BlockPos> lights;
    public LightSwitchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY, pos, state);
        lights = NonNullList.create();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ListTag tagList = new ListTag();
        lights.forEach(blockPos -> tagList.add(LongTag.valueOf(blockPos.asLong())));
        nbt.put("lights", tagList);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if(nbt.contains("lights", Tag.TAG_LIST)){
            lights.clear();
            ListTag lightTagList = nbt.getList("lights", Tag.TAG_LONG);
            lightTagList.forEach(nbtElement -> addLight(((LongTag)nbtElement).getAsLong()));
        }
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
