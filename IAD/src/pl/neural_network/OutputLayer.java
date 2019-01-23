package pl.neural_network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OutputLayer extends NeuralLayer {
    
    private List<Double> outputValuesFromFile = new ArrayList();
    private List<Double> singleNeuralErrorWithoutMultiply = new ArrayList();
    private List<Double> singlePatternError = new ArrayList();
    private double singleEpochError;
    
    public OutputLayer() {}
    public OutputLayer(int numberOfNeurals) {
        super(numberOfNeurals);
    }
    
    public void countErrorForSingleNeural() {
        for (int i=0; i<getNumberOfNeurals(); i++) {
            double errorValue = outputValuesFromFile.get(i) - getOutputNeuralValues().get(i);
            singleNeuralErrorWithoutMultiply.add(errorValue);

            errorValue *=  getValuesOfDerivedSigmoidalFunc().get(i);
            getSingleNeuralError().add(errorValue);
        }
    }
    
    public void countErrorForSinglePattern() {
        double tmpSum = 0;
        
        for (int i=0; i < singleNeuralErrorWithoutMultiply.size(); i++ ) {
            double tmpValue = 0.5 * Math.pow(singleNeuralErrorWithoutMultiply.get(i), 2.0);
            //double tmpValue =  Math.abs(singleNeuralErrorWithoutMultiply.get(i));
            tmpSum += tmpValue;
        }
        
        tmpSum = tmpSum / singleNeuralErrorWithoutMultiply.size();
        
        singlePatternError.add(tmpSum);
    }
    
    public void countErrorForSingleEpoch() {
        singleEpochError = 0;
        
        for (int i=0; i < singlePatternError.size(); i++ ) {
            singleEpochError += singlePatternError.get(i);
        }
        
        singleEpochError = singleEpochError / singlePatternError.size();

        
        singlePatternError.clear();
    }
    
    public void clearValues() {
        super.clearValues();
        singleNeuralErrorWithoutMultiply.clear();
    }

    public List<Double> getOutputValuesFromFile() {
        return outputValuesFromFile;
    }

    public void setOutputValuesFromFile(List<Double> outputValues) {
        this.outputValuesFromFile = outputValues;
    }

    public List<Double> getSinglePatternError() {
        return singlePatternError;
    }

    public void setSinglePatternError(List<Double> singlePatternError) {
        this.singlePatternError = singlePatternError;
    }

    public double getSingleEpochError() {
        return singleEpochError;
    }

    public void setSingleEpochError(double singleEpochError) {
        this.singleEpochError = singleEpochError;
    }
    
}
