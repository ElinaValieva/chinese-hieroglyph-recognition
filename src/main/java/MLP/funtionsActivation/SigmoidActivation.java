package MLP.funtionsActivation;

public class SigmoidActivation implements FunctionActivation {
    @Override
    public double evaluate(double value) {
        return 1 / (1 + Math.pow(Math.E, -value));
    }

    @Override
    public double evaluateDerivative(double value) {
        return (value - Math.pow(value, 2));
    }
}

