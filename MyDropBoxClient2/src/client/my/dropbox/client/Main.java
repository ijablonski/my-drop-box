package client.my.dropbox.client;

import java.util.HashSet;

/**
 * Created by my-own on 22.04.17.
 */
public class Main {

    public static void main(String[] args) {
        MyDropBoxService myDropBoxService = new MyDropBoxService();

        myDropBoxService.uploadFiles(new HashSet<>());
    }
}
