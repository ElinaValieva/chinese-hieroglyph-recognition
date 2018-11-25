package MLP.util;

import MLP.models.RImage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SegmentationUtil {

    private SegmentationUtil() {
    }

    public static void segmentation(RImage rImageInput) throws CloneNotSupportedException {
        verticalSegmentation(rImageInput);
        horizontalSegmentation(rImageInput);
    }

    private static List<RImage> verticalSegmentation(RImage rImage) throws CloneNotSupportedException {
        int sizeY = rImage.getSizeY();
        int startIndex = 0;
        int cntPixels = rImage.getPixels().size();
        List<RImage> resultList = new ArrayList<>();
        RImage rImageNew = new RImage();
        List<Integer> rImageNewPixels = new ArrayList<>();
        rImageNew.setSizeY(sizeY);
        while (startIndex < cntPixels) {
            List<Integer> verticalPixels = rImage.getPixels().subList(startIndex, startIndex + sizeY);
            boolean flagForVerticalSegmentation = verticalPixels.contains(1);

            if (flagForVerticalSegmentation) {
                rImageNewPixels.addAll(verticalPixels);
                rImageNew.setPixels(rImageNewPixels);
            } else {
                rImageNew.setSizeY(sizeY);
                rImageNew.setSizeX(rImageNew.getPixels().size() / sizeY);
                resultList.add(rImageNew.clone());
                rImageNew = new RImage();
                rImageNewPixels = new ArrayList<>();
            }
            startIndex = startIndex + sizeY;
            if (startIndex == cntPixels) {
                rImageNew.setSizeX(rImageNew.getPixels().size() / sizeY);
                rImageNew.setSizeY(sizeY);
                resultList.add(rImageNew);
            }
        }
        return resultList;
    }

    private static List<RImage> horizontalSegmentation(RImage rImage) throws CloneNotSupportedException {
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
        rImage.setSizeX(sizeY);
        rImage.setSizeY(sizeX);
        rImage.setPixels(pixels);
        return verticalSegmentation(rImage).stream()
                .map(RImage::changeSize)
                .collect(Collectors.toList());
    }
}
