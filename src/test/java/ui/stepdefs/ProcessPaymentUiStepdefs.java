package ui.stepdefs;

import static org.junit.Assert.*;
import java.io.IOException;
import org.openqa.selenium.WebDriver;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ui.pages.Drivers;
import ui.pages.PaymentPage;
import utilities.CreditCardDatabaseSupport;

public class ProcessPaymentUiStepdefs {
	private WebDriver driver;
	private PaymentPage page;
	private CreditCardDatabaseSupport dbSupport;
	
	@After
	public void cleanup(Scenario scenario) throws IOException {
		if (page != null) {
			if (scenario.isFailed()) {
				page.saveScreenshot(scenario.getId());
			}
			page.close();
			page = null;
		}
		
		if (dbSupport != null) {
			dbSupport.close();
			dbSupport = null;
		}
	}
	
	@Given("^an account (\\d*)/\"(.*)\"/\"(.*)\"$")
	public void anAccount(int accountId, String accountName, String accountKey) throws IOException {
		if (dbSupport == null) dbSupport = new CreditCardDatabaseSupport("deploy/db/");
		dbSupport.addAccount(accountId, accountName, accountKey);
	}
	
	@Given("^an existing credit card \"(.*)\"/\"(.*)\"/\"(.*)\"/\\$(.*)/(true|false)$")
	public void anExistingCreditCard(String type, String number, String expiry, double creditLimit, boolean isBlocked) throws IOException {
		if (dbSupport == null) dbSupport = new CreditCardDatabaseSupport("deploy/db/");
		dbSupport.addCreditCard(type, number, expiry, creditLimit, isBlocked);
	}
	
	@Given("^I am using Firefox$")
	public void iAmUsingFirefox() {
		driver = Drivers.getFirefox();
	}
	
	@Given("^I open the payment page on \"(.*)\"$")
	public void iOpenThePaymentPageOn(String hostUrl) {
		page = new PaymentPage(driver);
		page.open(hostUrl);
	}
	
	@Given("^I enter \"([^\"]*)\" as the account key$")
	public void iEnterTheAccountKey(String accountKey) {
		page.setAccountKey(accountKey);
	}

	@Given("^I enter \"([^\"]*)\" as the number$")
	public void iEnterTheNumber(String number) {
		page.setNumber(number);
	}

	@Given("^I enter \"([^\"]*)\" as the expiry$")
	public void iEnterTheExpiry(String expiry) {
		page.setExpiry(expiry);
	}

	@Given("^I enter \\$([^\"]*) as the amount$")
	public void iEnterTheAmount(String amount) {
		page.setAmount(amount);
	}

	@When("^I click the pay button$")
	public void iClickThePayButton() {
		page.clickPay();
	}

	@Then("^the status should be \"([^\"]*)\"$")
	public void theStatusShouldBe(String expectedStatus) {
		assertEquals(expectedStatus, page.getLastStatus());
	}
	
	@Then("^the message should be \"([^\"]*)\"$")
	public void theMessageShouldBe(String expectedMessage) {
		assertEquals(expectedMessage, page.getLastMessage());
	}
	
	@Then("^the type should be \"([^\"]*)\"$")
	public void theTypeShouldBe(String expectedType) {
		assertEquals(expectedType, page.getLastType());
	}
	
	@Then("^the funds should be \"([^\"]*)\"$")
	public void theFundsShouldBe(String expectedFunds) {
		assertEquals(expectedFunds, page.getLastFunds());
	}
}
