package client.my.dropbox.client;

import my.dropbox.api.IMyDropBoxService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by my-own on 22.04.17.
 */
public class MyDropBoxClient {

    private String clientLogin;

    private Path localFolder;

    private Path tmpFile;

    private ScheduledExecutorService scheduler;

    private MyDropBoxService myDropBoxService;

    public MyDropBoxClient(String clientLogin, String localFolder) {

        this.myDropBoxService = new MyDropBoxService();

        this.clientLogin = clientLogin;
        this.localFolder = Paths.get(localFolder);
        tmpFile = Paths.get(localFolder + "/.tmp");
        try {
            if (!Files.exists(this.localFolder)) {
                Files.createDirectory(this.localFolder);
            }
            if (!Files.exists(tmpFile)) {
                Files.createFile(Paths.get(localFolder + "/.tmp"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        scheduler = Executors.newScheduledThreadPool(3);

        System.out.println("Client: " + clientLogin + ", folder " + this.localFolder.toString());

    }

    public void observeLocalFolder() {
        scheduler.scheduleAtFixedRate(() -> CompletableFuture.supplyAsync(newFilesSupplier).thenApply(registerNewFiles), 2, 2, TimeUnit.SECONDS);
    }

    private Function<Set<Path>, Void> registerNewFiles = newFilesNames -> {
        try {
            Set<String> newFilePathsStr = newFilesNames.stream().map(filePath -> filePath.toAbsolutePath().toString()).collect(Collectors.toSet());
            Files.write(tmpFile, newFilePathsStr, StandardOpenOption.APPEND);

            myDropBoxService.uploadFiles(newFilePathsStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    };

    private Supplier<Set<Path>> newFilesSupplier = () -> {

        try {
            Set<String> syncFiles = Files.lines(tmpFile).collect(Collectors.toSet());
            Set<Path> newFilesPaths = Files.walk(localFolder)
                    .filter(filePath -> !filePath.equals(localFolder) && !filePath.equals(tmpFile))
                    .filter(filePath -> !syncFiles.contains(filePath.toAbsolutePath().toString()))
                    .collect(Collectors.toSet());

            System.out.println("New files detected: ");
            newFilesPaths.forEach(System.out::println);


            return newFilesPaths;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    };


}
