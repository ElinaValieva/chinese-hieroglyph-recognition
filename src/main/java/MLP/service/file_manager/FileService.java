package MLP.service.file_manager;

import MLP.exception.RecognitionException;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * author: ElinaValieva on 28.04.2019
 */
public interface FileService {

    String createImage(MultipartFile file) throws RecognitionException;

    String createImage(BufferedImage bufferedImage, String fileName) throws IOException;

    void saveImage(BufferedImage bufferedImage, String fileName) throws IOException;

    void deleteAll();

    String getPathDirectory(String fileName) throws IOException;

}
