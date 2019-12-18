package com.acidmanic.utility.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class MashMap implements Map<String, String> {

    private ArrayList<String> names;
    private ArrayList<String> values;

    public MashMap() {
        this.names = new ArrayList<>();

        this.values = new ArrayList<>();
    }

    @Override
    public int size() {
        return names.size();
    }

    @Override
    public boolean isEmpty() {
        return names.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return names.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values.contains(value);
    }

    @Override
    public String get(Object key) {
        return null;
    }

    @Override
    public String put(String key, String value) {
        return addItem(key, value);
    }

    private synchronized String addItem(String key, String value) {

        this.names.add(key);
        this.values.add(value);

        return value;
    }

    @Override
    public String remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {

    }

    @Override
    public void clear() {
        this.names.clear();
        this.values.clear();
    }

    @Override
    public Set<String> keySet() {

        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super String> action) {
        for (int i = 0; i < this.size(); i++) {
            action.accept(this.names.get(i), this.values.get(i));
        }
    }

    public boolean contains(String name,String value){
        for (int i = 0; i < this.size(); i++) {
            if(stringEquals(this.names.get(i), name)){
                if(stringEquals(this.values.get(i), value)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean stringEquals(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }

        if (s1 == null || s2 == null) {
            return false;
        }

        return s1.compareTo(s2) == 0;
    }

	public List<String> getAllNames() {
		return this.names;
	}
}