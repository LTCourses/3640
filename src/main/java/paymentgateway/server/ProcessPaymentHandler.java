package paymentgateway.server;

import java.util.logging.Logger;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import com.google.inject.Inject;

import paymentgateway.logic.CardIssuer;
import paymentgateway.logic.FundsStatus;
import paymentgateway.logic.PaymentLogic;
import paymentgateway.logic.PaymentStatus;

public class ProcessPaymentHandler implements HttpHandler {
	private static final Logger logger = Logger.getLogger("paymentgateway.server");
	private final PaymentLogic paymentLogic;
	
	@Inject
	public ProcessPaymentHandler(PaymentLogic paymentLogic) {
		this.paymentLogic = paymentLogic;
	}
	
	public ProcessPaymentHandler setRemoteHost(String remoteHost) {
		paymentLogic.setRemoteHost(remoteHost);
		return this;
	}
	
	public ProcessPaymentHandler setCardIssuer(CardIssuer cardIssuer) {
		paymentLogic.setCardIssuer(cardIssuer);
		return this;
	}
	
	public ProcessPaymentHandler openDatabase(String dbPath, String dbName) {
		paymentLogic.openDb(dbPath, dbName);
		return this;
	}
	
	public ProcessPaymentHandler disableConsoleLogging() {
		paymentLogic.disableConsoleLogging();
		return this;
	}
	
	@Override
	public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
		String path = request.uri().toLowerCase();
		if (isCorrectPath(path)) {
			logger.info(LoggerUtils.getIpAddress(request.remoteAddress()) + " Received " + request.method() + " " + request.uri());
			if (request.method().equals("OPTIONS")) testLogic(request, response); 
			else if (request.method().equals("GET")) invokeLogic(true, response, path, request.header(HeaderField.ACCOUNT_KEY.text));
			else if (request.method().equals("POST")) invokeLogic(false, response, path, request.header(HeaderField.ACCOUNT_KEY.text));
			else sendXmlResponse(response, PaymentStatus.INVALID_REQUEST, PaymentStatus.INVALID_REQUEST.message,
					null, null, 405, null);
			return;
		}
		control.nextHandler();
	}

	private boolean isCorrectPath(String path) {
		int breakPosition = path.indexOf('?');
		if (breakPosition != -1) path = path.substring(0, breakPosition);
		if (!path.endsWith("/")) path = path + "/";
		return path.equals("/api/pay/");
	}
	
	private void testLogic(HttpRequest request, HttpResponse response) {
		boolean isOk = true;
		if (isOk && request.hasHeader(HeaderField.REQUEST_METHOD.text)) {
			String method = request.header(HeaderField.REQUEST_METHOD.text);
			isOk = method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("POST");
		}
		if (isOk && request.hasHeader(HeaderField.REQUEST_HEADERS.text)) {
			String headers = request.header(HeaderField.REQUEST_HEADERS.text);
			isOk = headers.equalsIgnoreCase(HeaderField.ACCOUNT_KEY.text);
		}
		if (isOk) {
			logger.fine("Allowing cross origin request");
			response
				.header(HeaderField.ALLOW_CROSS_ORIGIN.text, "*")
				.header(HeaderField.ALLOW_METHODS.text, "GET, POST")
				.header(HeaderField.ALLOW_HEADERS.text, "Account-Key")
				.status(200)
				.end();
		} else sendXmlResponse(response, PaymentStatus.INVALID_REQUEST, PaymentStatus.INVALID_REQUEST.message,
				null, null, 405, null);
	}
	
	private void invokeLogic(boolean isValidateOnly, HttpResponse response, String path, String accountKey) {
		String number = null;
		String expiry = null;
		String amount = null;
		int breakPosition = path.indexOf('?');
		if (breakPosition != -1) {
			String[] args = path.substring(breakPosition + 1).split("&");
			for (String arg: args) {
				String[] nameValue = arg.split("=");
				String name = nameValue[0];
				String value = nameValue.length == 2 ? nameValue[1] : "";
				if ("number".equals(name)) {
					number = value;
				} else if ("expiry".equals(name)) {
					expiry = value;
				} else if ("amount".equals(name)) {
					amount = value;
				} else {
					sendXmlResponse(response, PaymentStatus.INVALID_PARAMETER,
						name + " is not recognised", null, null, 400, null);
					return;
				}
			}
		}
		paymentLogic.process(isValidateOnly, number, expiry, amount, accountKey);
		int httpStatus = 200;
		// TODO Change httpStatus to reflect status
		sendXmlResponse(response, paymentLogic.getPaymentStatus(), paymentLogic.getMessage(), 
			paymentLogic.getCardType(), paymentLogic.getFundsStatus(), httpStatus, paymentLogic.getTestModeMessage());
	}

	private void sendXmlResponse(HttpResponse response, PaymentStatus paymentStatus, String message, 
			CardIssuer cardType, FundsStatus fundsStatus, int httpStatus, String testModeMessage) {
		String xml = "<?xml version='1.0' ?><response><status>"
			+ paymentStatus.toString() + "</status><message>" + message + "</message>";
		if (cardType != null) {
			xml += "<type>" + cardType + "</type>";
		}
		if (fundsStatus != null) {
			xml += "<funds>" + fundsStatus.toString() + "</funds>";
		}
		if (testModeMessage != null && testModeMessage != "") {
			xml += "<testmode>WARNING: " + testModeMessage + "</testmode>";
		}
		xml = xml + "</response>";
		logger.info("Returning " + xml);
		response
			.header(HeaderField.CONTENT_TYPE.text, ContentType.XML.httpContentType)
			.header(HeaderField.ALLOW_CROSS_ORIGIN.text, "*")
			.content(xml)
			.status(httpStatus)
			.end();
	}
}
