package com.unlikepaladin.pfm.registry;

public interface QuadFunc<A, B, C, D, R> {
    R apply(A a, B b, C c, D d);
}