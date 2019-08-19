package ui;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import paymentgateway.logic.CardIssuer;
import paymentgateway.logic.DatabaseExpiryFixerModule;
import paymentgateway.logic.InjectorConfiguration;
import paymentgateway.server.ApiServer;
import paymentgateway.server.LoggerUtils;
import paymentgateway.server.UiServer;
import ui.pages.Drivers;

@RunWith(Cucumber.class)
@CucumberOptions(
	features = "tests/cucumber",
	glue = "ui.stepdefs",
	plugin = { "html:report/cucumberui", "json:results/ACCEPTANCE-CucumberUI.json", "junit:results/other/ACCEPTANCE-CucumberUI.xml" },
	tags = { "@UI" }
)
public class CucumberUiTest {
	private static final String DB_PATH = "deploy/db/";
	private static final String WEB_PATH = "deploy/webapp/";
	private static UiServer ui;
	private static ApiServer api;
	private static ApiServer otherApi;

	private CucumberUiTest() {
	}
	
	@BeforeClass
	public static void setup() throws Exception {
		LoggerUtils.setLoggingProperties();
		InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
		otherApi = InjectorConfiguration.getInstance(ApiServer.class);
		otherApi.start(5553, DB_PATH, false);
		api = InjectorConfiguration.getInstance(ApiServer.class);
		api.start(5552, "http://localhost:5553", CardIssuer.VISA, DB_PATH);
		ui = InjectorConfiguration.getInstance(UiServer.class);
		ui.start(5551, null, 5552, WEB_PATH);
	}

	@AfterClass
	public static void teardown() throws Exception {
		Drivers.closeFirefox();
		ui.stop();
		ui = null;
		api.stop();
		api = null;
		otherApi.stop();
		otherApi = null;
	}
}
