/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasvmpredict;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ritesh
 */
public class Problem {

    /**
     * The number of training data
     */
    public int l;
    /**
     * The number of features (including the bias feature if bias &gt;= 0)
     */
    public int n;
    /**
     * Array containing the target values
     */
    public int[] y;
    /**
     * Map of categories to allow various ID's to identify classes with.
     */
    public CategoryMap<Integer> catmap;
    /**
     * Array of sparse feature nodes
     */
    public FeatureNode[][] x;

    public Problem() {
        l = 0;
        n = 0;
        catmap = new CategoryMap<>();
    }

    /**
     * Loads a binary problem from file, i.e. having 2 classes. For SVMLIght
     * Ignore Comments or #
     *
     * @param filename The filename containing the problem in LibSVM format.
     * @throws java.io.IOException
     * @throws java.text.ParseException
     */
    public void loadBinaryProblem(String filename) throws IOException, ParseException {
        String row;
        ArrayList<Integer> classes = new ArrayList<>();
        ArrayList<FeatureNode[]> examples = new ArrayList<>();
        try {
            filename = writetoFormat(filename);
        } catch (IOException | IllegalArgumentException| ArrayIndexOutOfBoundsException | ParseException e) {
            throw new ParseException("Parse Error in test file" + e.getMessage() + filename, 0);
            //System.out.println(e);
        }
        try {
            BufferedReader r = new BufferedReader(new FileReader(filename));
            while ((row = r.readLine()) != null) {
                String[] elems = row.split(" ");
                //comments
                if (elems[0].charAt(0) == '#') {//to allow for comments
                    continue;
                }
                //Category:
                if (elems[0].charAt(0) == '+') {//to allow for example +1 as class label
                    elems[0] = elems[0].substring(1);
                }
                Integer cat = Integer.parseInt(elems[0]);
                catmap.addCategory(cat);
                if (catmap.size() > 2) {
                    throw new IllegalArgumentException("only 2 classes allowed!");
                }
                classes.add(catmap.getNewCategoryOf(cat));
                //Index/value pairs:
                examples.add(parseRow(elems));
            }
            x = new FeatureNode[examples.size()][];
            y = new int[examples.size()];
            for (int i = 0; i < examples.size(); i++) {
                x[i] = examples.get(i);
                y[i] = 2 * classes.get(i) - 1; //0,1 => -1,1
            }
            l = examples.size();
        } catch (IOException | IllegalArgumentException e) {
            throw new ParseException("Parse Error in test file" + e.getMessage() + filename, 0);
            //System.out.println(e);
        }
    }

    /**
     * Parses a row from a LibSVM format file.
     *
     * @param row The already split row on spaces.
     * @return The corresponding FeatureNode.
     */
    public FeatureNode[] parseRow(String[] row) {
        FeatureNode[] example = new FeatureNode[row.length - 1];
        int maxindex = 0;
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

    /**
     * Parses a file in fasta format file.
     *
     * @param filename
     * @return The corresponding File handle.
     * @throws java.io.IOException
     * @throws java.text.ParseException
     */
    public String writetoFormat(String filename) throws IOException,ArrayIndexOutOfBoundsException, ParseException {
        BufferedWriter out = null;
        String peptideInputFile = filename;
        String compositionOutputFile = filename.concat(".out");
        try {

            out = new BufferedWriter(new FileWriter(compositionOutputFile));
            System.out.print("# Amino Acid Composition of proteins \n");//prints on console

            out.write("# Amino Acid Composition of proteins \n");//prints in file
            System.out.print("# A,C,D,E,F,G,H,I,K,L,M,N,P,Q,R,S,T,V,W,Y\n");//prints on console
            out.write("# A,C,D,E,F,G,H,I,K,L,M,N,P,Q,R,S,T,V,W,Y\n");//prints in file
            BufferedReader br = null;
            String aa;
            aa = "ACDEFGHIKLMNPQRSTVWY";//defining reference aminoacid string
            int i;

            String line;
            br = new BufferedReader(new FileReader(peptideInputFile));
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                //initializing 1D array of size 20
                int comp[];
                int len  = 0;
                comp = new int[20];
                

                
                len = line.length();

                for (String retval : line.split("")) {
                    String chompAA = retval.trim();
                    if (chompAA.length() != 0) {//in built bug ?

                        int position;
                        position = aa.indexOf(chompAA);

                        comp[position]++;
                    }
                }
                out.write("+1 ");
                int j;
                for (j = 0; j < 20; j++) {
                    double perc;
                    perc = (double) (comp[j] * 100) / len;

                    DecimalFormat df = new DecimalFormat("###.##");
                    String num = Integer.toString(j + 1);

                    if (j < 19) {

                        System.out.print(num + ":" + df.format(perc) + " ");//prints on console
                        out.write(num + ":" + df.format(perc) + " ");////prints in file
                    } else {
                        System.out.print(num + ":" + df.format(perc) + "\n");//prints on console
                        out.write(num + ":" + df.format(perc) + "\n");//prints in file
                    }
                }
            }
            //out.close();         
        } catch (IOException | ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParseException("Parse Error in test file" + ex.getMessage() + filename, 0);
            
        } finally {
            try {
                out.close();
            } catch (IOException | ArrayIndexOutOfBoundsException ex) {
                Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
                throw new ParseException("Parse Error in test file" + ex.getMessage() + filename, 0);
            }
        }
        return compositionOutputFile;
    }

}
