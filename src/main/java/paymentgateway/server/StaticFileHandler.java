package paymentgateway.server;

import java.io.IOException;
import java.util.logging.Logger;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

public class StaticFileHandler extends FileHandlerBase implements HttpHandler {
	private static final Logger logger = Logger.getLogger("paymentgateway.server");
	private String pathPrefix;
	private String staticContentFolder;
	
	public StaticFileHandler setPaths(String pathPrefix, String staticContentFolder) {
		this.pathPrefix = pathPrefix;
		this.staticContentFolder = staticContentFolder;
		return this;
	}
	
	@Override
	public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws IOException {
		String path = request.uri().toLowerCase();
		if (path.startsWith(pathPrefix)) {
			if (!pathPrefix.equals("")) path = path.substring(pathPrefix.length());
			path = staticContentFolder + path;
			if (setResponseFromFile(response, path, 200)) {
				logger.info(LoggerUtils.getIpAddress(request.remoteAddress()) + " Returning 200 " + request.uri());
				return;
			}
		}
		control.nextHandler();
	}
}
