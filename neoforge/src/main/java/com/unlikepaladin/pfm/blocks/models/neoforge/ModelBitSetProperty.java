package com.unlikepaladin.pfm.blocks.models.neoforge;

import java.util.BitSet;
import java.util.function.Predicate;

public class ModelBitSetProperty implements Predicate<ModelBitSetProperty> {
    public ModelBitSetProperty(BitSet connections) {
        this.connections = connections;
    }

    public BitSet connections;
    @Override
    public boolean test(ModelBitSetProperty bedConnections) {
        return connections.equals(bedConnections.connections);
    }
}
