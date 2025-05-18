package com.estuate.runner;

import com.estuate.base.BaseTest;
import com.estuate.config.Constants;
import com.estuate.data.ExcelReader;
import com.estuate.pages.CommonPageActions;
import com.estuate.utils.ExcelWriter;

import com.microsoft.playwright.Tracing;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.*;

public class LMSExcelTestRunner extends BaseTest {

    @DataProvider(name = "testData")
    public Object[][] testData() throws Exception {
        List<Map<String, String>> testDataList = ExcelReader.readExcelData(Constants.TEST_DATA_PATH);
        return new Object[][]{{testDataList}};
    }

    @Test(dataProvider = "testData")
    public void runExcelDrivenTest(List<Map<String, String>> testSteps) throws Exception {
        String currentScenario = "";
        List<String[]> results = new ArrayList<>();
        results.add(new String[]{"Test ID", "Test Step", "Expected Result", "Status"});

        page = context.newPage();

        // Start tracing before executing any test steps
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        // Initialize CommonPageActions instance for action handling
        CommonPageActions actions = new CommonPageActions(page);

        // Iterate through each step in the test data
        for (Map<String, String> step : testSteps) {
            String scenarioId = step.get("Test Scenario ID");
            String testId = step.get("Test ID");
            String action = step.get("Test Step").toLowerCase().trim();
            String selector = step.get("Selector");
            String input = step.get("Input Value");
            String expected = step.get("Expected Result");
            String status = "PASS";  // Default status is PASS

            if (!scenarioId.equals(currentScenario)) {
                System.out.println("----- Running Scenario: " + scenarioId + " -----");
                currentScenario = scenarioId;
            }

            try {
                // Handling actions based on their type (Verify vs Perform Action)
                if (action.startsWith("verify")) {
                    boolean isVisible = actions.verifyVisible(selector);
                    if (!isVisible) {
                        status = "FAIL";
                    }
                    System.out.printf("Verification [%s] => %s%n", expected, status);
                } else {
                    actions.performAction(action, selector, input);
                }
            } catch (Exception e) {
                status = "FAIL";  // If there's an error during any action, mark as FAIL
                System.out.printf("Step FAILED [%s]: %s%n", action, e.getMessage());
            }

            // Store the result for the current step
            results.add(new String[]{testId, action, expected, status});
        }

        // Stop tracing after all actions are completed
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get(Constants.TRACE_PATH + currentScenario + ".zip")));

        // Write the final results into the Excel file
        ExcelWriter.writeResults(Constants.RESULTS_PATH, results);
    }
}
