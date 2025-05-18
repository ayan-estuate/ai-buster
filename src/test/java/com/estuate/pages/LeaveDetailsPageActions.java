package com.estuate.pages;

import com.microsoft.playwright.*;

public class LeaveDetailsPageActions {
    private final Page page;

    public LeaveDetailsPageActions(Page page) {
        this.page = page;
    }

    public void performAction(String action, String selector, String input) {
        try {
            switch (action) {
                case "navigate to login page" -> page.navigate(input);

                case "enter username", "enter password" -> page.fill(selector, input);

                case "click login button", "click leave details tab",
                     "click on the dropdown", "click logout" -> {
                    page.waitForSelector(selector).click();
                }

                case "verify leave type column is visible",
                     "verify from date column is visible",
                     "verify to date column is visible",
                     "verify no of days column is visible",
                     "verify project column is visible",
                     "verify approver status column is visible",
                     "verify applied on column is visible",
                     "verify filter by status dropdown is visible" -> {
                    if (!page.locator(selector).isVisible()) {
                        throw new AssertionError("❌ Element not visible: " + selector);
                    }
                }

                case "select 'approved' from filter by status",
                     "select 'rejected' from filter by status",
                     "select 'pending' from filter by status",
                     "select 'all' from filter by status" -> {
                    page.click(selector);
                    page.locator("text=" + input).click();
                }

                case "enter 'sick leave' in search field",
                     "enter 'casual leave' in search field",
                     "clear search field" -> page.fill(selector, input);

                default -> {
                    if (!input.isEmpty() && !page.content().toLowerCase().contains(input.toLowerCase())) {
                        throw new AssertionError("❌ Expected text not found: " + input);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error performing action: " + action + " | " + e.getMessage());
            throw e;
        }
    }
}
