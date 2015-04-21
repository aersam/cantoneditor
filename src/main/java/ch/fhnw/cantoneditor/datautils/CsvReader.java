package ch.fhnw.cantoneditor.datautils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import au.com.bytecode.opencsv.CSVReader;

public class CsvReader {

    public static void readCantons() throws IOException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(
                CsvReader.class.getResourceAsStream("/cantons.csv")));
        try (CSVReader reader = new CSVReader(fileReader, ',', '"', 1)) {

            // Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                    // Verifying the read data here
                    System.out.println(Arrays.toString(nextLine));
                }
            }
        }
    }
}
