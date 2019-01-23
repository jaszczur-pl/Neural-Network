package pl.neural_network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportToExcel {

    private List<String> globalErrosList = new ArrayList();
    private List<String> outputValuesList = new ArrayList();
    
    public void exportGlobalErrors() throws IOException{
        Path inputDataFilePath = Paths.get("E:/IAD/learning_mode_global_errors.txt");
        globalErrosList = (Files.readAllLines(inputDataFilePath));
        exportData();
    }
    
    public void exportData() throws FileNotFoundException, IOException {
        //Create new workbook and tab
        File myFile = new File("E:/IAD/analiza.xlsx");
        FileInputStream fileIn = new FileInputStream(myFile);
        XSSFWorkbook wb = new XSSFWorkbook(fileIn);
        
        wb.removeSheetAt(1);
        wb.createSheet("Dane1");
        wb.setSheetOrder("Dane1", 1);
        
        Sheet sheet = wb.getSheet("Dane1");

        for (int i=0; i < globalErrosList.size(); i++) {
            Row row = sheet.createRow(i);
            
            Cell epochNo = row.createCell(0);
            epochNo.setCellValue(i+1);
            
            Cell globalError = row.createCell(1);
            globalError.setCellValue(Double.parseDouble(globalErrosList.get(i)));   
        }
        
        FileOutputStream fileOut = new FileOutputStream(myFile);
        //Export Data
        wb.write(fileOut);
        fileIn.close();
        fileOut.close();
    } 
    
    public void exportOutputValues() throws IOException {
        Path inputDataFilePath = Paths.get("E:/IAD/output_values.txt");
        outputValuesList = (Files.readAllLines(inputDataFilePath));
        exportData2();
    }
    
    public void exportData2() throws FileNotFoundException, IOException {
        File myFile = new File("E:/IAD/analiza.xlsx");
        FileInputStream fileIn = new FileInputStream(myFile);
        XSSFWorkbook wb = new XSSFWorkbook(fileIn);
        
        wb.removeSheetAt(3);
        wb.createSheet("Dane2");
        wb.setSheetOrder("Dane2", 3);
        
        Sheet sheet = wb.getSheet("Dane2");
        
        for (int i=0; i < outputValuesList.size(); i++) {   
            
            String[] tmpString = outputValuesList.get(i).replace("[",", ").replace("]","").substring(2).split(", ");
            
            Row row = sheet.createRow(i);
            
            Cell irisSetosa = row.createCell(0);
            irisSetosa.setCellValue(Double.parseDouble(tmpString[0]));
            
            Cell irisVersicolor = row.createCell(1);
            irisVersicolor.setCellValue(Double.parseDouble(tmpString[1]));
            
            Cell irisVirginica = row.createCell(2);
            irisVirginica.setCellValue(Double.parseDouble(tmpString[2]));
            
            Cell sepalLength = row.createCell(3);
            sepalLength.setCellValue(Double.parseDouble(tmpString[3]));
            
            Cell sepalWidth = row.createCell(4);
            sepalWidth.setCellValue(Double.parseDouble(tmpString[4]));
            
            Cell petalLength = row.createCell(5);
            petalLength.setCellValue(Double.parseDouble(tmpString[5]));
            
            Cell petalWidth = row.createCell(6);
            petalWidth.setCellValue(Double.parseDouble(tmpString[6]));
            
            Cell expectedIrisSetosa = row.createCell(7);
            expectedIrisSetosa.setCellValue(Double.parseDouble(tmpString[7]));
            
            Cell expectedIrisVersicolor = row.createCell(8);
            expectedIrisVersicolor.setCellValue(Double.parseDouble(tmpString[8]));
            
            Cell expectedIrisVirginica = row.createCell(9);
            expectedIrisVirginica.setCellValue(Double.parseDouble(tmpString[9]));
        }
        
        FileOutputStream fileOut = new FileOutputStream(myFile);
        //Export Data
        wb.write(fileOut);
        fileIn.close();
        fileOut.close();
    }
}
