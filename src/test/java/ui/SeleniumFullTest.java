package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import paymentgateway.logic.DatabaseExpiryFixerModule;
import paymentgateway.logic.InjectorConfiguration;
import paymentgateway.server.ApiServer;
import paymentgateway.server.LoggerUtils;
import paymentgateway.server.UiServer;

public class SeleniumFullTest {
	private static final String DB_PATH = "deploy/db/";
	private static UiServer uiServer;
	private static ApiServer apiServer;

	@BeforeClass
	public static void setup() {
		LoggerUtils.setLoggingProperties();
		InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
		apiServer = InjectorConfiguration.getInstance(ApiServer.class);
		apiServer.start(5552, DB_PATH, false);
		uiServer = InjectorConfiguration.getInstance(UiServer.class);
		uiServer.start(5551, "http://localhost", 5552, "deploy/webapp/");
	}
	
	@AfterClass
	public static void teardown() {
		if (uiServer != null) {
			uiServer.stop();
			uiServer = null;
		}
		if (apiServer != null) {
			apiServer.stop();
			apiServer = null;
		}
	}

	@Test
	public void allTests() throws Exception {
		// TODO The following doesn't work, but launching it from the command line does work
//		String[] args = new String[] {
//			"--geckodriver tests/drivers/geckodriver.exe",
//			"--xml-result results/selenium",
//			"--html-result report/selenium",
//			"tests/selenium/*.side"
//		};
//		jp.vmi.selenium.selenese.Main.main(args);

		Process proc = Runtime.getRuntime().exec("java -jar tests/lib/selenese-runner.jar --geckodriver tests/drivers/geckodriver.exe --xml-result results/selenium --html-result report/selenium --screenshot-on-fail results/screenshots tests/selenium/*.side");
		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		int result = proc.waitFor();
		if (result != 0) fail();
	}
}
