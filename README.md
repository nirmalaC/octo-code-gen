# Java + JUnit 5 + Cucumber 7 + Selenium 4 + POM + Allure

Ready-to-run test automation boilerplate with:
- Page Object Model
- Explicit waits
- Exception handling
- Tags
- Allure reporting
- JUnit 5 runner
- WebDriverManager[6f5d2fd9-8983-4ef5-b351-4be045b329c5-attachment](allure-results/6f5d2fd9-8983-4ef5-b351-4be045b329c5-attachment)

## Quick start

1) Install **Java 17+** and **Maven 3.8+**.
2) (Optional) Install Allure CLI for local HTML reports.
3) From project root:

```bash
mvn clean verify                 # run tests (defaults to @smoke)
mvn -Dcucumber.filter.tags='@regression' verify   # run by tag
mvn allure:serve               # open Allure report
```

### Config
`src/test/resources/config.properties`

- `browser` = chrome|firefox|edge
- `headless` = true|false
- `baseUrl` = AUT base URL
- `timeout` = explicit wait seconds

### Notes
- Test site: https://the-internet.herokuapp.com/login
- Runner: `com.example.runners.CucumberTestRunner`
- Glue: `com.example.steps, com.example.hooks`






# 1) What’s in the project (quick map)

```
pom.xml
src
└─ test
   ├─ java
   │  └─ com.example
   │     ├─ hooks
   │     │  └─ TestHooks.java
   │     ├─ pages
   │     │  ├─ BasePage.java
   │     │  └─ LoginPage.java
   │     ├─ runners
   │     │  └─ TestRunner.java
   │     ├─ steps
   │     │  └─ LoginSteps.java
   │     └─ support
   │        ├─ ConfigReader.java
   │        ├─ DriverFactory.java
   │        └─ FrameworkException.java
   └─ resources
      ├─ config.properties
      └─ features
         └─ login.feature
```

# 2) How a test run flows (big picture)

1. You execute `mvn clean verify`.
2. Maven Failsafe runs your Cucumber TestRunner class.
3. Cucumber loads `login.feature` and your step definitions (`LoginSteps`) + hooks (`TestHooks`).
4. A @Before hook creates a WebDriver via DriverFactory using values from **config.properties**.
5. Steps drive the browser through Page Objects (`LoginPage`, backed by `BasePage` utilities).
6. A @After hook takes a failure screenshot + quits the driver.
7. Allure captures results (attachments, steps) and you can open the report.

# 3) Build: `pom.xml` (why Failsafe, key dependencies, how tags work)

* Java 17, Selenium 4, Cucumber 7, Allure/Cucumber Reports.
* Failsafe plugin is used (instead of Surefire) so the Cucumber runner executes in the `integration-test`+`verify` phases — this is the common pattern for UI/system tests.
* Test inclusion pattern includes `*Runner*.java`, so your `TestRunner.java` is picked up.
* Tag filtering: default tag is `@smoke` but can be overridden with `-Dcucumber.filter.tags="@tag"`.

Make sure these Cucumber dependencies are present to fix your compile error:

```
<dependency>
  <groupId>io.cucumber</groupId>
  <artifactId>cucumber-java</artifactId>
  <version>${cucumber.version}</version>
  <scope>test</scope>
</dependency>

<!-- JUnit 4 runner integration -->
<dependency>
  <groupId>io.cucumber</groupId>
  <artifactId>cucumber-junit</artifactId>
  <version>${cucumber.version}</version>
  <scope>test</scope>
</dependency>

<!-- JUnit 4 itself (runner annotation) -->
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.13.2</version>
  <scope>test</scope>
</dependency>
```

Without `cucumber-java`, imports like `io.cucumber.java.After` / `@Given` / `@When` don’t resolve — that’s exactly the “cannot find symbol: After” error.

# 4) Runtime config: `src/test/resources/config.properties`

