package com.unlikepaladin.pfm.runtime;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class PFMProvider {
    private final PFMGenerator parent;
    private final String providerName;
    private final Stopwatch stopwatch;
    private CountDownLatch countDownLatch;
    private BlockingQueue<Map.Entry<Path, String>> writeQueue;
    private static final Map.Entry<Path, String> DONE_SIGNAL = Map.entry(PFMFileUtil.getGamePath(), "DONE");
    private ExecutorService writerExecutor;

    public PFMProvider(PFMGenerator parent, String providerName) {
        this.parent = parent;
        this.providerName = providerName;
        this.stopwatch = Stopwatch.createUnstarted();
    }

    protected void startProviderRun() {
        parent.log("Starting provider: {}", providerName);
        this.stopwatch.start();
    }

    protected void endProviderRun() {
        stopwatch.stop();
        String notification = String.format("%s finished after %s ms", providerName, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        parent.log(notification);
        parent.setNotification(notification);
        parent.incrementCount();
    }


    protected <T> void generateAndQueueJsons(Path root, Map<T, ? extends Supplier<JsonElement>> jsons,
                                           BiFunction<Path, T, Path> locator,
                                           BlockingQueue<Map.Entry<Path, String>> queue) {
        jsons.forEach((object, supplier) -> {
            if (supplier != null && supplier.get() != null) {
                Path filePath = locator.apply(root, object);
                String jsonContent = PFMDataGenerator.GSON.toJson(supplier.get());
                enqueueJsonWrite(queue, filePath, jsonContent);
            }
        });
    }

    protected void enqueueJsonWrite(BlockingQueue<Map.Entry<Path, String>> queue, Path path, String content) {
        try {
            queue.put(Map.entry(path, content));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    protected void enqueueJsonWrite(BlockingQueue<Map.Entry<Path, String>> queue, Path path, JsonElement element) {
        try {
            queue.put(Map.entry(path, PFMDataGenerator.GSON.toJson(element)));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected BlockingQueue<Map.Entry<Path, String>> getWriteQueue() {
        return writeQueue;
    }

    public abstract void run();

    public void createWriter() {
        this.countDownLatch = new CountDownLatch(1);
        this.writeQueue = new LinkedBlockingQueue<>();
        this.writerExecutor = Executors.newSingleThreadExecutor();
        this.writerExecutor.submit(() -> {
            try {
                while (true) {
                    Map.Entry<Path, String> entry = writeQueue.take(); // Blocks until an entry is available
                    if (entry == DONE_SIGNAL) break; // Stop signal

                    Path filePath = entry.getKey();
                    String content = entry.getValue();

                    try {
                        if (!Files.exists(filePath.getParent())) {
                            Files.createDirectories(filePath.getParent());
                        }
                        Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    } catch (Exception e) {
                        getParent().getLogger().error("Couldn't save {}", filePath, e);
                    }
                }
            } catch (InterruptedException ignored) {
            } finally {
                this.countDownLatch.countDown(); // Signal completion to the main thread
            }
        });
    }

    public void waitForWrite() {
        writeQueue.add(DONE_SIGNAL);
        writerExecutor.shutdown();

        try {
            this.countDownLatch.await();
        } catch (InterruptedException e) {
            parent.getLogger().info("Interrupted while waiting for write to finish {}", e.getMessage());
        }
    }

    public PFMGenerator getParent() {
        return parent;
    }
}
