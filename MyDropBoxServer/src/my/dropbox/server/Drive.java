package my.dropbox.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by my-own on 23.04.17.
 */
class Drive {

    private Path drivePath;
    /**
     * Pozwala obserwować kolejkę zadań do wykonania.
     */
    private ThreadPoolExecutor threadPoolExecutor;

    public Drive(String driverName) {
        drivePath = Paths.get(driverName);

        if (!Files.exists(drivePath)) {
            try {
                Files.createDirectory(drivePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        threadPoolExecutor = new ThreadPoolExecutor(2, 3, 5, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(1000));
    }

    public int getQueueSize() {
        return threadPoolExecutor.getQueue().size();
    }

    public Future<Void> uploadFile(String filePath) {
        System.out.println(MessageFormat.format("{0}: uploading file: {1}", drivePath.toString(), filePath));
        return threadPoolExecutor.submit(() -> {
            Thread.sleep(Math.round(5000 + 10000 * Math.random()));
            System.out.println(MessageFormat.format("{0}: UPLOADED file: {1}", drivePath.toString(), filePath));
            return null;
        });

    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} - files queue: {1}", drivePath.toString(), getQueueSize());
    }

}
