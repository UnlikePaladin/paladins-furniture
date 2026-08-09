package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.data.PFMBlockSettings;
import com.unlikepaladin.pfm.data.ToolType;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PFMCookingForBlockHeadsCompat {

    public static void initBlockConnectors() {
        List<Block> connectorBlocks = new ArrayList<>(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicNightstandBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterBlock.class).getAllBlocks());
        connectorBlocks.addAll(Arrays.asList(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, PaladinFurnitureModBlocksItems.XBOX_FRIDGE, PaladinFurnitureModBlocksItems.GRAY_FRIDGE, PaladinFurnitureModBlocksItems.IRON_FRIDGE));
        connectorBlocks.addAll(Arrays.asList(PaladinFurnitureModBlocksItems.WHITE_STOVE, PaladinFurnitureModBlocksItems.GRAY_STOVE, PaladinFurnitureModBlocksItems.IRON_STOVE));
        connectorBlocks.addAll(Arrays.asList(PaladinFurnitureModBlocksItems.GRAY_FREEZER, PaladinFurnitureModBlocksItems.IRON_FREEZER, PaladinFurnitureModBlocksItems.WHITE_FREEZER));
        connectorBlocks.addAll(KitchenStovetopBlock.streamKitchenStovetop().collect(Collectors.toList()));
        connectorBlocks.forEach(KitchenMultiBlock::registerConnectorBlock);
    }

    public static final PFMCookingTableBlock COOKING_TABLE_BLOCK = new PFMCookingTableBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(PaladinFurnitureModBlocksItems.GRAY_STOVE), ToolType.PICKAXE));
    public static <T extends AbstractContainerMenu> TriFunc<Integer, Inventory, FriendlyByteBuf, T> getStoveScreenHandler() {
        return (integer, playerInventory, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            BlockEntity blockEntity = playerInventory.player.level.getBlockEntity(pos);
            return (T) new StoveScreenHandlerBalm(integer, playerInventory, (StoveBlockEntityBalm)blockEntity);
        };
    }

    public static void openMenuScreen(Level level, BlockPos pos, Player player) {
        StoveBlockEntityBalm stove = (StoveBlockEntityBalm)level.getBlockEntity(pos);
        if (!level.isClientSide) {
            NetworkHooks.openGui((ServerPlayer)player, stove, pos);
        }
    }

    public static BlockEntity getStoveBlockEntity() {
        return new StoveBlockEntityBalm();
    }

    public static InteractionResult onUseStove(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() == ModItems.heatingUnit) {
            return InteractionResult.PASS;
        } else if (hit.getDirection() == Direction.UP && CookingRegistry.isToolItem(heldItem)) {
            Direction stateFacing = state.getValue(StoveBlock.FACING);
            double hx =  (hit.getLocation().x - hit.getBlockPos().getX());
            double hz = (hit.getLocation().z - hit.getBlockPos().getZ());
            switch (stateFacing) {
                case NORTH:
                    hx = 1.0 - (hit.getLocation().x - hit.getBlockPos().getX());
                    hz = 1.0 - (hit.getLocation().z - hit.getBlockPos().getZ());
                    break;
                case WEST:
                    hz = 1.0 - (hit.getLocation().x - hit.getBlockPos().getX());
                    hx = (hit.getLocation().z - hit.getBlockPos().getZ());
                    break;
                case EAST:
                    hz = (hit.getLocation().x - hit.getBlockPos().getX());
                    hx = 1.0 - (hit.getLocation().z - hit.getBlockPos().getZ());
            }
            int index = -1;
            if (hx < 0.5f && hz < 0.5f) {
                index = 1;
            } else if (hx >= 0.5f && hz < 0.5f) {
                index = 0;
            } else if (hx < 0.5f && hz >= 0.5f) {
                index = 3;
            } else if (hx >= 0.5f && hz >= 0.5f) {
                index = 2;
            }
            if (index != -1) {
                StoveBlockEntityBalm stove = (StoveBlockEntityBalm)level.getBlockEntity(pos);
                if (stove != null && stove.getToolItem(index).isEmpty()) {
                    ItemStack toolItem = heldItem.split(1);
                    stove.setToolItem(index, toolItem);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            StoveBlockEntityBalm stove = (StoveBlockEntityBalm)level.getBlockEntity(pos);
            if (hit.getDirection() == state.getValue(BlockStateProperties.HORIZONTAL_FACING) && stove != null) {
                if (player.isShiftKeyDown()) {
                    return InteractionResult.SUCCESS;
                }

                if (!heldItem.isEmpty() && stove.getSmeltingResult(heldItem) != ItemStack.EMPTY) {
                    heldItem = ItemHandlerHelper.insertItemStacked(stove.getInputContainer(), heldItem, false);
                    player.setItemInHand(hand, heldItem);

                    return InteractionResult.SUCCESS;
                } else if (!heldItem.isEmpty() && stove.isItemFuel(heldItem)) {
                    heldItem = ItemHandlerHelper.insertItemStacked(stove.getFuelContainer(), heldItem, false);
                    player.setItemInHand(hand, heldItem);
                    return InteractionResult.SUCCESS;
                }
            }
            if (!level.isClientSide) {
                NetworkHooks.openGui((ServerPlayer)player, stove, pos);
            }
            return InteractionResult.SUCCESS;
        }
    }
}
