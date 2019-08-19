package paymentgateway.logic;

public class ResponseBase {
	private PaymentStatus paymentStatus;
	private String message;
	private CardIssuer cardType;
	private FundsStatus fundsStatus;
	private String testModeMessage;
	
	protected void setResponse(PaymentStatus paymentStatus) {
		setResponse(paymentStatus, paymentStatus.message);
	}

	protected void setResponse(PaymentStatus paymentStatus, String message) {
		this.paymentStatus = paymentStatus;
		this.message = message;
	}
	
	protected void setCardType(CardIssuer cardType) {
		this.cardType = cardType;
	}
	
	protected void setFundsStatus(FundsStatus fundsStatus) {
		this.fundsStatus = fundsStatus;
	}
	
	protected void setTestMode(String message) { testModeMessage = message; }
	protected void clearTestMode() { testModeMessage = null; }
	
	public PaymentStatus getPaymentStatus() { return paymentStatus; }
	public String getMessage() { return message; }
	public CardIssuer getCardType() { return cardType; }
	public FundsStatus getFundsStatus() { return fundsStatus; }
	public String getTestModeMessage() { return testModeMessage; }
}
