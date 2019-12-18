/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility;

import com.acidmanic.commandline.application.ExecutionEnvironment;
import com.acidmanic.commandline.commands.ApplicationWideTypeRegistery;
import com.acidmanic.utility.commands.Sync;

/**
 *
 * @author 80116
 */
public class AngularTest {
    
    
    
    public static void main(String[] args){
        ApplicationWideTypeRegistery.makeInstance().registerClass(Sync.class);

        ExecutionEnvironment application = new ExecutionEnvironment();

        application.execute(new String[]{"sync","debug"});
    }
}
