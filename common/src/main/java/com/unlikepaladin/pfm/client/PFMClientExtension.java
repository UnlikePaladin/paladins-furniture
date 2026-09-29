package com.unlikepaladin.pfm.client;

public interface PFMClientExtension {

    void invoke$runTasks();

    void invoke$renderFrame(boolean advanceGameTime);
}

