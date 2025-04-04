package com.rabbiter.association.utils;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class ClusterableDoubleArray implements Clusterable {
    private final double[] point;

    public ClusterableDoubleArray(double[] point) {
        this.point = point;
    }

    @Override
    public double[] getPoint() {
        return point;
    }
}