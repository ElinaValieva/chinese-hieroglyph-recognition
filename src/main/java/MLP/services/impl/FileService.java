package MLP.services.impl;

import MLP.models.RImage;
import MLP.services.IFileService;
import ch.qos.logback.core.util.FileUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 15.12.2018
 */
@Service
@Log4j
public class FileService implements IFileService {

    private static final String USER_DIR = "user.dir";

    @Override
    public String getFilesPath(String fileName) {
        File file = new File(FileUtil.class.getClassLoader().getResource(fileName).getFile());
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
        int size = (rImage.getSizeX() > rImage.getSizeY()) ? rImage.getSizeX() : rImage.getSizeY();
        int sizeMin = (rImage.getSizeX() > rImage.getSizeY()) ? rImage.getSizeY() : rImage.getSizeX();
        int[][] pix = new int[size][size];
        List<Integer> outPix = new ArrayList<>();
        IntStream.range(0, size)
                .forEach(i -> Arrays.fill(pix[i], 0));
        IntStream.range(0, rImage.getSizeY()).forEach(i ->
                IntStream.range(0, rImage.getSizeX()).forEach(j ->
                        pix[i][j] = rImage.getPixels().get(i * sizeMin + j)
                ));
        for (int i = 0; i < pix.length; i++) {
            for (int j = 0; j < pix[i].length; j++) {
                System.out.print(pix[i][j] + " ");
            }
            System.out.println();
        }
        IntStream.range(0, size).forEach(i ->
                IntStream.range(0, size).forEach(j ->
                        outPix.add(pix[i][j])
                ));

        return new RImage(size, size, outPix);
    }

    @Override
    public BufferedImage convertToImage(RImage rImage) {
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

    public RImage createRImage(String path, int x, int y) {
        RImage rImage = new RImage();
        List<Integer> pixels = createPixelsInput(path, x, y);
        if (x != y)
            rImage = resizeImage(new RImage(x, y, pixels));
        int size = (x > y) ? x : y;
        rImage.setSizeX(size);
        rImage.setSizeY(size);
        rImage.setPixels(pixels);
        return rImage;
    }

    public List<Integer> createPixelsInput(String path, int sizeWidth, int sizeHeight) {
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


}
