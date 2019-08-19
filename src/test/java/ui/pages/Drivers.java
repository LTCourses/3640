package ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class Drivers {
	private static FirefoxDriver firefox = null;
	
	public static WebDriver getFirefox() {
		if (firefox == null) {
			System.setProperty("webdriver.gecko.driver",  "tests\\drivers\\geckodriver.exe");
			firefox = new FirefoxDriver();
		}
		return firefox;
	}
	
	public static void closeFirefox() {
		if (firefox != null) {
			firefox.quit();
			firefox = null;
		}
	}
}
