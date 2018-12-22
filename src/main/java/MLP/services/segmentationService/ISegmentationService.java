package MLP.services.segmentationService;

import MLP.models.RImage;

import java.util.List;

/**
 * author: ElinaValieva on 15.12.2018
 */
public interface ISegmentationService {

    List<RImage> segmentation(RImage rImageInput);

    List<RImage> verticalSegmentation(RImage rImage);

    List<RImage> horizontalSegmentation(RImage rImage);
}
