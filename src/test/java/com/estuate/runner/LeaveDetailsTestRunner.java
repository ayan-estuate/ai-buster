package com.estuate.runner;

import com.estuate.base.BaseTest;
import com.estuate.config.Constants;
import com.estuate.data.ExcelReader;
import com.estuate.pages.LeaveDetailsPageActions;
import com.estuate.utils.ExcelWriter;
import com.microsoft.playwright.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.*;

public class LeaveDetailsTestRunner extends BaseTest {

    @DataProvider(name = "leaveDetailsData")
    public Object[][] fetchData() throws Exception {
        // Using the generic ExcelReader to fetch data from the Leave Details Excel file
        List<Map<String, String>> data = ExcelReader.readExcelData(Constants.LEAVE_DETAILS_TESTDATA_PATH);
        return new Object[][]{{data}}; // Returning data as a 2D array for the data provider
    }

    @Test(dataProvider = "leaveDetailsData")
    public void runLeaveDetailsTest(List<Map<String, String>> testSteps) {
        String currentScenario = "";
        List<String[]> results = new ArrayList<>();
        results.add(new String[]{"Test ID", "Test Step", "Expected Result", "Status"});  // Adding headers to the result

        LeaveDetailsPageActions actions = new LeaveDetailsPageActions(page);

        // Start tracing for Playwright context
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        // Iterate through each step in the test data
        for (Map<String, String> step : testSteps) {
            String scenarioId = step.get("Test Scenario ID");
            if (!scenarioId.equals(currentScenario)) {
                System.out.println("===== Executing Scenario: " + scenarioId + " =====");
                currentScenario = scenarioId;
            }

            String testId = step.get("Test ID");
            String action = step.get("Test Step").toLowerCase().trim();
            String selector = step.get("Selector").trim();
            String input = step.get("Input Value").trim();
            String expected = step.get("Expected Result").trim();
            String status = "PASS";  // Default status to PASS

            try {
                System.out.println("➡️  Test ID: " + testId + " | Step: " + action);
                // Perform the action based on the provided test data
                actions.performAction(action, selector, input);
            } catch (Exception e) {
                status = "FAIL - " + e.getMessage();  // In case of exception, mark the status as FAIL
            }

            // Add step results (Test ID, Step, Expected Result, Status)
            results.add(new String[]{testId, action, expected, status});
            System.out.println("✅ Step Status: " + status + "\n");
        }

        // Stop tracing and save the trace to the specified path
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get(Constants.TRACE_PATH + "LeaveDetails_" + currentScenario + ".zip")));

        // Write the test results to an Excel file
        try {
            ExcelWriter.writeResults(Constants.LEAVE_DETAILS_RESULTS_PATH, results);  // Updated to use ExcelWriter
        } catch (Exception e) {
            System.err.println("Error writing results to Excel: " + e.getMessage());
        }
    }
}
