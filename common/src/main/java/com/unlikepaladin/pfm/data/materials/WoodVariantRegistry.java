package com.unlikepaladin.pfm.data.materials;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class WoodVariantRegistry extends VariantRegistryBase<WoodVariant> {
    public static final WoodVariant OAK = new WoodVariant(new ResourceLocation("oak"), Blocks.OAK_PLANKS, Blocks.OAK_LOG, Boat.Type.OAK);
    public static final WoodVariantRegistry INSTANCE = new WoodVariantRegistry();
    public static Collection<String> getNamespaces() {
        return INSTANCE.variants.values().stream().map(VariantBase::getNamespace).collect(Collectors.toList());
    };

    public static Collection<WoodVariant> getVariants() {
        return Collections.unmodifiableCollection(INSTANCE.variants.values());
    }
    @Nullable
    public static WoodVariant getVariant(ResourceLocation name) {
        return INSTANCE.variants.getOrDefault(name, OAK);
    }

    public static Optional<WoodVariant> getOptionalVariant(ResourceLocation name) {
        return INSTANCE.variants.containsKey(name) ? Optional.of(INSTANCE.variants.get(name)) : Optional.empty();
    }

    @Nullable
    public static WoodVariant getVariantFromVanillaWoodType(Boat.Type type) {
        for (WoodVariant woodVariant : INSTANCE.variants.values()) {
            if (woodVariant.getVanillaWoodType() == type)
                return woodVariant;
        }
        return null;
    }
    /**
     * Simplified Wood/Block detection based on MoonlightLib<a href="https://github.com/MehVahdJukaar/Moonlight/blob/multi-loader/common/src/main/java/net/mehvahdjukaar/moonlight/api/set/BlockTypeRegistry.java#L18">...</a>
     */
    public Optional<WoodVariant> getVariantFromBlock(Block baseBlock, ResourceLocation blockId) {
        String name = null;
        String path = blockId.getPath();
        if (blockId.getNamespace().equals("tfc")) {
            if (path.contains("wood/planks/")) {
                Optional<Block> log = Registry.BLOCK.getOptional(
                        new ResourceLocation(blockId.getNamespace(), path.replace("planks", "log")));
                if (log.isPresent()) {
                    ResourceLocation id = new ResourceLocation(blockId.getNamespace(), path.replace("wood/planks/", ""));
                    return Optional.of(new WoodVariant(id, baseBlock, log.get()));
                }
            }
            return Optional.empty();
        }
        if (path.endsWith("_planks")) {
            name = path.substring(0, path.length() - "_planks".length());
        } else if (path.startsWith("planks_")) {
            name = path.substring("planks_".length());
        } else if (path.endsWith("_plank")) {
            name = path.substring(0, path.length() - "_plank".length());
        } else if (path.startsWith("plank_")) {
            name = path.substring("plank_".length());
        }
        String namespace = blockId.getNamespace();
        if (!namespace.equals("cozy_home") && name != null && !namespace.equals("securitycraft") &&
                !namespace.equals("absentbydesign") && !(namespace.equals("terrestria") && path.contains("sakura")) &&
                !(namespace.equals("betternether") && path.contains("nether_mushroom")) && !namespace.equals("chipped")
                && !(namespace.equals("regions_unexplored") && path.contains("alpha")) && !namespace.equals("modernlife")
                && !namespace.equals("excessive_building") && !namespace.equals("furnies")
                && !path.contains("cabinet") && !path.contains("shelf") && !path.contains("slab") && !path.contains("stairs")
                && !path.contains("fence") && !path.contains("door") && !path.contains("trapdoor") && !path.contains("sign")
                && !path.contains("button")) {

            BlockState state = baseBlock.defaultBlockState();
            // can't check if the block is a full one, so I do this. Adding some checks here
            if (state.getProperties().size() <= 2 && !(baseBlock instanceof SlabBlock) && !name.contains("slab") && !(baseBlock instanceof BasePressurePlateBlock) && !name.contains("pressure_plate")) {
                // needs to use wood sound type
                // if (state.getSoundType() == SoundType.WOOD) { //wood from tcon has diff sounds
                Material mat = state.getMaterial();
                // and have correct material
                if (mat == Material.WOOD || mat == Material.NETHER_WOOD) {
                    // we do not allow "/" in the wood name
                    name = name.replace("/", "_");
                    ResourceLocation id = new ResourceLocation(blockId.getNamespace(), name);
                    Block logBlock = findLog(id);
                    if (logBlock != null) {
                        return Optional.of(new WoodVariant(id, baseBlock, logBlock));
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public WoodVariant getDefaultType() {
        return OAK;
    }

    @Override
    public Class<WoodVariant> getType() {
        return WoodVariant.class;
    }

    @Nullable
    private static Block findLog(ResourceLocation id) {
        ResourceLocation[] test = {
                new ResourceLocation(id.getNamespace(), id.getPath() + "_log"),
                new ResourceLocation(id.getNamespace(), "log_" + id.getPath()),
                new ResourceLocation(id.getNamespace(), id.getPath() + "log"),
                new ResourceLocation(id.getPath() + "_log"),
                new ResourceLocation("log_" + id.getPath()),
                new ResourceLocation(id.getPath() + "log"),
                new ResourceLocation(id.getNamespace(), id.getPath() + "_stem"),
                new ResourceLocation(id.getNamespace(), "stem_" + id.getPath()),
                new ResourceLocation(id.getPath() + "_stem"),
                new ResourceLocation("stem_" + id.getPath()),
                new ResourceLocation(id.getNamespace(), "stalk_" + id.getPath()),
                new ResourceLocation(id.getPath() + "_stalk"),
                new ResourceLocation("stalk_" + id.getPath())
        };
        Block temp = null;
        for (ResourceLocation r : test) {
            if (Registry.BLOCK.getOrEmpty(r).isPresent()) {
                temp = Registry.BLOCK.get(r);
                break;
            }
        }
        return temp;
    }
}
