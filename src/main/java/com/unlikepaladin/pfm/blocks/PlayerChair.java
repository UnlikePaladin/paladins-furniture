package com.unlikepaladin.pfm.blocks;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import com.unlikepaladin.pfm.blockentities.PlayerChairBlockEntity;
import com.unlikepaladin.pfm.entity.EntityChair;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;


public class PlayerChair extends HorizontalFacingBlockWEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING;
    private NbtCompound nbt = new NbtCompound();
    private final PlayerChair.ChairType type = getChairType();
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING);
    }

    public PlayerChair(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }
    public float height = 0.36f;

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlayerChairBlockEntity(pos, state);
    }



    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(1, 0, 2 ,3.5 ,8 ,4.5), createCuboidShape(1, 0, 11, 3.5, 8, 13.5), createCuboidShape(11, 0, 2, 13.5, 8, 4.5), createCuboidShape(11, 0, 11, 13.5, 8, 13.5), createCuboidShape(0.32, 8,1.6, 14.3, 10.49, 14.6 ), createCuboidShape(0.32, 8, 1.6, 2.65, 24.49,14.6 ));
    protected static final VoxelShape FACE_EAST = VoxelShapes.union(createCuboidShape(2.5, 0, 2.5 ,5 ,8 ,5), createCuboidShape(2.5, 0, 11.5, 5, 8, 14), createCuboidShape(12.5, 0, 2.5, 15, 8, 5), createCuboidShape(12.5, 0, 11.5, 15, 8, 14), createCuboidShape(1.65, 8,1.4, 15.66, 10.49, 14.4 ), createCuboidShape(13.33, 8, 1.4, 15.66, 24.49,14.4 ) );
    protected static final VoxelShape FACE_NORTH = VoxelShapes.union(createCuboidShape(2.5, 0, 1 ,5 ,8 ,3.5), createCuboidShape(2.5, 0, 11, 5, 8, 13.5), createCuboidShape(11.5, 0, 1, 14, 8, 3.5), createCuboidShape(11.5, 0, 11, 14, 8, 13.5), createCuboidShape(1.39, 8,0.32, 14.4, 10.49, 14.32 ), createCuboidShape(1.39, 8, 0.32, 14.4, 24.49,2.65 ));
    protected static final VoxelShape FACE_SOUTH = VoxelShapes.union(createCuboidShape(2, 0, 2.5 ,4.5 ,8 ,5), createCuboidShape(2, 0, 12.5, 4.5, 8, 15), createCuboidShape(11, 0, 2.5, 13.5, 8, 5), createCuboidShape(11, 0, 12.5, 13.5, 8, 15), createCuboidShape(1.61, 8,1.65, 14.66, 10.49, 15.67 ), createCuboidShape(1.61, 8, 13.4, 14.66, 24.49,15.67 ) );

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
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlayerChairBlockEntity) {
            PlayerChairBlockEntity playerChairBlockEntity = (PlayerChairBlockEntity)blockEntity;
            GameProfile gameProfile = null;
            if (itemStack.hasNbt()) {
                NbtCompound nbtCompound = itemStack.getNbt();
                if (nbtCompound.contains("SkullOwner", 10)) {
                    System.out.println("Already has skull owner");
                    gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
                } else if (nbtCompound.contains("SkullOwner", 8) && !StringUtils.isBlank(nbtCompound.getString("SkullOwner"))) {
                    ///System.out.println("Intended Way");
                    gameProfile = new GameProfile((UUID)null, nbtCompound.getString("SkullOwner"));
                    //gameProfile = new GameProfile((UUID)nametoID(playerChairBlockEntity), nbtCompound.getString("SkullOwner"));
                }
            }
            System.out.println(gameProfile);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
         if (player.getStackInHand(hand).isItemEqual(Items.NAME_TAG.getDefaultStack()) && blockEntity instanceof PlayerChairBlockEntity) {
             System.out.println("Attempting to open GUI");

             if (world.isClient) {
                     NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                 System.out.println("World is Client");
                //if (screenHandlerFactory != null) {
              /*   ContainerProviderRegistry.INSTANCE.openContainer(PaladinFurnitureMod.Player_Chair_Screen, player, buf -> {
                     buf.readNbt();
                 });*/
                 System.out.println("Actually Opening GUI");
                return ActionResult.SUCCESS;
              //  }

            }

        }
       /* else if (world.isClient) {
            return ActionResult.FAIL;
        }*/
        else {

            if (player.hasVehicle() || player.isSpectator())
                return ActionResult.FAIL;

            double px = pos.getX() + 0.5;
            double py = pos.getY() + height;
            double pz = pos.getZ() + 0.5;

            List<EntityChair> active = world.getEntitiesByClass(EntityChair.class, new Box(pos), new Predicate<EntityChair>() {
                @Override
                public boolean test(EntityChair entity) {
                    return entity.hasPlayerRider();
                }
            });
            if (!active.isEmpty())
                return ActionResult.FAIL;

            float yaw = state.get(FACING).getOpposite().asRotation();
            EntityChair entity = EntityRegistry.CHAIR.create(world);
            entity.refreshPositionAndAngles(px, py, pz, yaw, 0);
            entity.setNoGravity(true);
            entity.setSilent(true);
            entity.setInvisible(true);
            entity.setInvulnerable(true);
            entity.setHeadYaw(yaw);
            entity.setYaw(yaw);
            entity.setBodyYaw(yaw);
             if (world.spawnEntity(entity) && !player.getStackInHand(hand).isItemEqual(Items.NAME_TAG.getDefaultStack())) {
                System.out.println("Now Riding");

                player.startRiding(entity, true);
                player.setYaw(yaw);
                player.setHeadYaw(yaw);
                entity.setYaw(yaw);
                entity.setBodyYaw(yaw);
                entity.setHeadYaw(yaw);
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.FAIL;

    }

    public PlayerChair.ChairType getChairType() {
        return this.type;
    }
    public interface ChairType {

    }

}
