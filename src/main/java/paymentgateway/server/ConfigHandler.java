package paymentgateway.server;

import java.io.IOException;
import java.util.logging.Logger;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

public class ConfigHandler extends FileHandlerBase implements HttpHandler {
	private static final Logger logger = Logger.getLogger("paymentgateway.server");
	private static final String DEFAULT_PROTOCOL = "http://";
	private String apiHostname;
	private int apiPort;

	public ConfigHandler setApiHost(String apiHostname, int apiPort) {
		this.apiHostname = apiHostname;
		if (this.apiHostname != null) {
			if (!this.apiHostname.toLowerCase().startsWith("http")) {
				this.apiHostname = DEFAULT_PROTOCOL + this.apiHostname;
			}
			if (this.apiHostname.endsWith("/")) {
				this.apiHostname = this.apiHostname.substring(0, this.apiHostname.length() - 1);
			}
		}
		this.apiPort = apiPort;
		return this;
	}
	
	@Override
	public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws IOException {
		if (request.uri().toLowerCase().equals("/config.js")) {
			logger.info(LoggerUtils.getIpAddress(request.remoteAddress()) + " Returning 200 " + request.uri());
			String apiHost = apiHostname;
			if (apiHost == null) {
				apiHost = "http://" + request.header("Host");
				int breakPosition = apiHost.lastIndexOf(':');
				if (breakPosition > 5) apiHost = apiHost.substring(0, breakPosition);
			}
			apiHost += ":" + apiPort;
			response.header(HeaderField.CONTENT_TYPE.text, ContentType.JAVASCRIPT.httpContentType)
				.content("var apiHost = \"" + apiHost + "\";\n")
				.status(200)
				.end();
		} else {
			control.nextHandler();
		}
	}
}
