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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
        if (world.isClientSide) {
            return InteractionResult.FAIL;
        }
        if (player.isShiftKeyDown()) {
            stack.remove(DataComponents.BLOCK_ENTITY_DATA);
            createNbt(stack);
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
            setShowerHeadPosNBT(context.getItemInHand(), pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        BlockPos pos = context.getClickedPos();
        LevelReader world = context.getLevel();
        LongTag showerHeadLong = getShowerHead(context.getItemInHand());
        Direction playerFacing = context.getHorizontalDirection();
        Direction placeDirection = context.getNearestLookingDirection();

        boolean canPlace = state.canSurvive(world, pos) && placeDirection.getAxis().isHorizontal();
        if (!canPlace) {
            return false;
        }
        if (showerHeadLong != null) {
            BlockPos headPos = BlockPos.of(showerHeadLong.getAsLong());
            BlockPos placedPos = pos.relative(playerFacing);

            double distance = Math.sqrt(headPos.distToLowCornerSqr(placedPos.getX() + 0.5, placedPos.getY() + 0.5, placedPos.getZ() + 0.5));
            if (distance > 16 && world.isClientSide()){
                context.getPlayer().displayClientMessage(Component.translatable("message.pfm.shower_handle_far", headPos.toString()), false);
            }
            if (distance > 16) {
                context.getItemInHand().remove(DataComponents.BLOCK_ENTITY_DATA);
                createNbt(context.getItemInHand());
            } else {
                setShowerHeadPosNBT(context.getItemInHand(), pos.subtract(headPos));
            }
            return state.canSurvive(world, pos) && placeDirection.getAxis().isHorizontal();
        }
        return true;
    }

    private void setShowerHeadPosNBT(ItemStack stack, BlockPos pos) {
        CompoundTag nbtCompound = createNbt(stack);
        if(!nbtCompound.contains("showerHead", Tag.TAG_LONG)) {
            nbtCompound.put("showerHead", LongTag.valueOf(0));
        }

        LongTag showerHeadPos = (LongTag) nbtCompound.get("showerHead");
        if(showerHeadPos.getAsLong() != pos.asLong()) {
            nbtCompound.put("showerHead", LongTag.valueOf(pos.asLong()));
        }
        stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(nbtCompound));
    }

    @Nullable
    public static LongTag getShowerHead(ItemStack stack) {
        if (stack.has(DataComponents.BLOCK_ENTITY_DATA)) {
            CompoundTag blockEntityTag = stack.get(DataComponents.BLOCK_ENTITY_DATA).getUnsafe();
            if(blockEntityTag.contains("showerHead", Tag.TAG_LONG)) {
                return (LongTag) blockEntityTag.get("showerHead");
            }
        }
        return null;
    }

    private static CompoundTag createNbt(ItemStack stack) {
        if(!stack.has(DataComponents.BLOCK_ENTITY_DATA))
        {
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putString("id", "pfm:shower_handle_block_entity");
            stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(nbtCompound));
        }
        return stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        if (stack.has(DataComponents.BLOCK_ENTITY_DATA) && getShowerHead(stack) != null) {
            tooltip.add(Component.translatable("tooltip.pfm.shower_handle_connected", 1));
        }
        super.appendHoverText(stack, context, tooltip, type);
    }
}
