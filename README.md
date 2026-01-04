# Octo Code Gen – Selenium Automation Framework

## Java + JUnit 5 + Cucumber 7 + Selenium 4 + POM + Allure


This repository contains a **professional-grade Selenium automation framework** built using **Java, Maven, Cucumber (BDD), and GitHub Actions**. It is designed to be clean, scalable, CI-friendly, and aligned with real-world **SDET best practices**.

---

## 1. Purpose of This Framework

The goal of this project is to:

* Automate end-to-end UI tests for the Octopus Energy website
* Follow industry-standard automation architecture
* Run reliably both **locally** and in **CI (GitHub Actions)**
* Generate rich test reports (Allure)
* Notify failures via email (SendGrid)

This framework demonstrates how a **professional SDET** structures, writes, and maintains automation code.

---

## 2. Tech Stack

| Tool           | Purpose                       |
| -------------- | ----------------------------- |
| Java 17        | Programming language          |
| Selenium 4     | Browser automation            |
| Cucumber       | BDD (Given/When/Then)         |
| Maven          | Build & dependency management |
| GitHub Actions | CI/CD execution               |
| Allure         | Test reporting                |
| SendGrid API   | Failure email notifications   |

---

## 3. Project Structure (Explained)

```
src/test/java
 ├── com.example.config
 │    └── ConfigReader.java
 ├── com.example.core
 │    ├── DriverFactory.java
 │    ├── BasePage.java
 │    └── TestContext.java
 ├── com.example.pages
 │    └── LoginPage.java
 ├── com.example.steps
 │    └── LoginSteps.java
 ├── com.example.hooks
 │    └── CucumberHooks.java
 ├── com.example.utils
 │    ├── ScreenshotUtil.java
 │    └── EmailService.java
 └── com.example.runners
      └── TestRunner.java

src/test/resources
 ├── features
 │    └── login.feature
 └── config.properties
```

### Why This Structure?

* **core** → Framework-level logic (driver, base page)
* **pages** → Page Object Model (UI interactions only)
* **steps** → Cucumber step definitions (business-readable)
* **hooks** → Test lifecycle management
* **utils** → Reusable utilities (screenshots, email)

This separation makes the framework:

* Easier to maintain
* Easier to scale
* Easier for new engineers to understand

---

## 4. Configuration Management

### config.properties

Used for non-sensitive configuration:

```properties
browser=chrome
headless=false
baseUrl=https://octopus.energy/
timeout=20

email.provider=sendgrid
email.sendgrid.api.key=${SENDGRID_API_KEY}
email.from=noreply@sendgrid.net
```

### Why This Matters

* Avoids hardcoding values
* Makes tests environment-agnostic
* Supports CI/CD pipelines cleanly

---

## 5. Secrets Management (Very Important)

❌ **Secrets are NEVER committed to Git**

### Required Secrets (GitHub Actions)

| Secret Name             | Description        |
| ----------------------- | ------------------ |
| SENDGRID_API_KEY        | SendGrid API key   |
| SELENIUM_OCTO1_USERNAME | Test user username |
| SELENIUM_OCTO1_PASSWORD | Test user password |

These are configured in:

```
GitHub → Settings → Secrets → Actions
```

### Why This Matters

* Prevents security leaks
* Required for GitHub push protection
* Industry-standard practice

---

## 6. Driver Management & Headless Support

The framework automatically adapts based on environment:

* **Local execution** → Headed browser
* **CI execution** → Headless browser with fixed window size

```java
options.addArguments("--window-size=1920,1080");
```

### Why This Matters

* Prevents CI-only failures
* Ensures consistent rendering
* Makes tests stable across environments

---

## 7. Page Object Model (POM)

Each page class:

* Contains only locators and actions
* No assertions
* No test logic

Example:

```java
public void clickOctoPlus() {
    click(octoPlusButton);
}
```

### Why This Matters

* Clean separation of concerns
* Reusable UI actions
* Easier UI updates

---

## 8. Robust Waiting & Clicking Strategy

All interactions go through `BasePage`:

* Explicit waits only (no Thread.sleep)
* Scroll into view before clicking
* Clickability checks

This removes:

* Flaky tests
* Timing issues
* CI instability

---

## 9. Cucumber Hooks & Failure Handling

Hooks manage:

* Driver setup & teardown
* Screenshot capture on failure
* Resource cleanup

```java
@After
public void tearDown(Scenario scenario) {
    if (scenario.isFailed()) {
        ScreenshotUtil.capture(scenario.getName());
    }
    DriverFactory.quitDriver();
}
```

### Why This Matters

* Automatic diagnostics
* Clean lifecycle management
* Professional test behavior

---

## 10. Reporting (Allure)

After each run:

* Allure results are generated
* HTML report is created
* Reports are uploaded as CI artifacts

### View Locally

```bash
mvn allure:serve
```

---

## 11. Email Notifications (SendGrid)

On test failure:

* Screenshot is captured
* Email is sent via SendGrid API

Benefits:

* No SMTP dependency
* Works in CI
* Secure API-based integration

---

## 12. GitHub Actions CI Pipeline

The pipeline:

1. Checks out code
2. Sets up Java
3. Injects secrets
4. Runs tests
5. Generates Allure report
6. Uploads artifacts

This ensures:

* Tests run on every push
* Failures are visible immediately
* Reports are preserved

---

## 13. Git Hygiene & Best Practices

* `.gitignore` excludes build artifacts
* No logs or reports committed
* Clean commit history
* Secrets never stored in repo

These practices reflect **real-world SDET standards**.

---

## 14. How to Run the Tests

### Local Execution

```bash
mvn test
```

### CI Execution

Triggered automatically on push via GitHub Actions.

---

## 15. Why This Framework Looks Professional

This project demonstrates:

* Clean architecture
* Secure configuration
* CI-ready automation
* Stable Selenium practices
* Maintainable Page Objects
* Enterprise reporting & notifications

This is the level expected from a **mid-to-senior SDET**.

---

## 16. Final Notes

This framework is designed to:

* Scale with new tests
* Support multiple environments

Further improvements can include:

* Parallel execution
* Dockerized test runs
* Cloud browser support

---