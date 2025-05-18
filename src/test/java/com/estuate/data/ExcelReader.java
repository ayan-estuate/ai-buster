package com.estuate.data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {

    /**
     * Reads data from an Excel file and returns it as a list of maps.
     * Each map represents a row with column headers as keys.
     *
     * @param path The file path of the Excel file.
     * @return List of maps containing test data.
     * @throws IOException If an I/O error occurs during file reading.
     * @throws IllegalArgumentException If the file path is invalid or file is not in correct format.
     */
    public static List<Map<String, String>> readExcelData(String path) throws Exception {
        List<Map<String, String>> testDataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(path)) {  // Using try-with-resources for automatic closing
            Workbook workbook = new XSSFWorkbook(fis);  // Workbook will be closed automatically
            Sheet sheet = workbook.getSheetAt(0);  // Get the first sheet
            Row header = sheet.getRow(0);  // Read the header row
            if (header == null) {
                throw new IllegalArgumentException("The Excel file has no header row.");
            }

            int colCount = header.getPhysicalNumberOfCells();  // Get the number of columns in the header

            // Iterate through the rows (starting from row 1 as row 0 is the header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;  // Skip empty rows

                Map<String, String> dataMap = new HashMap<>();
                for (int j = 0; j < colCount; j++) {
                    Cell key = header.getCell(j);
                    Cell value = row.getCell(j);

                    // Use null check for both key and value to avoid NullPointerExceptions
                    String keyValue = key != null ? key.getStringCellValue().trim() : "";
                    String valueText = value != null ? value.toString().trim() : "";

                    dataMap.put(keyValue, valueText);  // Add the key-value pair to the map
                }

                testDataList.add(dataMap);  // Add the data map to the list
            }
        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            throw e;  // Re-throw the exception
        } catch (Exception e) {
            System.err.println("Error processing Excel file: " + e.getMessage());
            throw e;  // Re-throw the exception
        }

        return testDataList;
    }
}
