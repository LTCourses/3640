package paymentgateway.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreditCardDatabase {
	private static final Logger logger = Logger.getLogger("paymentgateway.logic");
	protected static final String EXPIRY_PLUS_3_YEARS = getExpiry(0, 3);
	protected static final String EXPIRY_PLUS_5_YEARS = getExpiry(0, 5);
	protected static final String EXPIRY_MINUS_1_MONTH = getExpiry(-1, 0);
	
	private String accountPath;
	private String cardPath;
	
	public CreditCardDatabase open(String dbPath, String dbName) {
		if (!dbPath.endsWith("/")) dbPath += "/";
		this.accountPath = dbPath + "ACCOUNT.csv";
		this.cardPath = dbPath + dbName + ".csv";
		return this;
	}
	
	public String getAccount(String accountKey) {
		if (accountKey == null) accountKey = "";
		try (BufferedReader reader = new BufferedReader(new FileReader(accountPath))) {
			String line;
			reader.readLine();	// Skip the heading
			int lineNumber = 2;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(",");
				String testKey = "";
				if (fields.length == 3) testKey = fields[2];
				else if (fields.length != 2 || !line.endsWith(",")) {
					logger.severe("Account database file line " + lineNumber + " is invalid");
					return null;
				}
				if (testKey.equals(accountKey)) {
					return fields[1];
				}
				lineNumber++;
			}
		} catch (FileNotFoundException e) {
			logger.logp(Level.SEVERE, CreditCardDatabase.class.getName(), "getAccount", e.getMessage(), e);
		} catch (IOException e) {
			logger.logp(Level.SEVERE, CreditCardDatabase.class.getName(), "getAccount", e.getMessage(), e);
		}
		return null;
	}
	
	public CreditCard getCreditCard(String number, String expiry) {
		try (BufferedReader reader = new BufferedReader(new FileReader(cardPath))) {
			String line;
			reader.readLine();	// Skip the heading
			int lineNumber = 2;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(",");
				if (fields.length != 5) {
					logger.severe("Card database file line " + lineNumber + " is invalid");
					return null;
				}
				if (fields[1].equals(number)) {
					String cardExpiry = fields[2];
					if (cardExpiry.equals("0399")) cardExpiry = EXPIRY_PLUS_3_YEARS;
					else if (cardExpiry.equals("0599")) cardExpiry = EXPIRY_PLUS_5_YEARS;
					else if (cardExpiry.equals("0199")) cardExpiry = EXPIRY_MINUS_1_MONTH;
					if (expiry.equals(cardExpiry)) {
						return new CreditCard(number, expiry, Double.parseDouble(fields[3]), Boolean.parseBoolean(fields[4]));
					}
				}
				lineNumber++;
			}
		} catch (FileNotFoundException e) {
			logger.logp(Level.SEVERE, CreditCardDatabase.class.getName(), "getCreditCard", e.getMessage(), e);
		} catch (IOException e) {
			logger.logp(Level.SEVERE, CreditCardDatabase.class.getName(), "getCreditCard", e.getMessage(), e);
		}
		return null;
	}
	
	private static String getExpiry(int addMonths, int addYears) {
		LocalDate thisMonth = LocalDate.now();
		int month = thisMonth.getMonthValue();
		int year = thisMonth.getYear();
		month += addMonths;
		year += addYears;
		while (month > 12) {
			month -= 12;
			year++;
		}
		while (month < 1) {
			month += 12;
			year--;
		}
		return String.format("%02d%02d", month, year - 2000);
	}
}
