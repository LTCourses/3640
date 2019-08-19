package paymentgateway.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.webbitserver.HttpResponse;

public class FileHandlerBase {
	private static final Logger logger = Logger.getLogger("paymentgateway.server");
	private static final String DEFAULT_ROOT = "/";
	private static final String DEFAULT_FILE = "index.html";

	protected boolean setResponseFromFile(HttpResponse response, String path, int httpStatus) throws IOException {
		if (path.endsWith(DEFAULT_ROOT)) {
			path = path + DEFAULT_FILE;
		}
		String fileContent = getContentFromFile(path);
		if (fileContent == null) return false;
		for (ContentType contentType: ContentType.values()) {
			if (path.endsWith(contentType.fileExtension)) {
				response.header(HeaderField.CONTENT_TYPE.text, contentType.httpContentType)
					.content(fileContent)
					.status(httpStatus)
					.end();
				return true;
			}
		}
		return false;
	}

	private String getContentFromFile(String path) throws IOException {
		FileInputStream fis = null;
		try {
			File file = new File(path);
			if (!file.exists()) return null;
			byte[] content = new byte[(int)file.length()];
			fis = new FileInputStream(file);
			fis.read(content, 0, content.length);
			return new String(content);
		} catch (FileNotFoundException ex) {
			logger.logp(Level.FINEST, FileHandlerBase.class.getName(), "getContentFromFile", ex.getMessage(), ex);
			return null;
		} finally {
			if (fis != null) fis.close();
		}
	}
}
