package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.blocks.PendantBlock;
import com.unlikepaladin.pfm.blocks.PowerableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
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

public class LightSwitchItem extends BlockItem {
    private Block block;

    public LightSwitchItem(Block block, Settings settings) {
        super(block, settings);
        this.block = block;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (world.isClient) {
            return new TypedActionResult<>(ActionResult.FAIL, stack);
        }
        if (player.isSneaking()) {
            stack.setNbt(null);
            return new TypedActionResult<>(ActionResult.SUCCESS, stack);
        }
        return new TypedActionResult<>(ActionResult.PASS, stack);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        super.useOnBlock(context);
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
                    if (context.getWorld().isClient)
                        context.getPlayer().sendMessage(Text.translatable("message.pfm.light_switch_not_canopy"), false);
               }

           }
           else {
               addLight(context.getStack(), pos);
           }
            return ActionResult.SUCCESS;
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        BlockPos pos = context.getBlockPos();
        WorldView world = context.getWorld();
        if (getLights(context.getStack()) != null) {
            NbtList lights = getLights(context.getStack());
            Direction facing = context.getHorizontalPlayerFacing();
            for(int i = 0; i < lights.size(); i++)
            {
                NbtElement nbtElement = lights.get(i);
                BlockPos lightPos = BlockPos.fromLong(((NbtLong) nbtElement).longValue());
                BlockPos placedPos = pos.offset(facing);
                double distance = Math.sqrt(lightPos.getSquaredDistance(placedPos.getX() + 0.5, placedPos.getY() + 0.5, placedPos.getZ() + 0.5));
                return !(distance > 16) && state.getBlock().canPlaceAt(state, world, pos);
            }

        }
        return state.getBlock().canPlaceAt(state, world, pos);
    }

    private void addLight(ItemStack stack, BlockPos pos)
    {
        NbtCompound tagCompound = createTag(stack);
        if(!tagCompound.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
        {
            tagCompound.put("BlockEntityTag", new NbtCompound());
        }

        NbtCompound entityTagCompound = tagCompound.getCompound("BlockEntityTag");
        if(!entityTagCompound.contains("lights", NbtElement.LIST_TYPE))
        {
            entityTagCompound.put("lights", new NbtList());
        }

        NbtList tagList = (NbtList) entityTagCompound.get("lights");
        if(!containsLight(tagList, pos))
        {
            tagList.add(NbtLong.of(pos.asLong()));
        }
    }

    private boolean containsLight(NbtList tagList, BlockPos pos)
    {
        for(int i = 0; i < tagList.size(); i++)
        {
            NbtLong tagLong = (NbtLong) tagList.get(i);
            if(tagLong.longValue() == pos.asLong())
            {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static NbtList getLights(ItemStack stack)
    {
        NbtCompound tagCompound = createTag(stack);
        if(tagCompound.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE))
        {
            NbtCompound entityTagCompound = tagCompound.getCompound("BlockEntityTag");
            if(entityTagCompound.contains("lights", NbtElement.LIST_TYPE))
            {
                return (NbtList) entityTagCompound.get("lights");
            }
        }
        return null;
    }

    private static NbtCompound createTag(ItemStack stack)
    {
        if(!stack.hasNbt())
        {
            stack.setNbt(new NbtCompound());
        }
        return stack.getNbt();
    }

}
