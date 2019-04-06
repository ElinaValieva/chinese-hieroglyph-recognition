package MLP.services.recognition.resourcesService;

import MLP.services.recognition.models.RImage;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 15.12.2018
 */
@Service
@Log4j
public class ResourcesService implements IResourcesService {

    private static final String USER_DIR = "user.dir";

    @Override
    public String getFilesPath(String fileName) {
        File file = new File(ResourcesService.class.getClassLoader().getResource(fileName).getFile());
        return file.getAbsolutePath();

    }

    @Override
    public File createFile(String fileName) {
        Path path = Paths.get(System.getProperty(USER_DIR) + fileName);
        File file = null;
        try {
            file = Files.createFile(path).toFile();
        } catch (IOException e) {
            log.error("", e);
        }
        return file;
    }

    @Override
    public RImage resizeImage(RImage rImage) {
        int size = 50;
        if (rImage.getSizeX() != size || rImage.getSizeY() != size) {
            int[][] pix = new int[size][size];
            IntStream.range(0, size)
                    .forEach(i -> Arrays.fill(pix[i], 0));

            IntStream.range(0, rImage.getSizeY()).forEach(i ->
                    IntStream.range(0, rImage.getSizeX()).forEach(j ->
                            pix[i][j] = rImage.getPixels()[i][j]
                    ));

            return new RImage(size, size, pix);
        } else return rImage;
    }

    @Override
    public BufferedImage convertToImage(RImage rImage) {
        int sizeX = rImage.getSizeX();
        int sizeY = rImage.getSizeY();
        BufferedImage bufferedImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_BYTE_BINARY);

        IntStream.range(0, sizeX).forEach(x ->
                IntStream.range(0, sizeY).forEach(y -> {
                    int rgb = rImage.getPixels()[x][y] == 1 ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    bufferedImage.setRGB(y, x, rgb);
                })
        );

        return bufferedImage;
    }

    public RImage createRImage(String path, boolean flag) {
        int[][] pixels = createPixelsInput(path);
        return new RImage(pixels[0].length, pixels.length, pixels);
    }


    public int[][] createPixelsInput(String path) {
        File imgLoc = new File(path);
        int[][] data = null;
        try {
            BufferedImage img = ImageIO.read(imgLoc);
            int sizeWidth = img.getWidth();
            int sizeHeight = img.getHeight();
            BufferedImage bi = new BufferedImage(
                    sizeWidth,
                    sizeHeight,
                    BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = bi.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            data = new int[sizeHeight][sizeWidth];
            int[][] finalData = data;
            IntStream.range(0, sizeWidth)
                    .forEach(i ->
                            IntStream.range(0, sizeHeight)
                                    .forEach(j -> {
                                        int[] d = new int[3];
                                        bi.getRaster().getPixel(i, j, d);
                                        if (d[0] > 128)
                                            finalData[j][i] = 0;
                                        else
                                            finalData[j][i] = 1;
                                    }));
        } catch (IOException ex) {
            log.error(path + " not loaded");
        }
        return data;
    }


}
