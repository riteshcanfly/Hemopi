/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasvmpredictui;

import java.util.*;

/**
 *
 * @author Ritesh
 */
public class pcCalculator {

    HashMap amphiphilicity;
    HashMap charge;
    HashMap hydropathy;
    HashMap hydrophilicity;
    HashMap hydrophobicity;
    HashMap nethydrogen;
    HashMap solvation;
    HashMap steric;
    HashMap weight;
    HashMap allProps; 

    public  HashMap getAvg(String s1, HashMap h1,String propName) {
        double avgg = 0;
        String[] peptideArrayy = s1.split("");
        int lenn = peptideArrayy.length ;
        double summationn = 0.0;
        double averagee = 0.0;
        for (int ii = 0; ii < lenn; ii++) {
            summationn += ((Double) h1.get(peptideArrayy[ii]));
        }
        averagee = (double) (summationn) / lenn;
        allProps.put(propName, averagee) ;
        return allProps ;
    }

    public HashMap getSum(String s1, HashMap h1,String propName) {
        
        String[] peptideArrayy = s1.split("");
        int lenn = peptideArrayy.length ;
        double summationn = 0.0;
        double averagee = 0.0;
        for (int ii = 0; ii < lenn; ii++) {
            summationn += ((Double) h1.get(peptideArrayy[ii]));
        }
        averagee = (double) (summationn) / lenn;
       if (!propName.equalsIgnoreCase("Molecular weight")){
       } else {
           double water  = 18 * (lenn - 1);
           summationn =  summationn  - water ;
        }
        allProps.put(propName, summationn);
        return allProps;
     
    }

    public HashMap getPI(String s1, String propName) {
        String[] h1 = s1.split("");
        int lenn = h1.length - 1;
        int asp = 0, glu = 0, cys = 0, tyr = 0, his = 0, lys = 0, arg = 0;
        for (int j = 0; j <= lenn; j++) {
            //System.out.println(peptideArray[j]);
            if (h1[j].equals("D")) {
                asp++;
            }
            if (h1[j].equals("E")) {
                glu++;
            }
            if (h1[j].equals("C")) {
                cys++;
            }
            if (h1[j].equals("Y")) {
                tyr++;
            }
            if (h1[j].equals("H")) {
                his++;
            }
            if (h1[j].equals("K")) {
                lys++;
            }
            if (h1[j].equals("R")) {
                arg++;
            }
        }
        //System.out.println("D"+asp+"\tE"+glu+"\tC"+cys+"\tY"+tyr+"\tH"+his+"\tK"+lys+"\tR"+arg);
        double ph = 0.0;
        double nq = 0.0;
        do {
            ph += 0.01;
            double qn1 = 0.0, qn2 = 0.0, qn3 = 0.0, qn4 = 0.0, qn5 = 0.0, qp1 = 0.0, qp2 = 0.0, qp3 = 0.0, qp4 = 0.0;
            qn1 = -1 / (1 + Math.pow(10, (3.55 - ph)));             //C-terminal charge
            qn2 = -asp / (1 + Math.pow(10, (4.05 - ph)));           //D charge
            qn3 = -glu / (1 + Math.pow(10, (4.45 - ph)));            //E charge
            qn4 = -cys / (1 + Math.pow(10, (9 - ph)));            //C charge
            qn5 = -tyr / (1 + Math.pow(10, (10 - ph)));        //Y charge
            qp1 = his / (1 + Math.pow(10, (ph - 5.98)));            //H charge
            qp2 = 1 / (1 + Math.pow(10, (ph - 8.2)));                //NH2charge
            qp3 = lys / (1 + Math.pow(10, (ph - 10)));            //K charge
            qp4 = arg / (1 + Math.pow(10, (ph - 12)));            //R charge
            nq = qn1 + qn2 + qn3 + qn4 + qn5 + qp1 + qp2 + qp3 + qp4;
        } while (nq > 0);
        double pif = ph;
        
        allProps.put(propName, pif);
        return allProps ; 
    }

