package com.unlikepaladin.pfm.data.materials;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.mixin.PFMFeatureFlagFactory;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class WoodVariant extends VariantBase<WoodVariant> {
    private final Block plankBlock;
    private final Block logBlock;
    @Nullable
    private final Boat.Type vanillaWoodType;

    WoodVariant(ResourceLocation identifier, Block plankBlock, Block logBlock) {
        super(identifier);
        this.plankBlock = plankBlock;
        this.logBlock = logBlock;
        this.vanillaWoodType = Boat.Type.byName(identifier.getPath()) != Boat.Type.OAK &&  Boat.Type.byName(identifier.getPath()).getPlanks() == plankBlock ? Boat.Type.byName(identifier.getPath()) : null;
    }

    WoodVariant(ResourceLocation identifier, Block plankBlock, Block logBlock, Boat.@Nullable Type vanillaWoodType) {
        super(identifier);
        this.plankBlock = plankBlock;
        this.logBlock = logBlock;
        this.vanillaWoodType = vanillaWoodType;
    }

    public Boat.@Nullable Type getVanillaWoodType() {
        return vanillaWoodType;
    }

    @Override
    public String getSerializedName() {
        String postfix = this.isVanilla() ? "" : "_"+this.getNamespace();
        return this.identifier.getPath()+postfix;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ResourceLocation getTextureLocation(BlockType type) {
        if (type == BlockType.STRIPPED_LOG) {
            return ModelHelper.getTextureId((Block) this.getChild("stripped_log"));
        } else if (type == BlockType.LOG || type == BlockType.SECONDARY) {
            return ModelHelper.getTextureId(this.logBlock);
        } else if (type == BlockType.LOG_TOP) {
            return ModelHelper.getTextureId(this.logBlock, "_top");
        } else if (type == BlockType.STRIPPED_LOG_TOP) {
            return ModelHelper.getTextureId((Block) this.getChild("stripped_log"), "_top");
        }
        return ModelHelper.getTextureId(plankBlock);
    }

    @Override
    public String getPath() {
        return this.identifier.getPath();
    }

    @Nullable
    protected Block findLogRelatedBlock(String append, String postpend) {
        String post = postpend.isEmpty() ? "" : "_" + postpend;
        ResourceLocation id = this.getIdentifier();
        String logN = BuiltInRegistries.BLOCK.getKey(this.logBlock).getPath();
        ResourceLocation[] targets = {
                new ResourceLocation(id.getNamespace(), logN + "_" + append + post),
                new ResourceLocation(id.getNamespace(), logN + "_" + append + post.replace("_", "")),
                new ResourceLocation(id.getNamespace(), append + "_" + logN + post),
                new ResourceLocation(id.getNamespace(), append + "_" + logN + post.replace("_", "")),
                new ResourceLocation(id.getNamespace(), id.getPath() + "_" + append + post),
                new ResourceLocation(id.getNamespace(), id.getPath() + "_" + append + post.replace("_", "")),
                new ResourceLocation(id.getNamespace(), append + "_" + id.getPath() + post),
                new ResourceLocation(id.getNamespace(), append + "_" + id.getPath() + post.replace("_", ""))
        };
        String postNether = "";
        switch (postpend) {
            case "log" -> postNether = "stem";
            case "wood" -> postNether = "hyphae";
        }
        postNether = postpend.isEmpty() ? "" : "_" + postNether;
        Block found = null;
        if (!postNether.isEmpty()) {
            ResourceLocation[] nether_targets = {
                    new ResourceLocation(id.getNamespace(), logN + "_" + append + postNether),
                    new ResourceLocation(id.getNamespace(), logN + "_" + append + postNether.replace("_", "")),
                    new ResourceLocation(id.getNamespace(), append + "_" + logN + postNether),
                    new ResourceLocation(id.getNamespace(), append + "_" + logN + postNether.replace("_", "")),
                    new ResourceLocation(id.getNamespace(), id.getPath() + "_" + append + postNether),
                    new ResourceLocation(id.getNamespace(), id.getPath() + "_" + append + postNether.replace("_", "")),
                    new ResourceLocation(id.getNamespace(), append + "_" + id.getPath() + postNether),
                    new ResourceLocation(id.getNamespace(), append + "_" + id.getPath() + postNether.replace("_", ""))
            };
            for (ResourceLocation r : nether_targets) {
                if (BuiltInRegistries.BLOCK.containsKey(r)) {
                    found = BuiltInRegistries.BLOCK.get(r);
                    break;
                }
            }
        }
        for (ResourceLocation r : targets) {
            if (BuiltInRegistries.BLOCK.containsKey(r)) {
                found = BuiltInRegistries.BLOCK.get(r);
                break;
            }
        }
        return found;
    }

    @Override
    public Block getBaseBlock() {
        return this.plankBlock;
    }

    @Override
    public Block getSecondaryBlock() {
        return logBlock;
    }

    public Block getLogBlock() {
        return logBlock;
    }

    public String toString() {
            return this.identifier.toString();
        }

    public boolean isNetherWood() {
        return this.identifier.getPath().contains("warped") || this.identifier.getPath().contains("crimson");
    }

    @Override
    public boolean isVanilla() {
        return identifier.getNamespace().equals("") || identifier.getNamespace().equals("minecraft");
    }

    @Override
    public WoodVariant getVariantType() {
        return WoodVariant.this;
    }

    @Override
    public List<FeatureFlag> getFeatureList() {
        FeatureFlag flag = PFMFeatureFlagFactory.newFlag(getBaseBlock().requiredFeatures().universe, 0);
        flag.mask = getBaseBlock().requiredFeatures().mask;
        return List.of(flag);
    }

    @Override
    public void initializeChildrenBlocks() {
        this.addChild("planks", this.plankBlock);
        this.addChild("log", this.logBlock);
        this.addChild("leaves", this.findRelatedEntry("leaves", BuiltInRegistries.BLOCK));
        this.addChild("stripped_log", this.findLogRelatedBlock("stripped", "log"));
        this.addChild("stripped_wood", this.findLogRelatedBlock("stripped", "wood"));
        this.addChild("wood", this.findRelatedEntry("wood", BuiltInRegistries.BLOCK));
        this.addChild("slab", this.findRelatedEntry("slab", BuiltInRegistries.BLOCK));
        this.addChild("stairs", this.findRelatedEntry("stairs", BuiltInRegistries.BLOCK));
        this.addChild("fence", this.findRelatedEntry("fence", BuiltInRegistries.BLOCK));
        this.addChild("fence_gate", this.findRelatedEntry("fence_gate", BuiltInRegistries.BLOCK));
        this.addChild("door", this.findRelatedEntry("door", BuiltInRegistries.BLOCK));
        this.addChild("trapdoor", this.findRelatedEntry("trapdoor", BuiltInRegistries.BLOCK));
        this.addChild("button", this.findRelatedEntry("button", BuiltInRegistries.BLOCK));
        this.addChild("pressure_plate", this.findRelatedEntry("pressure_plate", BuiltInRegistries.BLOCK));
    }

    @Override
    public void initializeChildrenItems() {
        this.addChild("boat", this.findRelatedEntry("boat", BuiltInRegistries.ITEM));
        this.addChild("sign", this.findRelatedEntry("sign", BuiltInRegistries.ITEM));
    }

    public boolean hasStripped() {
        Object child = this.getChild("stripped_log");
        return child != null && child != this.getBaseBlock();
    }

    public @Nullable ItemLike getItemForRecipe(String key, Class<? extends Block> blockClass, boolean stripped) {
        if (stripped) {
            if (key.equals("base")) {
                return (ItemLike) getChild("stripped_log");
            } else if (key.equals("secondary"))
                return getBaseBlock();
        }
        return super.getItemForRecipe(key, blockClass);
    }

    @Override
    public Block mainChild() {
        return this.plankBlock;
    }

    public static class Finder implements VariantBase.SetFinder<WoodVariant> {

        private final Map<String, ResourceLocation> childNames = new HashMap<>();
        private final Supplier<Block> planksFinder;
        private final Supplier<Block> logFinder;
        private final ResourceLocation id;

        public Finder(ResourceLocation id, Supplier<Block> planks, Supplier<Block> log) {
            this.id = id;
            this.planksFinder = planks;
            this.logFinder = log;
        }

        public static Finder simple(String modId, String woodTypeName, String planksName, String logName) {
            return simple(new ResourceLocation(modId, woodTypeName), new ResourceLocation(modId, planksName), new ResourceLocation(modId, logName));
        }

        public static Finder simple(ResourceLocation woodTypeName, ResourceLocation planksName, ResourceLocation logName) {
            return new Finder(woodTypeName,
                    () -> BuiltInRegistries.BLOCK.get(planksName),
                    () -> BuiltInRegistries.BLOCK.get(logName));
        }

        public void addChild(String childType, String childName) {
            addChild(childType, new ResourceLocation(id.getNamespace(), childName));
        }

        public void addChild(String childType, ResourceLocation childName) {
            this.childNames.put(childType, childName);
        }

        public Optional<WoodVariant> get() {
            if (BlockItemRegistry.isModLoaded(id.getNamespace())) {
                try {
                    Block plank = planksFinder.get();
                    Block log = logFinder.get();
                    Block d = BuiltInRegistries.BLOCK.get(new ResourceLocation("minecraft","air"));
                    if (plank != d && log != d && plank != null && log != null) {
                        WoodVariant w = new WoodVariant(id, plank, log);
                        for (Map.Entry<String, ResourceLocation> entry : childNames.entrySet()){
                            Object child = BuiltInRegistries.BLOCK.getOptional(entry.getValue()).isPresent() ? BuiltInRegistries.BLOCK.get(entry.getValue()) : BuiltInRegistries.ITEM.get(entry.getValue());
                            w.addChild(entry.getKey(), child);
                        }
                        return Optional.of(w);
                    }
                } catch (Exception ignored) {
                }
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Failed to find custom wood type {}", id);
            }
            return Optional.empty();
        }
    }
}