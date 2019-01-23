package pl.neural_network;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MainProcess {
    private static HiddenLayer hiddenLay;
    private static InputLayer inLay = new InputLayer();
    private static OutputLayer outLay = new OutputLayer();
    private static HiddenLayerContainer hiddenLayContainer = new HiddenLayerContainer();  
    private static CommunicationInterface commLay = new CommunicationInterface(inLay, outLay, hiddenLayContainer);
    private static ExportToExcel excelData = new ExportToExcel();
    private static int hiddenLayCounter;
    private static List<HiddenLayer> hiddenLayerList;
    private static int epochsNumber;
    private static double globalError;
    private static char runMode;
    
    public static void run() throws IOException {
       
       runMode = commLay.typeRunMode();
       
       switch(runMode) {
       
       case 'l':
           writeInputValues();
           commLay.readPatternsFromFile();
           int i=1;
           
           //tryb nauki
           do {
              inLay.drawPatternQueue();
              
              for (int j = 0; j < inLay.getPatternList().size(); j++) {
                  
                  commLay.loadPatternToInput(j);
                  makeOperationsOnHiddenLayers(i,j);
                  makeOperationsOnOutputLayer(i,j);
                  countErrors();
                  doBackpropagation();
                  clearLayersLists();
              }
          
              outLay.countErrorForSingleEpoch();
              if(i % 1 == 0) {
                  commLay.addLinetoGlobalErrorBuffer(i);
              }
              
              i++;
              
           } while(epochsNumber >= i && globalError < outLay.getSingleEpochError());
           
           System.out.println(i);

           
           Path outLearningPath = Paths.get("E:/IAD/learning_mode_global_errors.txt");
           commLay.printGlobalErrorToFile(outLearningPath);
          
           excelData.exportGlobalErrors();
          
           //tryb testowania
           inLay.drawPatternQueue();
           for (int j=0; j < inLay.getPatternList().size(); j++) {
              Path outTestingPath = Paths.get("E:/IAD/testing_mode_report_run" + j + ".txt");
              
              
              commLay.loadPatternToInput(j);
              makeOperationsOnHiddenLayers(0,j);
              makeOperationsOnOutputLayer(0,j);
              commLay.addLinetoOutputValuesBuffer();
              countErrors();
              commLay.printReportToFile(outTestingPath);
              clearLayersLists();
          }
          Path outputValuesFilePath = Paths.get("E:/IAD/output_values.txt");
          commLay.printOutputValuesToFile(outputValuesFilePath);
          
          excelData.exportOutputValues();
          
          break;
          
       case 't':
           writeInputValues();
           commLay.readPatternsFromFile();
           
           //tryb testowania
          for (int j=0; j < inLay.getPatternList().size(); j++) {
              Path outTestingPath = Paths.get("E:/IAD/testing_mode_report_run" + j + ".txt");
              
              inLay.drawPatternQueue();
              commLay.loadPatternToInput(j);
              makeOperationsOnHiddenLayers(1,j);
              makeOperationsOnOutputLayer(1,j);
              commLay.addLinetoOutputValuesBuffer();
              countErrors();
              commLay.printReportToFile(outTestingPath);
              clearLayersLists();
          }
          
          break;
          
       default:
           commLay.wrongModePassed();
           break;
       }
       
    }
    
    public static void writeInputValues() { 
        
        if(runMode == 'l') {
            epochsNumber = commLay.typeEpochsNumber();
            globalError = commLay.typeGlobalError();
        }

        commLay.writeNumberOfInNeurals();
        commLay.writeNumberOfHiddenLayers();
        
        hiddenLayCounter = hiddenLayContainer.getNumberOfHiddenLayers();
        
        for (int i=0; i < hiddenLayCounter; i++) {
            hiddenLay = new HiddenLayer();
            hiddenLayContainer.addHiddenLayerToContainer(hiddenLay);
            commLay.writeNumberOfHiddenNeurals(i);
        }
        
        commLay.writeNumberOfOutNeurals();
        commLay.chooseBiasValue();
        commLay.choosePatternRandomness();
        
        if(runMode == 'l') {
            commLay.writeLearningRatio();
            commLay.writeMomentumRatio();
        }

    }
    
    public static void readInputValuesFromFiles() throws IOException {
        commLay.readPatternsFromFile();
    }
    
    public static void makeOperationsOnHiddenLayers(int epochsCounter, int runCounter) {
        hiddenLayerList = hiddenLayContainer.getHiddenLayerContainer();
        
        if (epochsCounter == 1 && runCounter == 0) {
            hiddenLayerList.get(0).drawNeuralWeights(inLay.getNumberOfNeurals());
        }
        
        hiddenLayerList.get(0).countMembranePotential(inLay.getInputValuesFromFile(), inLay.getNumberOfNeurals());
        hiddenLayerList.get(0).countSigmoidalFunc();
        hiddenLayerList.get(0).countDerivedSigmoidalFunc();
        
        for (int i=1; i < hiddenLayCounter; i++) {
            
            int prevLayerNumberOfNeurals = hiddenLayerList.get(i-1).getNumberOfNeurals();
            List<Double> prevLayerOutputValues = hiddenLayerList.get(i-1).getOutputNeuralValues();
            
            if (epochsCounter == 1 && runCounter == 0) {
                hiddenLayerList.get(i).drawNeuralWeights(prevLayerNumberOfNeurals);
            }
            
            hiddenLayerList.get(i).countMembranePotential(prevLayerOutputValues, prevLayerNumberOfNeurals);
            hiddenLayerList.get(i).countSigmoidalFunc();
            hiddenLayerList.get(i).countDerivedSigmoidalFunc();
        }
    }
    
    public static void makeOperationsOnOutputLayer(int epochsCounter, int runCounter) {
        
        hiddenLayerList = hiddenLayContainer.getHiddenLayerContainer();
        int prevLayerNumberOfNeurals = hiddenLayerList.get(hiddenLayCounter-1).getNumberOfNeurals();
        List<Double> prevLayerOutputValues = hiddenLayerList.get(hiddenLayCounter-1).getOutputNeuralValues();
        
        if (epochsCounter == 1 && runCounter == 0) {
            outLay.drawNeuralWeights(prevLayerNumberOfNeurals);
        }
        
        outLay.countMembranePotential(prevLayerOutputValues, prevLayerNumberOfNeurals);
        outLay.countSigmoidalFunc();
        outLay.countDerivedSigmoidalFunc();
        outLay.countErrorForSingleNeural();
        outLay.countErrorForSinglePattern();
    }
    
    public static void countErrors() {
        int lastHiddenLayerNumberOfNeurals = hiddenLayerList.get(hiddenLayCounter-1).getNumberOfNeurals();
        List<Double> lastHiddenLayerOutputValues = hiddenLayerList.get(hiddenLayCounter-1).getOutputNeuralValues();
        
        // licz wartosci bledow dla kazdego neuronu
        hiddenLayerList.get(hiddenLayCounter-1).countErrorForSingleNeural(outLay.getSingleNeuralError(), outLay.getLayerWeights(), hiddenLayCounter, hiddenLayCounter-1);
        
        for (int i=hiddenLayCounter-2 ; i>=0; i--) {
            List<Double> nextLayPatternError = hiddenLayerList.get(i+1).getSingleNeuralError();
            List<List<Double>> nextLayLayerWeigths = hiddenLayerList.get(i+1).getLayerWeights();
            
            hiddenLayerList.get(i).countErrorForSingleNeural(nextLayPatternError, nextLayLayerWeigths, hiddenLayCounter, i);
        }
    }
    
    public static void doBackpropagation() {
        
        int lastHiddenLayerNumberOfNeurals = hiddenLayerList.get(hiddenLayCounter-1).getNumberOfNeurals();
        List<Double> lastHiddenLayerOutputValues = hiddenLayerList.get(hiddenLayCounter-1).getOutputNeuralValues();
        
        //licz czesc uczaca i momentum, aktualizuj wagi z poprzedniej iteracji, wprowadz korekte wag
        hiddenLayerList.get(0).countLearningPart(inLay.getInputValuesFromFile(), inLay.getNumberOfNeurals());
        hiddenLayerList.get(0).countMomentumPart(inLay.getNumberOfNeurals());
        hiddenLayerList.get(0).moveLayerWeightsToPrevIter(inLay.getNumberOfNeurals());
        hiddenLayerList.get(0).updateNeuralWeights(inLay.getNumberOfNeurals());
        
        for (int i=1; i < hiddenLayCounter; i++)
        {
            int prevLayerNumberOfNeurals = hiddenLayerList.get(i-1).getNumberOfNeurals();
            List<Double> prevLayerOutputValues = hiddenLayerList.get(i-1).getOutputNeuralValues();
            
            hiddenLayerList.get(i).countLearningPart(prevLayerOutputValues, prevLayerNumberOfNeurals);
            hiddenLayerList.get(i).countMomentumPart(prevLayerNumberOfNeurals);
            hiddenLayerList.get(i).moveLayerWeightsToPrevIter(prevLayerNumberOfNeurals);
            hiddenLayerList.get(i).updateNeuralWeights(prevLayerNumberOfNeurals);
        }
        
        outLay.countLearningPart(lastHiddenLayerOutputValues, lastHiddenLayerNumberOfNeurals);
        outLay.countMomentumPart(lastHiddenLayerNumberOfNeurals);
        outLay.updateNeuralWeights(lastHiddenLayerNumberOfNeurals);
        outLay.moveLayerWeightsToPrevIter(lastHiddenLayerNumberOfNeurals);
    }
    
    public static void clearLayersLists() {
        outLay.clearValues();
        
        for(int i=0; i < hiddenLayCounter; i++) {
            hiddenLayerList.get(i).clearValues();
        }
    }

    public static int getEpochsNumber() {
        return epochsNumber;
    }

    public static void setEpochsNumber(int epochsNumber) {
        MainProcess.epochsNumber = epochsNumber;
    }
    
    
}
