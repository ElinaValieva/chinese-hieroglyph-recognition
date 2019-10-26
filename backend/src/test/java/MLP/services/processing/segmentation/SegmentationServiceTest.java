package MLP.services.processing.segmentation;

import MLP.exception.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * author: ElinaValieva on 06.04.2019
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SegmentationServiceTest {

    @Autowired
    private SegmentationService segmentationService;

    @Test
    public void simpleSegmentation() throws RecognitionException {
        segmentationService.segment("C:/Users/Elina/Desktop/4.png");
    }
}