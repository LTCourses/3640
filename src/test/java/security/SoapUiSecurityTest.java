package security;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.eviware.soapui.tools.SoapUISecurityTestRunner;

import paymentgateway.logic.DatabaseExpiryFixerModule;
import paymentgateway.logic.InjectorConfiguration;
import paymentgateway.server.ApiServer;
import paymentgateway.server.LoggerUtils;

public class SoapUiSecurityTest {
	private static final String TEST_PATH = "tests/soapui";
	private static final String DB_PATH = "deploy/db/";
	private static final String RESULTS_PATH = "results/security";
	private static ApiServer server;
	private static SoapUISecurityTestRunner runner;
	
	@BeforeClass
	public static void setup() {
		LoggerUtils.setLoggingProperties();
		InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
		server = InjectorConfiguration.getInstance(ApiServer.class);
		server.start(5552, DB_PATH, false);

		runner = new SoapUISecurityTestRunner();
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
		runner.run();
	}
}
