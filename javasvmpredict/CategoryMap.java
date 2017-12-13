/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasvmpredict;
import java.util.*;
/**
 *
 * @author Ritesh
 */

/**
 *
 * @author Ritesh
 * @param <T>
 */
public class CategoryMap<T> {

    HashMap<T, Integer> oldnew;
    HashMap<Integer, T> newold;
    int lastindex;

    public CategoryMap() {
        oldnew = new HashMap<>();
        newold = new HashMap<>();
        lastindex = -1;
    }

    public int size() {
        return oldnew.size();
    }

    public boolean isEmpty() {
        return oldnew.isEmpty();
    }

    public void addCategory(T cat) {
        if (!oldnew.containsKey(cat)) {
            lastindex++;
            oldnew.put(cat, lastindex);
            newold.put(lastindex, cat);
        }
    }

    public T getOldCategoryOf(int cat) {
        return newold.get(cat);
    }

    public int getNewCategoryOf(T cat) {
        
        return oldnew.get(cat);
        
    }
}
