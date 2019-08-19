package paymentgateway.server;

import java.io.File;
import java.net.SocketAddress;
import java.util.Random;

public class LoggerUtils {
	// The following IP addresses were obtained from http://www.nirsoft.net/countryip/
	private static String[] randomIpAddresses = {
			"66.60.0.0",	// Argentina
			"130.56.0.0",	// Australia
			"64.66.0.0",	// Bahamas
			"139.82.0.0",	// Brazil
			"23.16.0.0",	// Canada
			"146.155.0.0",	// Chile 
			"1.3.0.0",		// China
			"5.33.0.0",		// Denmark
			"41.32.0.0",	// Egypt
			"210.7.0.0",	// Fiji
			"129.20.0.0",	// France
			"5.4.0.0",		// Germany
			"5.54.0.0",		// Greece
			"88.83.0.0",	// Greenland
			"212.77.0.0",	// Holy See (Vatican City)
			"42.2.0.0",		// Hong Kong
			"1.6.0.0",		// India
			"130.208.0.0",	// Iceland
			"2.52.0.0",		// Israel
			"2.32.0.0",		// Italy
			"1.5.0.0",		// Japan
			"41.80.0.0",	// Kenya
			"82.117.0.0",	// Liechtenstein
			"146.3.0.0",	// Luxembourg
			"154.126.0.0",	// Madagascar
			"148.210.0.0",	// Mexico
			"160.90.0.0",	// Morocco
			"130.123.0.0",	// New Zealand
			"179.7.0.0",	// Peru
			"61.9.0.0",		// Philippines
			"5.3.0.0",		// Russian Federation
			"2.885.0.0",	// Saudi Arabia
			"8.128.0.0",	// Singapore
			"41.0.0.0",		// South Africa
			"1.11.0.0",		// South Korea
			"5.40.0.0",		// Spain
			"31.11.0.0",	// Switzerland
			"58.8.0.0",		// Thailand
			"5.255.0.0",	// Turkey
			"5.30.0.0",		// UAE
			"2.24.0.0",		// UK
			"3.0.0.0",		// USA
			"129.90.0.0",	// Venezuela
	};
	private static Random rand = new Random();
	
	public static void setLoggingProperties() {
		if (System.getProperty("java.util.logging.config.file") == null) {
			File config = new File("logging.properties");
			if (!config.exists()) config = new File("deploy/logging.properties");
			if (config.exists()) System.setProperty("java.util.logging.config.file", config.getPath());
		}
	}
	
	public static String getIpAddress(SocketAddress remoteAddress) {
//		String address = remoteAddress.toString();
//		int startPos = address.charAt(0) == '/' ? 1 : 0;
//		int endPos = address.lastIndexOf(':');
//		address = address.substring(startPos, endPos);
//		return address;
		
		return getRandomIpAddress();
	}
	
	private static String getRandomIpAddress() {
		int index = rand.nextInt(randomIpAddresses.length);
		return randomIpAddresses[index];
	}
}
