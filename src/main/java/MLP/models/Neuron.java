package MLP.models;
import java.util.stream.IntStream;

public class Neuron {
    public double value;
    public double[] weights;
    public double bias;
    public double delta;

    public Neuron(int prevLayerSize) {
        weights = new double[prevLayerSize];
        bias = Math.random() / 10000000000000.0;
        delta = Math.random() / 10000000000000.0;
        value = Math.random() / 10000000000000.0;

        IntStream.range(0, weights.length)
                .forEach(i ->
                        weights[i] = Math.random() / 10000000000000.0);
    }
}
