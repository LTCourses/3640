package api;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.eviware.soapui.tools.SoapUITestCaseRunner;

public class SoapUiApiDockerTest {
	private static final String TEST_PATH = "tests/soapui";
	private static final String RESULTS_PATH = "results/soapui";
	private static SoapUITestCaseRunner runner;

	@BeforeClass
	public static void setup() {
		runner = new SoapUITestCaseRunner();
		runner.setJUnitReport(true);
		runner.setOutputFolder(RESULTS_PATH);
	}
	
	@AfterClass
	public static void teardown() {
		runner = null;
	}

	@Test
	public void apiDockerTests() throws Exception {
		runner.setProjectFile(TEST_PATH + "/All.xml");
		runner.setTestSuite("APIFixedExpiry");
		runner.setEndpoint("http://localhost:5652");
		runner.run();
	}
}
