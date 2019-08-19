package paymentgateway.server;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import paymentgateway.logic.InjectorConfiguration;

public class AppUi {
	private static final int DEFAULT_PORT_UI = 5551;
	private static final String DEFAULT_HOSTNAME_API = null;
	private static final int DEFAULT_PORT_UI_API = 5552;
	private static final String DEFAULT_FOLDER_WEBCONTENT = "/webapp";
	
	public static void main(String[] args) throws InterruptedException, URISyntaxException {
		int portUi = Settings.getSettingInt("PG_PORT_UI", DEFAULT_PORT_UI);
		String hostnameApi = Settings.getSettingString("PG_HOSTNAME_API", DEFAULT_HOSTNAME_API);
		int portUiApi = Settings.getSettingInt("PG_PORT_UI_API", DEFAULT_PORT_UI_API);
		String folderWebcontent = Settings.getSettingString("PG_FOLDER_WEBCONTENT", null);
		if (folderWebcontent == null) {
			folderWebcontent = new File(AppUi.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + DEFAULT_FOLDER_WEBCONTENT;
		}

		LoggerUtils.setLoggingProperties();
		Logger logger = Logger.getLogger("paymentgateway.server");
		logger.info("AppUi is starting");
		
		InjectorConfiguration.configureWithStandardModule();
		@SuppressWarnings("unused")
		UiServer ui = InjectorConfiguration.getInstance(UiServer.class).start(portUi, hostnameApi, portUiApi, folderWebcontent);
		
		System.out.println("Payment Gateway");
		System.out.println("UI server running on port " + portUi);
		System.out.println("Press Ctrl+C to stop");
		Thread.sleep(Long.MAX_VALUE);
	}
}
