package paymentgateway.logic;

import java.util.logging.Logger;

import com.google.inject.Inject;

public class PaymentLogic extends ResponseBase {
	private static final Logger logger = Logger.getLogger("paymentgateway.logic");
	private final CardValidator cardValidator;
	private final OtherBank otherBank;
	private final CreditCardDatabase db;
	private CardIssuer cardIssuer;
	private boolean enableConsoleLogging = true;

	@Inject
	public PaymentLogic(CardValidator cardValidator, OtherBank otherBank, CreditCardDatabase db) {
		this.cardValidator = cardValidator;
		this.otherBank = otherBank;
		this.db = db;
	}
	
	public void setRemoteHost(String remoteHost) {
		this.otherBank.setRemoteHost(remoteHost);
	}
	
	public void setCardIssuer(CardIssuer cardIssuer) {
		this.cardIssuer = cardIssuer;
	}
	
	public void openDb(String dbPath, String dbName) {
		this.db.open(dbPath, dbName);
	}
	
	public void disableConsoleLogging() {
		enableConsoleLogging = false;
	}

	public void process(boolean isValidateOnly, String number, String expiry, String amount, String accountKey) {
		setFundsStatus(null);
		clearTestMode();
		String account = validateAccount(accountKey);
		if (account == null && accountKey != null & accountKey != "") {
			setCardType(null);
			setResponse(PaymentStatus.UNAUTHORISED);
			if (enableConsoleLogging)
				logger.severe("Invalid account key " + accountKey);
		} else {
			boolean isValid = cardValidator.validate(number, expiry, amount); 
			setCardType(cardValidator.getCardType());
			if (isValid) {
				if (account == null){
					setTestMode("This call is in test mode, and only generic validations have been performed.");
					setResponse(PaymentStatus.APPROVED);
				} else {
					if (account.equalsIgnoreCase("TEST")) setTestMode("This call is in test mode.");
					if (cardIssuer == null || cardIssuer == getCardType()) {
						processPaymentHere(isValidateOnly, number, expiry, amount);
					} else {
						sendRequestToOtherBank(isValidateOnly, number, expiry, amount);
					}
				}
			} else {
				setResponse(cardValidator.getPaymentStatus(), cardValidator.getMessage());
			}
		}
		if (enableConsoleLogging) {
			String fundsStatus = getFundsStatus() == null ? "" : getFundsStatus().toString();
			logger.fine("PAYMENT: " + account + "/" + isValidateOnly + "/" + number + "/" + expiry + "/" + amount + " => " + getPaymentStatus().toString() + "/" + getMessage() + "/" + getCardType() + "/" + fundsStatus + "/" + (getTestModeMessage() == null ? "Live" : "Test"));
		}
	}
	
	private String validateAccount(String accountKey) {
		return db.getAccount(accountKey);
	}
	
	private void processPaymentHere(boolean isValidateOnly, String number, String expiry, String amount) {
		CreditCard card = db.getCreditCard(number, expiry);
		if (card == null) {
			setResponse(PaymentStatus.INVALID_CARD_OR_EXPIRY);
		} else if (card.isExpired()) {
			setResponse(PaymentStatus.EXPIRED_CARD);
		} else if (card.isBlocked()) {
			setResponse(PaymentStatus.CARD_BLOCKED);
		} else if (!card.isPaymentWithinLimit(amount)) {
			setResponse(PaymentStatus.OVER_LIMIT);
		} else if (card.createPayment(amount)) {
			setResponse(PaymentStatus.APPROVED);
			setFundsStatus(isValidateOnly ? FundsStatus.AVAILABLE : FundsStatus.PAID);
		} else {
			setResponse(PaymentStatus.DECLINED);
		}
	}
	
	private void sendRequestToOtherBank(boolean isValidateOnly, String number, String expiry, String amount) {
		logger.fine("Sending request to " + getCardType().toString());
		otherBank.sendPayment(isValidateOnly, number, expiry, amount);
		setResponse(otherBank.getPaymentStatus(), otherBank.getMessage());
		setFundsStatus(otherBank.getFundsStatus());
	}
}
