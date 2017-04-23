package my.dropbox.api;

import java.util.Set;

/**
 * Created by my-own on 23.04.17.
 */
public interface IMyDropBoxService {

    void uploadNewFiles(String userLogin, Set<String> newFilesPaths);

}
