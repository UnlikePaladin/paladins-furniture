package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BasicChair extends HorizontalFacingBlock {
    public float height;

    private static final List<FurnitureBlock> WOOD_BASIC_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_BASIC_CHAIRS = new ArrayList<>();
    public BasicChair(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        this.height = 0.36f;
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(BasicChair.class)){
            WOOD_BASIC_CHAIRS.add(new FurnitureBlock(this, "chair"));
        }
        else if (this.getClass().isAssignableFrom(BasicChair.class)){
            STONE_BASIC_CHAIRS.add(new FurnitureBlock(this, "chair"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodBasicChairs() {
        return WOOD_BASIC_CHAIRS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneBasicChairs() {
        return STONE_BASIC_CHAIRS.stream();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(1, 0, 2 ,3.5 ,8 ,4.5), createCuboidShape(1, 0, 11, 3.5, 8, 13.5), createCuboidShape(11, 0, 2, 13.5, 8, 4.5), createCuboidShape(11, 0, 11, 13.5, 8, 13.5), createCuboidShape(0.32, 8,1.6, 14.3, 10.49, 14.6 ), createCuboidShape(0.32, 8, 1.6, 2.65, 24.49,14.6 ));
    protected static final VoxelShape FACE_EAST = VoxelShapes.union(createCuboidShape(2.5, 0, 2.5 ,5 ,8 ,5), createCuboidShape(2.5, 0, 11.5, 5, 8, 14), createCuboidShape(12.5, 0, 2.5, 15, 8, 5), createCuboidShape(12.5, 0, 11.5, 15, 8, 14), createCuboidShape(1.65, 8,1.4, 15.66, 10.49, 14.4 ), createCuboidShape(13.33, 8, 1.4, 15.66, 24.49,14.4 ) );
    protected static final VoxelShape FACE_NORTH = VoxelShapes.union(createCuboidShape(2.5, 0, 1 ,5 ,8 ,3.5), createCuboidShape(2.5, 0, 11, 5, 8, 13.5), createCuboidShape(11.5, 0, 1, 14, 8, 3.5), createCuboidShape(11.5, 0, 11, 14, 8, 13.5), createCuboidShape(1.39, 8,0.32, 14.4, 10.49, 14.32 ), createCuboidShape(1.39, 8, 0.32, 14.4, 24.49,2.65 ));
    protected static final VoxelShape FACE_SOUTH = VoxelShapes.union(createCuboidShape(2, 0, 2.5 ,4.5 ,8 ,5), createCuboidShape(2, 0, 12.5, 4.5, 8, 15), createCuboidShape(11, 0, 2.5, 13.5, 8, 5), createCuboidShape(11, 0, 12.5, 13.5, 8, 15), createCuboidShape(1.61, 8,1.65, 14.66, 10.49, 15.67 ), createCuboidShape(1.61, 8, 13.4, 14.66, 24.49,15.67 ) );
    @SuppressWarnings("deprecated")
    @Override

        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return FACE_WEST;
            case NORTH:
                return FACE_NORTH;
            case SOUTH:
                return FACE_SOUTH;
            case EAST:
            default:
                return FACE_EAST;
        }
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player.isSpectator() || player.isSneaking()) {
                return ActionResult.PASS;
            }
            double px = pos.getX() + 0.5;
            double py = pos.getY() + this.height;
            double pz = pos.getZ() + 0.5;

            List<ChairEntity> active = world.getEntitiesByClass(ChairEntity.class, new Box(pos), Entity::hasPlayerRider);
            if (!active.isEmpty())
                return ActionResult.PASS;

            float yaw = state.get(FACING).getOpposite().asRotation();
            ChairEntity entity = EntityRegistry.CHAIR.create(world);
            entity.refreshPositionAndAngles(px, py, pz, yaw, 0);
            entity.setNoGravity(true);
            entity.setSilent(true);
            entity.setInvisible(false);
            entity.setInvulnerable(true);
            entity.setAiDisabled(true);
            entity.setNoDrag(true);
            entity.setHeadYaw(yaw);
            entity.setYaw(yaw);
            entity.setBodyYaw(yaw);
            if (world.spawnEntity(entity)) {
                player.startRiding(entity, true);
                player.setYaw(yaw);
                player.setHeadYaw(yaw);
                entity.setYaw(yaw);
                entity.setBodyYaw(yaw);
                entity.setHeadYaw(yaw);
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

}

