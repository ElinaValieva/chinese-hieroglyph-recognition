package MLP.util;

import lombok.extern.log4j.Log4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

@Log4j
public class ImageRecognitionUtil {

    private ImageRecognitionUtil() {
    }

    public static double[] loadImage(String path, int sizeWidth, int sizeHeight) {
        File imgLoc = new File(path);
        double[] data = new double[sizeWidth * sizeHeight];
        try {
            BufferedImage img = ImageIO.read(imgLoc);
            BufferedImage bi = new BufferedImage(
                    sizeWidth,
                    sizeHeight,
                    BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = bi.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();

            IntStream.range(0, sizeWidth)
                    .forEach(i ->
                            IntStream.range(0, sizeHeight)
                                    .forEach(j -> {
                                        int[] d = new int[3];
                                        bi.getRaster().getPixel(i, j, d);
                                        if (d[0] > 128)
                                            data[i * sizeWidth + j] = 0.0;
                                        else data[i * sizeWidth + j] = 1.0;
                                    }));
        } catch (IOException ex) {
            log.error(path + " not loaded");
        }
        return data;
    }
}
