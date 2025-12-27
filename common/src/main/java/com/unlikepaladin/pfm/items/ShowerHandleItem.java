package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.blocks.BasicShowerHandleBlock;
import com.unlikepaladin.pfm.blocks.BasicShowerHeadBlock;
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
import net.minecraft.nbt.NbtLong;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ShowerHandleItem extends BlockItem {
    private Supplier<BasicShowerHandleBlock> block;

    public ShowerHandleItem(Supplier<BasicShowerHandleBlock> block, Settings settings) {
        super(block.get(), settings);
        this.block = block;
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (world.isClient()) {
            return ActionResult.FAIL;
        }
        if (player.isSneaking()) {
            stack.remove(PFMComponents.ACTIVATOR_COMPONENT);
            return ActionResult.SUCCESS.withNewHandStack(stack);
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        super.useOnBlock(context);
        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        Block block = state.getBlock();
        if(block instanceof BasicShowerHeadBlock){
            setShowerHeadPos(context.getStack(), pos);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        BlockPos pos = context.getBlockPos();
        WorldView world = context.getWorld();
        Direction playerFacing = context.getHorizontalPlayerFacing();
        Direction placeDirection = context.getSide();

        boolean canPlace = state.canPlaceAt(world, pos) && placeDirection.getAxis().isHorizontal();
        if (!canPlace) {
            return false;
        }
        BlockPos headPos = getShowerHead(context.getStack());
        if (headPos != null) {
            BlockPos placedPos = pos.offset(playerFacing);

            double distance = Math.sqrt(headPos.getSquaredDistance(placedPos.getX() + 0.5, placedPos.getY() + 0.5, placedPos.getZ() + 0.5));
            if (distance > 16 && world.isClient()){
                context.getPlayer().sendMessage(Text.translatable("message.pfm.shower_handle_far", headPos.toString()), false);
            }
            if (distance > 16) {
                context.getStack().remove(PFMComponents.ACTIVATOR_COMPONENT);
            } else {
                setShowerHeadPos(context.getStack(), pos.subtract(headPos));
            }
            return state.canPlaceAt(world, pos) && placeDirection.getAxis().isHorizontal();
        }
        return true;
    }

    private void setShowerHeadPos(ItemStack stack, BlockPos pos) {
        stack.set(PFMComponents.ACTIVATOR_COMPONENT, List.of(pos));
    }

    @Nullable
    public static BlockPos getShowerHead(ItemStack stack) {
        if (stack.get(PFMComponents.ACTIVATOR_COMPONENT) != null && !stack.get(PFMComponents.ACTIVATOR_COMPONENT).isEmpty()) {
            return stack.get(PFMComponents.ACTIVATOR_COMPONENT).getFirst();
        }
        return null;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (stack.get(DataComponentTypes.BLOCK_ENTITY_DATA) != null && getShowerHead(stack) != null) {
            textConsumer.accept(Text.translatable("tooltip.pfm.shower_handle_connected", 1));
        }
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
