package acceptance.stepdefs;

import static org.junit.Assert.*;

import java.io.IOException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import paymentgateway.logic.CardIssuer;
import paymentgateway.logic.DatabaseExpiryFixerModule;
import paymentgateway.logic.FundsStatus;
import paymentgateway.logic.InjectorConfiguration;
import paymentgateway.logic.PaymentLogic;
import paymentgateway.logic.PaymentStatus;
import paymentgateway.server.LoggerUtils;
import utilities.CreditCardDatabaseSupport;

public class ProcessPaymentStepdefs {
	private boolean isValidateOnly;
	private String number, expiry, amount, accountKey;
	private CreditCardDatabaseSupport dbSupport;
	private PaymentLogic sut;
	
	@Before
	public void setup() {
		isValidateOnly = false;
		number = expiry = amount = accountKey = "";
		
		LoggerUtils.setLoggingProperties();
		InjectorConfiguration.configureWithStandardModule();
		InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
		
		sut = InjectorConfiguration.getInstance(PaymentLogic.class);
		sut.openDb("deploy/db", "ALL");
	}
	
	@After
	public void cleanup() {
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
//		CreditCard creditCard = new CreditCard(number, expiry, creditLimit, isBlocked);
//		fakeDb.addCreditCard(creditCard);
	}
	
	@Given("^the account key is \"(.*)\"$")
	public void theAccountKeyIs(String accountKey) {
		this.accountKey = accountKey;
	}

	@Given("^the card number is \"(.*)\"$")
	public void theCardNumberIs(String number) {
		this.number = number;
	}

	@Given("^the expiry is \"(.*)\"$")
	public void theExpiryIs(String expiry) {
		this.expiry = expiry;
	}

	@Given("^the amount is \\$(.*)$")
	public void theAmountIs$(String amount) {
		this.amount = amount;
	}
	
	@Given("^the transaction is validate only$")
	public void theTransactionIsValidateOnly() {
		this.isValidateOnly = true;
	}

	@When("^I process the payment$")
	public void iProcessThePayment() {
		sut.process(isValidateOnly, number, expiry, amount, accountKey);
	}

	@Then("^the payment status should be \"([^\"]*)\"$")
	public void thePaymentStatusShouldBe(String expectedStatus) throws Throwable {
		PaymentStatus expectedPaymentStatus = expectedStatus == null ? null : PaymentStatus.valueOf(expectedStatus);
		assertEquals(expectedPaymentStatus, sut.getPaymentStatus());
	}

	@Then("^the card type should be \"([^\"]*)\"$")
	public void theCardTypeShouldBe(String expectedType) throws Throwable {
		CardIssuer expectedCardType = expectedType == null ? null : CardIssuer.valueOf(expectedType);
		assertEquals(expectedCardType, sut.getCardType());
	}

	@Then("^the card type should be blank$")
	public void theCardTypeShouldBeBlank() throws Throwable {
		assertEquals(null, sut.getCardType());
	}

	@Then("^the funds should be \"([^\"]*)\"$")
	public void theFundsShouldBe(String expectedFunds) throws Throwable {
		assertEquals(FundsStatus.valueOf(expectedFunds), sut.getFundsStatus());
	}

	@Then("^the funds should be blank$")
	public void theFundsShouldBeBlank() throws Throwable {
		assertEquals(null, sut.getFundsStatus());
	}
}
