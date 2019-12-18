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
public interface Logger {
    
    void error(String message);
    void warning(String message);
    void info(String message);
    void log(String message);
}
