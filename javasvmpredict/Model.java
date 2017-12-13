/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasvmpredict;

import java.io.*;

import java.text.ParseException;
//import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Ritesh
 */
public class Model extends Problem {

    /**
     * Array of alpha
     */
    public double[] alpha;
    /**
     * Bias
     */
    public double b;
    /**
     * Kernel parameters
     */
    KernelParams params;

    public Model(int l) {
        alpha = new double[l];

        b = 0;
        params = new KernelParams();
    }

    public void saveModel(String filename) {

    }

    public void loadModel(String filename,String ver) throws FileNotFoundException, IOException, ParseException {
        // read a model file in svmlight or libsvm format

        int linecnt = 0;
        boolean flag ;
        BufferedReader bi;
        bi = new BufferedReader(new FileReader(filename));
        String line,v;
        linecnt++;
        line = bi.readLine();
        v = "5.3";
        if (ver.startsWith("LIBsvm")){
             if (line.startsWith("SVM-light")){
                 System.out.println("user gave wrong version");
             }
        }else{
            v = ver.substring(ver.indexOf(' ') +1);
        }
      
        
//SvmLight 5.0
//SvmLight 6.1
//SvmLight 6.2
//LIBsvm')
        if (line.startsWith("SVM-light")) {
            //check which version it is
            String temp = line.substring(line.indexOf("Version"));
            String version = temp.substring(temp.indexOf(' ') + 2); // version has the version number
            if (ver.startsWith("Automatic")){
                flag  = true;
            }else{
                double vr  = Double.parseDouble(version);
                double vru = Double.parseDouble(v);
                flag  = ((vr == vru));
            }
            if (flag == false){
                System.out.println("user gave wrong version");
            }
            //System.out.println("version " + version);
            // Version 5 does not have lines in feature ending with # otherwise everything is same
            //This has been done only to modularise and future purpose
            loadSVMlightModel(bi, filename, linecnt,version);

        } else if (line.startsWith("svm_type")) {
            // call Libsvm
            String arg = line.substring(line.indexOf(' ') + 1);
            if (arg.equals("c_svc")) {
                loadLibSVMModel(bi, filename, linecnt);
                return;
            } else {
                //System.err.print("unknown svm type.\n");
                throw new ParseException("unknown SVM type. \n",0);

            }
        } else {
        }

    }

