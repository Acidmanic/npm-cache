package com.acidmanic.utility.models;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PackageDescription {


    private MashMap dependencies;
    private MashMap devDependencies;
    private String name;
    private String version;
    
    public MashMap getDependencies() {
        return dependencies;
    }

    public void setDependencies(MashMap dependencies) {
        this.dependencies = dependencies;
    }

    public PackageDescription() {
        this.dependencies = new MashMap();
    }

    public MashMap getDevDependencies() {
        return devDependencies;
    }

    public void setDevDependencies(MashMap devDependencies) {
        this.devDependencies = devDependencies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    /***
     * Both deps and dev-deps
     * @param cons 
     */
    public void forEachDependency(BiConsumer<String,String> cons){
        if(this.dependencies != null){
            this.dependencies.forEach(cons);
        }
        if(this.devDependencies != null){
            this.devDependencies.forEach(cons);
        }
    }
}