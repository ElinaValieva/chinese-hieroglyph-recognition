package MLP.services.impl;

import MLP.configs.StorageConfig;
import MLP.services.IFileManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * author: ElinaValieva on 15.12.2018
 */
@Service
public class FileManagerService implements IFileManagerService {

    public final Path rootDirectory;

    private final Logger logger = LoggerFactory.getLogger(FileManagerService.class);

    @Autowired
    public FileManagerService(StorageConfig storageConfig) {
        rootDirectory = Paths.get(storageConfig.getLocation());
    }

    @Override
    public void saveFile(MultipartFile file) {
        logger.debug("Try to save file {}", file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            Path path = getFileDirectory(file.getOriginalFilename());
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Save file {}", file.getOriginalFilename());
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public File createFile(String file) throws IOException {
        try {
            Path path = getFileDirectory(file);
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            logger.error("", e);
        }
        return getFileDirectory(file).toFile();
    }

    @Override
    public void saveFile(BufferedImage bufferedImage, String fileName) throws IOException {
        ImageIO.write(bufferedImage, "png", createFile(fileName));
    }


    @Override
    public void deleteAll() {
        logger.debug("Try to delete all files");
        FileSystemUtils.deleteRecursively(rootDirectory.toFile());
        logger.debug("Delete all files");
    }


    @Override
    public void deleteFile(String fileName) throws IOException {
        logger.debug("Try to delete file {}", fileName);
        Path path = getFileDirectory(fileName);
        Files.delete(path);
        logger.debug("Delete file {}", fileName);
    }


    @Override
    public Resource loadFile(String fileName) throws IOException {
        logger.debug("Try to load file {}", fileName);
        Path file = getFileDirectory(fileName);
        org.springframework.core.io.Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            logger.debug("Load resource ...");
            return resource;
        } else throw new IOException("File not found");
    }


    @Override
    public Path getFileDirectory(String fileName) throws IOException {
        if (!Files.exists(rootDirectory)) {
            logger.debug("Try to create directory for files");
            Files.createDirectory(rootDirectory);
            logger.debug("Create directory files");
        }
        return rootDirectory.resolve(fileName);
    }
}
