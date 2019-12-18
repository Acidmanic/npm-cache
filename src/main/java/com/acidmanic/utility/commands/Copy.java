/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.commands;

import com.acidmanic.commandline.commands.CommandBase;
import com.acidmanic.commandline.commands.parameters.ParameterBuilder;
import com.acidmanic.utility.services.CommandWrapperLogger;
import com.acidmanic.utility.services.FilesystemService;
import java.io.File;

/**
 *
 * @author 80116
 */
public class Copy extends CommandBase{

    
    private static final String CACHEDIR = "cache";
    
    @Override
    protected String getUsageString() {
        return "Copies all files and directories from the cache into the destination's node_modules whitout checking";
    }

     @Override
    protected void defineParameters(ParameterBuilder builder) {
        super.defineParameters(builder);

        builder.named("target-directory").described("the directory containing packages.json.").mandatory().indexAt(0)
                .ofType(File.class);
    }
    @Override
    public void execute() {
        File directory = ((File) getParameterValue("target-directory")).toPath().toAbsolutePath().normalize().toFile();
        
        File localRepo = getLocalRepoDirectory();
        
        File src = localRepo.toPath().resolve("node_modules").toFile();
        
        File dst = directory.toPath().resolve("node_modules").toFile();

        FilesystemService fs = new FilesystemService();
        
        fs.setLogger(new CommandWrapperLogger());
        
        fs.setLoglevel(FilesystemService.LOG_LEVEL_FILE);
        
        try {
            fs.copyContent(src, dst);
        } catch (Exception ex) {
            error("Error copying files: " + ex.getClass().getSimpleName());
        }
        
        
    }
    
    private File getLocalRepoDirectory() {
        try {
            File myrectory = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

            myrectory = myrectory.getParentFile().toPath().resolve(CACHEDIR).toFile();

            myrectory = myrectory.toPath().normalize().toFile();

            return myrectory;
            // myrectory = myrectory.toPath().resolve(CACHEDIR).toFile();

        } catch (Exception e) {
            error("Unable to find local cache");
        }

        return new File(".");
    }
    
}
