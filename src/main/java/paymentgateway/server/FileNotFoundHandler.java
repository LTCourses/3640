package paymentgateway.server;

import java.util.logging.Logger;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

public class FileNotFoundHandler extends FileHandlerBase implements HttpHandler {
	private static final Logger logger = Logger.getLogger("paymentgateway.server");
	private String notFoundFile;
	
	public FileNotFoundHandler setNotFoundFile(String notFoundFile) {
		this.notFoundFile = notFoundFile;
		return this;
	}
	
	@Override
	public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
		logger.warning(LoggerUtils.getIpAddress(request.remoteAddress()) + " Returning 404 " + request.uri());
		if (!setResponseFromFile(response, notFoundFile, 404)) {
			control.nextHandler();
		}
	}
}
