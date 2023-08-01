package com.unlikepaladin.pfm.compat;

public interface PFMClientModCompatibility {
    default void registerScreens() {}
    default void registerBlockColors() {}
    default void registerEntityRenderer() {}
    default void registerBlockEntityRenderer() {}
    PFMModCompatibility getCompatiblity();
}
