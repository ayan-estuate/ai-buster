package com.estuate.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ApplyLeavePageActions {
    private final Page page;

    public ApplyLeavePageActions(Page page) {
        this.page = page;
    }

    public void performApplyLeaveAction(String action, String selector, String input) {
        try {
            switch (action.toLowerCase()) {
                case "navigate to login page" -> {
                    System.out.println("Navigating to: " + input);
                    page.navigate(input);
                }
                case "enter username", "enter password", "select from date", "select to date", "enter reason" -> {
                    System.out.println("Filling: " + selector + " with value: " + input);
                    page.fill(selector, input);
                }
                case "click login button", "click on apply leave tab", "click submit request", "click ok button" -> {
                    clickWithWait(selector);
                }
                case "select leave type", "select project" -> {
                    System.out.println("Dropdown: " + selector + " -> Option: " + input);
                    selectDropdown(selector, input);
                }
                default -> {
                    System.out.println("Unrecognized action in ApplyLeave: " + action);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Action failed: " + action + " | Error: " + e.getMessage());
            throw e;
        }
    }

    public boolean verifyFieldAutoUpdate(String selector, String expected) {
        try {
            Locator locator = page.locator(selector);
            String actual = locator.inputValue().trim();
            System.out.println("Verifying field [" + selector + "] → Actual: " + actual + " | Expected: " + expected);
            return actual.toLowerCase().contains(expected.toLowerCase());
        } catch (Exception e) {
            System.err.println("❌ Verification failed: " + e.getMessage());
            return false;
        }
    }

    private void clickWithWait(String selector) {
        Locator element = page.locator(selector);
        element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        element.click();
    }

    private void selectDropdown(String dropdownSelector, String optionText) {
        page.locator(dropdownSelector).click();
        page.locator("//li[normalize-space()='" + optionText + "']").click();
    }
}
