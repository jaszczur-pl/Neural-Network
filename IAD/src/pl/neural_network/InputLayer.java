package pl.neural_network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InputLayer {

    private int numberOfNeurals;
    private List<Double> inputValuesFromFile = new ArrayList();
    private List<String> patternList = new ArrayList();
    private List<Integer> patternQueue = new ArrayList();
    private boolean isRandomPatternOn;

    public InputLayer(){}
    public InputLayer(int numberOfNeurals){
        this.numberOfNeurals = numberOfNeurals;
    }
   
    public int getNumberOfNeurals() {
        return numberOfNeurals;
    }

    public void setNumberOfNeurals(int numberOfNeurals) {
        this.numberOfNeurals = numberOfNeurals;
    }

    public List<Double> getInputValuesFromFile() {
        return inputValuesFromFile;
    }

    public void setInputValuesFromFile(List<Double> inputValuesFromFile) {
        this.inputValuesFromFile = inputValuesFromFile;
    }

    public List<String> getPatternList() {
        return patternList;
    }

    public void setPatternList(List<String> patternList) {
        this.patternList = patternList;
    }
    
    public List<Integer> getPatternQueue() {
        return patternQueue;
    }

    public void setPatternQueue(List<Integer> patternQueue) {
        this.patternQueue = patternQueue;
    }
    
    public boolean isRandomPatternOn() {
        return isRandomPatternOn;
    }

    public void setRandomPatternOn(boolean isRandomPatternOn) {
        this.isRandomPatternOn = isRandomPatternOn;
    }
    
    public void drawPatternQueue() {
        patternQueue.clear();
        
        for (int i=0; i < patternList.size(); i++) {
            patternQueue.add(i);
        }
        
        if (isRandomPatternOn == true) { 
            Collections.shuffle(patternQueue);
        }
    }

}
