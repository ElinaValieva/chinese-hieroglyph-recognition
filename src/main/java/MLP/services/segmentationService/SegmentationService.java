package MLP.services.segmentationService;

import MLP.models.RImage;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 * author: ElinaValieva on 15.12.2018
 */
@Service
@Log4j
public class SegmentationService implements ISegmentationService {

    List<RImage> rImagesResult;

    @Override
    public List<RImage> segmentation(RImage rImageInput) {
        rImagesResult = new ArrayList<>();
        for (int i = 0; i < rImageInput.getPixels().length; i++) {
            for (int j = 0; j < rImageInput.getPixels()[i].length; j++) {
                System.out.print(rImageInput.getPixels()[i][j] + " ");
            }
            System.out.println();
        }
        List<RImage> rImagesVertical = horizontalSegmentation(rImageInput);
        rImagesResult.addAll(rImagesVertical);
        optimize();
        return rImagesResult;
    }

    @Override
    public List<RImage> verticalSegmentation(RImage rImage) {
        return null;
    }


    @Override
    public List<RImage> horizontalSegmentation(RImage rImage) {
        List<RImage> rImages = new ArrayList<>();
        int[][] pixels = rImage.getPixels();
        int sizeX = rImage.getSizeX();
        int sizeY = rImage.getSizeY();
        int cntStartX = 0;
        int cntEndX = 0;
        boolean flagSegmentation = false;
        for (int i = cntStartX; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                flagSegmentation = false;
                if (pixels[i][j] == 1) {
                    flagSegmentation = true;
                    cntEndX++;
                    break;
                }
            }
            if (!flagSegmentation) {
                RImage rImageSegmented = getImageHorizontal(rImage, cntStartX, cntEndX);
                cntEndX++;
                cntStartX = cntEndX;
                rImages.add(rImageSegmented);
                i = 0;
            }
            if (cntEndX + 1 == sizeX) {
                RImage rImageSegmented = getImageHorizontal(rImage, cntStartX, cntEndX);
                rImages.add(rImageSegmented);
                break;
            }
        }
        return rImages;
    }

    private RImage getImageHorizontal(RImage rImage, int cntStart, int cntEnd) {
        int sizeX = cntEnd - cntStart + 1;
        int sizeY = rImage.getSizeY();
        int[][] pixels = rImage.getPixels();
        int[][] resultPixels = new int[sizeY][sizeX];
        final int[] k = {0};
        IntStream.range(0, sizeY).forEach(j -> {
            IntStream.range(cntStart, cntEnd).forEach(i -> {
                resultPixels[j][k[0]] = pixels[j][i];
                k[0]++;
            });
            k[0] = 0;
        });
        return new RImage(sizeX, sizeY, resultPixels);
    }

    private void optimize() {
        List<RImage> deletingRImage = new ArrayList<>();
        rImagesResult.forEach(rImage -> {
            if (checkEmpty(rImage))
                deletingRImage.add(rImage);
        });
        if (!deletingRImage.isEmpty())
            rImagesResult.removeAll(deletingRImage);
    }

    private boolean checkEmpty(RImage rImage) {
        int[][] pixels = rImage.getPixels();
        if (rImage.getSizeX() < 10 || rImage.getSizeY() < 10)
            return true;
        else {
            boolean flag = true;
            for (int i = 0; i < rImage.getSizeY(); i++) {
                for (int j = 0; j < rImage.getSizeX(); j++) {
                    if (pixels[i][j] == 1) {
                        flag = false;
                        break;
                    }
                }
                if (!flag)
                    break;
            }
            return flag;
        }
    }
}
