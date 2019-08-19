package load;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.JUnitSoapUiLoadTestRunner;

public class SoapUiLoadTest {
	private static final String TEST_PATH = "tests/soapui";
//	private static final String DB_PATH = "deploy/db/";
	private static final String RESULTS_PATH = "results/load";
//	private static ApiServer server;
	private static JUnitSoapUiLoadTestRunner runner;
	
	@BeforeClass
	public static void setup() {
//		LoggerUtils.setLoggingProperties();
//		InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
//		server = InjectorConfiguration.getInstance(ApiServer.class);
//		server.start(5552, DB_PATH, false);

		runner = new JUnitSoapUiLoadTestRunner();
		runner.setOutputFolder(RESULTS_PATH);
		runner.setPrintReport(true);
	}
	
	@AfterClass
	public static void teardown() {
		runner = null;
		
//		server.stop();
//		server = null;
	}

	@Test
	public void testApi() throws Exception {
		runner.setProjectFile(TEST_PATH + "/All.xml");
		runner.runNow(RESULTS_PATH);
		assertFalse(runner.getHasErrors());
	}
}
