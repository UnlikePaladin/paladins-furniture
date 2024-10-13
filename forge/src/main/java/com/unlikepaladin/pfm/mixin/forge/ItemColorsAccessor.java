package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.forge.BlockColorsExtension;
import com.unlikepaladin.pfm.client.forge.ItemColorsExtension;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IRegistryDelegate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(ItemColors.class)
public class ItemColorsAccessor implements ItemColorsExtension {

    @Shadow
    private final Map<IRegistryDelegate<Item>, ItemColorProvider> f_92674_ = new HashMap<>();

    @Override
    public Map<IRegistryDelegate<Item>, ItemColorProvider> getColorMap() {
        return f_92674_;
    }
}
