/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

import java.io.File;

/**
 *
 * @author 80116
 */
public class NpmPackageFactory {
    
    
    
    
    public static NpmPackage make(File directory){
        
        File description = directory.toPath().resolve("package.json").toFile();
        
        if(description.exists()){
            try {
                return new SinglePackage(directory);
            } catch (Exception e) {
                return null;
            }
        }
        
        return new SuperPackage(directory);
    }
}
