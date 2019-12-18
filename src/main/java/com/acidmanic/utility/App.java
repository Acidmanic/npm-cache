package com.acidmanic.utility;

import com.acidmanic.commandline.application.ExecutionEnvironment;
import com.acidmanic.commandline.commands.ApplicationWideTypeRegistery;
import com.acidmanic.utility.commands.Copy;
import com.acidmanic.utility.commands.Sync;
import com.acidmanic.utility.services.FilesystemService;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws Exception {

//        File cache = new File("target").toPath().resolve("cache").toFile();
//
//        cache.mkdirs();
//
//        Files.copy(Paths.get("package.json.template"),
//                 cache.toPath().resolve("package.json"),
//                 StandardCopyOption.REPLACE_EXISTING);

        ApplicationWideTypeRegistery.makeInstance().registerClass(Sync.class);
        ApplicationWideTypeRegistery.makeInstance().registerClass(Copy.class);

        ExecutionEnvironment application = new ExecutionEnvironment();

        application.execute(args);
    }

}
