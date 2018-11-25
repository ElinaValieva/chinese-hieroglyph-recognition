package MLP;

import MLP.funtionsActivation.SigmoidActivation;
import MLP.util.FileUtil;
import MLP.util.ImageUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class FigureTest {

    private final static Integer BATCH_SIZE = 3000;
    private final static Integer SIZE_HIDDEN_LAYERS = 7;
    private final static Integer SIZE_IMAGE_PIXELS_WIDTH = 7;
    private final static Integer SIZE_IMAGE_PIXELS_HEIGHT = 7;
    private final static Integer SIZE_INPUT = 49;
    private final static Integer SIZE_OUTPUT = 3;

    @Test
    public void recognizeTriangle_1() {
        double[] inputs = ImageUtil.loadImage(FileUtil.getFilesPath("tests/3.png"), SIZE_IMAGE_PIXELS_WIDTH, SIZE_IMAGE_PIXELS_HEIGHT);
        double result = TestPrecision(BATCH_SIZE, inputs);
        Assert.assertTrue(result == 1);
    }

    @Test
    public void recognizeTriangle_2() {
        double[] inputs = ImageUtil.loadImage(FileUtil.getFilesPath("tests/4.png"), SIZE_IMAGE_PIXELS_WIDTH, SIZE_IMAGE_PIXELS_HEIGHT);
        double result = TestPrecision(BATCH_SIZE, inputs);
        Assert.assertTrue(result == 1);
    }

    @Test
    public void recognizeCircle_1() {
        double[] inputs = ImageUtil.loadImage(FileUtil.getFilesPath("tests/5.png"), SIZE_IMAGE_PIXELS_WIDTH, SIZE_IMAGE_PIXELS_HEIGHT);
        double result = TestPrecision(BATCH_SIZE, inputs);
        Assert.assertTrue(result == 2);
    }

    @Test
    public void recognizeSquare_1() {
        double[] inputs = ImageUtil.loadImage(FileUtil.getFilesPath("tests/6.png"), SIZE_IMAGE_PIXELS_WIDTH, SIZE_IMAGE_PIXELS_HEIGHT);
        double result = TestPrecision(BATCH_SIZE, inputs);
        Assert.assertTrue(result == 3);
    }

    @Test
    public void recognizeSquare_2() {
        double[] inputs = ImageUtil.loadImage(FileUtil.getFilesPath("tests/2.png"), SIZE_IMAGE_PIXELS_WIDTH, SIZE_IMAGE_PIXELS_HEIGHT);
        double result = TestPrecision(BATCH_SIZE, inputs);
        Assert.assertTrue(result == 3 || result == 2);
    }

    @Test
    public void recognizeCircle_2() {
        double[] inputs = ImageUtil.loadImage(FileUtil.getFilesPath("tests/1.png"), SIZE_IMAGE_PIXELS_WIDTH, SIZE_IMAGE_PIXELS_HEIGHT);
        double result = TestPrecision(BATCH_SIZE, inputs);
        Assert.assertTrue(result == 2);
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
                                    String pattern = FileUtil.getFilesPath("patterns/" + k + ".png");
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
                        max[0] = 1;
                });

        System.out.println("Recognize value is ' " + output[max[0]] + " pattern " + (max[0] + 1));
        return max[0] + 1;
    }

}
/*
 public static void main(String[] args) throws CloneNotSupportedException {
        RImage rImage = new RImage();
//        rImage.setSizeX(4);
        rImage.setSizeX(3);
        rImage.setSizeY(4);
        List<Integer> pixels = new ArrayList<>(Arrays.asList(1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1));
//        List<Integer> pixels = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1));
        rImage.setPixels(pixels);
        List<RImage> rImages = verticalSegmentation(rImage);
        List<RImage> integerList = horizontalSegmentation(rImage);
        System.out.println(Arrays.toString(integerList.toArray()));
    }
 */