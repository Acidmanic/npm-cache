/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

import java.util.List;

/**
 *
 * @author 80116
 */
public interface NpmPackage {
    
    
    List<String> externalDependencies();
    
    String getName();
    
    String getVersion();
    
    List<NpmPackage> listAllPackages();
}
