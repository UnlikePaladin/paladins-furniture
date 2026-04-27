package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.LightSwitchItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class BasicLampBlock extends PowerableBlock implements EntityBlock {
    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    public BasicLampBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(LIT, false).setValue(POWERLOCKED, false));
    }

    @Override
    public void setPowered(Level world, BlockPos lightPos, boolean powered) {
        BlockState state = world.getBlockState(lightPos);
        world.setBlockAndUpdate(lightPos, state.setValue(LIT, powered).setValue(POWERLOCKED,powered));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasTag()) {
            CompoundTag nbtCompound = itemStack.getTagElement("BlockEntityTag");
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof LampBlockEntity && nbtCompound != null) {
                DyeColor color = DyeColor.byName(nbtCompound.getString("color"), DyeColor.WHITE);
                WoodVariant variant = WoodVariantRegistry.getVariant(ResourceLocation.tryParse(nbtCompound.getString("variant")));
                ((LampBlockEntity) blockEntity).setPFMColor(color);
                ((LampBlockEntity) blockEntity).setVariant(variant);
            }
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(LIT);
        stateManager.add(POWERLOCKED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter world) {
        return getBlockEntity();
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity() {
        return null;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public MaterialColor defaultMaterialColor() {
        return super.defaultMaterialColor();
    }

    private static final VoxelShape SINGLE = Shapes.or(box(7, 1.5, 7, 9, 6, 9), box(3, 0, 3,13, 1.5, 13),box(1.5, 5, 1.5,14.5, 16, 14.5));
    private static final VoxelShape TOP = Shapes.or(box(7, 0, 7,9, 6, 9),box(1.5, 5, 1.5,14.5, 16, 14.5));
    private static final VoxelShape MIDDLE = Shapes.or(box(7, 0, 7,9, 16, 9));
    private static final VoxelShape BOTTOM = Shapes.or(box(7, 1.5, 7, 9, 16, 9), box(3, 0, 3,13, 1.5, 13));
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean up = world.getBlockState(pos.above()).getBlock() instanceof BasicLampBlock;
        boolean down = world.getBlockState(pos.below()).getBlock() instanceof BasicLampBlock;
        if (up && down) {
            return MIDDLE;
        } else if (up) {
            return BOTTOM;
        } else if (down)
        {
            return TOP;
        }
        return SINGLE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).getItem() instanceof LightSwitchItem)
            return super.use(state, world, pos, player, hand, hit);

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!state.getValue(POWERLOCKED)) {
            togglePower(state, world, pos);
            float f = state.getValue(LIT) ? 0.9f : 0.8f;
            world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, f);
            world.updateNeighborsAt(pos, this);
            return InteractionResult.CONSUME;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    public BlockState togglePower(BlockState state, Level world, BlockPos pos) {
        state = state.cycle(LIT);
        world.setBlock(pos, state, 3);
        world.updateNeighborsAt(pos, this);
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction.getAxis().isVertical() && neighborState.getBlock() instanceof BasicLampBlock ? state.setValue(LIT, neighborState.getValue(LIT)).setValue(POWERLOCKED, neighborState.getValue(POWERLOCKED)) : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(world, pos, state);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LampBlockEntity) {
            CompoundTag nbtCompound = ((LampBlockEntity)blockEntity).writeColorAndVariant(new CompoundTag());
            stack.addTagElement("BlockEntityTag", nbtCompound);
        }
        return stack;
    }
}
