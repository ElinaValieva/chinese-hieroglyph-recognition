package MLP.util;

import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j
public class FileUtil {

    private static final String USER_DIR = "user.dir";

    private FileUtil() {
    }

    public static String getFilesPath(String fileName) {
        File file = new File(FileUtil.class.getClassLoader().getResource(fileName).getFile());
        return file.getAbsolutePath();
    }

    public static File createFile(String fileName) throws IOException {
        Path path = Paths.get(System.getProperty(USER_DIR) + fileName);
        return Files.createFile(path).toFile();
    }
}
