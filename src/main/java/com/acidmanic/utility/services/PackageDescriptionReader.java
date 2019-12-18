/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

import com.acidmanic.utility.models.PackageDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

/**
 *
 * @author 80116
 */
public class PackageDescriptionReader {
    
    public static PackageDescription readPackage(File directory) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        File packageJsonFile = directory.toPath().resolve("package.json").toFile();

        if (packageJsonFile.exists()) {
            return mapper.readValue(packageJsonFile, com.acidmanic.utility.models.PackageDescription.class);
        }

        return new com.acidmanic.utility.models.PackageDescription();
    }
    
    
}
