package com.foodtruck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVParser {
    private String fileName;
    private List<String> csvLines;

    public CSVParser(String fileName) {
        this.fileName = fileName;
        this.csvLines = new ArrayList<>();
    }

    public List<String> parse(){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file= new File(classLoader.getResource(fileName).getFile());
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);

            while(inputStream.hasNextLine()){
                String line= inputStream.nextLine();
                csvLines.add(line);
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return csvLines;
    }

}
