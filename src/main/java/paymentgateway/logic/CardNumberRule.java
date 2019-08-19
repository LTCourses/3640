package paymentgateway.logic;

public enum CardNumberRule {
	VISA(1, 4, 4, CardIssuer.VISA, 13, 16),
	MASTERCARD1(2, 51, 55, CardIssuer.MASTERCARD, 16, 19),
	MASTERCARD2(6, 222100, 272099, CardIssuer.MASTERCARD, 16, 19),
	AMEX1(2, 34, 34, CardIssuer.AMEX, 15, 15),
	AMEX2(2, 37, 37, CardIssuer.AMEX, 15, 15),
	DINERS1(3, 300, 305, CardIssuer.DINERS, 14, 14),
	DINERS2(2, 36, 36, CardIssuer.DINERS, 14, 14),
	DISCOVER1(4, 6011, 6011, CardIssuer.DISCOVER, 16, 16),
	DISCOVER2(6, 622126, 622925, CardIssuer.DISCOVER, 16, 16),
	DISCOVER3(3, 644, 649, CardIssuer.DISCOVER, 16, 16),
	DISCOVER4(2, 65, 65, CardIssuer.DISCOVER, 16, 16),
	INSTAPAYMENT(3, 637, 639, CardIssuer.INSTAPAYMENT, 16, 16),
	JCB(4, 3528, 3589, CardIssuer.JCB, 16, 16),
	// LASER1(4, 6304, 6304, CardType.LASER, 16, 19),
	LASER2(4, 6706, 6706, CardIssuer.LASER, 16, 19),
	LASER3(4, 6709, 6709, CardIssuer.LASER, 16, 19),
	LASER4(4, 6771, 6771, CardIssuer.LASER, 16, 19),
	MAESTRO1(4, 5018, 5018, CardIssuer.MAESTRO, 16, 19),
	MAESTRO2(4, 5020, 5020, CardIssuer.MAESTRO, 16, 19),
	MAESTRO3(4, 5038, 5038, CardIssuer.MAESTRO, 16, 19),
	MAESTRO4(4, 5893, 5893, CardIssuer.MAESTRO, 16, 19),
	// MAESTRO5(4, 6304, 6304, CardType.MAESTRO, 16, 19),
	MAESTRO6(4, 6759, 6759, CardIssuer.MAESTRO, 16, 19),
	MAESTRO7(4, 6761, 6761, CardIssuer.MAESTRO, 16, 19),
	;

	public final int prefixLength;
	public final int firstPrefix;
	public final int lastPrefix;
	public final CardIssuer cardType;
	public final int minLength;
	public final int maxLength;

	CardNumberRule(int prefixLength, int firstPrefix, int lastPrefix, 
		CardIssuer cardType, int minLength, int maxLength) {
		this.prefixLength = prefixLength;
		this.firstPrefix = firstPrefix;
		this.lastPrefix = lastPrefix;
		this.cardType = cardType;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}
}
