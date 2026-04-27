package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.blocks.PendantBlock;
import com.unlikepaladin.pfm.blocks.PowerableBlock;
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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LightSwitchItem extends BlockItem {
    private Block block;

    public LightSwitchItem(Block block, Properties settings) {
        super(block, settings);
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
                    if (context.getLevel().isClientSide)
                        context.getPlayer().displayClientMessage(new TranslatableComponent("message.pfm.light_switch_not_canopy"), false);
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
        ListTag lights = getLights(context.getItemInHand());
        boolean canPlace = state.getBlock().canSurvive(state, world, pos) && side.getAxis().isHorizontal();

        if (!canPlace) {
            return false;
        }
        if (lights != null) {
            ArrayList<BlockPos> removedLights = new ArrayList<>();
            ArrayList<BlockPos> lightOffsets = new ArrayList<>();
            for (Iterator<Tag> iterator = lights.iterator(); iterator.hasNext();) {
                Tag nbtElement = iterator.next();
                BlockPos lightPos = BlockPos.of(((LongTag) nbtElement).getAsLong());
                double distance = Math.sqrt(lightPos.distSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, true));
                if (distance > 16) {
                    removedLights.add(BlockPos.of(((LongTag) nbtElement).getAsLong()));
                    iterator.remove();
                } else {
                    lightOffsets.add(pos.subtract(lightPos));
                }
            }
            context.getItemInHand().setTag(new CompoundTag());
            for (BlockPos blockPos : lightOffsets) {
                addLight(context.getItemInHand(), blockPos);
            }

            if (!removedLights.isEmpty() && context.getLevel().isClientSide){
                context.getPlayer().displayClientMessage(new TranslatableComponent("message.pfm.light_switch_far", removedLights.toString()), false);
            }
        }
        return true;
    }

    private void addLight(ItemStack stack, BlockPos pos)
    {
        CompoundTag nbtCompound = createTag(stack);
        if(!nbtCompound.contains("BlockEntityTag", 10))
        {
            nbtCompound.put("BlockEntityTag", new CompoundTag());
        }

        CompoundTag blockEntityTag = nbtCompound.getCompound("BlockEntityTag");
        if(!blockEntityTag.contains("lights", 9))
        {
            blockEntityTag.put("lights", new ListTag());
        }

        ListTag tagList = (ListTag) blockEntityTag.get("lights");
        if(!containsLight(tagList, pos))
        {
            tagList.add(LongTag.valueOf(pos.asLong()));
        }
    }

    private boolean containsLight(ListTag tagList, BlockPos pos)
    {
        for(int i = 0; i < tagList.size(); i++)
        {
            LongTag tagLong = (LongTag) tagList.get(i);
            if(tagLong.getAsLong() == pos.asLong())
            {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static ListTag getLights(ItemStack stack)
    {
        if(stack.hasTag()) {
            CompoundTag nbtCompound = stack.getTag();
            if(nbtCompound.contains("BlockEntityTag", 10))
            {
                CompoundTag blockEntityTag = nbtCompound.getCompound("BlockEntityTag");
                if(blockEntityTag.contains("lights", 9))
                {
                    return (ListTag) blockEntityTag.get("lights");
                }
            }
        }
        return null;
    }

    private static CompoundTag createTag(ItemStack stack)
    {
        if(!stack.hasTag())
        {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        if (stack.hasTag() && getLights(stack) != null) {
            int lightNum = getLights(stack).size();
            tooltip.add(new TranslatableComponent("tooltip.pfm.light_switch_connected", lightNum));
        }
        super.appendHoverText(stack, world, tooltip, context);
    }
}
