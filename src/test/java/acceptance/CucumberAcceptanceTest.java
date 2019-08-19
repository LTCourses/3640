package acceptance;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
	features = "tests/cucumber",
	glue = "acceptance.stepdefs",
	plugin = { "html:report/cucumber", "json:results/ACCEPTANCE-Cucumber.json", "junit:results/other/ACCEPTANCE-Cucumber.xml" },
	tags = { "~@UI", "~@API" }
)
public class CucumberAcceptanceTest { }
