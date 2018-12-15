package MLP.services;

import MLP.models.RImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * author: ElinaValieva on 15.12.2018
 */
public interface IFileService {

    String getFilesPath(String fileName);

    File createFile(String fileName);

    RImage resizeImage(RImage rImage);

    BufferedImage convertToImage(RImage rImage);

    RImage createRImage(String path, int x, int y);

    List<Integer> createPixelsInput(String path, int sizeWidth, int sizeHeight);
}
