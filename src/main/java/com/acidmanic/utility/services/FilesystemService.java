package com.acidmanic.utility.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FilesystemService {
    
    
    private Logger logger = new NullLogger();
    public static final int LOG_LEVEL_FILE =2;
    public static final int LOG_LEVEL_DIRECTORY =1;
    public static final int LOG_LEVEL_METHOD =0;
    private int loglevel = LOG_LEVEL_DIRECTORY;
    
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public int getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(int loglevel) {
        this.loglevel = loglevel;
    }
    
    
    private void info(String message,int level){
        if(this.loglevel >= level){
            this.logger.info(message);
        }
    }
    

    public void syncInto(File src, File dst, List<String> ignoreList) throws Exception {
        
        ignoreList = normalize(ignoreList);

        dst.mkdirs();

        deleteRepoContents(dst, ignoreList);

        copyRepoContent(src, dst, ignoreList);

    }

    private void copyRepoContent(File src, File dst, List<String> ignoreList) throws Exception {

        File[] files = src.listFiles();

        for (File file : files) {

            if (!ignoreList.contains(file.getName().toLowerCase())) {

                copyInto(file, dst);

            }
        }
    }

    public void copyInto(File file, File dst) throws Exception {

        if (!file.isDirectory()) {

            copySingleFileToDirectory(file, dst);

        } else {
            File[] subs = file.listFiles();

            File newBase = dst.toPath().resolve(file.getName()).toFile();

            newBase.mkdirs();

            for (File sub : subs) {
                copyInto(sub, newBase);
            }
        }

    }

    private void copySingleFileToDirectory(File file, File dstDir) throws Exception {

        info("Coping " + file.getName() + " into " + dstDir.getName(),LOG_LEVEL_FILE);
        
        Path dstFile = dstDir.toPath().resolve(file.getName());
        if(!dstFile.toFile().exists()){
            Files.copy(file.toPath(), dstFile, StandardCopyOption.COPY_ATTRIBUTES);
        }
    }

    private List<String> normalize(List<String> ignoreList) {

        List<String> ret = new ArrayList<>();

        for (String item : ignoreList) {

            item = item.replace("\\", "/");

            item = item.toLowerCase();

            if (item.startsWith("./")) {

                item = item.substring(2, item.length());
            }

            ret.add(item);
        }

        return ret;
    }

    private void deleteRepoContents(File dst, List<String> ignoreList) {

        File[] files = dst.listFiles();

        for (File file : files) {

            if (!ignoreList.contains(file.getName().toLowerCase())) {

                deleteAway(file);
            }
        }

    }

    public void deleteAway(File file) {

        if (file.isDirectory()) {

            File[] files = file.listFiles();

            for (File f : files) {
                deleteAway(f);
            }

        }

        file.delete();
    }

    public void deleteContent(File dir, String...ignoreList) {
        File[] subs = dir.listFiles();

        for (File sub : subs) {
            if (!isIgnored(sub.getName(), ignoreList)) {

                deleteAway(sub);
            }
        }
    }

    private boolean isIgnored(String name, String[] ignoreList) {

        for (String item : ignoreList) {
            if (item.compareTo(name) == 0) {
                return true;
            }
        }

        return false;
    }

    public void moveContent(File src, File dst) throws Exception {

        File[] files = src.listFiles();

        for (File sFile : files) {

            File dFile = dst.toPath().resolve(sFile.getName()).toFile();

            if (sFile.isDirectory()) {

                dFile.mkdirs();

                moveContent(sFile, dFile);
            } else {
                copySingleFileToDirectory(sFile, dst);
            }

            deleteAway(sFile);
        }

    }

    public boolean sameLocation(File file1, File file2) {

        String path1 = file1.toPath().toAbsolutePath().normalize().toString();
        String path2 = file2.toPath().toAbsolutePath().normalize().toString();

        return path1.compareTo(path2) == 0;
    }

    public Path resolve(Path base, String... relative) {
        Path ret = base;

        for (String rel : relative) {

            ret = ret.resolve(rel);
        }

        return ret;
    }

    public File resolve(File base, String... relative) {
        return resolve(base.toPath(), relative).toFile();
    }

    public void copyContent(File srcDir, File dstDir, String... ignoreList) throws Exception {

        info("Coping " + srcDir.getName() + " into " + dstDir.getName(),LOG_LEVEL_DIRECTORY);
        
        File[] subs = srcDir.listFiles();

        for (File f : subs) {

            String fname = f.getName();

            if (!isIgnored(fname, ignoreList)) {

                if (f.isDirectory()) {

                    File dstF = dstDir.toPath().resolve(fname).toFile();

                    dstF.mkdirs();

                    copyContent(f, dstF);
                } else {

                    copySingleFileToDirectory(f, dstDir);
                }
            }
        }
    }

    public File getFile(String... path) {
        if (path.length < 1) {
            return new File("");
        }
        Path ret = Paths.get(path[0]);

        for (int i = 1; i < path.length; i++) {
            ret = ret.resolve(path[i]);
        }
        return ret.toFile();
    }
}
