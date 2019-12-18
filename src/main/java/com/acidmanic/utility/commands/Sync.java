package com.acidmanic.utility.commands;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.acidmanic.commandline.commands.CommandBase;
import com.acidmanic.commandline.commands.parameters.ParameterBuilder;
import com.acidmanic.consoletools.terminal.Terminal;
import com.acidmanic.consoletools.terminal.styling.TerminalControlEscapeSequences;
import com.acidmanic.consoletools.terminal.styling.TerminalStyle;
import com.acidmanic.utility.models.PackageDescription;
import com.acidmanic.utility.services.Cache;
import com.acidmanic.utility.services.FilesystemService;
import com.acidmanic.utility.services.NpmPackage;
import com.acidmanic.utility.services.NpmPackageFactory;

public class Sync extends CommandBase {

    private static final String CACHEDIR = "cache";

    @Override
    public void execute() {

        File directory = ((File) getParameterValue("target-directory")).toPath().toAbsolutePath().normalize().toFile();

        File localRepo = getLocalRepoDirectory();

        Cache cache = new Cache(localRepo);

        try {
            updateNode(cache, directory);
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @Override
    protected String getUsageString() {
        return "This command will Read target directorie's node dependencies. if any of theme is not present in the cache, it will rettive them first, then it syncs node_module directory of target directory with cache regarding package dependencies.";
    }

    @Override
    protected void defineParameters(ParameterBuilder builder) {
        super.defineParameters(builder);

        builder.named("target-directory").described("the directory containing packages.json.").mandatory().indexAt(0)
                .ofType(File.class);
    }

    public void updateNode(Cache cache, File directory) throws Exception {
        info("Target directory: " + directory.toString());

        PackageDescription target = Cache.readPackage(directory);

        NpmPackage localPackage = NpmPackageFactory.make(getLocalRepoDirectory());

         info("Listing all packages and dependencies to be delivered to target.");
        
        List<String> allDependencies = resolveDependencies(cache, target, localPackage);

        info("Syncing " + allDependencies.size() + " packages into the target's node_modules directory.");

        allDependencies.forEach((pack) -> syncIntoTarget(directory, pack));

    }

    private List<String> getAllDependencies(Cache cache, PackageDescription target) {

        List<String> ret = new ArrayList<>();

        List<String> passed = new ArrayList<>();

        target.getDependencies().forEach((pack,version)-> addAllDependencies(cache, pack, ret, passed));
        
        return ret;
    }

    private void addAllDependencies(Cache cache, String pack, List<String> ret, List<String> passed) {

        if (!passed.contains(pack)) {

            passed.add(pack);

            List<String> dependencies = cache.getDirectDependecies(pack);

            dependencies.forEach((dep) -> addAllDependencies(cache, dep, ret, passed));

            ret.add(pack);

            info("All dependecies of " + pack + " has been listed");
        }

    }

    private void syncIntoTarget(File targetRoot, String pack) {
        info("Syncing " + pack);

        Path nodeModules = targetRoot.toPath().resolve("node_modules");

        nodeModules.toFile().mkdirs();

        Path localNodeModules = getLocalRepoDirectory().toPath().resolve("node_modules");

        File src = localNodeModules.resolve(pack).toFile();

        File dst = nodeModules.resolve(pack).toFile();

        FilesystemService fs = new FilesystemService();

        src.mkdirs();

        dst.mkdirs();

        try {
            fs.copyContent(src, dst);
        } catch (Exception e) {
            warning("Error while copying '" + src.getName() + ": " + e.getClass().getSimpleName());
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


    @Override
    protected void info(String message) {
        Terminal t = new Terminal(System.out);

        TerminalStyle style = new TerminalStyle(TerminalControlEscapeSequences.BACKGROUND_BLACK,
                TerminalControlEscapeSequences.FOREGROUND_CYAN);
        t.setScreenAttributes(style);

        System.out.println(message);

        t.resetScreenAttributes();
    }

    private List<String> resolveDependencies(Cache cache, PackageDescription target, NpmPackage localPackage) {

        boolean dirty = true;

        List<String> deps = new ArrayList<>();

        while (dirty) {

            cache.updateCacheIndex();

            deps = getAllDependencies(cache, target);

            dirty = false;
            
            for (String dep : deps) {
                if (!cache.isInstalled(dep)) {

                    try {
                        cache.install(dep);

                        dirty = true;
                    } catch (Exception ex) {
                        error("Error fetching dependency: " + dep
                                + ": " + ex.getClass().getSimpleName());
                    }
                }
            }
        }

        return deps;
    }
}
