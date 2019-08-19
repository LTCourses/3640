package utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.testsuite.LoadTest;
import com.eviware.soapui.model.testsuite.LoadTestRunContext;
import com.eviware.soapui.model.testsuite.LoadTestRunner;
import com.eviware.soapui.tools.SoapUILoadTestRunner;

public class JUnitSoapUiLoadTestRunner extends SoapUILoadTestRunner {
	private static final String PROPERTY_ASSERTION_PREFIX = "com.eviware.soapui.impl.wsdl.loadtest.assertions.TestStep";
	private static final double NANOTIME_TO_SECONDS = 1000000000.0;
	private String resultsPath = null;
	private JUnitOutput jUnit = null;
	private String currentTestProjectName = null;	// Maps to JUnit package
	private String currentTestSuiteName = null;	// Maps to JUnit class/test suite
	private String currentTestCaseName = null;	// Maps to JUnit test
	private String currentLoadTestName = null;	// Maps to JUnit test
	private boolean hasErrors = false;
	private long loadTestStartTime = 0;
	private long loadTestStopTime = 0;
	private long testSuiteStartTime = 0;
	private List<String> messages = new ArrayList<String>();
	
	public boolean getHasErrors() { return hasErrors; }
	
	public boolean runNow(String resultsPath) throws Exception {
		try {
			this.resultsPath = resultsPath;
			jUnit = null;
			currentTestProjectName = currentTestSuiteName = null;
			currentTestCaseName = currentLoadTestName = null;
			hasErrors = false;
			testSuiteStartTime = System.nanoTime();
			return run();
		} finally {
			addJUnitTest();
			saveJUnitTestSuite();
		}
	}
	
	@Override
	public void beforeLoadTest(LoadTestRunner loadTestRunner, LoadTestRunContext context) {
		super.beforeLoadTest(loadTestRunner, context);
		
		LoadTest loadTest = loadTestRunner.getLoadTest();
		ModelItem testCase = loadTest.getParent();
		ModelItem testSuite = testCase.getParent();
		ModelItem testProject = testSuite.getParent();
		
		addJUnitTest();
		
		if (jUnit == null || !testProject.getName().equals(currentTestProjectName) || !testSuite.getName().equals(currentTestSuiteName)) {
			saveJUnitTestSuite();
			testSuiteStartTime = System.nanoTime();
			currentTestProjectName = testProject.getName();
			currentTestSuiteName = testSuite.getName();
			jUnit = new JUnitOutput(resultsPath, "LOAD", "load." + testProject.getName(), testSuite.getName());
		}

		currentTestCaseName = testCase.getName();
		currentLoadTestName = loadTest.getName();
		loadTestStartTime = System.nanoTime(); 
	}
	
	@Override
	public void afterLoadTest(LoadTestRunner loadTestRunner, LoadTestRunContext context) {
		loadTestStopTime = System.nanoTime();
		
		for (String propertyName : context.getPropertyNames()) {
			String property = context.getProperty(propertyName).toString();
			if (propertyName.startsWith(PROPERTY_ASSERTION_PREFIX)) propertyName = propertyName.substring(PROPERTY_ASSERTION_PREFIX.length());
			messages.add(propertyName + " had " + property + " errors.");
		}
		
		super.afterLoadTest(loadTestRunner, context);
	}
	
	private void saveJUnitTestSuite() {
		if (jUnit == null) return;
		
		long testSuiteStopTime = System.nanoTime();
		double testSuiteElapsedTime = (testSuiteStopTime - testSuiteStartTime) / NANOTIME_TO_SECONDS;
		try {
			jUnit.saveTestSuite(testSuiteElapsedTime);
		} catch (IOException ex) {
		}
		jUnit = null;
	}
	
	private void addJUnitTest() {
		if (jUnit == null) return;
		
		double loadTestElapsedTime = (loadTestStopTime - loadTestStartTime) / NANOTIME_TO_SECONDS; 
		try (BufferedReader reader = new BufferedReader(new FileReader(
				resultsPath + "/" + currentLoadTestName.replaceAll(" ",  "_") + "-statistics.txt"))) {
			reader.readLine();	// Skip the heading
			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(",");
				if (!fields[0].equals("TestCase:")) continue;
				if (fields[9].equals("0")) {
					jUnit.addTestSuccess(currentLoadTestName + " (" + currentTestCaseName + ")", loadTestElapsedTime);
				} else {
					hasErrors = true;
					double failureRate = Double.parseDouble(fields[9]) * 100 / Double.parseDouble(fields[5]);
					String description = fields[9] + " errors (" + String.format("%1$,.0f", failureRate) + "%)\n" 
							+ fields[5] + " runs, " + fields[3] + "ms average time, " 
							+ fields[6] + " transactions per second";
					for (String message: messages) {
						description += "\n" + message;
					}
					messages.clear();
					jUnit.addTestFailure(currentLoadTestName + " (" + currentTestCaseName + ")", 
							loadTestElapsedTime, "Load test failed", "", description);
				}
				break;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
}
