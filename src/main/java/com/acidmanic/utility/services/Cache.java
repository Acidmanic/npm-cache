/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acidmanic.utility.services;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import com.acidmanic.utility.models.PackageDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author 80116
 */
public class Cache {

    private final File directory;
    private CommandWrapperLogger logger = new CommandWrapperLogger();
    private List<NpmPackage> packages;

    public Cache(File directory) {
        this.directory = directory;

        updateCacheIndex();
    }

    public File getDirectory() {
        return directory;
    }

    private NpmPackage find(List<NpmPackage> packages, String name) {
        String lowname = name.toLowerCase();
        for (NpmPackage p : packages) {
            if (p.getName().toLowerCase().compareTo(lowname) == 0) {
                return p;
            }
        }

        return null;
    }

    public List<String> getDirectDependecies(String pack) {

        logger.info("Getting dependecies of " + pack);

        String[] key = pack.split("/");

        if (key.length < 1) {
            return new ArrayList<>();
        }

        List<NpmPackage> searchingPackages = this.packages;

        NpmPackage parent = find(searchingPackages, key[0]);

        if (parent == null) {
            return new ArrayList<>();
        }

        if (key.length == 1) {
            return parent.externalDependencies();
        }

        if (!(parent instanceof SuperPackage)) {
            return new ArrayList<>();
        }

        List<NpmPackage> subs = ((SuperPackage) parent).getSubPackages();

        NpmPackage subPackage = find(subs, pack);

        if (subPackage == null) {
            //TODO: handle error
            return new ArrayList<>();
        }
        return subPackage.externalDependencies();

    }

    public CommandWrapperLogger getLogger() {
        return logger;
    }

    public void setLogger(CommandWrapperLogger logger) {
        this.logger = logger;
    }

    public static PackageDescription readPackage(File directory) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        File packageJsonFile = directory.toPath().resolve("package.json").toFile();

        if (packageJsonFile.exists()) {
            return mapper.readValue(packageJsonFile, com.acidmanic.utility.models.PackageDescription.class);
        }

        return new com.acidmanic.utility.models.PackageDescription();
    }

    private void checkAddDependency(List<String> result, String dependency, String pack) {
        if (dependency.toLowerCase().startsWith("file")) {
            return;
        }
        if (dependency.toLowerCase().compareTo(pack.toLowerCase()) == 0) {
            return;
        }
        result.add(dependency);
    }

    private List<NpmPackage> listAllPackages(File directory) {

        List<NpmPackage> ret = new ArrayList<>();

        File[] files = directory.toPath().resolve("node_modules").toFile().listFiles();

        if (files != null) {
            for (File f : files) {
                NpmPackage pack = NpmPackageFactory.make(f);

                if (pack != null) {
                    ret.add(pack);
                }
            }
        }

        return ret;
    }

    public final void updateCacheIndex() {
        this.packages = listAllPackages(directory);
    }

    public void install(String name) throws Exception {
        this.install(name, null);
    }

    public void install(String name, String version) throws Exception {

        String packageParameter = name;
        if (version != null && version.length() > 0) {
            packageParameter += "@" + version;
        }

        logger.info("fetching package: " + packageParameter);

        ProcessBuilder pb = new ProcessBuilder(
                "cmd.exe",
                "/c",
                "npm",
                "install",
                packageParameter,
                "--save");

        pb.directory(this.directory);
        pb.redirectErrorStream(true);

        Process pr = pb.start();

        pr.waitFor();

    }

    public boolean isInstalled(String pack) {

        String lowPack = pack.toLowerCase();

        for (NpmPackage pk : this.packages) {

            for (NpmPackage p : pk.listAllPackages()) {
                if (p.getName().compareTo(lowPack) == 0) {
                    return true;
                }
            }

        }
        return false;
    }
}
