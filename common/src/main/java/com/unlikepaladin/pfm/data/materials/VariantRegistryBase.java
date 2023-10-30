package com.unlikepaladin.pfm.data.materials;

import com.google.common.collect.ImmutableMap;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;


public abstract class VariantRegistryBase<T extends VariantBase<T>> {
    private final List<VariantBase.SetFinder<T>> finders = new ArrayList<>();
    protected final List<T> builder = new ArrayList<>();
    protected Map<Identifier, T> variants = new LinkedHashMap<>();
    private final Object2ObjectOpenHashMap<Object, T> childrenToType = new Object2ObjectOpenHashMap<>();

    public void addFinder(VariantBase.SetFinder<T> finder) {
        finders.add(finder);
    }

    protected void finalizeAndFreeze() {
        LinkedHashMap<Identifier, T> linkedHashMap = new LinkedHashMap<>();
        List<String> modOrder = new ArrayList<>();
        modOrder.add("minecraft");
        builder.forEach(e -> {
            String modId = e.getNamespace();
            if (!modOrder.contains(modId)) modOrder.add(modId);
        });
        for (String modId : modOrder) {
            builder.forEach(entry -> {
                if (Objects.equals(entry.getNamespace(), modId)) {
                    if (!linkedHashMap.containsKey(entry.getIdentifier())) {
                        linkedHashMap.put(entry.getIdentifier(), entry);
                    }else PaladinFurnitureMod.GENERAL_LOGGER.warn("Found block type with duplicate id ({}), skipping",entry.identifier);
                }
            });
        }
        this.variants = ImmutableMap.copyOf(linkedHashMap);
        builder.clear();
    }

    public void registerBlockType(T newType) {
        builder.add(newType);
    }

    public abstract Optional<T> getVariantFromBlock(Block baseBlock, Identifier blockId);

    public void buildAll() {
        //adds default
        this.registerBlockType(this.getDefaultType());
        //adds finders
        finders.stream().map(VariantBase.SetFinder::get).forEach(f -> f.ifPresent(this::registerBlockType));
        for (Block block : Registry.BLOCK) {
            this.getVariantFromBlock(block, Registry.BLOCK.getId(block)).ifPresent(this::registerBlockType);
        }
        this.finalizeAndFreeze();
    }

    public void onBlockInit(){
        this.variants.values().forEach(VariantBase::initializeChildrenBlocks);
        this.variants.values().forEach(VariantBase::initializeChildrenItems);
    }

    public abstract T getDefaultType();

    public abstract Class<T> getType();

    protected void mapBlockToType(Object itemLike, VariantBase<T> type) {
        this.childrenToType.put(itemLike, type.getVariantType());
    }

    public Object2ObjectOpenHashMap<Object, T> getChildrenToType() {
        return childrenToType;
    }
}
