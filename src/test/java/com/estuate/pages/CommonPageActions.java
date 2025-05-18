package com.estuate.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CommonPageActions {
    private final Page page;

    public CommonPageActions(Page page) {
        this.page = page;
    }

    // Perform action based on test step
    public void performAction(String action, String selector, String input) {
        try {
            switch (action.toLowerCase()) {
                case "navigate to login page" -> {
                    System.out.println("Navigating to login page: " + input);
                    page.navigate(input);
                }
                case "enter username", "enter password", "enter from date", "enter to date", "enter reason" -> {
                    System.out.println("Entering value in field: " + selector + " with input: " + input);
                    page.fill(selector, input);
                }
                case "click login button", "click apply leave tab", "click leave approval tab", "click submit request" -> clickWithWait(selector);
                case "click approve leave button", "click ok button" -> {
                    System.out.println("Clicking button: " + selector);
                    page.click(selector);
                }
                case "select leave type", "select project name", "select approver" -> {
                    System.out.println("Selecting option in dropdown: " + selector + " with input: " + input);
                    page.click(selector);
                    page.getByText(input).click();
                }
                case "select pending leave request" -> {
                    System.out.println("Selecting pending leave request checkboxes");
                    Locator checkboxes = page.locator("//table[contains(@class, 'cds--data-table')]//tbody/tr[td[11][normalize-space()='Pending']]/td[1]//div[@class='cds--checkbox--inline']");
                    for (int i = 0; i < checkboxes.count(); i++) {
                        checkboxes.nth(i).click();
                    }
                }
                default -> System.out.println("Unrecognized action: " + action);
            }
        } catch (Exception e) {
            System.err.println("Error during action: " + action + " | Error: " + e.getMessage());
            throw e; // Rethrow exception to ensure the test fails
        }
    }

    // Method to wait for an element and click it
    private void clickWithWait(String selector) {
        try {
            Locator tabLocator = page.locator(selector);
            tabLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(10000));
            tabLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            System.out.println("Clicking element after waiting: " + selector);
            tabLocator.click(new Locator.ClickOptions().setTimeout(5000));
        } catch (Exception e) {
            System.err.println("Error while waiting for element to be clickable: " + selector + " | Error: " + e.getMessage());
            throw e; // Rethrow exception to ensure the test fails
        }
    }

    // Method to verify if an element is visible
    public boolean verifyVisible(String selector) {
        try {
            boolean isVisible = page.isVisible(selector);
            System.out.println("Verification for visibility [" + selector + "]: " + (isVisible ? "PASS" : "FAIL"));
            return isVisible;
        } catch (Exception e) {
            System.err.println("Error verifying visibility of element: " + selector + " | Error: " + e.getMessage());
            return false; // Return false in case of error
        }
    }
}