```
browser=chrome          # chrome | firefox | edge
headless=false          # true to run without UI
baseUrl=https://the-internet.herokuapp.com
timeout=10              # explicit wait seconds
```

These values are read by the framework at runtime.

# 5) Core support utilities

## a) `ConfigReader.java`

* Loads `config.properties` from the classpath once (static block).
* `ConfigReader.get("key")` returns string values anywhere in the code.
* If you pass JVM properties via `-D`, you can either:

    * Add a small fallback in `get(...)` to prefer `System.getProperty(key)`, or
    * Just set them in the file.

## b) `DriverFactory.java`

* Keeps a **ThreadLocal<WebDriver>** (`tlDriver`) — thread-safe if you parallelize later.
* `initDriver()` (inside this class) uses **WebDriverManager** to resolve the right driver binary and creates the browser according to `browser` and `headless`.
* Sets timeouts and maximizes the window.
* `getDriver()` returns the current thread’s driver.
* `quitDriver()` quits + cleans up the ThreadLocal.

(If your file shows `...` placeholders, a minimal working core looks like this):

```
public static void initDriver() {
    if (tlDriver.get() != null) return;

    String browser = ConfigReader.get("browser").toLowerCase();
    boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));

    WebDriver driver;
    switch (browser) {
        case "firefox":
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions ff = new FirefoxOptions();
            if (headless) ff.addArguments("-headless");
            driver = new FirefoxDriver(ff);
            break;
        case "edge":
            WebDriverManager.edgedriver().setup();
            EdgeOptions edge = new EdgeOptions();
            if (headless) edge.addArguments("--headless=new");
            driver = new EdgeDriver(edge);
            break;
        default:
            WebDriverManager.chromedriver().setup();
            ChromeOptions ch = new ChromeOptions();
            if (headless) ch.addArguments("--headless=new");
            ch.addArguments("--window-size=1920,1080");
            driver = new ChromeDriver(ch);
    }

    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
    driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(60));
    driver.manage().window().maximize();

    tlDriver.set(driver);
}
```

# 6) Hooks: `TestHooks.java`

* **@Before**: typically calls `DriverFactory.initDriver()` and may log scenario info.
* **@After**: on failure, takes a screenshot, attaches to Allure, then quits the driver via `DriverFactory.quitDriver()`.

Your file already has the `@After` hook. If you see `...` for the `@Before`, drop this in:

```
@Before(order = 0)
public void setUp(Scenario scenario) {
    DriverFactory.initDriver();
}
```

# 7) Base Page Object utilities: `BasePage.java`

* Abstract base class for all pages.
* Holds `protected WebDriver driver;` and a `WebDriverWait`.
* Provides safe wrappers with explicit waits:

    * `waitForVisible(By)`
    * `click(By)`
    * `type(By, String)`
    * `getText(By)`
    * `isDisplayed(By)`
* Throws a custom `FrameworkException` with context when things fail.

If your file shows `...`, a minimal constructor & a wait helper would be:

```
public BasePage(WebDriver driver) {
    this.driver = driver;
    long secs = Long.parseLong(ConfigReader.get("timeout"));
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(secs));
    this.timeout = Duration.ofSeconds(secs);
}

protected WebElement waitForVisible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
}
```

# 8) A concrete page: `LoginPage.java`

* Extends `BasePage`.
* Reads `baseUrl` from config: `https://the-internet.herokuapp.com`.
* Locators: `username`, `password`, `loginButton`, `flash`.
* Key methods:

    * `open()` → navigates to `baseUrl + "/login"` and returns `this`.
    * `login(user, pass)` → types credentials + clicks Submit.
    * `flashMessage()` → returns the flash banner text.

If `open()` is elided with `...`, add:

```
public LoginPage(WebDriver driver) { super(driver); }

@Step("Open the login page")
public LoginPage open() {
    driver.get(baseUrl + "/login");
    return this;
}
```

# 9) Step definitions: `LoginSteps.java`

