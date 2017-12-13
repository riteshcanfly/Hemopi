/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasvmpredictui;
import java.util.ArrayList;

/**
 *
 * @author Ritesh
 */
public class SlidingWindow {
    ArrayList<String> allSubstrings;
        
    ArrayList<Integer> allPositions;
    SlidingWindow(String pept, int window){
        String protein=pept;
        
        int len = protein.length();
        int diff = len-window;
        int pos;
         allSubstrings = new ArrayList<>();
         allPositions = new ArrayList<>();
        
        for(int i=0;i<=diff;i++){
            pos =i+1;
            String substring = protein.substring(i,i+window);
            System.out.println(substring+"\t"+pos);
            allSubstrings.add(substring);
            allPositions.add(pos);
        }
    }
}
