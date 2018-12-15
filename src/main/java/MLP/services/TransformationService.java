package MLP.services;

import MLP.funtionsActivation.SigmoidActivation;
import MLP.models.RImage;
import MLP.services.api.IFileManagerService;
import MLP.services.api.IMultiLayerPerceptronService;
import MLP.services.api.IResourcesService;
import MLP.services.api.ISegmentationService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
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

    private static final Integer ITERATIONS = 100;

    private static final Integer CNT_PATTERS = 7;

    private static final Integer SIZE = 50;


    public void transform() {
        RImage rImage = resourcesService.createRImage(resourcesService.getFilesPath("tests_Hieroglyph/8.png"), SIZE, SIZE);
        List<RImage> rImageList = segmentationService.segmentation(rImage);
        training();
        rImageList.forEach(rImageItem -> {
            try {
                RImage newRImage = resourcesService.resizeImage(rImageItem);
                BufferedImage bufferedImage = resourcesService.convertToImage(newRImage);
                String fileName = rImageList.indexOf(rImageItem) + ".png";
                testing(newRImage);
                fileManagerService.saveFile(bufferedImage, fileName);
            } catch (IOException e) {
                log.error("", e);
            }
        });
    }


    public void training() {
        final double[] error = new double[1];
        int[] layers = new int[]{SIZE * SIZE, SIZE, CNT_PATTERS};
        multiLayerPerceptronService.prepareLayers(layers, 0.6, new SigmoidActivation());
        IntStream.range(0, ITERATIONS).forEach(i ->
                IntStream.range(1, CNT_PATTERS).forEach(j -> {
                            RImage rImagePattern = resourcesService.createRImage(resourcesService.getFilesPath("patters_Hieroglyph/" + j + ".png"), 50, 50);
                            Double[] inputs = getList(rImagePattern.getPixels());

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

    public void testing(RImage rImage) {
        Double[] inputs = getList(rImage.getPixels());
        Double[] output = multiLayerPerceptronService.execute(inputs);
        final int[] max = {0};
        IntStream.range(0, CNT_PATTERS).forEach(i -> {
            if (output[i] > output[max[0]])
                max[0] = i;
        });
        System.out.println("Il valore massimo e' " + output[max[0]] + " pattern " + (max[0] + 1));
    }

    private Double[] getList(List<Integer> list) {
        Double[] result = new Double[list.size()];
        IntStream.range(0, list.size()).forEach(i ->
                result[i] = Double.valueOf(list.get(i))
        );
        return result;
    }

}
