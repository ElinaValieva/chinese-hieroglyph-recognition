package MLP.util;

import MLP.models.RImage;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j
public class SegmentationUtil {

    private SegmentationUtil() {
    }

    private static List<RImage> rImagesResult = new ArrayList<>();

    private static void segmentation(RImage rImageInput) {
        List<RImage> rImages = verticalSegmentation(rImageInput);
        rImages.forEach(rImage ->
                rImagesResult.addAll(horizontalSegmentation(rImage)));

        if (rImagesResult.size() != rImages.size())
            rImagesResult.forEach(rImage -> segmentation(rImage));
    }

    private static List<RImage> verticalSegmentation(RImage rImage) {
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

    private static List<RImage> horizontalSegmentation(RImage rImage) {
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

    public static void main(String[] args) {
        RImage rImage = ImageUtil.createRImage(FileUtil.getFilesPath("tests_Hieroglyph/8.png"), 50, 50);
        segmentation(rImage);
        IntStream.range(0, rImagesResult.size())
                .forEach(rImagesResultIndex ->
                        ImageUtil.saveImage(rImagesResult.get(rImagesResultIndex), "/selectedCodes/" + rImagesResultIndex + ".jpg"));
    }
}
