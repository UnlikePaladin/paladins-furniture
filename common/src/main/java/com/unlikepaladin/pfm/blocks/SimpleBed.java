package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.LogTable.rotateShape;

public class SimpleBed extends BedBlock implements DyeableFurniture {
    public static EnumProperty<MiddleShape> SHAPE = EnumProperty.of("shape", MiddleShape.class);
    private static final List<FurnitureBlock> SIMPLE_BEDS = new ArrayList<>();
    public static final BooleanProperty BUNK = BooleanProperty.of("bunk");

    public SimpleBed(DyeColor color, Settings settings) {
        super(color, settings);
        setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(PART, BedPart.FOOT).with(OCCUPIED, false).with(BUNK, false).with(SHAPE, MiddleShape.SINGLE));
        if(this.getClass().isAssignableFrom(SimpleBed.class)){
            String bedColor = color.getName();
            SIMPLE_BEDS.add(new FurnitureBlock(this, bedColor+"_simple_bed"));
        }
    }

    public static Stream<FurnitureBlock> streamSimpleBeds() {
        return SIMPLE_BEDS.stream();
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        Direction direction = ctx.getPlayerFacing();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(direction);
        if (ctx.getWorld().getBlockState(blockPos2).canReplace(ctx)) {
            return getShape(blockState, ctx.getWorld(), ctx.getBlockPos(), blockState.get(FACING));
        }
        return null;
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.CONSUME;
        }
        if (state.get(PART) != BedPart.HEAD && !((state = world.getBlockState(pos = pos.offset(state.get(FACING)))).getBlock() instanceof SimpleBed)) {
            return ActionResult.CONSUME;
        }
        if (!BedBlock.isBedWorking(world)) {
            world.removeBlock(pos, false);
            BlockPos blockPos = pos.offset(state.get(FACING).getOpposite());
            if (world.getBlockState(blockPos).isOf(this)) {
                world.removeBlock(blockPos, false);
            }
            world.createExplosion(null, DamageSource.badRespawnPoint(), null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 5.0f, true, Explosion.DestructionType.DESTROY);
            return ActionResult.SUCCESS;
        }
        if (state.get(OCCUPIED)) {
            if (!this.isFree(world, pos)) {
                player.sendMessage(new TranslatableText("block.minecraft.bed.occupied"), true);
            }
            return ActionResult.SUCCESS;
        }
        player.trySleep(pos).ifLeft(reason -> {
            if (reason != null) {
                player.sendMessage(reason.getMessage(), true);
            }
        });
        return ActionResult.SUCCESS;
    }

    private boolean isFree(World world, BlockPos pos) {
        List<VillagerEntity> list = world.getEntitiesByClass(VillagerEntity.class, new Box(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        }
        list.get(0).wakeUp();
        return true;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (direction == getDirectionTowardsOtherPart(state.get(PART), state.get(FACING))) {
            if (neighborState.getBlock() instanceof SimpleBed && neighborState.get(PART) != state.get(PART)) {
                return state.with(OCCUPIED, neighborState.get(OCCUPIED));
            }
            return Blocks.AIR.getDefaultState();
        }
        return getShape(state, world, pos, state.get(FACING));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockPos;
        BlockState blockState;
        BedPart bedPart;
        if (!world.isClient && player.isCreative() && (bedPart = state.get(PART)) == BedPart.FOOT && (blockState = world.getBlockState(blockPos = pos.offset(getDirectionTowardsOtherPart(bedPart, state.get(FACING))))).isOf(this) && blockState.get(PART) == BedPart.HEAD) {
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
        }
        this.spawnBreakParticles(world, player, pos, state);
        if (state.isIn(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinBrain.onGuardedBlockInteracted(player, false);
        }
        world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(SHAPE);
        stateManager.add(BUNK);
        super.appendProperties(stateManager);
    }

    public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    public boolean isBed(WorldAccess world, BlockPos pos, Direction direction, Direction bedDirection, BlockState originalState)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if(state.getBlock().getClass().isAssignableFrom(SimpleBed.class) && state.getBlock() instanceof SimpleBed)
        {
            if (state.get(PART) == originalState.get(PART)) {
                Direction sourceDirection = state.get(FACING);
                return sourceDirection.equals(bedDirection);
            }
        }
        return false;
    }

    public BlockState getShape(BlockState state, WorldAccess world, BlockPos pos, Direction dir)
    {
        boolean left = isBed(world, pos, dir.rotateYCounterclockwise(), dir, state);
        boolean right = isBed(world, pos, dir.rotateYClockwise(), dir, state);
        if(left && right)
        {
            state = state.with(SHAPE, MiddleShape.MIDDLE);
        }
        else if(left)
        {
            state = state.with(SHAPE, MiddleShape.RIGHT);
        }
        else if(right)
        {
            state = state.with(SHAPE, MiddleShape.LEFT);
        }
        else {
            state = state.with(SHAPE, MiddleShape.SINGLE);
        }
        boolean down = isBed(world, pos, Direction.DOWN, dir, state);
        if (down) {
            state = state.with(BUNK, true);
        }
        else {
            state = state.with(BUNK, false);
        }
        return state;
    }

    static final VoxelShape HEAD = VoxelShapes.union(createCuboidShape(0, 2, 0, 16, 14, 3),createCuboidShape(0, 2, 3, 16, 9, 16),createCuboidShape(1, 9, 3, 15, 10, 11));
    static final VoxelShape HEAD_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD);
    static final VoxelShape HEAD_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD);
    static final VoxelShape HEAD_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD);

    static final VoxelShape FOOT = VoxelShapes.union(createCuboidShape(0, 2, 0, 16, 9, 13),createCuboidShape(0, 2, 13, 16, 10, 16));
    static final VoxelShape FOOT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT);
    static final VoxelShape FOOT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT);
    static final VoxelShape FOOT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT);

    static final VoxelShape FOOT_FOOT_R = createCuboidShape(13, 0, 13, 16, 2, 16);
    static final VoxelShape FOOT_FOOT_L = createCuboidShape(0, 0, 13, 3, 2, 16);
    static final VoxelShape FOOT_HEAD_R = createCuboidShape(13, 0, 0, 16, 2, 3);
    static final VoxelShape FOOT_HEAD_L = createCuboidShape(0, 0, 0, 3, 2, 3);

    static final VoxelShape HEAD_SINGLE = VoxelShapes.union(HEAD, FOOT_HEAD_L, FOOT_HEAD_R);
    static final VoxelShape HEAD_SINGLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_SINGLE);
    static final VoxelShape HEAD_SINGLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_SINGLE);
    static final VoxelShape HEAD_SINGLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_SINGLE);

    static final VoxelShape FOOT_SINGLE = VoxelShapes.union(FOOT, FOOT_FOOT_L, FOOT_FOOT_R);
    static final VoxelShape FOOT_SINGLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_SINGLE);
    static final VoxelShape FOOT_SINGLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_SINGLE);
    static final VoxelShape FOOT_SINGLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_SINGLE);

    static final VoxelShape HEAD_LEFT = VoxelShapes.union(HEAD, FOOT_HEAD_L);
    static final VoxelShape HEAD_LEFT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_LEFT);
    static final VoxelShape HEAD_LEFT_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_LEFT);
    static final VoxelShape HEAD_LEFT_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_LEFT);

    static final VoxelShape FOOT_LEFT = VoxelShapes.union(FOOT, FOOT_FOOT_L);
    static final VoxelShape FOOT_LEFT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_LEFT);
    static final VoxelShape FOOT_LEFT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_LEFT);
    static final VoxelShape FOOT_LEFT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_LEFT);

    static final VoxelShape HEAD_RIGHT = VoxelShapes.union(HEAD, FOOT_HEAD_R);
    static final VoxelShape HEAD_RIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_RIGHT);
    static final VoxelShape HEAD_RIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_RIGHT);
    static final VoxelShape HEAD_RIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_RIGHT);

    static final VoxelShape FOOT_RIGHT = VoxelShapes.union(FOOT, FOOT_FOOT_R);
    static final VoxelShape FOOT_RIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_RIGHT);
    static final VoxelShape FOOT_RIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_RIGHT);
    static final VoxelShape FOOT_RIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_RIGHT);

    static final VoxelShape HEAD_BUNK = createCuboidShape(0, -2, 0,16, 2, 3);
    static final VoxelShape HEAD_BUNK_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_BUNK);
    static final VoxelShape HEAD_BUNK_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_BUNK);
    static final VoxelShape HEAD_BUNK_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_BUNK);

    static final VoxelShape HEAD_SINGLE_BUNK = VoxelShapes.union(HEAD_SINGLE, HEAD_BUNK);
    static final VoxelShape HEAD_SINGLE_SOUTH_BUNK = VoxelShapes.union(HEAD_SINGLE_SOUTH, HEAD_BUNK_SOUTH);
    static final VoxelShape HEAD_SINGLE_EAST_BUNK = VoxelShapes.union(HEAD_SINGLE_EAST, HEAD_BUNK_EAST);
    static final VoxelShape HEAD_SINGLE_WEST_BUNK = VoxelShapes.union(HEAD_SINGLE_WEST, HEAD_BUNK_WEST);

    static final VoxelShape FOOT_BUNK_LEFT = createCuboidShape(0, -6, 13,3, 0, 16);
    static final VoxelShape FOOT_BUNK_LEFT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_BUNK_LEFT);
    static final VoxelShape FOOT_BUNK_LEFT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_BUNK_LEFT);
    static final VoxelShape FOOT_BUNK_LEFT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_BUNK_LEFT);

    static final VoxelShape FOOT_BUNK_RIGHT = createCuboidShape(13, -6, 13,16, 0, 16);
    static final VoxelShape FOOT_BUNK_RIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_BUNK_RIGHT);
    static final VoxelShape FOOT_BUNK_RIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_BUNK_RIGHT);
    static final VoxelShape FOOT_BUNK_RIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_BUNK_RIGHT);

    static final VoxelShape FOOT_SINGLE_BUNK = VoxelShapes.union(FOOT_SINGLE, FOOT_BUNK_LEFT, FOOT_BUNK_RIGHT);
    static final VoxelShape FOOT_SINGLE_SOUTH_BUNK = VoxelShapes.union(FOOT_SINGLE_SOUTH, FOOT_BUNK_LEFT_SOUTH, FOOT_BUNK_RIGHT_SOUTH);
    static final VoxelShape FOOT_SINGLE_EAST_BUNK = VoxelShapes.union(FOOT_SINGLE_EAST, FOOT_BUNK_LEFT_EAST, FOOT_BUNK_RIGHT_EAST);
    static final VoxelShape FOOT_SINGLE_WEST_BUNK = VoxelShapes.union(FOOT_SINGLE_WEST, FOOT_BUNK_LEFT_WEST, FOOT_BUNK_RIGHT_WEST);

    static final VoxelShape FOOT_LEFT_BUNK = VoxelShapes.union(FOOT_LEFT, FOOT_BUNK_LEFT);
    static final VoxelShape FOOT_LEFT_SOUTH_BUNK = VoxelShapes.union(FOOT_LEFT_SOUTH, FOOT_BUNK_LEFT_SOUTH);
    static final VoxelShape FOOT_LEFT_EAST_BUNK = VoxelShapes.union(FOOT_LEFT_EAST, FOOT_BUNK_LEFT_EAST);
    static final VoxelShape FOOT_LEFT_WEST_BUNK = VoxelShapes.union(FOOT_LEFT_WEST, FOOT_BUNK_LEFT_WEST);

    static final VoxelShape FOOT_RIGHT_BUNK = VoxelShapes.union(FOOT_RIGHT, FOOT_BUNK_RIGHT);
    static final VoxelShape FOOT_RIGHT_SOUTH_BUNK = VoxelShapes.union(FOOT_RIGHT_SOUTH, FOOT_BUNK_RIGHT_SOUTH);
    static final VoxelShape FOOT_RIGHT_EAST_BUNK = VoxelShapes.union(FOOT_RIGHT_EAST, FOOT_BUNK_RIGHT_EAST);
    static final VoxelShape FOOT_RIGHT_WEST_BUNK = VoxelShapes.union(FOOT_RIGHT_WEST, FOOT_BUNK_RIGHT_WEST);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        BedPart bedPart = state.get(PART);
        MiddleShape middleShape = state.get(SHAPE);
        boolean bunk = state.get(BUNK);
        if(!bunk){
            switch (middleShape){
                case MIDDLE -> {
                    switch (dir){
                        case NORTH -> {
                            if(bedPart == BedPart.HEAD){
                                return HEAD;
                            }
                            return FOOT;
                        }
                        case EAST -> {
                            if(bedPart == BedPart.HEAD){
                                return HEAD_EAST;
                            }
                            return FOOT_EAST;
                        }
                        case WEST -> {
                            if(bedPart == BedPart.HEAD){
                                return HEAD_WEST;
                            }
                            return FOOT_WEST;
                        }
                        default -> {
                            if(bedPart == BedPart.HEAD){
                                return HEAD_SOUTH;
                            }
                            return FOOT_SOUTH;
                        }
                    }
                }
                case SINGLE -> {
                    switch (dir){
                        case NORTH -> {
                            if(bedPart == BedPart.HEAD){
                                return HEAD_SINGLE;
                            }
                            return FOOT_SINGLE;
                        }
                        case EAST -> {
                            if(bedPart == BedPart.HEAD){
                                return HEAD_SINGLE_EAST;
                            }
                            return FOOT_SINGLE_EAST;
                        }
                        case WEST -> {
                            if(bedPart == BedPart.HEAD){
                                return HEAD_SINGLE_WEST;
                            }
                            return FOOT_SINGLE_WEST;
                        }
                        default -> {
                            if(bedPart == BedPart.HEAD){
                                return HEAD_SINGLE_SOUTH;
                            }
                            return FOOT_SINGLE_SOUTH;
                        }
                    }
                }
                case RIGHT -> {
                    switch (dir) {
                        case NORTH -> {
                            if (bedPart == BedPart.HEAD) {
                                return HEAD_RIGHT;
                            }
                            return FOOT_RIGHT;
                        }
                        case EAST -> {
                            if (bedPart == BedPart.HEAD) {
                                return HEAD_RIGHT_EAST;
                            }
                            return FOOT_RIGHT_EAST;
                        }
                        case WEST -> {
                            if (bedPart == BedPart.HEAD) {
                                return HEAD_RIGHT_WEST;
                            }
                            return FOOT_RIGHT_WEST;
                        }
                        default -> {
                            if (bedPart == BedPart.HEAD) {
                                return HEAD_RIGHT_SOUTH;
                            }
                            return FOOT_RIGHT_SOUTH;
                        }
                    }
                }
                default -> {
                    switch (dir) {
                        case NORTH -> {
                            if (bedPart == BedPart.HEAD) {
                                return HEAD_LEFT;
                            }
                            return FOOT_LEFT;
                        }
                        case EAST -> {
                            if (bedPart == BedPart.HEAD) {
                                return HEAD_LEFT_EAST;
                            }
                            return FOOT_LEFT_EAST;
                        }
                        case WEST -> {
                            if (bedPart == BedPart.HEAD) {
                                return HEAD_LEFT_WEST;
                            }
                            return FOOT_LEFT_WEST;
                        }
                        default -> {
                            if (bedPart == BedPart.HEAD) {
                                return HEAD_LEFT_SOUTH;
                            }
                            return FOOT_LEFT_SOUTH;
                        }
                    }
                }
            }
        }
        else {
            if (bedPart == BedPart.HEAD) {
                switch (dir) {
                    case NORTH -> {
                        return HEAD_SINGLE_BUNK;
                    }
                    case EAST -> {
                        return HEAD_SINGLE_EAST_BUNK;
                    }
                    case WEST -> {
                        return HEAD_SINGLE_WEST_BUNK;
                    }
                    default -> {
                        return HEAD_SINGLE_SOUTH_BUNK;
                    }
                }
            }
            else{
                switch (middleShape) {
                    case SINGLE -> {
                        switch (dir){
                            case NORTH -> {
                                return FOOT_SINGLE_BUNK;
                            }
                            case EAST -> {
                                return FOOT_SINGLE_EAST_BUNK;
                            }
                            case WEST -> {
                                return FOOT_SINGLE_WEST_BUNK;
                            }
                            default -> {
                                return FOOT_SINGLE_SOUTH_BUNK;
                            }
                        }
                    }
                    case MIDDLE -> {
                        switch (dir){
                            case NORTH -> {
                                return FOOT;
                            }
                            case EAST -> {
                                return FOOT_EAST;
                            }
                            case WEST -> {
                                return FOOT_WEST;
                            }
                            default -> {
                                return FOOT_SOUTH;
                            }
                        }
                    }
                    case RIGHT ->{
                        switch (dir) {
                            case NORTH -> {
                                return FOOT_RIGHT_BUNK;
                            }
                            case EAST -> {
                                return FOOT_RIGHT_EAST_BUNK;
                            }
                            case WEST -> {
                                return FOOT_RIGHT_WEST_BUNK;
                            }
                            default -> {
                                return FOOT_RIGHT_SOUTH_BUNK;
                            }
                        }
                    }
                    default -> {
                        switch (dir) {
                            case NORTH -> {
                                return FOOT_LEFT_BUNK;
                            }
                            case EAST -> {
                                return FOOT_LEFT_EAST_BUNK;
                            }
                            case WEST -> {
                                return FOOT_LEFT_WEST_BUNK;
                            }
                            default -> {
                                return FOOT_LEFT_SOUTH_BUNK;
                            }
                        }
                    }
                }
            }
        }
    }
}