    public void loadSVMlightModel(BufferedReader bi, String filename, int linecnt,String version) throws ParseException {
        String line;
        try {
            line = bi.readLine();
            /*For uniformity we have 
             0: user defined
             1: linear
             2: polynomial
             3: guassian
             4: sigmoid
             but in SVMLight we have 
             0: linear (default)
             1: polynomial (s a*b+c)^d
             2: radial basis function exp(-gamma ||a-b||^2)
             3: sigmoid tanh(s a*b + c)
             4: user defined kernel from kernel.h
             so we have put it as 
             (kerneltype + 1 ) % 5
            
             */
            params.kernel = (Integer.parseInt((line.substring(0, line.indexOf('#'))).trim()) + 1) % 5;

            linecnt++;
            line = bi.readLine();
            params.c = Double.parseDouble((line.substring(0, line.indexOf('#'))).trim());

            linecnt++;
            line = bi.readLine();
            params.a = Double.parseDouble((line.substring(0, line.indexOf('#'))).trim());
            linecnt++;
            line = bi.readLine();
            double sParam;
            sParam = Double.parseDouble((line.substring(0, line.indexOf('#'))).trim());
            linecnt++;
            if (params.kernel == 2 || params.kernel == 4) { // to check if it is sigmoidal or polynomial
                params.a = sParam;
            }
            line = bi.readLine();
            params.b = Double.parseDouble((line.substring(0, line.indexOf('#'))).trim());
            linecnt++;
            line = bi.readLine();
            String uParam;
            uParam = (line.substring(0, line.indexOf('#'))).trim();
            linecnt++;
            line = bi.readLine();

            n = Integer.parseInt((line.substring(0, line.indexOf('#'))).trim());

            linecnt++;
            line = bi.readLine();
            l = Integer.parseInt((line.substring(0, line.indexOf('#'))).trim());

            linecnt++;
            line = bi.readLine();
            int numSupVecs;
            numSupVecs = Integer.parseInt((line.substring(0, line.indexOf('#'))).trim());
            alpha = new double[numSupVecs - 1];
            linecnt++;
            line = bi.readLine();
            b = Double.parseDouble((line.substring(0, line.indexOf('#'))).trim());

            // now read the alphas*y and feature vectors
            ArrayList<Integer> classes = new ArrayList<>();
            ArrayList<FeatureNode[]> examples = new ArrayList<>();
            Integer k = 0;
            y = new int[numSupVecs-1];
            while ((line = bi.readLine()) != null) {
                String[] elems = line.split(" ");
                //Category:

                double cat = Double.parseDouble(elems[0]);
                alpha[k] = Math.abs(cat);

                int yc;
                yc = ((cat > 0) ? 1 : -1);
                y[k] = yc ;
                k = k + 1;
                catmap.addCategory(yc);
                if (catmap.size() > 2) {
                    throw new IllegalArgumentException("only 2 classes allowed!");
                }
                classes.add(catmap.getNewCategoryOf(yc));
                //Index/value pairs:
                double ver = Double.parseDouble(version); 
                if (ver == 5.00){
                    examples.add(parseRowLibSVM(elems));
                }else{
                    examples.add(parseRow1(elems));
                }
               

            }
            x = new FeatureNode[examples.size()][];
           // y = new int[examples.size()];
            for (int i = 0; i < examples.size(); i++) {
                x[i] = examples.get(i);
              
                //y[i] = 2 * classes.get(i) - 1; //0,1 => -1,1

            }
            l = examples.size();
            bi.close();

        } catch (IOException | NullPointerException | IndexOutOfBoundsException | NumberFormatException ioe) {
            throw new ParseException("Parse error in header at line " + linecnt
                    + ": " + ioe.getMessage() + " of file: '" + filename
                    + "'. Not an svmlight-model file ?!", 0);
        }

    }

