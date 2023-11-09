package com.unlikepaladin.pfm.runtime;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public abstract class PFMGenerator {
    protected final Path output;
    private final boolean logOrDebug;
    private final Logger logger;
    public static final HashFunction SHA1 = Hashing.sha1();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private boolean running = false;

    protected PFMGenerator(Path output, boolean logOrDebug, Logger logger) {
        this.output = output;
        this.logOrDebug = logOrDebug;
        this.logger = logger;
    }

    protected void createPackIcon() {
        File file = new File(output.toFile(), "icon.png");
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(PFMRuntimeResources.getImageData());
        } catch (IOException e) {
            logger.warn("Failed to create resource icon {}", e.getMessage());
        }
    }

    public boolean isRunning() {
        return running;
    }

    protected void setRunning(boolean running) {
        this.running = running;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getOutput() {
        return output;
    }

    public Path getOrCreateSubDirectory(String name) {
        return PFMRuntimeResources.createDirIfNeeded(output.resolve(name));
    }

    public abstract void  run() throws IOException;

    public void log(String s, Object p0) {
        log(s, p0, "");
    }

    public void log(String s) {
        log(s, "", "");
    }

    public void log(String s, Object p0, Object p1) {
        if (this.logOrDebug)
            logger.info(s, p0, p1);
        else
            logger.debug(s, p0, p1);
    }

    public List<String> hashDirectory(File directory, boolean includeHiddenFiles) throws IOException {
        if (!directory.isDirectory()) {
            logger.error("Not a directory");
            throw new IllegalArgumentException("Not a directory");
        }
        Vector<String> fileStreams = new Vector<>();
        collectFiles(directory, fileStreams, includeHiddenFiles);
        return fileStreams;
    }

    private void collectFiles(File directory, List<String> hashList,
                                     boolean includeHiddenFiles) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));

            for (File file : files) {
                if (includeHiddenFiles || !Files.isHidden(file.toPath())) {
                    if (file.isDirectory()) {
                        collectFiles(file, hashList, includeHiddenFiles);
                    } else {
                        FileInputStream stream = new FileInputStream(file);
                        try {
                            HashCode code = HashCode.fromBytes(stream.readAllBytes());
                            hashList.add(code.toString());
                        } catch (Exception e) {
                            logger.warn("File {} was less than 1 byte or invalid, skipping, {}", file.getName(), e);
                        }
                        stream.close();
                    }
                }
            }
        }
    }
}
