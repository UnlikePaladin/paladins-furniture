package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.blocks.blockentities.StoveData;
import com.unlikepaladin.pfm.blocks.blockentities.StovePacket;
import com.unlikepaladin.pfm.blocks.neoforge.StoveBlockImpl;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.CombinedContainer;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.entity.FridgeBlockEntity;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class PFMCookingForBlockHeadsCompat {
    public static final PFMCookingTableBlock COOKING_TABLE_BLOCK = new PFMCookingTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE));//PaladinFurnitureModBlocksItems.GRAY_STOVE));
    public static TriFunc<Integer, Inventory, StoveData, StoveScreenHandlerBalm> getStoveScreenHandler() {
        return (integer, playerInventory, data) -> {
            BlockPos pos = data.pos();
            BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(pos);
            return new StoveScreenHandlerBalm(integer, playerInventory, (StoveBlockEntityBalm)blockEntity);
        };
    }

    public static <D extends StovePacket> StreamCodec<RegistryFriendlyByteBuf, D> getStovePacket() {
        return (StreamCodec<RegistryFriendlyByteBuf, D>) StoveData.PACKET_CODEC;
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

    public static ItemInteractionResult onUseStove(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() == ModItems.heatingUnit) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else if (hit.getDirection() == Direction.UP && heldItem.is(ModItemTags.UTENSILS)) {
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
            return ItemInteractionResult.SUCCESS;
        } else {
            StoveBlockEntityBalm oven = (StoveBlockEntityBalm)level.getBlockEntity(pos);
            if (hit.getDirection() == state.getValue(BlockStateProperties.HORIZONTAL_FACING) && oven != null) {
                if (player.isShiftKeyDown()) {
                    return ItemInteractionResult.SUCCESS;
                }

                if (!heldItem.isEmpty() && oven.getSmeltingResult(heldItem, player.level().registryAccess()) != ItemStack.EMPTY) {
                    heldItem = ContainerUtils.insertItemStacked(oven.getInputContainer(), heldItem, false);
                    player.setItemInHand(hand, heldItem);

                    return ItemInteractionResult.SUCCESS;
                } else if (!heldItem.isEmpty() && oven.isItemFuel(heldItem)) {
                    heldItem = ContainerUtils.insertItemStacked(oven.getFuelContainer(), heldItem, false);
                    player.setItemInHand(hand, heldItem);
                    return ItemInteractionResult.SUCCESS;
                }
            }
            if (!level.isClientSide) {
                Balm.getNetworking().openGui(player, oven);
            }
            return ItemInteractionResult.SUCCESS;
        }
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProvider.class), BlockEntities.FREEZER_BLOCK_ENTITY, (entity, side) -> {
            return new ContainerKitchenItemProvider(entity){
                private final ItemStack snowStack;
                private final ItemStack iceStack;
                {
                    this.snowStack = new ItemStack(Items.SNOWBALL);
                    this.iceStack = new ItemStack(Blocks.ICE);
                }

                @Override
                public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
                    IngredientToken result = applyIceUnit(ingredient::test);
                    if (result != null)
                        return result;

                    return super.findIngredient(ingredient, ingredientTokens, cacheHint);
                }

                @Override
                public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
                    IngredientToken result = applyIceUnit(stack -> ItemStack.isSameItem(stack, itemStack));
                    if (result != null)
                        return result;

                    return super.findIngredient(itemStack, ingredientTokens, cacheHint);
                }

                private @Nullable IngredientToken applyIceUnit(Function<ItemStack, Boolean> predicate) {
                    if (predicate.apply(this.snowStack))
                        return new FridgeBlockEntity.IceUnitIngredientToken(ContainerUtils.copyStackWithSize(this.snowStack, 64));
                    else
                        return predicate.apply(this.iceStack) ? new FridgeBlockEntity.IceUnitIngredientToken(ContainerUtils.copyStackWithSize(this.iceStack, 64)) : null;
                }
            };
        });

        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProcessor.class), BlockEntities.STOVE_BLOCK_ENTITY, (entity, side) -> (StoveBlockEntityBalm) entity);
        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProcessor.class), BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, (entity, side) -> (OvenBlockEntityBalm) entity);
        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProcessor.class), BlockEntities.STOVE_TOP_BLOCK_ENTITY, (entity, side) -> (StovetopBlockEntityBalm) entity);

        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProvider.class), BlockEntities.DRAWER_BLOCK_ENTITY, (entity, side) -> ((GenericStorageBlockEntityBalm9x3)entity).itemProvider);
        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProvider.class), BlockEntities.FRIDGE_BLOCK_ENTITY, (entity, side) -> ((FridgeBlockEntityBalm)entity).itemProvider);
        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProvider.class), BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, (entity, side) -> ((OvenBlockEntityBalm)entity).itemProvider);
        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProvider.class), BlockEntities.STOVE_BLOCK_ENTITY, (entity, side) -> ((StoveBlockEntityBalm)entity).itemProvider);
        event.registerBlockEntity(((NeoForgeBalmProviders)Balm.getProviders()).getBlockCapability(KitchenItemProvider.class), BlockEntities.KITCHEN_DRAWER_SMALL_BLOCK_ENTITY, (entity, side) -> ((GenericStorageBlockEntityBalm3x3)entity).itemProvider);
    }
}
