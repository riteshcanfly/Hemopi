/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasvmpredict;

/**
 *
 * @author Ritesh
 */
public class FeatureNode {

    public final int index;
    public final double value;

    public FeatureNode(final int index, final double value) {
        if (index < 1) {
            throw new IllegalArgumentException("index must be >= 1");
        }
        this.index = index;
        this.value = value;
    }

}
