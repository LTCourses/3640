package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JUnitOutput {
	private String outputDirectory;
	private String filePrefix;
	private String packageName;
	private String className;
	private int numberOfTests = 0;
	private int numberOfFailures = 0;
	private int numberOfErrors = 0;
	private int numberSkipped = 0;
	private List<String> tests = new ArrayList<String>();
	
	public JUnitOutput(String outputDirectory, String filePrefix, String packageName, String className) {
		this.outputDirectory = outputDirectory;
		this.filePrefix = filePrefix;
		this.packageName = packageName;
		this.className = className;
	}
	
	public void addTestSuccess(String testName, double testCaseTimeInSeconds) {
		tests.add("<testcase classname='" + packageName + "." + className + "' name='" 
				+ testName + "' time='" + String.format("%1$,.3f", testCaseTimeInSeconds) + "' />");
		numberOfTests++;
	}
	
	public void addTestFailure(String testName, double testCaseTimeInSeconds, String message, String type, String description) {
		tests.add("<testcase classname='" + packageName + "." + className + "' name='" 
				+ testName + "' time='" + String.format("%1$,.3f", testCaseTimeInSeconds) 
				+ "'><failure message='" + message + "' type='" + type + "'>" 
				+ description + "</failure></testcase>");
		numberOfTests++;
		numberOfFailures++;
	}
	
	public void saveTestSuite(double testSuiteTimeInSeconds) throws IOException {
		String fileName = outputDirectory + "/" + filePrefix + "-" + className.replaceAll(" ", "") + ".xml";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			// Doesn't support hostname
			writer.write("<?xml version='1.0' encoding='UTF-8' ?><testsuite errors='" + numberOfErrors
					+ "' failures='" + numberOfFailures + "' name='" + packageName + "." + className
					+ "' skipped='" + numberSkipped + "' tests='" + numberOfTests + "' time='" 
					+ String.format("%1$,.3f", testSuiteTimeInSeconds) + "' timestamp='" 
					+ new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()) + "'>");
			for (String test : tests) {
				writer.write(test);
			}
			writer.write("</testsuite>");
		}
	}
}
