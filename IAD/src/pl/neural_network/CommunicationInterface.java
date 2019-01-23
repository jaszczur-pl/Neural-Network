package pl.neural_network;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CommunicationInterface {

    private InputLayer inputLayer;
    private OutputLayer outputLayer;
    private HiddenLayerContainer hiddenLayerContainer;
    private StringBuffer globalErrorBuff = new StringBuffer();
    private StringBuffer outputValuesBuff = new StringBuffer();
    private char runMode = 'l';
    private int numberOfInputNeurals = 4;
    private int numberOfOutputNeurals = 3;
    private int numberOfHiddenLayers = 1;
    private int epochsNumber = 10000;
    private double globalError = 0.01;
    private String biasMode = "t";
    private String patternRandomness = "t";
    private double learningRatio = 0.2;
    private double momentumRatio = 0.8;

    
    public CommunicationInterface(InputLayer inputLayer, OutputLayer outputLayer, HiddenLayerContainer hiddenLayerContainer){
        this.inputLayer = inputLayer;
        this.outputLayer = outputLayer;
        this.hiddenLayerContainer = hiddenLayerContainer;
    }
    
    public char typeRunMode() {
       /* System.out.print("Podaj tryb działania (l-nauka z testowaniem, t-samo testowanie): ");
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        char runMode = txt.charAt(0); */

        return runMode;
    }
    
    public void writeNumberOfInNeurals() {
        /*System.out.print("Podaj ilosc neurownow wejsciowych: ");
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        int numberOfInputNeurals = Integer.parseInt(txt); */
        
        inputLayer.setNumberOfNeurals(numberOfInputNeurals);
    }
    
    public void writeNumberOfOutNeurals() {
        /*System.out.print("Podaj ilosc neurownow wyjsciowych: ");
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        int numberOfOutputNeurals = Integer.parseInt(txt);*/
        
        outputLayer.setNumberOfNeurals(numberOfOutputNeurals);
    }
    
    public void writeNumberOfHiddenNeurals(int i) {
        i++;
        System.out.print("Podaj ilosc neurownow w " + i + " warstwie ukrytej: ");
        i--;
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        int tmpInt = Integer.parseInt(txt);
        
        hiddenLayerContainer.getHiddenLayerContainer().get(i).setNumberOfNeurals(tmpInt);;
    }
    
    public void writeNumberOfHiddenLayers() {
       /* System.out.print("Podaj ilosc warstw ukrytych: ");
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        int numberOfHiddenLayers = Integer.parseInt(txt); */
        
        hiddenLayerContainer.setNumberOfHiddenLayers(numberOfHiddenLayers);
    }
    
    public void writeLearningRatio() {
       /* System.out.print("Podaj wspolczynnik nauki: ");
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        double learningRatio = Double.parseDouble(txt); */       
        
        for (int i=0; i < hiddenLayerContainer.getHiddenLayerContainer().size(); i++){
            hiddenLayerContainer.getHiddenLayerContainer().get(i).setLearningRatio(learningRatio);
        }
        
        outputLayer.setLearningRatio(learningRatio);
    }
    
    public void writeMomentumRatio() {
      /*  System.out.print("Podaj wspolczynnik momentum: ");
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        double momentumRatio = Double.parseDouble(txt); */
        
        for (int i=0; i < hiddenLayerContainer.getHiddenLayerContainer().size(); i++){
            hiddenLayerContainer.getHiddenLayerContainer().get(i).setMomentumRatio(momentumRatio);
        }
        
        outputLayer.setMomentumRatio(momentumRatio);
    }
    
    public int typeEpochsNumber() {
       /* System.out.print("Podaj liczbę epok: ");
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        int epochsNumber = Integer.parseInt(txt); */
        
        return epochsNumber;
    }
    
    public double typeGlobalError() {
     /*   System.out.print("Podaj do jakiego błędu globalnego ma być uczona sieć: ");
        Scanner input = new Scanner(System.in);
        String txt = input.nextLine();
        double globalError = Double.parseDouble(txt); */
        
        return globalError;
    }
    
    public void chooseBiasValue() {
      /*  System.out.print("Czy uwzgledniac bias? [t/n]: ");
        Scanner input = new Scanner(System.in);
        String biasMode = input.nextLine(); */
        
        if (biasMode.charAt(0) == 't')
        {
            for (int i=0; i < hiddenLayerContainer.getHiddenLayerContainer().size(); i++){
                hiddenLayerContainer.getHiddenLayerContainer().get(i).setBiasActive(true);
            }
            outputLayer.setBiasActive(true);
        }
    }
    
    public void choosePatternRandomness() {
       /* System.out.print("Czy losować wzorce? [t/n]: ");
        Scanner input = new Scanner(System.in);
        String patternRandomness = input.nextLine(); */
        
        if (patternRandomness.charAt(0) == 't')
        {
            inputLayer.setRandomPatternOn(true);
        }
    }
    
    public void readPatternsFromFile() throws IOException{
        Path inputDataFilePath = Paths.get("E:/IAD/patterns.txt");
        inputLayer.setPatternList(new ArrayList(Files.readAllLines(inputDataFilePath)));
    }
    
    public void loadPatternToInput(int nextPattern) { 
        List<Double> tmpInList = new ArrayList();
        List<Double> tmpOutList = new ArrayList();
        int patternNo = inputLayer.getPatternQueue().get(nextPattern);
        
        String[] tmpString = inputLayer.getPatternList().get(patternNo).split("/");
            
        String[] inputVal = tmpString[0].split(",");
        String[] outputVal = tmpString[1].split(",");
        
        for (int j = 0; j < inputLayer.getNumberOfNeurals(); j++){
            double x = Double.parseDouble(inputVal[j]); 
            tmpInList.add(x);
        }
           
        for (int j = 0; j < outputLayer.getNumberOfNeurals(); j++){
            double x = Integer.parseInt(outputVal[j]); 
            tmpOutList.add(x);

        }
    
        inputLayer.setInputValuesFromFile(tmpInList); 
        outputLayer.setOutputValuesFromFile(tmpOutList);
        
    }
    
    public void wrongModePassed() {
        System.out.println("Podano zły tryb działania!");
    }
     
    public void printReportToFile(Path out) throws IOException {
        
        StringBuffer strBuff = new StringBuffer();
        strBuff.append("Raport");
        strBuff.append(System.lineSeparator());
        strBuff.append("Wzorzec wejściowy: " + inputLayer.getInputValuesFromFile().toString());
        strBuff.append(System.lineSeparator());
        strBuff.append("Wzorzec wyjściowy: " + outputLayer.getOutputValuesFromFile().toString());
        strBuff.append(System.lineSeparator());
        
        if (outputLayer.isBiasActive() == true){
            strBuff.append("Uwzględniono wartość wejścia obciążającego (bias)");
        } else {
            strBuff.append("Nie uwzględniono wartości wejścia obciążającego (bias)");
        }
        strBuff.append(System.lineSeparator());
        strBuff.append("Wartość współczynnika nauki: " + outputLayer.getLearningRatio());
        strBuff.append(System.lineSeparator());
        strBuff.append("Wartość współczynnika momentum: " + outputLayer.getMomentumRatio());

        
        strBuff.append(System.lineSeparator());
        strBuff.append(System.lineSeparator());
        strBuff.append("Wagi neuronów warstw ukrytych: ");
        strBuff.append(System.lineSeparator());
            for (int j=0; j < hiddenLayerContainer.getHiddenLayerContainer().size(); j++) {
                int z = j+1;
                strBuff.append(z + " warstwa ukryta: " + hiddenLayerContainer.getHiddenLayerContainer().get(j).getLayerWeights().toString());
                strBuff.append(System.lineSeparator());
            }
       strBuff.append("Wagi neuronów warstwy wyjściowej: " + outputLayer.getLayerWeights());
       strBuff.append(System.lineSeparator());
       strBuff.append(System.lineSeparator());
       
       strBuff.append("Wartości wyjściowe neuronów warstw ukrytych: ");
       strBuff.append(System.lineSeparator());
           for (int j=0; j < hiddenLayerContainer.getHiddenLayerContainer().size(); j++) {
               int z = j+1;
               strBuff.append(z + " warstwa ukryta: " + hiddenLayerContainer.getHiddenLayerContainer().get(j).getOutputNeuralValues().toString());
               strBuff.append(System.lineSeparator());
           }
       strBuff.append("Wartości wyjściowe neuronów warstwy wyjściowej: " + outputLayer.getOutputNeuralValues());
       strBuff.append(System.lineSeparator());
       strBuff.append(System.lineSeparator());
       
       strBuff.append("Pochodne funkcji sigmoidalnej neuronów warstw ukrytych: ");
       strBuff.append(System.lineSeparator());
           for (int j=0; j < hiddenLayerContainer.getHiddenLayerContainer().size(); j++) {
               int z = j+1;
               strBuff.append(z + " warstwa ukryta: " + hiddenLayerContainer.getHiddenLayerContainer().get(j).getValuesOfDerivedSigmoidalFunc().toString());
               strBuff.append(System.lineSeparator());
           }
       strBuff.append("Pochodne funkcji sigmoidalnej neuronów warstwy wyjściowej: " + outputLayer.getValuesOfDerivedSigmoidalFunc());
       strBuff.append(System.lineSeparator());
       strBuff.append(System.lineSeparator());
       
       strBuff.append("Wartość błędu na wyjściu warstwy wyjściowej: " + outputLayer.getSingleNeuralError());
       strBuff.append(System.lineSeparator());
       for (int j=hiddenLayerContainer.getHiddenLayerContainer().size()-1; j >= 0 ; j--) {
           int z = j+1;
           strBuff.append("Wartość błędu " + z + " warstwy ukrytej: " + hiddenLayerContainer.getHiddenLayerContainer().get(j).getSingleNeuralError());
           strBuff.append(System.lineSeparator());
       }
       strBuff.append(System.lineSeparator());
       
       int nextPatternError = outputLayer.getSinglePatternError().size()-1;
       strBuff.append("Wartość błędu wzorca: " + outputLayer.getSinglePatternError().get(nextPatternError));
       
       Files.write(out, strBuff.toString().getBytes());
    }
    
    public void addLinetoGlobalErrorBuffer(int epochNumber) {
        //globalErrorBuff.append("Błąd globalny dla epoki nr " + epochNumber + ": " + outputLayer.getSingleEpochError());
        globalErrorBuff.append(outputLayer.getSingleEpochError());
        globalErrorBuff.append(System.lineSeparator());
    }
    
    public void addLinetoOutputValuesBuffer() {
        outputValuesBuff.append(outputLayer.getOutputNeuralValues());
        outputValuesBuff.append(inputLayer.getInputValuesFromFile());
        outputValuesBuff.append(outputLayer.getOutputValuesFromFile());
        outputValuesBuff.append(System.lineSeparator());
    }
    
    public void printGlobalErrorToFile(Path out) throws IOException {
        Files.write(out, globalErrorBuff.toString().getBytes());
    }
    
    public void printOutputValuesToFile(Path out) throws IOException {
        Files.write(out, outputValuesBuff.toString().getBytes());
    }
}
