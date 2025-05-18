package com.estuate.pages;

import com.microsoft.playwright.*;

public class LeaveApprovalPageActions {
    private final Page page;

    public LeaveApprovalPageActions(Page page) {
        this.page = page;
    }

    public void performAction(String action, String selector, String input) {
        try {
            switch (action.toLowerCase().trim()) {

                // Navigation
                case "navigate to login page" -> page.navigate(input);

                // Input fields
                case "enter manager username", "enter manager password" ->
                        page.locator(selector).fill(input);

                // Click actions
                case "click login button", "navigate to leave approval",
                     "approve selected leave", "reject selected leave",
                     "okay button", "bulk approve leaves", "logout" ->
                        page.locator(selector).click();

                // Conditional click if visible
                case "select a pending leave", "select another pending leave" -> {
                    Locator checkbox = page.locator(selector);
                    if (checkbox.isVisible(new Locator.IsVisibleOptions().setTimeout(3000))) {
                        checkbox.click();
                    } else {
                        throw new RuntimeException("❌ Checkbox not visible for action: " + action + " | Selector: " + selector);
                    }
                }

                // Loop through all matching checkboxes
                case "selectpendingcheckboxes" -> {
                    Locator checkboxes = page.locator(selector);
                    int count = checkboxes.count();
                    if (count == 0) {
                        throw new RuntimeException("❌ No checkboxes found for selector: " + selector);
                    }
                    for (int i = 0; i < count; i++) {
                        checkboxes.nth(i).click();
                    }
                }

                // Unknown step
                default -> throw new UnsupportedOperationException("❌ Unsupported action: " + action);
            }

            System.out.println("✅ Action performed: " + action);

        } catch (Exception e) {
            System.err.println("❌ Exception during action '" + action + "' with selector '" + selector + "': " + e.getMessage());
            throw e; // rethrow for reporting in test runner
        }
    }
}
