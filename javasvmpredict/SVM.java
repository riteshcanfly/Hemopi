/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasvmpredict;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ritesh
 */
public class SVM {

    private Model model;
    /**
     * Regularization parameter
     */
    private double C = 1;
    /**
     * Tolerance
     */
    private double tol = 10e-3;
    /**
     * Tolerance
     */
    private double tol2 = 10e-5;

    

  

    /*some global variables of the SMO algorithm*/
    private double Ei, Ej;
   
    private double L, H;
    private double maxup, minlow;
    private double bup, blow;
    private int iup, ilow;
    private double eta;
    private List<Integer> I0, I1, I2, I3, I4;
    private double[] E;
    /* ---------------------------------------- */

    public SVM() {
    }

    /**
     * Test a whole data set
     *
     * @param test The test data
     * @param filename model filename
     * @param ver version
     * @return An array of -1 and 1's
     * @throws java.io.IOException
     */
    public double[] svmTest(Problem test, String filename,String ver) throws IOException {
        if (test == null) {
            return null;
        }
        int[] pred = new int[test.l];
        double[] pred1 = new double[test.l];
        model  = new Model(test.l);
       // model.params = new KernelParams();
        try {
            model.loadModel(filename,ver);
        } catch (ParseException ex) {
            Logger.getLogger(SVM.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < test.l; i++) {
           // pred[i] = (svmTestOne(test.x[i]) < 0 ? -1 : 1);
            pred1[i] = svmTestOne(test.x[i]);
        }
        return pred1;
    }

    /**
     * Test one example
     *
     * @param x The test example
     * @return Class of x: -1 or 1
     */
    public double svmTestOne(FeatureNode[] x) {
        double f = 0;
       
        for (int i = 0; i < model.l; i++) {
            f += model.alpha[i] * (model.y[i]) * kernel(x, model.x[i]);
        }
        //return  -1*(f + model.b)  ;
        return  (f - model.b)  ;
    }

    /**
     * Based on the kernel parameters/settings of the model, calculates the
     * kernel value between two points
     *
     * @param x First point/vector
     * @param z Second point/vector
     * @return Kernel value between x and z
     */
    private double kernel(FeatureNode[] x, FeatureNode[] z) {
        double ret = 0;
        if (model.params == null) {
            model.params = new KernelParams(1, 1, 1, 1);
        }
        switch (model.params.kernel) {
            case 0: //user defined
                break;
            case 1: //linear
                ret = Kernel.kLinear(x, z);
                break;
            case 2: //polynomial
                ret = Kernel.kPoly(x, z, model.params.a, model.params.b, model.params.c);
                break;
            case 3: //gaussian
                ret = Kernel.kGaussian(x, z, model.params.a,model.b);
                break;
            case 4: //tanh
                ret = Kernel.kTanh(x, z, model.params.a, model.params.b);
                break;
        }
        return ret;
    }

    /**
     * Based on the kernel parameters/settings of the model, calculates the
     * kernel value between two points
     *
     * @param i Index of the first vector in model.x
     * @param j Index of the second vector in model.x
     * @return Kernel value between model.x[i] and model.x[j]
     */
    private double kernel(int i, int j) {
        double ret = 0;
        if (model.params == null) {
            model.params = new KernelParams(1, 1, 1, 1);
        }
        switch (model.params.kernel) {
            case 0: //user defined
                break;
            case 1: //linear
                ret = Kernel.kLinear(model.x[i], model.x[j]);
                break;
            case 2: //polynomial
                ret = Kernel.kPoly(model.x[i], model.x[j], model.params.a, model.params.b, model.params.c);
                break;
            case 3: //gaussian
                ret = Kernel.kGaussian(model.x[i], model.x[j], model.params.a,model.b);
                break;
            case 4: //tanh
                ret = Kernel.kTanh(model.x[i], model.x[j], model.params.a, model.params.b);
                break;
        }
        return ret;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model m) {
        model = m;
    }

    public double getC() {
        return C;
    }

    public void setC(double C) {
        this.C = C;
    }

    public double getTolerance() {
        return tol;
    }

    public void setTolerance(double tol) {
        this.tol = tol;
    }

    public double getTolerance2() {
        return tol2;
    }

    public void setTolerance2(double tol2) {
        this.tol2 = tol2;
    }

   

    

   
}
