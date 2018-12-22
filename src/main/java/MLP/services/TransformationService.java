package MLP.services;

import MLP.funtionsActivation.SigmoidActivation;
import MLP.models.ImageSegmentation;
import MLP.models.RImage;
import MLP.services.fileManagerService.IFileManagerService;
import MLP.services.multiLayerPerseptronService.IMultiLayerPerceptronService;
import MLP.services.resourcesService.IResourcesService;
import MLP.services.segmentationService.ISegmentationService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 15.12.2018
 */

@Service
@Log4j
public class TransformationService {

    @Autowired
    private ISegmentationService segmentationService;

    @Autowired
    private IResourcesService resourcesService;

    @Autowired
    private IFileManagerService fileManagerService;

    @Autowired
    private IMultiLayerPerceptronService multiLayerPerceptronService;

    private List<ImageSegmentation> imageSegmentations;

    private static final Integer ITERATIONS = 1000;

    private static final Integer CNT_PATTERS = 10;

    private static final Integer SIZE = 50;


    public void transform(MultipartFile multipartFile) throws IOException {
        fileManagerService.deleteAll();
        fileManagerService.saveFile(multipartFile);
        String path = fileManagerService.getFileDirectory(multipartFile.getOriginalFilename()).toString();
        RImage rImage = resourcesService.createRImage(path, 153, 50, false);
        List<RImage> rImageList = segmentationService.segmentation(rImage);
        training();
        imageSegmentations = new ArrayList<>();
        rImageList.forEach(rImageItem -> {
            try {
                RImage newRImage = resourcesService.resizeImage(rImageItem);
                BufferedImage bufferedImage = resourcesService.convertToImage(newRImage);
                String fileName = rImageList.indexOf(rImageItem) + ".png";
                ImageSegmentation imageSegmentation = new ImageSegmentation();
                imageSegmentation.setPathImage(fileName);
                testing(newRImage, imageSegmentation);
                fileManagerService.saveFile(bufferedImage, fileName);
            } catch (IOException e) {
                log.error("", e);
            }
        });
        imageSegmentations.forEach(System.out::println);
    }


    public void training() {
        final double[] error = new double[1];
        int[] layers = new int[]{SIZE * SIZE, SIZE, CNT_PATTERS};
        multiLayerPerceptronService.prepareLayers(layers, 0.6, new SigmoidActivation());
        IntStream.range(0, ITERATIONS).forEach(i ->
                IntStream.range(1, CNT_PATTERS).forEach(j -> {
                            RImage rImagePattern = resourcesService.createRImage(resourcesService.getFilesPath("patters_Hieroglyph/" + j + ".png"), 50, 50, true);
                            Double[] inputs = getList(rImagePattern);

                            if (inputs == null)
                                System.out.println("Cant find " + j);

                            Double[] output = new Double[CNT_PATTERS];
                            for (int l = 0; l < CNT_PATTERS; l++)
                                output[l] = 0.0;
                            output[j - 1] = 1.0;
                            error[0] = multiLayerPerceptronService.backPropagate(inputs, output);
                            System.out.println("Error is " + error[0] + " (" + i + " " + " " + j + " )");
                        }
                )
        );
    }

    public void testing(RImage rImage, ImageSegmentation imageSegmentation) {
        Double[] inputs = getList(rImage);
        Double[] output = multiLayerPerceptronService.execute(inputs);
        final int[] max = {0};
        IntStream.range(0, CNT_PATTERS).forEach(i -> {
            if (output[i] > output[max[0]])
                max[0] = i;
        });
        System.out.println("Il valore massimo e' " + output[max[0]] + " pattern " + (max[0] + 1));
        imageSegmentation.setCodeResult(max[0] + 1);
        imageSegmentation.setError(output[max[0]]);
        imageSegmentations.add(imageSegmentation);
    }

    private Double[] getList(RImage rImage) {
        Double[] result = new Double[rImage.getSizeY() * rImage.getSizeX()];
        final int[] count = {0};
        IntStream.range(0, rImage.getPixels().length).forEach(i ->
                IntStream.range(0, rImage.getPixels()[i].length).forEach(j -> {
                    result[count[0]] = Double.valueOf(rImage.getPixels()[i][j]);
                    count[0]++;
                })
        );
        return result;
    }

}
