package MLP.models;

import java.util.stream.IntStream;

public class Layer {
    public Neuron neurons[];
    public int length;

    public Layer(int length, int prev) {
        this.length = length;
        neurons = new Neuron[length];

        IntStream.range(0, length)
                .forEach(i ->
                        neurons[i] = new Neuron(prev));
    }
}
