package paymentgateway.logic;

public enum PaymentStatus {
	NO_NUMBER("No number was provided"),
	INVALID_NUMBER_FORMAT("The number must be a sequence of between 13 and 19 digits"),
	INVALID_NUMBER("The number is not valid"),
	NO_EXPIRY("No expiry was provided"),
	INVALID_EXPIRY_FORMAT("The expiry must be a valid 4 digit month/year"),
	NO_AMOUNT("No amount was provided"),
	INVALID_AMOUNT_FORMAT("The amount must be a valid number with no more than 2 decimal places"),
	UNRECOGNISED_ISSUER("The number does not match any recognised issuer"),
	INVALID_PARAMETER("A parameter is not recognised"),
	INVALID_REQUEST("This request type is not supported"),
	UNABLE_TO_CONNECT("Unable to connect to the issuing bank"),
	APPROVED("Approved"),
	INVALID_CARD_OR_EXPIRY("The number and/or expiry is invalid"),
	EXPIRED_CARD("The card has expired"),
	CARD_BLOCKED("The card has been blocked"),
	OVER_LIMIT("The credit limit has been exceeded"),
	DECLINED("The payment has been declined"),
	UNAUTHORISED("The account key is not valid"),
	;
	
	public final String message;
	
	PaymentStatus(String message) {
		this.message = message;
	}
}
