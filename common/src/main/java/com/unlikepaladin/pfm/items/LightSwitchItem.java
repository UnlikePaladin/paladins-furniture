package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.blocks.PendantBlock;
import com.unlikepaladin.pfm.blocks.PowerableBlock;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class LightSwitchItem extends BlockItem {

    public LightSwitchItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return InteractionResult.FAIL;
        }
        if (player.isShiftKeyDown()) {
            stack.remove(PFMComponents.ACTIVATOR_COMPONENT);
            createComponents(stack);
            return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
        }
        return InteractionResult.PASS;
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Block block = state.getBlock();
        if(block instanceof PowerableBlock){
           if (block instanceof PendantBlock){
               boolean isSingle = (!state.getValue(PendantBlock.DOWN) && !state.getValue(PendantBlock.UP));
               boolean isRoot = (state.getValue(PendantBlock.DOWN) && !state.getValue(PendantBlock.UP));

               if (isSingle || isRoot) {
                    addLight(context.getItemInHand(), pos);
               }
               else {
                    if (context.getLevel().isClientSide())
                        context.getPlayer().displayClientMessage(Component.translatable("message.pfm.light_switch_not_canopy"), false);
               }
           }
           else {
               addLight(context.getItemInHand(), pos);
           }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        BlockPos pos = context.getClickedPos();
        LevelReader world = context.getLevel();
        Direction side = context.getNearestLookingDirection();
        List<BlockPos> lights = getLights(context.getItemInHand());
        boolean canPlace = state.canSurvive(world, pos) && side.getAxis().isHorizontal();

        if (!canPlace) {
            return false;
        }
        if (lights != null) {
            ArrayList<BlockPos> removedLights = new ArrayList<>();
            ArrayList<BlockPos> lightOffsets = new ArrayList<>();
            for (BlockPos lightPos : lights) {
                double distance = Math.sqrt(lightPos.distToLowCornerSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                if (distance > 16) {
                    removedLights.add(lightPos);
                    lights.remove(lightPos);
                } else {
                    lightOffsets.add(pos.subtract(lightPos));
                }
            }
            context.getItemInHand().remove(PFMComponents.ACTIVATOR_COMPONENT);
            createComponents(context.getItemInHand());
            for (BlockPos blockPos : lightOffsets) {
                addLight(context.getItemInHand(), blockPos);
            }

            if (!removedLights.isEmpty() && context.getLevel().isClientSide()){
                context.getPlayer().displayClientMessage(Component.translatable("message.pfm.light_switch_far", removedLights.toString()), false);
            }
        }
        return true;
    }

    private void addLight(ItemStack stack, BlockPos pos)
    {
        createComponents(stack);
        if (!stack.get(PFMComponents.ACTIVATOR_COMPONENT).contains(pos)) {
            stack.get(PFMComponents.ACTIVATOR_COMPONENT).add(pos);
        }
    }

    @Nullable
    public static List<BlockPos> getLights(ItemStack stack) {
        if(stack.has(PFMComponents.ACTIVATOR_COMPONENT)) {
            return stack.get(PFMComponents.ACTIVATOR_COMPONENT);
        }
        return null;
    }

    private static void createComponents(ItemStack stack)
    {
        if(!stack.has(PFMComponents.ACTIVATOR_COMPONENT))
        {
            stack.set(PFMComponents.ACTIVATOR_COMPONENT, new ArrayList<>());
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        if (stack.get(PFMComponents.ACTIVATOR_COMPONENT) != null && !stack.get(PFMComponents.ACTIVATOR_COMPONENT).isEmpty()) {
            int lightNum = stack.get(PFMComponents.ACTIVATOR_COMPONENT).size();
            tooltip.accept(Component.translatable("tooltip.pfm.light_switch_connected", lightNum));
        }
        super.appendHoverText(stack, context, tooltipDisplay, tooltip, tooltipFlag);
    }
}
