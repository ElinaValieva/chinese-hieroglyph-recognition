package MLP.util;

import MLP.models.RImage;
import lombok.extern.log4j.Log4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Log4j
public class ImageUtil {

    private static final Integer REQUIRED_SIZE = 50;

    private ImageUtil() {
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

    private static List<Integer> createPixelsInput(String path, int sizeWidth, int sizeHeight) {
        File imgLoc = new File(path);
        List<Integer> data = new ArrayList<>();
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
                                            data.add(0);
                                        else
                                            data.add(1);
                                    }));
        } catch (IOException ex) {
            log.error(path + " not loaded");
        }
        return data;
    }

    private static BufferedImage scale(RImage rImage) {
        BufferedImage bufferedImage = convertToImage(rImage);
        return resize(bufferedImage);
    }

    private static BufferedImage resize(BufferedImage bufferedImage) {
        Image tmp = bufferedImage.getScaledInstance(REQUIRED_SIZE, REQUIRED_SIZE, Image.SCALE_SMOOTH);
        BufferedImage resizedBufferedImage = new BufferedImage(REQUIRED_SIZE, REQUIRED_SIZE, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = resizedBufferedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resizedBufferedImage;
    }

    private static BufferedImage convertToImage(RImage rImage) {
        int sizeX = rImage.getSizeX();
        int sizeY = rImage.getSizeY();
        BufferedImage bufferedImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_BYTE_BINARY);

        IntStream.range(0, sizeX).forEach(x ->
                IntStream.range(0, sizeY).forEach(y -> {
                    int rgb = rImage.getPixels().get(x * sizeX + y) == 1 ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    bufferedImage.setRGB(x, y, rgb);
                })
        );

        return bufferedImage;
    }

    public static void saveImage(RImage rImage, String fileName) {
        BufferedImage bufferedImage = scale(rImage);
        try {
            ImageIO.write(bufferedImage, "jpg", FileUtil.createFile(fileName));
        } catch (IOException e) {
            log.error("", e);
        }
    }

    public static RImage createRImage(String path, int x, int y) {
        RImage rImage = new RImage();
        rImage.setSizeX(x);
        rImage.setSizeY(y);
        rImage.setPixels(createPixelsInput(path, x, y));
        return rImage;
    }
}
