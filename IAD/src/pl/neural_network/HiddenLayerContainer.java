package pl.neural_network;

import java.util.ArrayList;
import java.util.List;

public class HiddenLayerContainer {
    
    private int numberOfHiddenLayers;
    private List<HiddenLayer> hiddenLayerContainer = new ArrayList();
    
    public HiddenLayerContainer(){}
    public HiddenLayerContainer(int numberOfHiddenLayers) {
        this.numberOfHiddenLayers = numberOfHiddenLayers;
    }
    
    public void addHiddenLayerToContainer (HiddenLayer hidLay) {
        hiddenLayerContainer.add(hidLay);
    }

    public int getNumberOfHiddenLayers() {
        return numberOfHiddenLayers;
    }

    public void setNumberOfHiddenLayers(int numberOfHiddenLayers) {
        this.numberOfHiddenLayers = numberOfHiddenLayers;
    }

    public List<HiddenLayer> getHiddenLayerContainer() {
        return hiddenLayerContainer;
    }

    public void setHiddenLayerContainer(List<HiddenLayer> hiddenLayerContainer) {
        this.hiddenLayerContainer = hiddenLayerContainer;
    }

    
}
