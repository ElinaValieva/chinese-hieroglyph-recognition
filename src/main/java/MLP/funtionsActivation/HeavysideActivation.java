package MLP.funtionsActivation;


public class HeavysideActivation implements FunctionActivation {

    @Override
    public double evaluate(double value) {
        return (value >= 0.0) ? 1.0 : 0.0;
    }

    @Override
    public double evaluateDerivative(double value) {
        return 1.0;
    }

}
