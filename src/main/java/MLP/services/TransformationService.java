package MLP.services;

import MLP.models.RImage;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * author: ElinaValieva on 15.12.2018
 */

@Service
@Log4j
public class TransformationService {

    @Autowired
    private ISegmentationService segmentationService;

    @Autowired
    private IFileService fileService;

    public void transform() {
        RImage rImage = fileService.createRImage(fileService.getFilesPath("tests_Hieroglyph/8.png"), 50, 50);
        List<RImage> rImageList = segmentationService.segmentation(rImage);
        final int[] index = {0};
        rImageList.forEach(rImageItem -> {
            RImage newRImage = fileService.resizeImage(rImageItem);
            BufferedImage bufferedImage = fileService.convertToImage(newRImage);
            String fileName = "C:/Users/Elina/Desktop/file" + index[0] + ".png";
            try {
                ImageIO.write(bufferedImage, "png", new File(fileName));
            } catch (IOException e) {
                log.error("", e);
            }
            index[0]++;
        });
    }
}
