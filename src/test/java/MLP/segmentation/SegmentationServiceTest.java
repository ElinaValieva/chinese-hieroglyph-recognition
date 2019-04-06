package MLP.segmentation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author: ElinaValieva on 06.04.2019
 */
public class SegmentationServiceTest {

    @Test
    public void simpleSegmentation() {
        SegmentationService segmentationService = new SegmentationService();
        segmentationService.simpleSegmentation();
    }
}