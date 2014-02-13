/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author sergeiw
 */
public class JsonObjectCollection<T> extends JsonObject implements Collection<T> {

    Collection<T> col;

    public JsonObjectCollection() {
        this.col = new ArrayList<T>();
    }

    public JsonObjectCollection(Collection<T> col) {
        this.col = col;
    }
    
    @Override
    public String toString() {
        try {
            return toJson();
        } catch (IOException ex) {
            return super.toString();
        }
    }
    
    @Override
    public int size() {
        return col.size();
    }

    @Override
    public boolean isEmpty() {
        return col.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return col.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return col.iterator();
    }

    @Override
    public Object[] toArray() {
        return col.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return col.toArray(ts);
    }

    @Override
    public boolean add(T e) {
        return col.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return col.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        return col.containsAll(clctn);
    }

    @Override
    public boolean addAll(Collection<? extends T> clctn) {
        return col.addAll(clctn);
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        return col.removeAll(clctn);
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        return col.retainAll(clctn);
    }

    @Override
    public void clear() {
        col.clear();
    }
    
}
