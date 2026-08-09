package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import net.blay09.mods.balm.world.level.block.BlockLike;
import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.blay09.mods.balm.world.level.block.internal.DeferredBlockImpl;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(ModBlockEntities.class)
public class PFMCFBModBlockEntitiesMixin {

    @ModifyArg(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/blay09/mods/balm/world/level/block/entity/BalmBlockEntityTypeRegistrar;register(Ljava/lang/String;Lnet/blay09/mods/balm/world/level/block/entity/BalmBlockEntityTypeRegistrar$BlockEntitySupplier;Ljava/lang/Iterable;)Lnet/blay09/mods/balm/world/level/block/entity/BalmBlockEntityTypeRegistration;",
                    ordinal = 0
            ),
            index = 2
    )
    private static Iterable<? extends BlockLike> modifyCookingTables(Iterable<? extends BlockLike> blocks) {
        List<BlockLike> blocksList = new ArrayList<>();
        blocks.forEach(blocksList::add);
        DeferredBlock cookingTable = new DeferredBlockImpl(BuiltInRegistries.BLOCK.wrapAsHolder(PFMCookingForBlockHeadsCompat.COOKING_TABLE_BLOCK));
        blocksList.add(cookingTable);
        return blocksList;
    }
}
