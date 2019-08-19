package paymentgateway.server;

public enum HeaderField {
	CONTENT_TYPE("Content-Type"),
	ALLOW_CROSS_ORIGIN("Access-Control-Allow-Origin"),
	ALLOW_METHODS("Access-Control-Allow-Methods"),
	ALLOW_HEADERS("Access-Control-Allow-Headers"),
	ACCOUNT_KEY("Account-Key"),
	REQUEST_METHOD("Access-Control-Request-Method"),
	REQUEST_HEADERS("Access-Control-Request-Headers"),
	;
	
	public final String text;
	
	HeaderField(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
