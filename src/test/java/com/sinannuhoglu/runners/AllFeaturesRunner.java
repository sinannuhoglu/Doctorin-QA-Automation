package com.sinannuhoglu.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Projedeki TÜM feature dosyalarını koşturmak için genel runner.
 * Path: src/test/resources/features
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.sinannuhoglu.steps",
                "com.sinannuhoglu.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-report/cucumber.html",
                "json:target/cucumber-report/cucumber.json"
        },
        monochrome = true,
        tags = "not @ignore"
)
public class AllFeaturesRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
