package MLP.funtionsActivation;

public class HyperbolicActivation implements FunctionActivation {

    @Override
    public double evaluate(double value) {
        return Math.tanh(value);
    }

    @Override
    public double evaluateDerivative(double value) {
        return 1 - Math.pow(value, 2);
    }
}
