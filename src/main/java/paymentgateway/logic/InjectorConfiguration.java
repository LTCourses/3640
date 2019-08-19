package paymentgateway.logic;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorConfiguration {
	private static Injector injector = null;
	
	public static class NullModule extends AbstractModule {
		protected void configure() { }
	}
	
	public static class StandardModule extends AbstractModule {
		protected void configure() {
		}
	}
	
	public static void configure(AbstractModule module) {
		injector = Guice.createInjector(module);
	}
	
	public static void configureWithStandardModule() {
		injector = Guice.createInjector(new StandardModule());
	}
	
	public static <T> T getInstance(Class<T> type) {
		return injector.getInstance(type);
	}
	
	public static Injector getInjector() {
		return injector;
	}
}
