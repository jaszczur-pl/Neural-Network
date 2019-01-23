package pl.neural_network;

import java.util.ArrayList;
import java.util.List;

public class HiddenLayer extends NeuralLayer {
    
    private HiddenLayerContainer hiddenLayerContainer;
    
    public HiddenLayer(){}
    public HiddenLayer(int numberOfNeurals) {
        super(numberOfNeurals);
        hiddenLayerContainer.addHiddenLayerToContainer(this);
    }

    public void countErrorForSingleNeural(List<Double> pattErr, List<List<Double>> layWeigths, int numberOfHiddenLayers, int layerNo) {
        int tmpNumOfNeurals = getNumberOfNeurals();
        int prevErrorsFromNeurals = pattErr.size();
        
        if (isBiasActive() == true) {
            tmpNumOfNeurals += 1;
            
            if((numberOfHiddenLayers - 1) != layerNo) {
                prevErrorsFromNeurals -= 1;
            }
        }
        
        for (int i=0; i<tmpNumOfNeurals; i++) {
            double tmpError = 0;
            
            for (int j=0; j<prevErrorsFromNeurals; j++) {
                tmpError += pattErr.get(j) * layWeigths.get(j).get(i);
            } 
            
            if (i < getNumberOfNeurals()) {                                  // warunek wystawiony, aby błąd dla biasu nie był mnożony dodatkowo przez pochodną funkcji sigmoidalnej
                tmpError *=  getValuesOfDerivedSigmoidalFunc().get(i);
            }
                
            getSingleNeuralError().add(tmpError);
        }
    }
}

