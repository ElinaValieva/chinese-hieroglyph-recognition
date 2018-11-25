package MLP.util;

import lombok.extern.log4j.Log4j;

import java.io.File;

@Log4j
public class FileUtil {

    private FileUtil() {
    }

    public static String getFilesPath(String fileName) {
        File file = new File(FileUtil.class.getClassLoader().getResource(fileName).getFile());
        return file.getAbsolutePath();
    }
}
