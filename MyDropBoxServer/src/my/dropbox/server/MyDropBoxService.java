package my.dropbox.server;


import my.dropbox.api.IMyDropBoxService;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by my-own on 23.04.17.
 */
public class MyDropBoxService implements IMyDropBoxService {

    public static final String DRIVE_NAME_PREFIX = "drive_";

    private ScheduledExecutorService monitoringExecutorService;

    private List<Drive> drives;

    public MyDropBoxService() {
        drives = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> drives.add(new Drive(DRIVE_NAME_PREFIX + String.valueOf(i))));

        monitoringExecutorService = Executors.newSingleThreadScheduledExecutor();
        monitoringExecutorService.scheduleAtFixedRate(monitoringRunnable, 2, 2, TimeUnit.SECONDS);
    }

    @Override
    public void uploadNewFiles(String userLogin, Set<String> newFilesPaths) {

        newFilesPaths.forEach(newFilesPath ->
                drives.stream().min(Comparator.comparingInt(Drive::getQueueSize)).get().uploadFile(newFilesPath));


    }

    private Runnable monitoringRunnable = () -> {
        StringBuilder monitoring = new StringBuilder("----- MONITORING -----\n");
        drives.stream().map(driver -> driver.toString()).forEach(driverStr -> monitoring.append(driverStr).append('\n'));
        monitoring.append("----------------------\n");

        System.out.println(monitoring.toString());
    };

}
