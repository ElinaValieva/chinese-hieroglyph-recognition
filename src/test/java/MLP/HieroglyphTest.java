package MLP;

import MLP.funtionsActivation.SigmoidActivation;
import MLP.util.FileUtil;
import MLP.util.ImageUtil;
import org.junit.Test;

import java.util.stream.IntStream;

public class HieroglyphTest {

    private final static Integer BATCH_SIZE = 1000;
    private final static Integer SIZE_HIDDEN_LAYERS = 10;
    private final static Integer SIZE_IMAGE_PIXELS_WIDTH = 50;
    private final static Integer SIZE_IMAGE_PIXELS_HEIGHT = 50;
    private final static Integer SIZE_INPUT = 2500;
    private final static Integer SIZE_OUTPUT = 7;

    @Test
    public void recognizeHieroglyph() {
        double[] inputs = ImageUtil.loadImage(FileUtil.getFilesPath("tests_Hieroglyph/7.png"), SIZE_IMAGE_PIXELS_WIDTH, SIZE_IMAGE_PIXELS_HEIGHT);
        double result = TestPrecision(BATCH_SIZE, inputs);
        System.out.println(result);
    }


    public static double TestPrecision(int iteration, double[] inputForTest) {
        final double[] ERROR = new double[1];
        int[] layers = new int[]{SIZE_INPUT, SIZE_HIDDEN_LAYERS, SIZE_OUTPUT};
        MultiLayerPerceptron multiLayerPerception = new MultiLayerPerceptron(layers, 0.6, new SigmoidActivation());


        Long startTime = System.nanoTime();
        IntStream.range(0, iteration)
                .forEach(i ->
                        IntStream.range(1, SIZE_OUTPUT)
                                .forEach(k -> {
                                    String pattern = FileUtil.getFilesPath("patters_Hieroglyph/" + k + ".png");
                                    double[] inputs = ImageUtil.loadImage(pattern, SIZE_IMAGE_PIXELS_WIDTH, SIZE_IMAGE_PIXELS_HEIGHT);
                                    if (inputs == null)
                                        System.out.println("Cant find " + pattern);
                                    double[] output = new double[SIZE_OUTPUT];
                                    IntStream.range(0, SIZE_OUTPUT)
                                            .forEach(ind -> output[ind] = 0.0);
                                    output[k - 1] = 1.0;
                                    ERROR[0] = multiLayerPerception.backPropagate(inputs, output);
                                    System.out.println("Error is " + ERROR[0] + " (" + i + " " + " " + k + " )");
                                })
                );
        Long timeLearning = System.nanoTime() - startTime;
        System.out.println("Time learning: " + timeLearning);

        startTime = System.nanoTime();
        double[] output = multiLayerPerception.execute(inputForTest);
        timeLearning = System.nanoTime() - startTime;
        System.out.println("Time testing: " + timeLearning);
        final int[] max = {0};
        IntStream.range(0, SIZE_OUTPUT)
                .forEach(i -> {
                    if (output[i] > output[max[0]])
                        max[0] = i;
                });

        System.out.println(output[0] + " " + output[1] + " " + output[2] + " " + output[3] + " " + output[4] + " " + output[5] + " " + output[6]);
        System.out.println("Recognize value is ' " + output[max[0]] + " pattern " + (max[0] + 1));
        return max[0] + 1;
    }
}
