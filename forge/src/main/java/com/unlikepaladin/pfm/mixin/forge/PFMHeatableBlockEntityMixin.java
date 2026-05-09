package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

@Pseudo
@Mixin(HeatableBlockEntity.class)
public class PFMHeatableBlockEntityMixin {
    @Inject(method = "isHeated", at = @At("HEAD"), cancellable = true)
    private void pfm$stovesHeatable(Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Block checkBlock = world.getBlockState(pos.below()).getBlock();
        if (checkBlock instanceof StoveBlock || checkBlock instanceof KitchenStovetopBlock)
            cir.setReturnValue(true);
    }
}