* Cucumber glue that bridges Gherkin to your page methods.

    * `Given I am on the login page` → `new LoginPage(getDriver()).open()`
    * `When I login with username "..." and password "..."` → `login(...)`
    * `Then I should see message "..."` → reads flash and asserts it `contains` expected text.

Assertions use JUnit’s `Assert.assertTrue(...)`.

# 10) Runner: `TestRunner.java`

* Uses **JUnit 4**’s `@RunWith(Cucumber.class)` and `@CucumberOptions`.
* Points to:

    * `features = "src/test/resources/features"`
    * `glue = {"com.example.steps","com.example.hooks"}`
* Plugins include `pretty` + **Allure Cucumber 7** adapter.
* `tags = "@smoke"` (overridable).

Note: Your README title says “JUnit 5”, but this runner is JUnit 4 (that’s fine). Just make sure `cucumber-junit` and `junit:4.13.2` are in the POM (see section 3).

# 11) Feature: `login.feature`

* Uses the public site’s login:

    * Username: `tomsmith`
    * Password: `SuperSecretPassword!`
* Expects the flash message to contain: `You logged into a secure area!`

# 12) Running it (commands you can use now)

1. Standard run (uses tags from the POM, default `@smoke`):

```
mvn clean verify
```

2. Change tags at runtime:

```
mvn clean verify -Dcucumber.filter.tags="@login"
```

3. Run headless / different browser by editing `config.properties` (or extend `ConfigReader` to also read `System.getProperty` and pass `-Dbrowser=edge -Dheadless=true`).

# 13) Allure reporting

* Results land in `target/allure-results` during the run.
* If the Allure Maven plugin is in your POM (it is), you can do:

```
mvn allure:serve
```

This builds and opens a live report locally (no extra steps).

# 14) OOP concepts (where they appear)

* **Abstraction**: `BasePage` is `abstract` and exposes high-level actions (`click`, `type`, `getText`) while hiding waits, exceptions, etc.
* **Encapsulation**: WebDriver and waits are `protected`/private to the page, used via methods rather than directly.
* **Polymorphism**: All specific pages (e.g., `LoginPage`) are used through the same `BasePage` API (e.g., any page can `click`, `type`, etc.). You can also return `this` for fluent flows.
* **(If you later add interfaces)**: You can define, say, a `Navigable` interface with `open()` to unify “openable” pages across web and mobile implementations.

# 15) Common pitfalls & quick fixes

* **`cannot find symbol: class After` in `TestHooks`**
  Add the missing dependency:

  ```
  io.cucumber:cucumber-java:${cucumber.version}
  ```

  Ensure imports are `io.cucumber.java.Before` and `io.cucumber.java.After` (they are).

* **Runner not executed**
  Confirm Failsafe includes `**/*Runner*.java` and your class name matches (`TestRunner.java`).

* **No tests found**
  Check `features` path and `glue` packages are correct, and your scenario has the tag you’re filtering by.

* **Browser doesn’t open / driver errors**
  Make sure WebDriverManager is used and `DriverFactory.initDriver()` is called in a `@Before` hook.

# 16) How to extend (tiny checklist)

* Add a new `*.feature` under `src/test/resources/features`.
* Create or extend page objects under `com.example.pages`.
* Write matching steps in `com.example.steps`.
* Tag your scenarios and run with `-Dcucumber.filter.tags="@yourTag"`.

---

Explnation for Why I used abstract class for basepage instead of interface
You should never instantiate a “generic page”
BasePage is a concept (shared behavior/utilities), not a real screen. Marking it abstract prevents new BasePage() by mistake, so the only things you can create are real pages like LoginPage, ProfilePage, etc.

Shared, stateful infrastructure lives in one place
Real pages all need the same state: WebDriver, WebDriverWait, maybe Actions and a timeout. Classes (not interfaces) can hold instance fields and initialize them via a constructor.
