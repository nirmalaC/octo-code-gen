package com.example.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"src/test/java/com/example/steps", "src/test/java/com/example/hooks"},
    plugin = {
            "pretty",

            // Raw report files for CI / advanced reporting
            "json:target/cucumber-report/cucumber.json",
            "junit:target/cucumber-report/cucumber.xml",

            // Simple built-in Cucumber HTML (optional; fine for local viewing)
            "html:target/cucumber-report/cucumber.html",

            // Rerun file (only failed scenarios) – handy for quick retries
            "rerun:target/cucumber-report/rerun.txt"

            // Allure is still available if you keep its plugin too:
            // "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    tags = "@positive",
    monochrome = true
)
public class TestRunner {
}
