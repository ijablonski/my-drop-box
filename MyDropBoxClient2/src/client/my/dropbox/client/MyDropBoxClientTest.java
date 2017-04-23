package client.my.dropbox.client;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by my-own on 22.04.17.
 */
public class MyDropBoxClientTest {


    @Test
    public void folderObservingTest() throws IOException {

        MyDropBoxClient myDropBoxClient = new MyDropBoxClient("login", "localFolder");
        myDropBoxClient.observeLocalFolder();


        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void connect() {
        MyDropBoxService myDropBoxService = new MyDropBoxService();

        myDropBoxService.uploadFiles(new HashSet<>());
    }


    @Test
    void blockingQueue() {
        BlockingQueue<Runnable> bq = new ArrayBlockingQueue<>(1000);


        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(2, 3, 10, TimeUnit.MINUTES, bq);


        IntStream.range(0,30).forEach(i -> {

            CompletableFuture.supplyAsync(() -> {

                System.out.println("###### "+executor.getQueue().size());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return  null;},executor);

        });


        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
