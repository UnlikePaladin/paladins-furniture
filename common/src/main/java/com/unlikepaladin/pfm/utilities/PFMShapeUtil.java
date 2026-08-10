package com.unlikepaladin.pfm.utilities;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PFMShapeUtil {
    /**
     * Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/
     */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    /** Method to tuck a Chair's Voxel Shapes */
    public static VoxelShape tuckShape(Direction from, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        switch (from) {
            case NORTH -> { buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(minX, minY, minZ + 0.5, maxX, maxY, maxZ + 0.5)));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();}
            case SOUTH -> { buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(minX, minY, minZ - 0.5, maxX, maxY, maxZ - 0.5)));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();}
            case WEST -> { buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(minX + 0.5, minY, minZ, maxX + 0.5, maxY, maxZ)));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();}
            default -> { buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(minX - 0.5, minY, minZ, maxX - 0.5, maxY, maxZ)));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();}
        }
        return buffer[0];
    }
}
