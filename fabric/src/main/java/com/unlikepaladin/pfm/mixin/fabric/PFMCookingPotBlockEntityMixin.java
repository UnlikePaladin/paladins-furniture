package com.unlikepaladin.pfm.mixin.fabric;

import com.nhoryzon.mc.farmersdelight.entity.block.CookingPotBlockEntity;
import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(CookingPotBlockEntity.class)
public abstract class PFMCookingPotBlockEntityMixin extends BlockEntity {
    public PFMCookingPotBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "isAboveLitHeatSource", at = @At("HEAD"), cancellable = true, remap = false)
    private void pfm$isStove(CallbackInfoReturnable<Boolean> cir){
        Block checkBlock = this.world.getBlockState(this.pos.down()).getBlock();
        if (checkBlock instanceof StoveBlock || checkBlock instanceof KitchenStovetopBlock)
            cir.setReturnValue(true);
    }

}
