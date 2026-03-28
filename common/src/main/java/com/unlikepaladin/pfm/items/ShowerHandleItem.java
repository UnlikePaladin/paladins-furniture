package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.blocks.BasicShowerHandleBlock;
import com.unlikepaladin.pfm.blocks.BasicShowerHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
        if (player.isShiftKeyDown()) {
            stack.setTag(null);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
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

        boolean canPlace = state.getBlock().canSurvive(state, world, pos) && placeDirection.getAxis().isHorizontal();
        if (!canPlace) {
            return false;
        }
        if (showerHeadLong != null) {
            BlockPos headPos = BlockPos.of(showerHeadLong.getAsLong());
            BlockPos placedPos = pos.relative(playerFacing);

            double distance = Math.sqrt(headPos.distToLowCornerSqr(placedPos.getX() + 0.5, placedPos.getY() + 0.5, placedPos.getZ() + 0.5));
            if (distance > 16 && world.isClientSide()){
                context.getPlayer().displayClientMessage(new TranslatableComponent("message.pfm.shower_handle_far", headPos.toString()), false);
            }
            if (distance > 16) {
                context.getItemInHand().setTag(null);
            } else {
                setShowerHeadPosNBT(context.getItemInHand(), pos.subtract(headPos));
            }
            return state.getBlock().canSurvive(state, world, pos) && placeDirection.getAxis().isHorizontal();
        }
        return true;
    }

    private void setShowerHeadPosNBT(ItemStack stack, BlockPos pos)
    {
        CompoundTag nbtCompound = createNbt(stack);
        if(!nbtCompound.contains("BlockEntityTag", Tag.TAG_COMPOUND))
        {
            nbtCompound.put("BlockEntityTag", new CompoundTag());
        }

        CompoundTag blockEntityTag = nbtCompound.getCompound("BlockEntityTag");
        if(!blockEntityTag.contains("showerHead", Tag.TAG_LONG))
        {
            blockEntityTag.put("showerHead", LongTag.valueOf(0));
        }

        LongTag showerHeadPos = (LongTag) blockEntityTag.get("showerHead");
        if(showerHeadPos.getAsLong() != pos.asLong()) {
            blockEntityTag.put("showerHead", LongTag.valueOf(pos.asLong()));
        }
    }

    @Nullable
    public static LongTag getShowerHead(ItemStack stack)
    {
        if (stack.hasTag()) {
            CompoundTag stackNbt = stack.getTag();
            if(stackNbt.contains("BlockEntityTag", Tag.TAG_COMPOUND))
            {
                CompoundTag blockEntityTag = stackNbt.getCompound("BlockEntityTag");
                if(blockEntityTag.contains("showerHead", Tag.TAG_LONG))
                {
                    return (LongTag) blockEntityTag.get("showerHead");
                }
            }
        }
        return null;
    }

    private static CompoundTag createNbt(ItemStack stack)
    {
        if(!stack.hasTag())
        {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        if (stack.hasTag() && getShowerHead(stack) != null) {
            tooltip.add(new TranslatableComponent("tooltip.pfm.shower_handle_connected", 1));
        }
        super.appendHoverText(stack, world, tooltip, context);
    }
}
