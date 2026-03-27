package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.forge.StoveBlockImpl;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

import java.util.ArrayList;
import java.util.List;

public class PFMCookingForBlockHeadsCompat {

    public static void initBlockConnectors() {
        List<Block> connectorBlocks = new ArrayList<>(PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(ClassicNightstandBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).getAllBlocks());
        connectorBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterBlock.class).getAllBlocks());
        connectorBlocks.addAll(List.of(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, PaladinFurnitureModBlocksItems.XBOX_FRIDGE, PaladinFurnitureModBlocksItems.GRAY_FRIDGE, PaladinFurnitureModBlocksItems.IRON_FRIDGE));
        connectorBlocks.addAll(List.of(PaladinFurnitureModBlocksItems.WHITE_STOVE, PaladinFurnitureModBlocksItems.GRAY_STOVE, PaladinFurnitureModBlocksItems.IRON_STOVE));
        connectorBlocks.addAll(List.of(PaladinFurnitureModBlocksItems.GRAY_FREEZER, PaladinFurnitureModBlocksItems.IRON_FREEZER, PaladinFurnitureModBlocksItems.WHITE_FREEZER));
        connectorBlocks.forEach(KitchenMultiBlock::registerConnectorBlock);
    }

    public static final PFMCookingTableBlock COOKING_TABLE_BLOCK = new PFMCookingTableBlock(BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE));//PaladinFurnitureModBlocksItems.GRAY_STOVE));
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
            Balm.getNetworking().openGui(player, stove);
        }
    }

    public static BlockEntity getStoveBlockEntity(BlockPos pos, BlockState state) {
        return new StoveBlockEntityBalm(pos, state);
    }

    public static <T extends BlockEntity> BlockEntityTicker<T> getStoveTicker(Level level, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return StoveBlockImpl.createTickerHelper(type, BlockEntities.STOVE_BLOCK_ENTITY, StoveBlockEntityBalm::clientTick);
        } else {
            return StoveBlockImpl.createTickerHelper(type, BlockEntities.STOVE_BLOCK_ENTITY, StoveBlockEntityBalm::serverTick);
        }
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
                StoveBlockEntityBalm tileOven = (StoveBlockEntityBalm)level.getBlockEntity(pos);
                if (tileOven != null && tileOven.getToolItem(index).isEmpty()) {
                    ItemStack toolItem = heldItem.split(1);
                    tileOven.setToolItem(index, toolItem);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            StoveBlockEntityBalm oven = (StoveBlockEntityBalm)level.getBlockEntity(pos);
            if (hit.getDirection() == state.getValue(BlockStateProperties.HORIZONTAL_FACING) && oven != null) {
                if (player.isShiftKeyDown()) {
                    return InteractionResult.SUCCESS;
                }

                if (!heldItem.isEmpty() && oven.getSmeltingResult(heldItem) != ItemStack.EMPTY) {
                    heldItem = ContainerUtils.insertItemStacked(oven.getInputContainer(), heldItem, false);
                    player.setItemInHand(hand, heldItem);

                    return InteractionResult.SUCCESS;
                } else if (!heldItem.isEmpty() && StoveBlockEntityBalm.isItemFuel(heldItem)) {
                    heldItem = ContainerUtils.insertItemStacked(oven.getFuelContainer(), heldItem, false);
                    player.setItemInHand(hand, heldItem);
                    return InteractionResult.SUCCESS;
                }
            }
            if (!level.isClientSide) {
                Balm.getNetworking().openGui(player, oven);
            }
            return InteractionResult.SUCCESS;
        }
    }
}
