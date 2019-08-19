package paymentgateway.logic;

public class CardValidator extends ResponseBase {
	// Used for calculating the checksum.
	private static final int SET_1DIGIT = 10;

	public boolean validate(String number, String expiry, String amount) {
		setCardType(null);
		return validateArguments(number, expiry, amount)
				&& validateCardNumberChecksum(number)
				&& validateCardNumberType(number)
				;
	}
	
	private boolean validateArguments(String number, String expiry, String amount) {
		if (number == null || number.equals("")) {
			setResponse(PaymentStatus.NO_NUMBER);
		} else if (!number.matches("^\\d*$")) {
			setResponse(PaymentStatus.INVALID_NUMBER_FORMAT);
		} else if (expiry == null || expiry.equals("")) {
			setResponse(PaymentStatus.NO_EXPIRY);
		} else if (!expiry.matches("^(0[1-9]|1[012])\\d{2}$")) {
			setResponse(PaymentStatus.INVALID_EXPIRY_FORMAT);
		} else if (amount == null || amount.equals("")) {
			setResponse(PaymentStatus.NO_AMOUNT);
		} else if (amount != null && !amount.matches("^\\d+(\\.\\d{2})?$")) {
			setResponse(PaymentStatus.INVALID_AMOUNT_FORMAT);
		} else {
			return true;
		}
		// Only gets here if one of the failure conditions is processed
		return false;
	}
	
	private boolean validateCardNumberChecksum(String number) {
		int sum = 0;
		boolean isOdd = true;
		int digit0 = (int)'0';
		for (int iDigit = number.length() - 2;  iDigit >= 0;  --iDigit) {
			int add = (int)number.charAt(iDigit) - digit0;
			if (isOdd) {
				add = add * 2;
			}
			isOdd = !isOdd;
			if (add >= SET_1DIGIT) {
				add = add + 1 - SET_1DIGIT;
			}
			sum += add;
		}
		while (sum > SET_1DIGIT) {
			sum = sum - SET_1DIGIT;
		}
		int digitValue = SET_1DIGIT - sum + digit0;
		if (digitValue == (int)number.charAt(number.length() - 1)) {
			return true;
		} else {
			setResponse(PaymentStatus.INVALID_NUMBER);
			return false;
		}
	}
	
	private boolean validateCardNumberType(String number) {
		for (CardNumberRule rule: CardNumberRule.values()) {
			int prefix = Integer.parseInt(number.substring(0, rule.prefixLength));
			if (rule.firstPrefix <= prefix && prefix <= rule.lastPrefix) {
				setCardType(rule.cardType);
				int length = number.length();
				if (rule.minLength <= length && length <= rule.maxLength) {
					return true;
				}
				else {
					if (rule.minLength == rule.maxLength) {
						setResponse(PaymentStatus.INVALID_NUMBER_FORMAT, "The number must be a sequence of " + rule.minLength + " digits");
					} else {
						setResponse(PaymentStatus.INVALID_NUMBER_FORMAT, "The number must be a sequence of between "
								+ rule.minLength + " and " + rule.maxLength + " digits");
					}
					return false;
				}
			}
		}
		setResponse(PaymentStatus.UNRECOGNISED_ISSUER);
		return false;
	}
}
