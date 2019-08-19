package api;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.eviware.soapui.tools.SoapUITestCaseRunner;

import paymentgateway.logic.DatabaseExpiryFixerModule;
import paymentgateway.logic.InjectorConfiguration;
import paymentgateway.server.ApiServer;
import paymentgateway.server.LoggerUtils;

public class SoapUiApiTest {
	private static final String TEST_PATH = "tests/soapui";
	private static final String DB_PATH = "deploy/db/";
	private static final String RESULTS_PATH = "results/soapui";
	private static ApiServer server;
	private static SoapUITestCaseRunner runner;
	
	@BeforeClass
	public static void setup() {
		LoggerUtils.setLoggingProperties();
		InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
		server = InjectorConfiguration.getInstance(ApiServer.class);
		server.start(5552, DB_PATH, false);

		runner = new SoapUITestCaseRunner();
		runner.setJUnitReport(true);
		runner.setOutputFolder(RESULTS_PATH);
		runner.setPrintReport(true);
	}
	
	@AfterClass
	public static void teardown() {
		runner = null;
		
		server.stop();
		server = null;
	}

	@Test
	public void testApi() throws Exception {
		runner.setProjectFile(TEST_PATH + "/All.xml");
		runner.setTestSuite("API");
		runner.run();
	}
}
