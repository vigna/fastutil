package it.unimi.dsi.fastutil;

/*		 
 * Copyright (C) 2003-2014 Sebastiano Vigna 
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


/** A class providing static methods and objects that do useful things with maps.
 *
 * @see java.util.Collections
 */

public class Maps {

	private Maps() {}

	/** A standard default return value to be used in maps containing <code>null</code> values.
	 * @deprecated Since fastutil 5.0, the introduction of generics
	 * makes this object pretty useless.
	 */

	@Deprecated
	public static final Object MISSING = new Object();
}
