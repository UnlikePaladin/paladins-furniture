package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RawLogTableBlock extends LogTableBlock{
    private static final List<FurnitureBlock> LOG_TABLES = new ArrayList<>();

    public RawLogTableBlock(Settings settings) {
        super(settings);
        if(this.getClass().isAssignableFrom(RawLogTableBlock.class)){
            LOG_TABLES.add(new FurnitureBlock(this, "table_"));
        }
    }

    public static Stream<FurnitureBlock> streamLogTables() {
        return LOG_TABLES.stream();
    }
}
