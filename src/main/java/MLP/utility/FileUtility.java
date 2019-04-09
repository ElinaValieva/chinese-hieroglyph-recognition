package MLP.utility;

import MLP.configuration.StorageProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;

/**
 * author: ElinaValieva on 15.12.2018
 * Component for working with files
 */
@Component
public class FileUtility {

    private final Path rootDirectory;

    private final Logger logger = LoggerFactory.getLogger(FileUtility.class);

    @Autowired
    public FileUtility(StorageProperty storageProperty) {
        rootDirectory = Paths.get(storageProperty.location);
    }

    public void createImage(MultipartFile file) {
        logger.debug("Try to save file {}", file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            Path path = getFileDirectory(file.getOriginalFilename());
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Save file {}", file.getOriginalFilename());
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    private File createFile(String fileName) throws IOException {
        try {
            Path path = getFileDirectory(fileName);
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            logger.error("", e);
        }
        return getFileDirectory(fileName).toFile();
    }

    public void createImage(BufferedImage bufferedImage, String fileName) throws IOException {
        ImageIO.write(bufferedImage, "png", createFile(fileName));
    }

    public void saveImage(BufferedImage bufferedImage, String fileName) throws IOException {
        ImageIO.write(bufferedImage, "png", new File(fileName));
    }


    public void deleteAll() {
        logger.debug("Try to delete all files");
        FileSystemUtils.deleteRecursively(rootDirectory.toFile());
        logger.debug("Delete all files");
    }


    public Path getFileDirectory(String fileName) throws IOException {
        if (!Files.exists(rootDirectory)) {
            logger.debug("Try to create directory for files");
            Files.createDirectory(rootDirectory);
            logger.debug("Create directory files");
        }
        return rootDirectory.resolve(fileName);
    }

    public String getFileResourceDirectory(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        return file.getPath();
    }
}
