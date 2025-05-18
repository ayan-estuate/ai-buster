package com.estuate.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {

    /**
     * Writes test results to an Excel file.
     * If the file already exists, it appends the results to the existing file.
     *
     * @param filePath The path to the Excel file.
     * @param results The results to write (each row is an array of Strings).
     * @throws IOException If an error occurs during file writing.
     */
    public static void writeResults(String filePath, List<String[]> results) throws IOException {
        // Check if the file already exists
        boolean fileExists = fileExists(filePath);

        Workbook workbook;
        Sheet sheet;

        // If the file exists, open it and add results to the existing sheet.
        if (fileExists) {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);  // Get the existing sheet
            fis.close();
        } else {
            // Create a new workbook and sheet if the file doesn't exist
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Test Results");
        }

        // Write the results to the sheet
        int rowCount = sheet.getPhysicalNumberOfRows();
        for (String[] result : results) {
            Row row = sheet.createRow(rowCount++);
            for (int i = 0; i < result.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(result[i]);
            }
        }

        // Save the workbook to the file
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    /**
     * Checks if the file already exists.
     *
     * @param filePath The path to the file.
     * @return True if the file exists, false otherwise.
     */
    private static boolean fileExists(String filePath) {
        return new java.io.File(filePath).exists();
    }
}
