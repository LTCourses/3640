package paymentgateway.server;

import java.util.logging.Logger;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import com.google.inject.Inject;

public class UiServer {
	private static final Logger logger = Logger.getLogger("paymentgateway.server");
	private final ConfigHandler configHandler;
	private final StaticFileHandler webContentHandler;
	private final FileNotFoundHandler fileNotFoundHandler;
	private WebServer server;
	
	@Inject
	public UiServer(ConfigHandler configHandler, StaticFileHandler webContentHandler, FileNotFoundHandler fileNotFoundHandler) {
		this.configHandler = configHandler;
		this.webContentHandler = webContentHandler;
		this.fileNotFoundHandler = fileNotFoundHandler;
	}
	
	public UiServer start(int httpPort, String apiHostname, int apiPort, String folderWebContent) {
		if (folderWebContent.endsWith("/")) folderWebContent = folderWebContent.substring(0, folderWebContent.length() - 1);
		server = WebServers.createWebServer(httpPort)
				.add(configHandler.setApiHost(apiHostname, apiPort))
				.add(webContentHandler.setPaths("", folderWebContent))
				.add(fileNotFoundHandler.setNotFoundFile(folderWebContent + "/404.html"));
		server.start();
		logger.config("UiServer is running on port " + httpPort + ", connecting to port " + apiPort
				+ (apiHostname == null ? "" : " on " + apiHostname));
		return this;
	}
	
	public void stop() {
		server.stop();
	}
}
