package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.data.PFMTags;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BasicChairBlock extends AbstractSittableBlock {
    private static final List<FurnitureBlock> WOOD_BASIC_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_BASIC_CHAIRS = new ArrayList<>();

    public static final BooleanProperty TUCKED = BooleanProperty.create("tucked");
    public BasicChairBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TUCKED, false));
        this.height = 0.36f;
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(BasicChairBlock.class)){
            WOOD_BASIC_CHAIRS.add(new FurnitureBlock(this, "chair"));
        }
        else if (this.getClass().isAssignableFrom(BasicChairBlock.class)){
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(TUCKED);
    }

    protected static final VoxelShape FACE_WEST = Shapes.or(box(1, 0, 2 ,3.5 ,8 ,4.5), box(1, 0, 11, 3.5, 8, 13.5), box(11, 0, 2, 13.5, 8, 4.5), box(11, 0, 11, 13.5, 8, 13.5), box(0.32, 8,1.6, 14.3, 10.49, 14.6 ), box(0.32, 8, 1.6, 2.65, 24.49,14.6 ));
    protected static final VoxelShape FACE_EAST = Shapes.or(box(2.5, 0, 2.5 ,5 ,8 ,5), box(2.5, 0, 11.5, 5, 8, 14), box(12.5, 0, 2.5, 15, 8, 5), box(12.5, 0, 11.5, 15, 8, 14), box(1.65, 8,1.4, 15.66, 10.49, 14.4 ), box(13.33, 8, 1.4, 15.66, 24.49,14.4 ) );
    protected static final VoxelShape FACE_NORTH = Shapes.or(box(2.5, 0, 1 ,5 ,8 ,3.5), box(2.5, 0, 11, 5, 8, 13.5), box(11.5, 0, 1, 14, 8, 3.5), box(11.5, 0, 11, 14, 8, 13.5), box(1.39, 8,0.32, 14.4, 10.49, 14.32 ), box(1.39, 8, 0.32, 14.4, 24.49,2.65 ));
    protected static final VoxelShape FACE_SOUTH = Shapes.or(box(2, 0, 2.5 ,4.5 ,8 ,5), box(2, 0, 12.5, 4.5, 8, 15), box(11, 0, 2.5, 13.5, 8, 5), box(11, 0, 12.5, 13.5, 8, 15), box(1.61, 8,1.65, 14.66, 10.49, 15.67 ), box(1.61, 8, 13.4, 14.66, 24.49,15.67 ) );
    protected static final VoxelShape FACE_NORTH_TUCKED = PFMShapeUtil.tuckShape(Direction.NORTH, FACE_NORTH);
    protected static final VoxelShape FACE_SOUTH_TUCKED = PFMShapeUtil.tuckShape(Direction.SOUTH, FACE_SOUTH);
    protected static final VoxelShape FACE_EAST_TUCKED = PFMShapeUtil.tuckShape(Direction.EAST, FACE_EAST);
    protected static final VoxelShape FACE_WEST_TUCKED = PFMShapeUtil.tuckShape(Direction.WEST, FACE_WEST);

    @Override
        public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        if (state.getValue(TUCKED)) {
            switch (dir) {
                case WEST:{
                    return FACE_WEST_TUCKED;}
                case NORTH:
                    return FACE_NORTH_TUCKED;
                case SOUTH:
                    return FACE_SOUTH_TUCKED;
                default:
                    return FACE_EAST_TUCKED;
            }
        }
        switch (dir) {
            case WEST:
                return FACE_WEST;
            case NORTH:
                return FACE_NORTH;
            case SOUTH:
                return FACE_SOUTH;
            default:
                return FACE_EAST;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!canTuck(world.getBlockState(pos.relative(state.getValue(FACING).getOpposite()))) && state.getValue(TUCKED)){
            return state.setValue(TUCKED, false);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    public boolean canTuck(BlockState state) {
        return state.is(PFMTags.TUCKABLE_BLOCKS);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown() && this.canTuck(world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())))) {
            if (state.getValue(TUCKED)) {
                world.setBlockAndUpdate(pos, state.setValue(TUCKED, false));
            }
            else {
                world.setBlockAndUpdate(pos, state.setValue(TUCKED, true));
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }
}

