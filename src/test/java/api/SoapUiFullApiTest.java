package api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.eviware.soapui.tools.SoapUITestCaseRunner;

import paymentgateway.logic.DatabaseExpiryFixerModule;
import paymentgateway.logic.InjectorConfiguration;
import paymentgateway.server.ApiServer;
import paymentgateway.server.LoggerUtils;

@RunWith(Parameterized.class)
public class SoapUiFullApiTest {
	private static final String TEST_PATH = "tests/soapui";
	private static final String TEST_SUFFIX = ".xml";
	private static final String DB_PATH = "deploy/db/";
	private static final String RESULTS_PATH = "results/soapui";
	private static ApiServer server;
	private static SoapUITestCaseRunner runner;
	private String fileName;
	
	public SoapUiFullApiTest(String fileName, String testName) {
		this.fileName = fileName;
	}

	@Parameters(name="File: {1}")
	public static List<Object[]> data() {
		List<Object[]> fileNames = new ArrayList<Object[]>();
		int testPathLength1 = TEST_PATH.length() + 1;
		int testSuffixLength = TEST_SUFFIX.length();
		File directory = new File(TEST_PATH);
		for (File file : directory.listFiles()) {
			String filePath = file.getPath();
			String filePathLower = filePath.toLowerCase();
			if (filePathLower.endsWith(TEST_SUFFIX)) {
				filePath = filePath.substring(testPathLength1);
				String testName = filePath.substring(0, filePath.length() - testSuffixLength);
				String[] fileName = { filePath, testName };
				fileNames.add(fileName);
			}
		}
		return fileNames;
	}
	
	@BeforeClass
	public static void setup() {
		LoggerUtils.setLoggingProperties();
		InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
		server = InjectorConfiguration.getInstance(ApiServer.class);
		server.start(5552, DB_PATH, false);

		runner = new SoapUITestCaseRunner();
		runner.setJUnitReport(true);
		runner.setOutputFolder(RESULTS_PATH);
	}
	
	@AfterClass
	public static void teardown() {
		runner = null;
		
		server.stop();
		server = null;
	}

	@Test
	public void allSoapUiTests() throws Exception {
		runner.setProjectFile(TEST_PATH + "/" + fileName);
		runner.run();
	}
}
