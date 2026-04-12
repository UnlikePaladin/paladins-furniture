package com.unlikepaladin.pfm.data.materials;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class StoneVariantRegistry extends VariantRegistryBase<StoneVariant> {
    public static final StoneVariant STONE = new StoneVariant(ResourceLocation.parse("stone"), Blocks.STONE, Blocks.COBBLESTONE);
    public static final StoneVariantRegistry INSTANCE = new StoneVariantRegistry();
    public static Collection<String> getNamespaces() {
        return INSTANCE.variants.values().stream().map(VariantBase::getNamespace).collect(Collectors.toUnmodifiableList());
    };

    public static Collection<StoneVariant> getVariants() {
        return Collections.unmodifiableCollection(INSTANCE.variants.values());
    }
    @Nullable
    public static StoneVariant getVariant(ResourceLocation name) {
        return INSTANCE.variants.getOrDefault(name, STONE);
    }

    public static Optional<StoneVariant> getOptionalVariant(ResourceLocation name) {
        return INSTANCE.variants.containsKey(name) ? Optional.of(INSTANCE.variants.get(name)) : Optional.empty();
    }

    /**
     * Simplified Wood/Block detection based on MoonlightLib<a href="https://github.com/MehVahdJukaar/Moonlight/blob/multi-loader/common/src/main/java/net/mehvahdjukaar/moonlight/api/set/BlockTypeRegistry.java#L18">...</a>
     */
    public Optional<StoneVariant> getVariantFromBlock(Block baseBlock, ResourceLocation blockId) {
        String name = null;
        String path = blockId.getPath();
        if (blockId.getNamespace().equals("tfc")) {
            if (path.contains("rock/polished/")) {
                Optional<Block> cobble = BuiltInRegistries.BLOCK.getOptional(
                        ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), path.replace("polished", "raw")));
                if (cobble.isPresent()) {
                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), path.replace("rock/polished/", ""));
                    return Optional.of(new StoneVariant(id, baseBlock, cobble.get()));
                }
            }
            return Optional.empty();
        }
        if (path.endsWith("_polished")) {
            name = path.substring(0, path.length() - "_polished".length());
        } else if (path.startsWith("polished_")) {
            name = path.substring("polished_".length());
        } else if (path.endsWith("_polish")) {
            name = path.substring(0, path.length() - "_polish".length());
        } else if (path.startsWith("polish_")) {
            name = path.substring("polish_".length());
        }
        String namespace = blockId.getNamespace();
        if (!namespace.equals("cozy_home") && !namespace.equals("adorn") && name != null && !namespace.equals("securitycraft") &&
                !namespace.equals("absentbydesign") && !namespace.equals("chipped") && !namespace.equals("polydecorations") && !namespace.equals("extshape") && !namespace.equals("modernlife") && !(namespace.equals("ars_nouveau") && path.contains("sconce"))
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
                if (soundGroup == SoundType.DEEPSLATE || soundGroup == SoundType.POLISHED_DEEPSLATE || soundGroup == SoundType.STONE || instrument == NoteBlockInstrument.BASEDRUM) {
                    // we do not allow "/" in the wood name
                    name = name.replace("/", "_");
                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), name);
                    Block rawBlock = findRaw(id);
                    if (rawBlock != null) {
                        return Optional.of(new StoneVariant(id, baseBlock, rawBlock));
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public StoneVariant getDefaultType() {
        return STONE;
    }

    @Override
    public Class<StoneVariant> getType() {
        return StoneVariant.class;
    }

    @Nullable
    private static Block findRaw(ResourceLocation id) {
        ResourceLocation[] test = {
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath()),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "raw"),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_raw"),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "raw_" + id.getPath()),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "raw" + id.getPath()),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_cobble"),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "cobble"),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "cobble_" + id.getPath()),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "cobble" + id.getPath()),
                ResourceLocation.parse(id.getPath()),
                ResourceLocation.parse(id.getPath() + "raw"),
                ResourceLocation.parse(id.getPath() + "_raw"),
                ResourceLocation.parse("raw_" + id.getPath()),
                ResourceLocation.parse("raw" + id.getPath()),
                ResourceLocation.parse(id.getPath() + "_cobble"),
                ResourceLocation.parse(id.getPath() + "cobble"),
                ResourceLocation.parse("cobble_" + id.getPath()),
                ResourceLocation.parse("cobble" + id.getPath())
        };
        Block temp = null;
        for (ResourceLocation r : test) {
            if (BuiltInRegistries.BLOCK.containsKey(r)) {
                temp = BuiltInRegistries.BLOCK.get(r);
                break;
            }
        }
        return temp;
    }
}
