package it.unimi.dsi.fastutil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Contains utility methods for running the {@code public static void main(String[]} method of a class if it has one.
 * 
 * <p>Several classes in fastutils have legacy tests in {@code main} methods that may or may not
 * be generated based on code generation flags. This will query whether the main method ex
 *
 * @author C. Sean Young (csyoung@google.com)
 *
 */
public final class MainRunner {
	private MainRunner() {} // Static utility class
	
	private static final Class<?>[] MAIN_PARAM_TYPES = {String[].class};
	
	public static void callMainIfExists(Class<?> clazz, String... args) throws ReflectiveOperationException {
		if (clazz.getSimpleName().endsWith("Test")) {
			throw new IllegalArgumentException(
					"Trying to run the main method of a test class " + clazz.getSimpleName()
					+ ". This isn't what you want. Instead, run the main method of the class under test.");
		}
		Method mainMethod = null;
		try {
			mainMethod = clazz.getDeclaredMethod("main", MAIN_PARAM_TYPES);
		} catch(NoSuchMethodException unused) {
			// Gracefully do nothing if no main method.
		}
		if (mainMethod == null) {
			return;
		}
		if (!Modifier.isStatic(mainMethod.getModifiers())) {
			// Not a static method, so don't try to call it.
			return;
		}
		mainMethod.invoke(null, new Object[] {args});
	}
}
