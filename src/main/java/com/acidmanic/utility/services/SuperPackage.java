/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author 80116
 */
public class SuperPackage implements NpmPackage{

    
    private final File directory;
    
    private final List<NpmPackage> subPackages;
    
    

    public SuperPackage(File directory) {
        this.directory = directory;
        
        this.subPackages = listSubPackages(directory);
    }
    
    
    @Override
    public List<String> externalDependencies() {
        ArrayList<String> ret = new ArrayList<>();
        
        this.subPackages.forEach(p -> ret.addAll(p.externalDependencies()));
        
        return ret;
    }

    private List<NpmPackage> listSubPackages(File directory) {
        
        ArrayList<NpmPackage> packages = new ArrayList<>();
        
        File[] files = directory.listFiles();
        
        if(files != null){
            for(File f : files){
                if (f.isDirectory() && f.toPath().resolve("package.json").toFile().exists()){
                    
                    try {
                        NpmPackage p = new SinglePackage(f);
                        
                        packages.add(p);
                        
                    } catch (Exception e) {
                    }
                }
            }
        }
        
        return packages;
    }

    @Override
    public String getName() {
        return this.directory.getName();
    }
    
    
    public List<NpmPackage> getSubPackages(){
        return this.subPackages;
    }

    @Override
    public String getVersion() {
       return Version.ALWAYSMATCH;
    }

    @Override
    public List<NpmPackage> listAllPackages() {
        
        List<NpmPackage> ret = new ArrayList<>();
        
        ret.addAll(this.subPackages);
        
        return ret;
    }
}
