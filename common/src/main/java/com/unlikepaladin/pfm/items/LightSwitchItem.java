package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.blocks.PendantBlock;
import com.unlikepaladin.pfm.blocks.PowerableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class LightSwitchItem extends BlockItem {

    public LightSwitchItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (world.isClient()) {
            return ActionResult.FAIL;
        }
        if (player.isSneaking()) {
            stack.remove(PFMComponents.ACTIVATOR_COMPONENT);
            createComponents(stack);
            return ActionResult.SUCCESS.withNewHandStack(stack);
        }
        return ActionResult.PASS;
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        Block block = state.getBlock();
        if(block instanceof PowerableBlock){
           if (block instanceof PendantBlock){
               boolean isSingle = (!state.get(PendantBlock.DOWN) && !state.get(PendantBlock.UP));
               boolean isRoot = (state.get(PendantBlock.DOWN) && !state.get(PendantBlock.UP));

               if (isSingle || isRoot) {
                    addLight(context.getStack(), pos);
               }
               else {
                    if (context.getWorld().isClient())
                        context.getPlayer().sendMessage(Text.translatable("message.pfm.light_switch_not_canopy"), false);
               }
           }
           else {
               addLight(context.getStack(), pos);
           }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    @Override
    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        BlockPos pos = context.getBlockPos();
        WorldView world = context.getWorld();
        Direction side = context.getSide();
        List<BlockPos> lights = getLights(context.getStack());
        boolean canPlace = state.canPlaceAt(world, pos) && side.getAxis().isHorizontal();

        if (!canPlace) {
            return false;
        }
        if (lights != null) {
            ArrayList<BlockPos> removedLights = new ArrayList<>();
            ArrayList<BlockPos> lightOffsets = new ArrayList<>();
            for (BlockPos lightPos : lights) {
                double distance = Math.sqrt(lightPos.getSquaredDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                if (distance > 16) {
                    removedLights.add(lightPos);
                    lights.remove(lightPos);
                } else {
                    lightOffsets.add(pos.subtract(lightPos));
                }
            }
            context.getStack().remove(PFMComponents.ACTIVATOR_COMPONENT);
            createComponents(context.getStack());
            for (BlockPos blockPos : lightOffsets) {
                addLight(context.getStack(), blockPos);
            }

            if (!removedLights.isEmpty() && context.getWorld().isClient()){
                context.getPlayer().sendMessage(Text.translatable("message.pfm.light_switch_far", removedLights.toString()), false);
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
        if(stack.get(PFMComponents.ACTIVATOR_COMPONENT) != null) {
            return stack.get(PFMComponents.ACTIVATOR_COMPONENT);
        }
        return null;
    }

    private static void createComponents(ItemStack stack)
    {
        if(stack.get(PFMComponents.ACTIVATOR_COMPONENT) == null)
        {
            stack.set(PFMComponents.ACTIVATOR_COMPONENT, new ArrayList<>());
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (stack.get(PFMComponents.ACTIVATOR_COMPONENT) != null && !stack.get(PFMComponents.ACTIVATOR_COMPONENT).isEmpty()) {
            int lightNum = stack.get(PFMComponents.ACTIVATOR_COMPONENT).size();
            textConsumer.accept(Text.translatable("tooltip.pfm.light_switch_connected", lightNum));
        }
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
