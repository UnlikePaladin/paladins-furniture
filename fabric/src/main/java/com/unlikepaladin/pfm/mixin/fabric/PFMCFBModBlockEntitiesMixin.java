package com.unlikepaladin.pfm.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(ModBlockEntities.class)
public class PFMCFBModBlockEntitiesMixin {
    @ModifyReturnValue(method = "lambda$static$0", at = @At(value = "RETURN"))
    private static Block[] modifyCookingTables(Block[] blocks) {
        List<Block> blocksList = new ArrayList<>(Arrays.stream(blocks).toList());
        blocksList.add(PFMCookingForBlockHeadsCompat.COOKING_TABLE_BLOCK);
        return blocksList.toArray(new Block[0]);
    }
}
