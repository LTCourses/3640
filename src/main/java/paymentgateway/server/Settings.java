package paymentgateway.server;

public final class Settings {
	public static String getSettingString(String key, String defaultValue) {
		String value = System.getProperty(key);
		if (value == null) value = System.getenv(key);
		if (value == null) return defaultValue;
		else return value;
	}
	
	public static int getSettingInt(String key, int defaultValue) {
		String value = System.getProperty(key);
		if (value == null) value = System.getenv(key);
		if (value == null) return defaultValue;
		else return Integer.parseInt(value);
	}
}