    public void loadLibSVMModel(BufferedReader bi, String filename, int linecnt) throws ParseException {
        // read a model file from libsvm format

        String line;
        String arg = new String();
        String kernel_type_table[]
                = {
                    "precomputed", "linear", "polynomial", "rbf", "sigmoid"};
        try {
            while (true) {

                line = bi.readLine();
                arg = line.substring(line.indexOf(' ') + 1);
                /*For uniformity we have 
                 0: user defined
                 1: linear
                 2: polynomial
                 3: gaussian
                 4: sigmoid
                                    
                 */
                if (line.startsWith("kernel_type")) {
                    int i;
                    for (i = 0; i < kernel_type_table.length; i++) {
                        if (arg.contains(kernel_type_table[i])) {
                            params.kernel = i;
                            break;
                        }
                    }
                    if (i == kernel_type_table.length) {
                        //System.err.print("unknown svm type.\n");
                        throw new ParseException("unknown LIBsvm type. \n",0);

                    }
                } //params.kernel = (Integer.parseInt((line.substring(0, line.indexOf('#'))).trim()) + 1) % 5;
                else if (line.startsWith("degree")) {
                    params.c = Integer.parseInt(arg);
                    linecnt++;
                } else if (line.startsWith("gamma")) {
                    params.a = Double.parseDouble(arg);
                    linecnt++;
                } else if (line.startsWith("coef0")) {
                    params.b = Double.parseDouble(arg);
                    linecnt++;
                } else if (line.startsWith("nr_class")) {
                    int nr_class = Integer.parseInt(arg);
                    linecnt++;
                } else if (line.startsWith("total_sv")) {
                    int numSupVecs;
                    numSupVecs = Integer.parseInt(arg);
                    alpha = new double[numSupVecs];
                    linecnt++;
                } else if (line.startsWith("rho")) {
                    b = Double.parseDouble(arg);
                    linecnt++;

                } else if (line.startsWith("label")) {
                    // get the label when needed right now we are dealing with 2 
                    // so just move
                    linecnt++;

                } else if (line.startsWith("probA")) {
                    // get the label when needed right now we are dealing with 2 
                    // so just move
                    linecnt++;
                } else if (line.startsWith("probB")) {
                    //
                    linecnt++;
                } else if (line.startsWith("nr_sv")) {
                    linecnt++;
                } else if (line.startsWith("SV")) {
                    linecnt++;
                    break;
                } else {
                    //System.err.print("unknown text in model file: [" + line + "]\n");
                    throw new ParseException( "unknown text in model file: [" + line,0);

                }
                n = 20;
            }

            // now read the alphas*y and feature vectors
            ArrayList<Integer> classes = new ArrayList<>();
            ArrayList<FeatureNode[]> examples = new ArrayList<>();
            int k = 0;
            while ((line = bi.readLine()) != null) {
                String[] elems = line.split(" ");
                //Category:
                linecnt++;
                double cat = Double.parseDouble(elems[0]);
                //alpha[k] = Math.abs(cat);
                alpha[k] = cat;
                int yc;
                yc = (cat > 0) ? 1 : -1;
                k = k + 1;
                catmap.addCategory(yc);
                if (catmap.size() > 2) {
                    throw new IllegalArgumentException("only 2 classes allowed!");
                }
                classes.add(catmap.getNewCategoryOf(yc));
                //Index/value pairs:
                examples.add(parseRowLibSVM(elems));

            }
            x = new FeatureNode[examples.size()][];
            y = new int[examples.size()];
            for (int i = 0; i < examples.size(); i++) {
                x[i] = examples.get(i);
                //y[i] = 2 * classes.get(i) - 1; //0,1 => -1,1
            }
            l = examples.size();
            for (int i = 0; i < examples.size(); i++) {
                y[i] = (alpha[i] > 0) ? 1 : -1;
                alpha[i] = Math.abs(alpha[i]);

            }
            bi.close();

        } catch (IOException | NullPointerException | IndexOutOfBoundsException | NumberFormatException ioe) {
            throw new ParseException("Parse error in header at line " + linecnt
                    + ": " + ioe.getMessage() + " of file: '" + filename
                    + "'. Not a Libsvm-model file ?!", 0);
        }

    }

    /**
     * Parses a row from file.
     *
     * @param row The already split row on spaces.
     * @return The corresponding FeatureNode.
     */
    public FeatureNode[] parseRow1(String[] row) {
        FeatureNode[] example = new FeatureNode[row.length - 2];
        int maxindex = 0;
        // till row.length-1 as last element is #
        for (int i = 1; i < row.length - 1; i++) {
            String[] iv = row[i].split(":");
            int index = Integer.parseInt(iv[0]);
            if (index <= maxindex) {
                throw new IllegalArgumentException("indices must be in increasing order!");
            }
            maxindex = index;
            double value = Double.parseDouble(iv[1]);
            example[i - 1] = new FeatureNode(index, value);
        }
        if (n < maxindex) {
            n = maxindex;
        }
        return example;
    }

    public FeatureNode[] parseRowLibSVM(String[] row) {
        FeatureNode[] example = new FeatureNode[row.length - 1];
        int maxindex = 0;
        // till row.length as last element is not #
        for (int i = 1; i < row.length; i++) {
            String[] iv = row[i].split(":");
            int index = Integer.parseInt(iv[0]);
            if (index <= maxindex) {
                throw new IllegalArgumentException("indices must be in increasing order!");
            }
            maxindex = index;
            double value = Double.parseDouble(iv[1]);
            example[i - 1] = new FeatureNode(index, value);
        }
        if (n < maxindex) {
            n = maxindex;
        }
        return example;
    }
}
