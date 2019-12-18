/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

/**
 *
 * @author 80116
 */
public class Version {

    public static String ALWAYSMATCH = "*";

    private final int[] values;
    private boolean andGreater;
    private boolean alwaysMatch = false;

    public Version(String value) {
        if (value.compareTo("*") == 0) {
            
            this.andGreater = false;
            this.values = new int []{0,0,0};
            
            this.alwaysMatch = true;
        } else {
            this.andGreater = false;

            if (value.startsWith("^")) {
                value = value.substring(1, value.length());

                this.andGreater = true;
            }
            String[] numbers = value.split("\\.");

            values = new int[numbers.length];

            for (int i = 0; i < numbers.length; i++) {
                values[i] = Integer.parseInt(numbers[i]);
            }
        }
    }

    private int compareTo(Version v) {

        for (int i = 0; i < this.values.length; i++) {
            if (v.values[i] > this.values[i]) {
                return -1;
            }
            if (v.values[i] < this.values[i]) {
                return 1;
            }
        }

        return 0;
    }

    public boolean matches(Version v) {
        
        if(this.alwaysMatch || v.alwaysMatch){
            return true;
        }
        
        int compare = this.compareTo(v);

        if (this.andGreater && compare >= 0) {
            return true;
        }

        if (v.andGreater && compare <= 0) {
            return true;
        }

        if (!this.andGreater && !v.andGreater && compare == 0) {
            return true;
        }
        return false;
    }
}
