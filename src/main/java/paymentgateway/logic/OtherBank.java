package paymentgateway.logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import paymentgateway.server.HeaderField;

public class OtherBank extends ResponseBase {
	private static final Logger logger = Logger.getLogger("paymentgateway.logic");
	private static final String DEFAULT_PROTOCOL = "http://";
	private String remoteHost;
	
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
		if (!this.remoteHost.toLowerCase().startsWith("http")) {
			this.remoteHost = DEFAULT_PROTOCOL + this.remoteHost;
		}
		if (this.remoteHost.endsWith("/")) {
			this.remoteHost = this.remoteHost.substring(0, this.remoteHost.length() - 1);
		}
	}
	
	public void sendPayment(boolean isValidateOnly, String number, String expiry, String amount) {
		setFundsStatus(null);
		String xml = getResponse(isValidateOnly, number, expiry, amount);
		if (xml == null) {
			setResponse(PaymentStatus.UNABLE_TO_CONNECT);
		} else {
			setResponse(PaymentStatus.valueOf(getXmlContent(xml, "<status>")), getXmlContent(xml, "<message>"));
			String cardIssuerName = getXmlContent(xml, "<type>");
			if (cardIssuerName != null) setCardType(CardIssuer.valueOf(cardIssuerName));
			String fundsStatus = getXmlContent(xml, "<funds>");
			if (fundsStatus != null) setFundsStatus(FundsStatus.valueOf(fundsStatus));
		}
	}
	
	private String getResponse(boolean isValidateOnly, String number, String expiry, String amount) {
		// TODO Handle timeout
		InputStream is = null;
		try {
			URL url = new URL(remoteHost + "/api/pay?Number=" + number + "&Expiry=" + expiry + "&Amount=" + amount);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty(HeaderField.ACCOUNT_KEY.text, "VWXYZ");	// TODO This should be a configurable option
			connection.setRequestMethod(isValidateOnly ? "GET" : "POST");
			is = connection.getInputStream();
			byte[] content = new byte[(int)connection.getContentLength()];
			is.read(content, 0, content.length);
			return new String(content);
		} catch (MalformedURLException e) {
			logger.logp(Level.SEVERE, OtherBank.class.getName(), "getResponse", e.getMessage(), e);
			return null;
		} catch (IOException e) {
			logger.logp(Level.SEVERE, OtherBank.class.getName(), "getResponse", e.getMessage(), e);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.logp(Level.SEVERE, OtherBank.class.getName(), "getResponse", e.getMessage(), e);
				}
			}
		}
	}
	
	private String getXmlContent(String xml, String element) {
		int startPosition = xml.indexOf(element);
		if (startPosition == -1) return null;
		startPosition += element.length();
		int endPosition = xml.indexOf('<', startPosition);
		if (endPosition == -1) return null;
		return xml.substring(startPosition, endPosition).trim();
	}
}
