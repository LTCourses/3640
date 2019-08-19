package paymentgateway.server;

import java.util.logging.Logger;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import com.google.inject.Inject;

import paymentgateway.logic.CardIssuer;

public class ApiServer {
	private static final Logger logger = Logger.getLogger("paymentgateway.server");
	private final ProcessPaymentHandler handler;
	private WebServer server;
	
	@Inject
	public ApiServer(ProcessPaymentHandler handler) {
		this.handler = handler;
	}
	
	public ApiServer start(int httpPort, String dbPath) {
		server = WebServers.createWebServer(httpPort)
				.add(handler.openDatabase(dbPath, "ALL"));
		server.start();
		logger.config("ApiServer is running on port " + httpPort);
		return this;
	}
	
	public ApiServer start(int httpPort, String dbPath, boolean enableConsoleLogging) {
		if (enableConsoleLogging) return start(httpPort, dbPath);
		server = WebServers.createWebServer(httpPort)
				.add(handler.openDatabase(dbPath, "ALL").disableConsoleLogging());
		server.start();
		logger.config("ApiServer is running on port " + httpPort);
		return this;
	}
	
	public ApiServer start(int httpPort, String remoteHost, CardIssuer cardIssuer, String dbPath) {
		server = WebServers.createWebServer(httpPort)
				.add(handler.setRemoteHost(remoteHost).setCardIssuer(cardIssuer).openDatabase(dbPath, cardIssuer.toString()));
		server.start();
		logger.config("ApiServer for " + cardIssuer.name() + " is running on port " + httpPort + ", forwarding other cards to " + remoteHost);
		return this;
	}
	
	public void stop() {
		server.stop();
	}
}
