/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

import com.acidmanic.commandline.commands.CommandBase;
import com.acidmanic.consoletools.terminal.Terminal;
import com.acidmanic.consoletools.terminal.styling.TerminalControlEscapeSequences;
import com.acidmanic.consoletools.terminal.styling.TerminalStyle;

/**
 *
 * @author 80116
 */
public class CommandWrapperLogger implements Logger{
    
    private class Exposer extends CommandBase{
        
           
        public void w(String t){warning(t);}
        
        public void e(String t){error(t);}
        
        public void i(String t){info(t);}
        
        public void l(String t){log(t);}

        @Override
        protected String getUsageString() {return null;}

        @Override
        public void execute() {}
    }
    
    private final Exposer exposer = new Exposer();
    
    @Override
    public void warning(String text){
        exposer.w(text);
    }
    @Override
    public void error(String text){
        exposer.e(text);
    }
    @Override
    public void info(String text){
        Terminal t = new Terminal(System.out);

        TerminalStyle style = new TerminalStyle(TerminalControlEscapeSequences.BACKGROUND_BLACK,
                TerminalControlEscapeSequences.FOREGROUND_CYAN);
        t.setScreenAttributes(style);

        System.out.println(text);

        t.resetScreenAttributes();
    }
    @Override
    public void log(String text){
        exposer.l(text);
    }
}
