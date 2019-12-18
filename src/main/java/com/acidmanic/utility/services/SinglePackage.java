/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

import com.acidmanic.utility.models.MashMap;
import java.io.File;
import com.acidmanic.utility.models.PackageDescription;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author 80116
 */
public class SinglePackage implements NpmPackage{
    
    
    private final File directory;
    
    private final File descriptionFile;
    
    private final PackageDescription description;
    
    private final File nodeModulesFile;

    public SinglePackage(File directory) throws Exception {
        
        this.directory = directory;
        
        this.descriptionFile = directory.toPath().resolve("package.json").toFile();
        
        this.description = PackageDescriptionReader.readPackage(directory);
        
        this.nodeModulesFile = directory.toPath().resolve("node_modules").toFile();
    }

    @Override
    public List<String> externalDependencies() {
        
        ArrayList<String> ret = new ArrayList<>(); 
        
        addDependencies(this.description.getDependencies(),ret);
        
//        addDependencies(this.description.getDevDependencies(),ret);
        
        
        return ret;
    }

    private void checkAdd(String dep, ArrayList<String> ret) {
        if(!ret.contains(dep)){
            ret.add(dep);
        }
    }

    @Override
    public String getName() {
        return this.description.getName();
    }

    private void addDependencies(MashMap dependencies, ArrayList<String> ret) {
        if(dependencies != null){
            List<String>  names =  dependencies.getAllNames();
            
            if(names != null){
                ret.addAll(names);
            }
        }
    }

    @Override
    public String getVersion() {
        return this.description.getVersion();
    }

    @Override
    public List<NpmPackage> listAllPackages() {
        List<NpmPackage> ret = new ArrayList<>();
        
        ret.add(this);
        
        return ret;
    }
    
    
    
    
}
