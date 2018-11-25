package MLP;

import MLP.funtionsActivation.FunctionActivation;
import MLP.models.Layer;

import java.util.stream.IntStream;

public class MultiLayerPerceptron {

    protected double fLearningRate;
    protected Layer[] fLayers;
    protected FunctionActivation fFunctionActivation;


    public MultiLayerPerceptron(int[] layers, double learningRate, FunctionActivation fFunctionActivation) {
        fLearningRate = learningRate;
        this.fFunctionActivation = fFunctionActivation;

        fLayers = new Layer[layers.length];

        IntStream.range(0, layers.length)
                .forEach(i -> {
                    if (i != 0)
                        fLayers[i] = new Layer(layers[i], layers[i - 1]);
                    else
                        fLayers[i] = new Layer(layers[i], 0);
                });
    }


    public double[] execute(double[] input) {

        final double[] newValue = new double[1];

        double[] output = new double[fLayers[fLayers.length - 1].length];

        IntStream.range(0, fLayers[0].length)
                .forEach(i ->
                        fLayers[0].neurons[i].value = input[i]
                );


        IntStream.range(1, fLayers.length)
                .forEach(k ->
                        IntStream.range(0, fLayers[k].length)
                                .forEach(i -> {
                                    newValue[0] = 0.0;
                                    IntStream.range(0, fLayers[k - 1].length)
                                            .forEach(j ->
                                                    newValue[0] = newValue[0] + fLayers[k].neurons[i].weights[j] * fLayers[k - 1].neurons[j].value
                                            );
                                    newValue[0] += fLayers[k].neurons[i].bias;
                                    fLayers[k].neurons[i].value = fFunctionActivation.evaluate(newValue[0]);
                                })
                );


        IntStream.range(0, fLayers[fLayers.length - 1].length)
                .forEach(i ->
                        output[i] = fLayers[fLayers.length - 1].neurons[i].value
                );

        return output;
    }


    public double backPropagate(double[] input, double[] output) {
        double[] newOutput = execute(input);
        final double[] error = new double[1];

        IntStream.range(0, fLayers[fLayers.length - 1].length)
                .forEach(i -> {
                    error[0] = output[i] - newOutput[i];
                    fLayers[fLayers.length - 1].neurons[i].delta = error[0] * fFunctionActivation.evaluateDerivative(newOutput[i]);
                });

        for (int k = fLayers.length - 2; k >= 0; k--) {
            int finalK = k;
            IntStream.range(0, fLayers[k].length)
                    .forEach(i -> {
                        error[0] = 0.0;
                        IntStream.range(0, fLayers[finalK + 1].length)
                                .forEach(j ->
                                        error[0] = error[0] + fLayers[finalK + 1].neurons[j].delta * fLayers[finalK + 1].neurons[j].weights[i]);

                        fLayers[finalK].neurons[i].delta = error[0] * fFunctionActivation.evaluateDerivative(fLayers[finalK].neurons[i].value);
                    });
            int finalK1 = k;
            IntStream.range(0, fLayers[k + 1].length)
                    .forEach(i -> {
                        IntStream.range(0, fLayers[finalK1].length)
                                .forEach(j ->
                                        fLayers[finalK1 + 1].neurons[i].weights[j] = fLayers[finalK1 + 1].neurons[i].weights[j] + fLearningRate * fLayers[finalK1 + 1].neurons[i].delta *
                                                fLayers[finalK1].neurons[j].value
                                );
                        fLayers[finalK1 + 1].neurons[i].bias += fLearningRate * fLayers[finalK1 + 1].neurons[i].delta;
                    });
        }

        error[0] = 0.0;
        IntStream.range(0, output.length)
                .forEach(i ->
                        error[0] = error[0] + Math.abs(newOutput[i] - output[i])
                );

        return error[0] / output.length;
    }
}

