package paymentgateway.server;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import paymentgateway.logic.DatabaseExpiryFixerModule;
import paymentgateway.logic.InjectorConfiguration;

public class AppOther {
	private static final int DEFAULT_PORT_OTHER = 5553;
	private static final String DEFAULT_FOLDER_DB = "/db/";
	
	public static void main(String[] args) throws InterruptedException, URISyntaxException {
		int portOther = Settings.getSettingInt("PG_PORT_OTHER", DEFAULT_PORT_OTHER);
		String folderDb = Settings.getSettingString("PG_FOLDER_DB", null);
		if (folderDb == null) {
			folderDb = new File(AppOther.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + DEFAULT_FOLDER_DB;
		}

		LoggerUtils.setLoggingProperties();
		Logger logger = Logger.getLogger("paymentgateway.server");
		logger.info("AppOther is starting");
		
		if (Settings.getSettingString("PG_EXPIRY", "").equalsIgnoreCase("fix")) {
			InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
		} else {
			InjectorConfiguration.configureWithStandardModule();
		}
		@SuppressWarnings("unused")
		ApiServer other = InjectorConfiguration.getInstance(ApiServer.class).start(portOther, folderDb);
		
		System.out.println("Other Payment Gateway");
		System.out.println("Other server running on port " + portOther);
		System.out.println("Press Ctrl+C to stop");
		Thread.sleep(Long.MAX_VALUE);
	}
}
