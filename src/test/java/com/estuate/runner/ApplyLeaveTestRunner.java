package com.estuate.runner;

import com.estuate.base.BaseTest;
import com.estuate.config.Constants;
import com.estuate.data.ExcelReader;
import com.estuate.pages.ApplyLeavePageActions;
import com.estuate.utils.ExcelWriter;

import com.microsoft.playwright.Tracing;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.*;

public class ApplyLeaveTestRunner extends BaseTest {

    @DataProvider(name = "applyLeaveTestData")
    public Object[][] applyLeaveTestData() throws Exception {
        List<Map<String, String>> testDataList = ExcelReader.readExcelData(Constants.APPLY_LEAVE_TESTDATA_PATH);
        return new Object[][]{{testDataList}};
    }

    @Test(dataProvider = "applyLeaveTestData")
    public void runApplyLeaveTests(List<Map<String, String>> testSteps) throws Exception {
        String currentScenario = "";
        List<String[]> results = new ArrayList<>();
        results.add(new String[]{"Test ID", "Test Step", "Expected Result", "Status"});

        page = context.newPage();

        // Start Playwright trace
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        ApplyLeavePageActions actions = new ApplyLeavePageActions(page);

        for (Map<String, String> step : testSteps) {
            String scenarioId = step.get("Test Scenario ID");
            String testId = step.get("Test ID");
            String action = step.get("Test Step").toLowerCase().trim();
            String selector = step.get("Selector").trim();
            String input = step.get("Input Value").trim();
            String expected = step.get("Expected Result").trim();
            String status = "PASS";

            if (!scenarioId.equals(currentScenario)) {
                System.out.println("=== Running Scenario: " + scenarioId + " ===");
                currentScenario = scenarioId;
            }

            try {
                if (action.startsWith("verify")) {
                    boolean matched = actions.verifyFieldAutoUpdate(selector, expected);
                    if (!matched) {
                        status = "FAIL";
                        System.out.println("❌ Verification failed | Selector: " + selector + " | Expected: " + expected);
                    } else {
                        System.out.println("✅ Verification passed");
                    }
                } else {
                    actions.performApplyLeaveAction(action, selector, input);
                    System.out.println("✅ Action performed: " + action);
                }
            } catch (Exception e) {
                status = "FAIL";
                System.out.println("❌ Step failed | Action: " + action + " | Error: " + e.getMessage());
            }

            results.add(new String[]{testId, action, expected, status});
        }

        // Stop tracing and save
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get(Constants.TRACE_PATH + currentScenario + ".zip")));

        // Write result
        ExcelWriter.writeResults(Constants.RESULTS_PATH, results);
    }
}
