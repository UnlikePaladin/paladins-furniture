package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.blocks.BasicShowerHandleBlock;
import com.unlikepaladin.pfm.blocks.BasicShowerHeadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ShowerHandleItem extends BlockItem {
    private Supplier<BasicShowerHandleBlock> block;

    public ShowerHandleItem(Supplier<BasicShowerHandleBlock> block, Properties settings) {
        super(block.get(), settings);
        this.block = block;
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return InteractionResult.FAIL;
        }
        if (player.isShiftKeyDown()) {
            stack.remove(PFMComponents.ACTIVATOR_COMPONENT);
            return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        super.useOn(context);
        BlockPos pos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Block block = state.getBlock();
        if(block instanceof BasicShowerHeadBlock){
            setShowerHeadPos(context.getItemInHand(), pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        BlockPos pos = context.getClickedPos();
        LevelReader world = context.getLevel();
        Direction playerFacing = context.getHorizontalDirection();
        Direction placeDirection = context.getNearestLookingDirection();

        boolean canPlace = state.canSurvive(world, pos) && placeDirection.getAxis().isHorizontal();
        if (!canPlace) {
            return false;
        }

        BlockPos headPos = getShowerHead(context.getItemInHand());
        if (headPos != null) {
            BlockPos placedPos = pos.relative(playerFacing);

            double distance = Math.sqrt(headPos.distToLowCornerSqr(placedPos.getX() + 0.5, placedPos.getY() + 0.5, placedPos.getZ() + 0.5));
            if (distance > 16 && world.isClientSide()){
                context.getPlayer().displayClientMessage(Component.translatable("message.pfm.shower_handle_far", headPos.toString()), false);
            }
            if (distance > 16) {
                context.getItemInHand().remove(PFMComponents.ACTIVATOR_COMPONENT);
            } else {
                setShowerHeadPos(context.getItemInHand(), pos.subtract(headPos));
            }
            return state.canSurvive(world, pos) && placeDirection.getAxis().isHorizontal();
        }
        return true;
    }

    private void setShowerHeadPos(ItemStack stack, BlockPos pos) {
        stack.set(PFMComponents.ACTIVATOR_COMPONENT, List.of(pos));
    }

    @Nullable
    public static BlockPos getShowerHead(ItemStack stack) {
        if (stack.has(PFMComponents.ACTIVATOR_COMPONENT) && !stack.get(PFMComponents.ACTIVATOR_COMPONENT).isEmpty()) {
            return stack.get(PFMComponents.ACTIVATOR_COMPONENT).getFirst();
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        if (stack.has(PFMComponents.ACTIVATOR_COMPONENT) && getShowerHead(stack) != null) {
            tooltip.accept(Component.translatable("tooltip.pfm.shower_handle_connected", 1));
        }
        super.appendHoverText(stack, context, tooltipDisplay, tooltip, tooltipFlag);
    }
}
