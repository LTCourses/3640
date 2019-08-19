package paymentgateway.logic;

import java.util.logging.Logger;

import com.google.inject.AbstractModule;

public class DatabaseExpiryFixerModule extends AbstractModule {
	private static class DatabaseExpiryFixer extends CreditCardDatabase {
		@Override
		public CreditCard getCreditCard(String number, String expiry) {
			if (expiry.equals("0199")) expiry = EXPIRY_MINUS_1_MONTH;
			else if (expiry.equals("0399")) expiry = EXPIRY_PLUS_3_YEARS;
			else if (expiry.equals("0599")) expiry = EXPIRY_PLUS_5_YEARS;
			return super.getCreditCard(number, expiry);
		}
	}
	
	protected void configure() {
		bind(CreditCardDatabase.class).to(DatabaseExpiryFixer.class);
	}
}
