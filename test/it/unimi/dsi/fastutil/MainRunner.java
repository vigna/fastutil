/*
 * Copyright (C) 2017-2021 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unimi.dsi.fastutil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Contains utility methods for running the {@code public static void main(String[]} method of a class if it has one.
 * 
 * <p>Several classes in fastutils have legacy tests in {@code main} methods that may or may not
 * be generated based on code generation flags. This class will query whether the class given has a
 * {@code main} method, and if so, calls it with the parameters given. The idea being to ensure
 * that these legacy tests get called by the unit tests.
 * <br>It is intended that these tests will be converted into proper unit tests over time, but this
 * can help bridge the gap until that is done. 
 *
 * <p>To have these {@code main} method tests included in the code generation (as well as to generate
 * some micro-benchmarks), pass {@code TEST=1} to the {@code make} command.
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
		// The array itself is the argument, so we need to wrap it in another array first.
		mainMethod.invoke(null, new Object[] {args});
	}
}
