package com.unlikepaladin.pfm.data.materials;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class WoodVariantRegistry extends VariantRegistryBase<WoodVariant> {
    public static final WoodVariant OAK = new WoodVariant(Identifier.parse("oak"), Blocks.OAK_PLANKS, Blocks.OAK_LOG);
    public static final WoodVariantRegistry INSTANCE = new WoodVariantRegistry();
    public static Collection<String> getNamespaces() {
        return INSTANCE.variants.values().stream().map(VariantBase::getNamespace).collect(Collectors.toUnmodifiableList());
    };

    public static Collection<WoodVariant> getVariants() {
        return Collections.unmodifiableCollection(INSTANCE.variants.values());
    }
    @Nullable
    public static WoodVariant getVariant(Identifier name) {
        return INSTANCE.variants.getOrDefault(name, OAK);
    }

    public static Optional<WoodVariant> getOptionalVariant(Identifier name) {
        return INSTANCE.variants.containsKey(name) ? Optional.of(INSTANCE.variants.get(name)) : Optional.empty();
    }

    /**
     * Simplified Wood/Block detection based on MoonlightLib<a href="https://github.com/MehVahdJukaar/Moonlight/blob/multi-loader/common/src/main/java/net/mehvahdjukaar/moonlight/api/set/BlockTypeRegistry.java#L18">...</a>
     */
    public Optional<WoodVariant> getVariantFromBlock(Block baseBlock, Identifier blockId) {
        String name = null;
        String path = blockId.getPath();
        if (blockId.getNamespace().equals("tfc")) {
            if (path.contains("wood/planks/")) {
                Optional<Block> log = BuiltInRegistries.BLOCK.getOptional(
                        Identifier.fromNamespaceAndPath(blockId.getNamespace(), path.replace("planks", "log")));
                if (log.isPresent()) {
                    Identifier id = Identifier.fromNamespaceAndPath(blockId.getNamespace(), path.replace("wood/planks/", ""));
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
                SoundType soundGroup = state.getSoundType();
                NoteBlockInstrument instrument = state.instrument();
                // and have correct material
                if (soundGroup == SoundType.BAMBOO_WOOD || soundGroup == SoundType.CHERRY_WOOD || soundGroup == SoundType.WOOD || soundGroup == SoundType.NETHER_WOOD || instrument == NoteBlockInstrument.BASS) {
                    // we do not allow "/" in the wood name
                    name = name.replace("/", "_");
                    Identifier id = Identifier.fromNamespaceAndPath(blockId.getNamespace(), name);
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
    private static Block findLog(Identifier id) {
        Identifier[] test = {
                Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_log"),
                Identifier.fromNamespaceAndPath(id.getNamespace(), "log_" + id.getPath()),
                Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "log"),
                Identifier.parse(id.getPath() + "_log"),
                Identifier.parse("log_" + id.getPath()),
                Identifier.parse(id.getPath() + "log"),
                Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_stem"),
                Identifier.fromNamespaceAndPath(id.getNamespace(), "stem_" + id.getPath()),
                Identifier.parse(id.getPath() + "_stem"),
                Identifier.parse("stem_" + id.getPath()),
                Identifier.fromNamespaceAndPath(id.getNamespace(), "stalk_" + id.getPath()),
                Identifier.parse(id.getPath() + "_stalk"),
                Identifier.parse("stalk_" + id.getPath())
        };
        Block temp = null;
        for (Identifier r : test) {
            if (BuiltInRegistries.BLOCK.containsKey(r)) {
                temp = BuiltInRegistries.BLOCK.getValue(r);
                break;
            }
        }
        return temp;
    }
}
