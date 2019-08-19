package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.io.Files;

public class CreditCardDatabaseSupport {
	private String dbPath;
	private File currentCards, originalCards, tempCards;
	private File currentAccounts, originalAccounts, tempAccounts;
	
	public CreditCardDatabaseSupport(String dbPath) {
		this.dbPath = dbPath;
		if (!this.dbPath.endsWith("/")) this.dbPath += "/";
	}

	public void close() {
		closeFiles(originalAccounts, currentAccounts, tempAccounts);
		closeFiles(originalCards, currentCards, tempCards);
		originalAccounts = currentAccounts = tempAccounts = null;
		originalCards = currentCards = tempCards = null;
	}
	
	private void closeFiles(File original, File current, File temp) {
		if (original != null) {
			current.delete();
			original.renameTo(current);
		}
	}
	
	public void addAccount(int id, String name, String key) throws IOException {
		if (originalAccounts == null) {
			currentAccounts = new File(dbPath + "ACCOUNT.csv");
			originalAccounts = new File(dbPath + "ACCOUNT-ORIGINAL.csv");
			tempAccounts = new File(dbPath + "temp.csv");
			Files.copy(currentAccounts, originalAccounts);
		}
		insertLine(currentAccounts, tempAccounts,
				id + "," + name + "," + key);
		
	}
	
	public void addCreditCard(String type, String number, String expiry, double creditLimit, boolean isBlocked) throws IOException {
		if (originalCards == null) {
			currentCards = new File(dbPath + "ALL.csv");
			originalCards = new File(dbPath + "ALL-ORIGINAL.csv");
			tempCards = new File(dbPath + "temp.csv");
			Files.copy(currentCards, originalCards);
		}
		insertLine(currentCards, tempCards, 
				"\"" + type + "\"," + number + "," + expiry + "," + creditLimit + "," + isBlocked);
	}
	
	private void insertLine(File current, File temp, String lineToAdd) throws IOException {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(current));
			writer = new BufferedWriter(new FileWriter(temp));
			writer.write(reader.readLine());
			writer.newLine();
			writer.write(lineToAdd);
			writer.newLine();
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}
			writer.flush();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		current.delete();
		temp.renameTo(current);
	}
}
