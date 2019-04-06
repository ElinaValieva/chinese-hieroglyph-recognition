package MLP.services.recognition.models;
import java.util.stream.IntStream;

public class Neuron {

    public double value;
    public double[] weights;
    public double bias;
    public double delta;
    public static final double DELTA = 10000000000000.0;

    public Neuron(int prevLayerSize) {
        weights = new double[prevLayerSize];
        bias = Math.random() / DELTA;
        delta = Math.random() / DELTA;
        value = Math.random() / DELTA;

        IntStream.range(0, weights.length)
                .forEach(i ->
                        weights[i] = Math.random() / DELTA);
    }
}
