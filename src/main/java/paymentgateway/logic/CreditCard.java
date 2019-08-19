package paymentgateway.logic;

import java.time.LocalDate;

public class CreditCard {
	private final String number;
	private final String expiry;
	private double creditLimit;
	private boolean isBlocked;
	
	public CreditCard(String number, String expiry, double creditLimit, boolean isBlocked) {
		this.number = number;
		this.expiry = expiry;
		this.creditLimit = creditLimit;
		this.isBlocked = isBlocked;
	}
	
	public boolean matches(String number, String expiry) {
		return this.number.equals(number) && this.expiry.equals(expiry);
	}
	
	public boolean isExpired() {
		LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate expires = LocalDate.of(2000 + Integer.parseInt(expiry.substring(2)), 
				Integer.parseInt(expiry.substring(0, 2)),
				1);
		return expires.isBefore(thisMonth);
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public boolean isPaymentWithinLimit(String amount) {
		return Double.parseDouble(amount) <= creditLimit;
	}

	public boolean createPayment(String amount) {
		return true;
	}
}