    private void initializeVals(){
        
        amphiphilicity = new HashMap();
        charge = new HashMap();
        hydropathy = new HashMap();
        hydrophilicity = new HashMap();
        hydrophobicity = new HashMap();
        nethydrogen = new HashMap();
        solvation = new HashMap();
        steric = new HashMap();
        weight = new HashMap();
        //encoded hashtable values
        
        allProps = new HashMap(); //all properties hashmap
        
        amphiphilicity.put("A", 0.0);
        amphiphilicity.put("R", 2.45);
        amphiphilicity.put("N", 0.0);
        amphiphilicity.put("D", 0.0);
        amphiphilicity.put("C", 0.0);
        amphiphilicity.put("Q", 1.25);
        amphiphilicity.put("E", 1.27);
        amphiphilicity.put("G", 0.0);
        amphiphilicity.put("H", 1.45);
        amphiphilicity.put("I", 0.0);
        amphiphilicity.put("L", 0.0);
        amphiphilicity.put("K", 3.67);
        amphiphilicity.put("M", 0.0);
        amphiphilicity.put("F", 0.0);
        amphiphilicity.put("P", 0.0);
        amphiphilicity.put("S", 0.0);
        amphiphilicity.put("T", 0.0);
        amphiphilicity.put("W", 6.93);
        amphiphilicity.put("Y", 5.06);
        amphiphilicity.put("V", 0.0);
        amphiphilicity.put("B", 0.0);
        amphiphilicity.put("J", 0.0);
        amphiphilicity.put("O", 0.0);
        amphiphilicity.put("U", 0.0);
        amphiphilicity.put("Z", 0.0);//MITS020101
        charge.put("A", 0.0);
        charge.put("R", 1.0);
        charge.put("N", 0.0);
        charge.put("D", -1.0);
        charge.put("C", 0.0);
        charge.put("Q", 0.0);
        charge.put("E", -1.0);
        charge.put("G", 0.0);
        charge.put("H", 0.0);
        charge.put("I", 0.0);
        charge.put("L", 0.0);
        charge.put("K", 1.0);
        charge.put("M", 0.0);
        charge.put("F", 0.0);
        charge.put("P", 0.0);
        charge.put("S", 0.0);
        charge.put("T", 0.0);
        charge.put("W", 0.0);
        charge.put("Y", 0.0);
        charge.put("V", 0.0);
        charge.put("B", 0.0);
        charge.put("J", 0.0);
        charge.put("O", 0.0);
        charge.put("U", 0.0);
        charge.put("Z", 0.0);//KLEP840101
        hydropathy.put("A", 1.8);
        hydropathy.put("R", -4.5);
        hydropathy.put("N", -3.5);
        hydropathy.put("D", -3.5);
        hydropathy.put("C", 2.5);
        hydropathy.put("Q", -3.5);
        hydropathy.put("E", -3.5);
        hydropathy.put("G", -0.4);
        hydropathy.put("H", -3.2);
        hydropathy.put("I", 4.5);
        hydropathy.put("L", 3.8);
        hydropathy.put("K", -3.9);
        hydropathy.put("M", 1.9);
        hydropathy.put("F", 2.8);
        hydropathy.put("P", -1.6);
        hydropathy.put("S", -0.8);
        hydropathy.put("T", -0.7);
        hydropathy.put("W", -0.9);
        hydropathy.put("Y", -1.3);
        hydropathy.put("V", 4.2);
        hydropathy.put("B", 0.0);
        hydropathy.put("J", 0.0);
        hydropathy.put("O", 0.0);
        hydropathy.put("U", 0.0);
        hydropathy.put("Z", 0.0);//KYTJ820101
        hydrophilicity.put("A", -0.5);
        hydrophilicity.put("R", 3.0);
        hydrophilicity.put("N", 0.2);
        hydrophilicity.put("D", 3.0);
        hydrophilicity.put("C", -1.0);
        hydrophilicity.put("Q", 0.2);
        hydrophilicity.put("E", 3.0);
        hydrophilicity.put("G", 0.0);
        hydrophilicity.put("H", -0.5);
        hydrophilicity.put("I", -1.8);
        hydrophilicity.put("L", -1.8);
        hydrophilicity.put("K", 3.0);
        hydrophilicity.put("M", -1.3);
        hydrophilicity.put("F", -2.5);
        hydrophilicity.put("P", 0.0);
        hydrophilicity.put("S", 0.3);
        hydrophilicity.put("T", -0.4);
        hydrophilicity.put("W", -3.4);
        hydrophilicity.put("Y", -2.3);
        hydrophilicity.put("V", -1.5);
        hydrophilicity.put("B", 0.0);
        hydrophilicity.put("J", 0.0);
        hydrophilicity.put("O", 0.0);
        hydrophilicity.put("U", 0.0);
        hydrophilicity.put("Z", 0.0);//HOPT810101
        hydrophobicity.put("A", 0.25);
        hydrophobicity.put("R", -1.76);
        hydrophobicity.put("N", -0.64);
        hydrophobicity.put("D", -0.72);
        hydrophobicity.put("C", 0.04);
        hydrophobicity.put("Q", -0.69);
        hydrophobicity.put("E", -0.62);
        hydrophobicity.put("G", 0.16);
        hydrophobicity.put("H", -0.40);
        hydrophobicity.put("I", 0.73);
        hydrophobicity.put("L", 0.53);
        hydrophobicity.put("K", -1.10);
        hydrophobicity.put("M", 0.26);
        hydrophobicity.put("F", 0.61);
        hydrophobicity.put("P", -0.07);
        hydrophobicity.put("S", -0.26);
        hydrophobicity.put("T", -0.18);
        hydrophobicity.put("W", 0.37);
        hydrophobicity.put("Y", 0.02);
        hydrophobicity.put("V", 0.54);
        hydrophobicity.put("B", 0.0);
        hydrophobicity.put("J", 0.0);
        hydrophobicity.put("O", 0.0);
        hydrophobicity.put("U", 0.0);
        hydrophobicity.put("Z", 0.0);//EISD840101
        nethydrogen.put("A", 0.0);
        nethydrogen.put("R", 4.0);
        nethydrogen.put("N", 2.0);
        nethydrogen.put("D", 1.0);
        nethydrogen.put("C", 0.0);
        nethydrogen.put("Q", 2.0);
        nethydrogen.put("E", 1.0);
        nethydrogen.put("G", 0.0);
        nethydrogen.put("H", 1.0);
        nethydrogen.put("I", 0.0);
        nethydrogen.put("L", 0.0);
        nethydrogen.put("K", 2.0);
        nethydrogen.put("M", 0.0);
        nethydrogen.put("F", 0.0);
        nethydrogen.put("P", 0.0);
        nethydrogen.put("S", 1.0);
        nethydrogen.put("T", 1.0);
        nethydrogen.put("W", 1.0);
        nethydrogen.put("Y", 1.0);
        nethydrogen.put("V", 0.0);
        nethydrogen.put("B", 0.0);
        nethydrogen.put("J", 0.0);
        nethydrogen.put("O", 0.0);
        nethydrogen.put("U", 0.0);
        nethydrogen.put("Z", 0.0);//FAUJ880109
        solvation.put("A", 0.67);
        solvation.put("R", -2.1);
        solvation.put("N", -0.6);
        solvation.put("D", -1.2);
        solvation.put("C", 0.38);
        solvation.put("Q", -0.22);
        solvation.put("E", -0.76);
        solvation.put("G", 0.0);
        solvation.put("H", 0.64);
        solvation.put("I", 1.9);
        solvation.put("L", 1.9);
        solvation.put("K", -0.57);
        solvation.put("M", 2.4);
        solvation.put("F", 2.3);
        solvation.put("P", 1.2);
        solvation.put("S", 0.01);
        solvation.put("T", 0.52);
        solvation.put("W", 2.6);
        solvation.put("Y", 1.6);
        solvation.put("V", 1.5);
        solvation.put("B", 0.0);
        solvation.put("J", 0.0);
        solvation.put("O", 0.0);
        solvation.put("U", 0.0);
        solvation.put("Z", 0.0);//EISD860101
        steric.put("A", 0.52);
        steric.put("R", 0.68);
        steric.put("N", 0.76);
        steric.put("D", 0.76);
        steric.put("C", 0.62);
        steric.put("Q", 0.68);
        steric.put("E", 0.68);
        steric.put("G", 0.0);
        steric.put("H", 0.70);
        steric.put("I", 1.02);
        steric.put("L", 0.98);
        steric.put("K", 0.68);
        steric.put("M", 0.78);
        steric.put("F", 0.70);
        steric.put("P", 0.36);
        steric.put("S", 0.53);
        steric.put("T", 0.50);
        steric.put("W", 0.70);
        steric.put("Y", 0.70);
        steric.put("V", 0.76);
        steric.put("B", 0.0);
        steric.put("J", 0.0);
        steric.put("O", 0.0);
        steric.put("U", 0.0);
        steric.put("Z", 0.0);//CHAM810101
        weight.put("A", 80.09);
        weight.put("R", 174.20);
        weight.put("N", 132.12);
        weight.put("D", 133.10);
        weight.put("C", 121.15);
        weight.put("Q", 146.15);
        weight.put("E", 147.13);
        weight.put("G", 75.07);
        weight.put("H", 155.16);
        weight.put("I", 131.17);
        weight.put("L", 131.17);
        weight.put("K", 146.19);
        weight.put("M", 149.21);
        weight.put("F", 165.19);
        weight.put("P", 115.13);
        weight.put("S", 105.09);
        weight.put("T", 119.12);
        weight.put("W", 204.24);
        weight.put("Y", 181.19);
        weight.put("V", 117.15);
        weight.put("B", 0.0);
        weight.put("J", 0.0);
        weight.put("O", 0.0);
        weight.put("U", 0.0);
        weight.put("Z", 0.0);//FASG760101
        hydrophobicity.put("B", 0.0);
        hydrophobicity.put("J", 0.0);
        hydrophobicity.put("O", 0.0);
        hydrophobicity.put("U", 0.0);
        hydrophobicity.put("Z", 0.0);
        hydrophobicity.put("A", .25);
        hydrophobicity.put("R", -1.76);
        hydrophobicity.put("N", -.64);
        hydrophobicity.put("D", -.72);
        hydrophobicity.put("C", .04);
        hydrophobicity.put("Q", -.69);
        hydrophobicity.put("E", -.62);
        hydrophobicity.put("G", .16);
        hydrophobicity.put("H", -.4);
        hydrophobicity.put("I", .73);
        hydrophobicity.put("L", .53);
        hydrophobicity.put("K", -1.1);
        hydrophobicity.put("M", .26);
        hydrophobicity.put("F", .61);
        hydrophobicity.put("P", -.07);
        hydrophobicity.put("S", -.26);
        hydrophobicity.put("T", -.18);
        hydrophobicity.put("W", .37);
        hydrophobicity.put("Y", .02);
        hydrophobicity.put("V", .54);
        
    }
    public HashMap calcPhysicalProperties(String peptide){
       
        allProps = getAvg(peptide, amphiphilicity,"Amphipathicity");
       
        allProps = getSum(peptide, charge,"Charge");
      
        allProps = getAvg(peptide, hydropathy,"Hydropathicity");
       
        allProps = getAvg(peptide, hydrophilicity,"Hydrophilicity");
       
        allProps = getAvg(peptide, hydrophobicity,"Hydrophobicity");
        
        allProps = getSum(peptide, nethydrogen,"Net Hydrogen");
        
        allProps = getAvg(peptide, solvation,"Solvation");
        
        allProps = getAvg(peptide, steric,"Steric hinderance");
       
        allProps  = getPI(peptide,"pI");
        
        allProps = getSum(peptide, weight,"Molecular weight");
       
        return allProps;
    }
 
    public double[] getPhysicalProperties(String peptide, ArrayList<String> stCheckboxes) {
        // we have the strings for which user wants the physical properties 
        int length = stCheckboxes.size();
        double physicalProps[] = new double[length];
        allProps = calcPhysicalProperties(peptide);
        // Get a set of the entries
        Set set = allProps.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        int k  = 0 ;
        while (i.hasNext()&& k < length) {
            System.out.println("setchkbox "+ stCheckboxes.get(k) ) ;
            String s  = stCheckboxes.get(k) ; 
            Double l = (Double)allProps.get(s);
            System.out.println("setchkbox val"+ l ) ;
            physicalProps[k] = (Double)allProps.get(stCheckboxes.get(k)) ;
            
             k = k + 1 ; 
        }
  
        return physicalProps;
    }
    pcCalculator() {

        initializeVals();
  
    }
}
