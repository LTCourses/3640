package paymentgateway.server;

public enum ContentType {
	// CSS(".css", "text/css"),
	// JSON(".json", "application/json"),
	JAVASCRIPT(".js", "application/javascript"),
	XML(".xml", "text/xml"),
	HTML(".html", "text/html"),
	ICO(".ico", "image/x-icon"),
	;

	public final String fileExtension;
	public final String httpContentType;

	ContentType(String fileExtension, String httpContentType) {
		this.fileExtension = fileExtension;
		this.httpContentType = httpContentType;
	}
}
