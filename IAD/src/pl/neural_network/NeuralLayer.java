package pl.neural_network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class NeuralLayer {
    
    private int numberOfNeurals;
    private double learningRatio;
    private double momentumRatio;
    private boolean isBiasActive;
    private List<List<Double>> layerWeights = new ArrayList<List<Double>>();
    private List<Double> membranePotential = new ArrayList();
    private List<Double> outputNeuralValues = new ArrayList();
    private List<Double> valuesOfDerivedSigmoidalFunc = new ArrayList();
    private List<Double> singleNeuralError = new ArrayList();
    private List<List<Double>> learningPart = new ArrayList();
    private List<List<Double>> momentumPart = new ArrayList();
    private List<List<Double>> prevIterLayerWeights = new ArrayList<List<Double>>();
    
    public NeuralLayer(){}
    public NeuralLayer(int numberOfNeurals){
        this.numberOfNeurals = numberOfNeurals;
    }
   
    public int getNumberOfNeurals() {
        return numberOfNeurals;
    }

    public void setNumberOfNeurals(int numberOfNeurals) {
        this.numberOfNeurals = numberOfNeurals;
    }
    
    public double getLearningRatio() {
        return learningRatio;
    }

    public void setLearningRatio(double learningRatio) {
        this.learningRatio = learningRatio;
    }
    
    public double getMomentumRatio() {
        return momentumRatio;
    }

    public void setMomentumRatio(double momentumRatio) {
        this.momentumRatio = momentumRatio;
    }
    
    public boolean isBiasActive() {
        return isBiasActive;
    }

    public void setBiasActive(boolean isBiasActive) {
        this.isBiasActive = isBiasActive;
    }

    public List<List<Double>> getLayerWeights() {
        return layerWeights;
    }

    public List<Double> getMembranePotential() {
        return membranePotential;
    }
    
    public List<Double> getOutputNeuralValues() {
        return outputNeuralValues;
    }

    public void setOutputNeuralValues(List<Double> outputNeuralValues) {
        this.outputNeuralValues = outputNeuralValues;
    }

    public List<Double> getValuesOfDerivedSigmoidalFunc() {
        return valuesOfDerivedSigmoidalFunc;
    }
    
    public List<Double> getSingleNeuralError() {
        return singleNeuralError;
    }
    
    public void drawNeuralWeights(int prevLayerNumberOfNeurals){
        
        double min = -1.0;
        double max = 1.0;
        
        for (int i = 0; i < numberOfNeurals; i++){
            
            layerWeights.add(new ArrayList<Double>());
            prevIterLayerWeights.add(new ArrayList<Double>());                        
            
            for (int j = 0; j < prevLayerNumberOfNeurals; j++) { 
                double randomNum = ThreadLocalRandom.current().nextDouble(min, max);
                layerWeights.get(i).add(randomNum);
                prevIterLayerWeights.get(i).add(randomNum);                                   // dodaj wage do listy wag z interacji (i-1) - wartosci niezbedne do czlonu momentum
            }
            
            if (isBiasActive == true) {
                double randomNum = ThreadLocalRandom.current().nextDouble(min, max);
                layerWeights.get(i).add(randomNum);
                prevIterLayerWeights.get(i).add(randomNum);
            }
        }
    }
    
    public void countMembranePotential(List<Double> inputValues, int prevLayerNumberOfNeurals) {
        
        double sum = 0;
        int j;
        
        for (int i=0; i < numberOfNeurals; i++) { 
            
            for (j=0; j < prevLayerNumberOfNeurals; j++) {
                sum += layerWeights.get(i).get(j) * inputValues.get(j);
            }

            if (isBiasActive == true) {
                sum += layerWeights.get(i).get(prevLayerNumberOfNeurals) * 1.0; 
            }
            
            membranePotential.add(sum);
            sum = 0;
        }
    }   
    
    public void countSigmoidalFunc() {
        
        for (int i=0; i < numberOfNeurals; i++) {
            double x = membranePotential.get(i);
            double outValue = 1 / (1 + Math.pow(Math.E, -1.0 * x));
            outputNeuralValues.add(outValue);
        }
    }
    
    public void countDerivedSigmoidalFunc() {
        
        for (int i=0; i < numberOfNeurals; i++){
            double tempValue = outputNeuralValues.get(i) * (1.0 - outputNeuralValues.get(i));
            valuesOfDerivedSigmoidalFunc.add(tempValue);
        }
    }
    
    public void updateNeuralWeights(int prevLayerNumberOfNeurals) { 
        
        for (int i = 0; i < numberOfNeurals; i++) {
            int j = 0;
                                  
            for (j = 0; j < prevLayerNumberOfNeurals; j++) { 
                double tmpValue = layerWeights.get(i).get(j) + learningPart.get(i).get(j) + momentumPart.get(i).get(j);
                layerWeights.get(i).set(j, tmpValue);
            }
            
            if (isBiasActive == true) {
                double tmpValue = layerWeights.get(i).get(j) + learningPart.get(i).get(j) + momentumPart.get(i).get(j);
                layerWeights.get(i).set(j, tmpValue);
            }
        }
    }
    
    public void countLearningPart(List<Double> inputValues, int prevLayerNumberOfNeurals) {
        
        for (int i = 0; i < numberOfNeurals; i++) {
            
            learningPart.add(new ArrayList<Double>());
            
            for (int j = 0; j < prevLayerNumberOfNeurals; j++) { 
               double tmpValue = learningRatio * singleNeuralError.get(i) * inputValues.get(j);
               learningPart.get(i).add(tmpValue);
            }
            
            if (isBiasActive == true) {
                double tmpValue = learningRatio * singleNeuralError.get(i) * 1.0;
                learningPart.get(i).add(tmpValue);
            }
        }
    }
    
    public void countMomentumPart(int prevLayerNumberOfNeurals) {
        
        for (int i = 0; i < numberOfNeurals; i++) {
            
            int j = 0;
            momentumPart.add(new ArrayList<Double>());
            
            for (j = 0; j < prevLayerNumberOfNeurals; j++) { 
               double tmpValue = momentumRatio * (layerWeights.get(i).get(j) - prevIterLayerWeights.get(i).get(j));
               momentumPart.get(i).add(tmpValue);
            }

            if (isBiasActive == true) {
                double tmpValue = momentumRatio * (layerWeights.get(i).get(j) - prevIterLayerWeights.get(i).get(j));
                momentumPart.get(i).add(tmpValue);
            }
        }
    }
    
    public void moveLayerWeightsToPrevIter (int prevLayerNumberOfNeurals) {
        
        for (int i = 0; i < numberOfNeurals; i++) {
            
            int j = 0;
                                  
            for (j = 0; j < prevLayerNumberOfNeurals; j++) { 
                double layWeightToSet = layerWeights.get(i).get(j);
                prevIterLayerWeights.get(i).set(j, layWeightToSet);
            }
            
            if (isBiasActive == true) {
                double layWeightToSet = layerWeights.get(i).get(j);
                prevIterLayerWeights.get(i).set(j, layWeightToSet);
            }
        }
    }
    
    public void clearValues() {
        membranePotential.clear();
        outputNeuralValues.clear();
        valuesOfDerivedSigmoidalFunc.clear();
        singleNeuralError.clear();
        learningPart.clear();
        momentumPart.clear();
    }
    
}
