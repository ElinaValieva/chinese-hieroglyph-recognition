package MLP.services.resourcesService;

import MLP.models.RImage;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * author: ElinaValieva on 15.12.2018
 */
public interface IResourcesService {

    String getFilesPath(String fileName);

    File createFile(String fileName);

    RImage resizeImage(RImage rImage);

    BufferedImage convertToImage(RImage rImage);

    RImage createRImage(String path, boolean flag);

    int[][] createPixelsInput(String path);
}
