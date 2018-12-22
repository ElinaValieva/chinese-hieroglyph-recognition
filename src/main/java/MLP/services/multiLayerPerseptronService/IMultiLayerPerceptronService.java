package MLP.services.multiLayerPerseptronService;

import MLP.funtionsActivation.FunctionActivation;

/**
 * author: ElinaValieva on 15.12.2018
 */
public interface IMultiLayerPerceptronService {

    void prepareLayers(int[] layers, double learningRate, FunctionActivation fFunctionActivation);

    Double[] execute(Double[] input);

    double backPropagate(Double[] input, Double[] output);
}
