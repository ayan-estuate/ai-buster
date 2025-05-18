package com.estuate.runner;

import com.estuate.base.BaseTest;
import com.estuate.config.Constants;
import com.estuate.pages.LeaveApprovalPageActions;
import com.microsoft.playwright.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class LeaveApprovalTestRunner extends BaseTest {

    @DataProvider(name = "testData")
    public Object[][] provideData() throws Exception {
        return new Object[][]{{readExcelData()}};
    }

    @Test(dataProvider = "testData")
    public void runApprovalFlow(List<Map<String, String>> steps) throws IOException {
        String currentScenario = "";
        LeaveApprovalPageActions actions = new LeaveApprovalPageActions(page);

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true).setSnapshots(true).setSources(true));

        List<String[]> results = new ArrayList<>();
        results.add(new String[]{"Test ID", "Test Step", "Expected Result", "Status"});

        for (Map<String, String> step : steps) {
            String scenarioId = step.get("Test Scenario ID");
            String testId = step.get("Test ID");
            String action = step.get("Test Step").toLowerCase().trim();
            String selector = step.get("Selector").trim();
            String input = step.get("Input Value").trim();
            String expected = step.get("Expected Result").trim();
            String status = "PASS";

            if (!scenarioId.equals(currentScenario)) {
                currentScenario = scenarioId;
                System.out.println("----- Running Scenario: " + scenarioId + " -----");
            }

            try {
                System.out.println("➡️  Test ID: " + testId + " | Step: " + action);
                actions.performAction(action, selector, input);
            } catch (Exception e) {
                status = "FAIL - " + e.getMessage();
            }

            results.add(new String[]{testId, action, expected, status});
            System.out.println("✅ Step Status: " + status + "\n");
        }

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get(Constants.TRACE_PATH + "Trace_" + currentScenario + ".zip")));

        saveResultsToExcel(results, currentScenario);
    }

    private List<Map<String, String>> readExcelData() throws IOException {
        List<Map<String, String>> testDataList = new ArrayList<>();
        FileInputStream fis = new FileInputStream(Constants.LEAVE_APPROVAL_TESTDATA_PATH);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Row header = sheet.getRow(0);
        int colCount = header.getLastCellNum();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Map<String, String> dataMap = new HashMap<>();
            for (int j = 0; j < colCount; j++) {
                Cell key = header.getCell(j);
                Cell value = row.getCell(j);
                dataMap.put(key.getStringCellValue().trim(), value != null ? value.toString().trim() : "");
            }
            testDataList.add(dataMap);
        }
        workbook.close();
        return testDataList;
    }

    private void saveResultsToExcel(List<String[]> results, String scenarioId) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Results");

        for (int i = 0; i < results.size(); i++) {
            Row row = sheet.createRow(i);
            String[] data = results.get(i);
            for (int j = 0; j < data.length; j++) {
                row.createCell(j).setCellValue(data[j]);
            }
        }

        FileOutputStream fos = new FileOutputStream(Constants.LEAVE_APPROVAL_RESULTS_PATH + scenarioId + ".xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}
