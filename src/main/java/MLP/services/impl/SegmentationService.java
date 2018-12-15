package MLP.services.impl;

import MLP.models.RImage;
import MLP.services.ISegmentationService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 15.12.2018
 */
@Service
@Log4j
public class SegmentationService implements ISegmentationService {

    @Override
    public List<RImage> segmentation(RImage rImageInput) {
        List<RImage> rImagesResult = new ArrayList<>();
        List<RImage> rImagesVertical = verticalSegmentation(rImageInput);
        List<RImage> rImagesHorizontal = new ArrayList<>();
        rImagesVertical.forEach(rImage ->
                rImagesHorizontal.addAll(horizontalSegmentation(rImage)));

        if (rImagesHorizontal.size() != rImagesVertical.size())
            rImagesResult.forEach(rImage -> segmentation(rImage));

        rImagesResult.addAll(rImagesHorizontal);
        return rImagesResult;
    }

    @Override
    public List<RImage> verticalSegmentation(RImage rImage) {
        int sizeY = rImage.getSizeY();
        int startIndex = 0;
        int cntPixels = rImage.getPixels().size();
        List<RImage> resultList = new ArrayList<>();
        RImage rImageNew = new RImage();
        List<Integer> rImageNewPixels = new ArrayList<>();
        rImageNew.setSizeY(sizeY);
        try {
            while (startIndex < cntPixels) {
                List<Integer> verticalPixels = rImage.getPixels().subList(startIndex, startIndex + sizeY);
                boolean flagForVerticalSegmentation = verticalPixels.contains(1);

                if (flagForVerticalSegmentation) {
                    rImageNewPixels.addAll(verticalPixels);
                    rImageNew.setPixels(rImageNewPixels);
                } else {
                    if (rImageNew.getPixels() != null) {
                        rImageNew.setSizeY(sizeY);
                        rImageNew.setSizeX(rImageNew.getPixels().size() / sizeY);
                        resultList.add(rImageNew.clone());
                        rImageNew = new RImage();
                        rImageNewPixels = new ArrayList<>();
                    }
                }
                startIndex = startIndex + sizeY;
                if (startIndex == cntPixels) {
                    rImageNew.setSizeX(rImageNew.getPixels().size() / sizeY);
                    rImageNew.setSizeY(sizeY);
                    resultList.add(rImageNew);
                }
            }
            return resultList;
        } catch (CloneNotSupportedException e) {
            log.error("", e);
        }
        return resultList;
    }

    @Override
    public List<RImage> horizontalSegmentation(RImage rImage) {
        int sizeX = rImage.getSizeX();
        int sizeY = rImage.getSizeY();
        List<Integer> pixels = new ArrayList<>();

        IntStream.range(0, sizeY)
                .forEach(i -> {
                    int[] p = {i};
                    IntStream.range(0, sizeX)
                            .forEach(j -> {
                                pixels.add(rImage.getPixels().get(p[0]));
                                p[0] = p[0] + sizeY;
                            });
                });
        RImage newRImage = new RImage();
        newRImage.setSizeX(sizeY);
        newRImage.setSizeY(sizeX);
        newRImage.setPixels(pixels);
        return verticalSegmentation(newRImage).stream()
                .map(RImage::changeSize)
                .collect(Collectors.toList());
    }
}
