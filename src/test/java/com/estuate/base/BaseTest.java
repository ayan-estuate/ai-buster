package com.estuate.base;

import com.estuate.config.Constants;
import com.microsoft.playwright.*;
import org.testng.annotations.*;

import java.nio.file.*;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass
    public void setupBrowser() {
        // Check if Trace folder exists, if not, create it
        Path traceDir = Paths.get(Constants.TRACE_PATH);
        if (!Files.exists(traceDir)) {
            try {
                Files.createDirectories(traceDir); // Create the directory if it doesn't exist
            } catch (Exception e) {
                e.printStackTrace(); // Print if directory creation fails
            }
        }

        // Set Playwright options based on headless mode (configured via Constants or system property)
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));

        System.out.println("Browser setup completed. Headless mode: " + headless);
    }

    @AfterClass
    public void tearDownBrowser() {
        try {
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public void setupContext() {
        // Create a new context and page for each test method
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterMethod
    public void closeContext() {
        // Close context after each test
        if (context != null) {
            context.close();
        }
    }
}
