package MLP.services.fileManagerService;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * author: ElinaValieva on 15.12.2018
 */
public interface IFileManagerService {

    void saveFile(MultipartFile file);

    File createFile(String fileName) throws IOException;

    void saveFile(BufferedImage bufferedImage, String fileName) throws IOException;

    void deleteAll() throws IOException;

    void deleteFile(String fileName) throws IOException;

    Resource loadFile(String fileName) throws IOException;

    Path getFileDirectory(String fileName) throws IOException;

    String getFileResourceDirectory(String fileName);
}
