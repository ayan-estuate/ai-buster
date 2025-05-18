# AI-Buster Automation Framework

## 🔍 Overview

**AI-Buster** is a scalable, maintainable, and industry-grade test automation framework built using **Java**, **Microsoft Playwright**, **TestNG**, and **Excel-based data-driven testing**. This framework is designed to automate the functional testing of a Leave Management System (LMS) and integrates with **Jira**, **SLF4J + Logback logging**, and other utilities to support enterprise-level QA needs.

---

## 🧱 Project Structure (Scalable Project architecture)

```sql
+---src
|   +---main
|   |   +---java
|   |   |   \---com
|   |   |       \---estuate
|   |   |           +---config
|   |   |           |       PropertyLoader.java
|   |   |           |       
|   |   |           +---jira
|   |   |           |       JiraAttachmentDownloader.java
|   |   |           |       JiraAttachmentUploader.java
|   |   |           |       JiraService.java
|   |   |           |       
|   |   |           \---utils
|   |   |                   FileUtil.java
|   |   |                   
|   |   \---resources
|   |           application.properties
|   |           logback.xml
|   |           
|   \---test
|       +---java
|       |   \---com
|       |       \---estuate
|       |           +---base
|       |           |       BaseTest.java
|       |           |       
|       |           +---config
|       |           |       Constants.java
|       |           |       
|       |           +---data
|       |           |       ExcelReader.java
|       |           |       
|       |           +---pages
|       |           |       ApplyLeavePageActions.java
|       |           |       CommonPageActions.java
|       |           |       LeaveApprovalPageActions.java
|       |           |       LeaveDetailsPageActions.java
|       |           |       
|       |           +---runner
|       |           |       ApplyLeaveTestRunner.java
|       |           |       LeaveApprovalTestRunner.java
|       |           |       LeaveDetailsTestRunner.java
|       |           |       LMSExcelTestRunner.java
|       |           |       
|       |           +---tests
|       |           |       JiraAttachmentTest.java
|       |           |       JiraAttachmentUploadTest.java
|       |           |       
|       |           \---utils
|       |                   ExcelWriter.java
|       |                   
|       \---resources
|           +---testdata
|           |       Apply-Leave-Tab_AI_info_TestCases_20250504_134308.xlsx
|           |       Leave-details-tab_AI_info_TestCases_20250506_123841.xlsx
|           |       Leave_approval_AI_info_TestCases_20250505_143604.xlsx
|           |       Leave_Management_System_Doc_V1_AI_Info_TestCases_20250424_092902.xlsx
|           |       
|           \---Trace
|                   LeaveDetails_TC_014.zip
|                   Leave_Details_Results.xlsx
|                   Manager_Approval_Results_.xlsx
|                   Results.xlsx
|                   TC_004.zip
|                   TC_005.zip
|                   Trace_.zip


```
## 🚀 Key Features

- ✅ **Java + Playwright**: Fast, modern browser automation
- ✅ **TestNG**: Structured test execution with annotations, parallelism, and reporting
- ✅ **Data-Driven Testing**: Input test steps from Excel for flexibility and reusability
- ✅ **SLF4J + Logback**: Structured logging with daily file rotation and console output
- ✅ **Jira Integration**: Attach evidence or trigger workflows via API
- ✅ **Page Action Design Pattern**: Clear separation of UI actions and logic
- ✅ **Playwright Tracing**: Collect ZIP traces for each scenario for debugging
- ✅ **Excel Result Writing**: Automatically logs results back to Excel for reporting

---

## 🧪 How to Run Tests

1. **Install dependencies:**
   ```sql
   mvn clean install
   ```
2. **Run a specific test runner:**
    ```sql
    mvn test -Dtest=ApplyLeaveTestRunner
    ```

3. **View Logs:**
- Console output for real-time feedback
- File logs at `../logs/test-automation.log` with daily rotation

4. **View Results:**
- Excel files updated in `src/test/resources/Trace/`
- Playwright traces saved as `.zip` files for detailed UI replay

---

## ⚙️ Logging Setup

- Logging is configured via `logback.xml` in `src/main/resources`
- Logs are written to:
- Console (all levels)
- File: `../logs/test-automation.YYYY-MM-DD.log`
- Old logs are rotated daily and kept for 14 days

---

## 📊 Test Data & Results

- All test steps are defined in Excel files (`.xlsx`) under `src/test/resources/testdata/`
- Each row in the Excel represents one test step with:
- Scenario ID, Test Step, Selector, Input, Expected Output
- Results are written back into result files for analysis

---

## 📂 Trace Artifacts

- Each test scenario generates a Playwright `.zip` trace:
- HTML snapshots
- Screenshots
- Action timeline
- Traces are saved in `src/test/resources/Trace/` for replay and debugging

---

## 🔒 Best Practices Followed

- Page Object + Action pattern
- Externalized test data (no hardcoded steps)
- Central config management via constants
- Clean separation between logic, test runners, and data
- Structured logging with log levels
- Reusable utilities for Excel I/O and file operations

---

## 🔧 Tech Stack

| Layer             | Technology                     |
|------------------|--------------------------------|
| Language          | Java 17                        |
| Automation Tool   | Microsoft Playwright (Java)    |
| Test Framework    | TestNG                         |
| Logging           | SLF4J + Logback                |
| Data Handling     | Apache POI                     |
| Reporting         | Excel (via ExcelWriter)        |
| CI/CD Ready       | Yes (Maven + CLI support)      |

---

## 🤝 Contribution & Maintenance

This framework is modular and extensible. You can add:
- New test modules by adding TestNG runners
- More Page Action classes for other tabs/pages
- API integrations, DB verifications, or visual validation

All configurations, logs, and test data are externalized to support scalable, maintainable, and robust test automation.

---

## 🧹 Cleanup & Ignore

Make sure your `.gitignore` includes:
```sql
  logs/
  *.log
  *.zip
  *.xlsx
```
