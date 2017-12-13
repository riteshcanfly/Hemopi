/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasvmpredictui;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ritesh
 */
public class Mutant {
    ArrayList<String> allMutants;
        
    ArrayList<Integer> allPositions;
    String peptideArray[] ;
    Mutant(String pept){
        String[] aminoAcids = new String[]{"A","C","D","E","F","G","H","I","K","L","M","N","P","Q","R","S","T","V","W","Y"};
       
        System.out.println("Src peptide: "+ pept);
        
        peptideArray = pept.split("");
       
        int len = peptideArray.length;
      
        String peptideArrayNew[] = new String[len];
      
        System.arraycopy(peptideArray,0,peptideArrayNew,0,len);
       
        int i; 
        
       allMutants = new ArrayList<>();
       allPositions = new ArrayList<>();
       // added for the original peptide
       allMutants.add(pept); allPositions.add(0);
 
       for(i=0;i<len;i++){int j;int pos=i+1;
            for(j=0;j<20;j++){
                
                String transPeptide[];
                String altPeptide[];
                
                altPeptide=Arrays.copyOf(peptideArrayNew,peptideArrayNew.length);
                
                if(!aminoAcids[j].equals(altPeptide[i])){
                    altPeptide[i]=aminoAcids[j];
                   
                    String mutant = Arrays.toString(altPeptide);
                    mutant = mutant.substring(1, mutant.length()-1).replaceAll(",", "").replaceAll(" ", "");
                    System.out.println(mutant+"\t"+ pos);
                    
                    allMutants.add(mutant);allPositions.add(pos);
                }
            }
        }
      
    }
    
    
}
