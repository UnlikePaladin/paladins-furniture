package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.StovePacket;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.core.BlockPos;

public class StoveScreenHandler extends AbstractFurnaceMenu {
    private final Container inventory;
    public StoveScreenHandler(int syncId, Inventory playerInventory, StoveData data) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipePropertySet.SMOKER_INPUT, RecipeBookType.SMOKER, syncId, playerInventory,
                (Container) playerInventory.player.level().getBlockEntity(data.pos()), new SimpleContainerData(4));
        this.inventory = (Container) playerInventory.player.level().getBlockEntity(data.pos());
        inventory.startOpen(playerInventory.player);
    }

    public StoveScreenHandler(int containerId, Inventory playerInventory, Container inventory, ContainerData dataAccess) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipePropertySet.SMOKER_INPUT, RecipeBookType.SMOKER, containerId, playerInventory, inventory, dataAccess);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, StoveData> PACKET_CODEC = StreamCodec.ofMember(StoveData::write, StoveData::new);
    public record StoveData(BlockPos pos) implements StovePacket {
        public StoveData(RegistryFriendlyByteBuf buf) {
            this(buf.readBlockPos());
        }
        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
        }
    }
}