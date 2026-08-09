package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.PFMCookingForBlockHeadsCompat;
import net.blay09.mods.balm.world.level.block.BlockLike;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
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
        blocksList.add(new BlockLike() {
            @Override
            public Block asBlock() {
                return PFMCookingForBlockHeadsCompat.getCookingTableBlock();
            }

            @Override
            public Holder<Block> asHolder() {
                return BuiltInRegistries.BLOCK.wrapAsHolder(asBlock());
            }

            @Override
            public BlockState defaultBlockState() {
                return asBlock().defaultBlockState();
            }

            @Override
            public Item asItem() {
                return asBlock().asItem();
            }
        });
        return blocksList;
    }
}