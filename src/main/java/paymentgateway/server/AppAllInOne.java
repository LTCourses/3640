package paymentgateway.server;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import paymentgateway.logic.CardIssuer;
import paymentgateway.logic.DatabaseExpiryFixerModule;
import paymentgateway.logic.InjectorConfiguration;

public class AppAllInOne {
	private static final String DEFAULT_ISSUER = CardIssuer.VISA.toString();
	private static final int DEFAULT_PORT_UI = 5551;
	private static final int DEFAULT_PORT_API = 5552;
	private static final int DEFAULT_PORT_UI_API = 0;	// 0 = use portApi
	private static final int DEFAULT_PORT_OTHER = 5553;
	private static final String DEFAULT_FOLDER_WEBCONTENT = "/webapp";
	private static final String DEFAULT_FOLDER_DB = "/db/";
	
	public static void main(String[] args) throws InterruptedException, URISyntaxException {
		CardIssuer issuer = CardIssuer.valueOf(Settings.getSettingString("PG_ISSUER", DEFAULT_ISSUER));
		int portUi = Settings.getSettingInt("PG_PORT_UI", DEFAULT_PORT_UI);
		int portApi = Settings.getSettingInt("PG_PORT_API", DEFAULT_PORT_API);
		int portUiApi = Settings.getSettingInt("PG_PORT_UI_API", DEFAULT_PORT_UI_API);
		if (portUiApi == 0) portUiApi = portApi;
		int portOther = Settings.getSettingInt("PG_PORT_OTHER", DEFAULT_PORT_OTHER);
		String folderWebcontent = Settings.getSettingString("PG_FOLDER_WEBCONTENT", null);
		if (folderWebcontent == null) {
			folderWebcontent = new File(AppAllInOne.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + DEFAULT_FOLDER_WEBCONTENT;
		}
		String folderDb = Settings.getSettingString("PG_FOLDER_DB", null);
		if (folderDb == null) {
			folderDb = new File(AppAllInOne.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + DEFAULT_FOLDER_DB;
		}
		
		LoggerUtils.setLoggingProperties();
		Logger logger = Logger.getLogger("paymentgateway.server");
		logger.info("AppAllInOne is starting");
		
		if (Settings.getSettingString("PG_EXPIRY", "").equalsIgnoreCase("fix")) {
			InjectorConfiguration.configure(new DatabaseExpiryFixerModule());
		} else {
			InjectorConfiguration.configureWithStandardModule();
		}
		@SuppressWarnings("unused")
		ApiServer otherApi = InjectorConfiguration.getInstance(ApiServer.class).start(portOther, folderDb, false);
		@SuppressWarnings("unused")
		ApiServer api = InjectorConfiguration.getInstance(ApiServer.class).start(portApi, "http://localhost:" + portOther, issuer, folderDb);
		@SuppressWarnings("unused")
		UiServer ui = InjectorConfiguration.getInstance(UiServer.class).start(portUi, null, portUiApi, folderWebcontent);
		
		System.out.println(issuer + " Payment Gateway");
		System.out.println("UI server running on port " + portUi);
		System.out.println("API server running on port " + portApi);
		System.out.println("Press Ctrl+C to stop");
		Thread.sleep(Long.MAX_VALUE);
	}
}
