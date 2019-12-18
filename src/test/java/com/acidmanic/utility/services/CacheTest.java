/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author 80116
 */
public class CacheTest extends TestCase {
    
    public CacheTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testGetDirectDependecies() {
        System.out.println("getDirectDependecies");
        String pack = "@angular/platform-server";
        Cache instance = new Cache(Paths.get("debug").resolve("test").toFile());
        List<String> result = instance.getDirectDependecies(pack);
        assertNotNull(result);
        
        
    }

    
}
