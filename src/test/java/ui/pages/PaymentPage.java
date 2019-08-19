package ui.pages;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PaymentPage {
	private WebDriver driver;
	
	public PaymentPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void open(String hostUrl) {
		driver.get(hostUrl);
		if (!"Process Payment".equals(driver.getTitle())) {
			throw new IllegalStateException("The payment page did not load correctly.");
		}
	}
	
	public void close() {
		driver = null;
	}
	
	public void saveScreenshot(String fileName) throws IOException {
		File directory = new File("results\\screenshots");
		if (!directory.exists()) {
			directory.mkdir();
		}
		File image = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String time = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		FileUtils.copyFile(image, new File("results\\screenshots\\" + time + "-" + fileName + ".jpg"));
	}
	
	public void setAccountKey(String accountKey) {
		setFieldById("AccountKey", accountKey);
	}
	
	public void setNumber(String number) {
		setFieldById("Number", number);
	}
	
	public void setExpiry(String expiry) {
		setFieldById("Expiry", expiry);
	}
	
	public void setAmount(String amount) {
		setFieldById("Amount", amount);
	}
	
	private void setFieldById(String id, String value) {
		WebElement element = driver.findElement(By.id(id));
		element.clear();
		element.sendKeys(value);
	}
	
	public void clickPay() {
//		int numberOfElements = driver.findElements(By.tagName("tr")).size();
		driver.findElement(By.id("Pay")).click();
//		WebDriverWait wait = new WebDriverWait(driver, 1);
//		wait.until(ExpectedConditions.numberOfElementsToBe(By.tagName("tr"), numberOfElements + 1));
	}
	
	public String getLastStatus() {
		return driver.findElement(By.xpath("//table[@id='Results']/tbody/tr[2]/td[7]")).getText();
	}
	
	public String getLastMessage() {
		return driver.findElement(By.xpath("//table[@id='Results']/tbody/tr[2]/td[8]")).getText();
	}
	
	public String getLastType() {
		return driver.findElement(By.xpath("//table[@id='Results']/tbody/tr[2]/td[9]")).getText();
	}
	
	public String getLastFunds() {
		return driver.findElement(By.xpath("//table[@id='Results']/tbody/tr[2]/td[10]")).getText();
	}
}
