package client.my.dropbox.client;

import com.caucho.hessian.client.HessianProxyFactory;
import my.dropbox.api.IMyDropBoxService;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by my-own on 23.04.17.
 */
public class MyDropBoxService {
    private IMyDropBoxService myDropBoxService;

    public MyDropBoxService() {
        HessianProxyFactory factory = new HessianProxyFactory();
        try {
            myDropBoxService = (IMyDropBoxService) factory.create(IMyDropBoxService.class, "http://localhost:8080/hessian");
        } catch (MalformedURLException e) {
            e.getStackTrace();
        }
    }

    public void uploadFiles(Set<String> set) {
        myDropBoxService.uploadNewFiles("test", set);
    }
}
