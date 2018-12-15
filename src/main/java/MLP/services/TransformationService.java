package MLP.services;

import MLP.models.RImage;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
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
    private IResourcesService fileService;

    @Autowired
    private IFileManagerService fileManagerService;

    public void transform() {
        try {
            fileManagerService.deleteAll();
            RImage rImage = fileService.createRImage(fileService.getFilesPath("tests_Hieroglyph/8.png"), 50, 50);
            List<RImage> rImageList = segmentationService.segmentation(rImage);
            rImageList.forEach(rImageItem -> {
                try {
                    RImage newRImage = fileService.resizeImage(rImageItem);
                    BufferedImage bufferedImage = fileService.convertToImage(newRImage);
                    String fileName = "file" + rImageList.indexOf(rImageItem) + ".png";
                    System.out.println(fileName);
                    fileManagerService.saveFile(bufferedImage, fileName);
                } catch (IOException e) {
                    log.error("", e);
                }
            });
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
