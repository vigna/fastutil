/*
 * Copyright (C) 2020 Sebastiano Vigna
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

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * A utility class for managing how parallel threaded operations are handled in Fastutil.
 *
 * <p>For example, this class is how {@link it.unimi.dsi.fastutil.ints#parallelQuickSort} chooses
 * which {@link ForkJoinPool} to use.
 *
 * <p>For methods involving the default pool, the default is only used when a parallel operation is
 * called and not already in a {@code ForkJoinPool}. If the call already takes place in a
 * {@code ForkJoinPool}, then that pool is used for subsequent threads regardless of this
 * default.
 *
 * <p>The default default-pool is {@linkplain ForkJoinPool#commonPool() the common ForkJoinPool}.
 * 
 * @author C. Sean Young &lt;csyoung@google.com&gt;
 *
 */
public class Threading {

	private static volatile ForkJoinPool defaultExecutor = ForkJoinPool.commonPool();

	/**
	 * Get the pool to use for a Fastutil's parallel operation.
	 *
	 * <p>This mimicks the selection logic of {@link java.util.concurrent.ForkJoinTask#fork() ForkJoinTask.fork()},
	 * except it allows a {@linkplain #getCurrentDefaultFastutilExecutor() custom fallback pool}
	 * instead of always choosing the {@linkplain ForkJoinPool#commonPool() common pool}. 
	 * 
	 * <p>This method is intended for use by Fastutil's classes, not for general use.
	 * 
	 * @return the {@linkplain ForkJoinTask#getPool() current pool} if running in a {@link ForkJoinPool},
	 *   the {@linkplain #getCurrentDefaultFastutilExecutor() current Fastutil default pool} otherwise.
	 */
	public static ForkJoinPool getPool() {
		ForkJoinPool current = ForkJoinTask.getPool();
		return current == null ? defaultExecutor : current;
	}

	/**
	 * Returns the current default executor that will be used by Fastutil's parallel operations not already in a {@link ForkJoinPool}.
	 */
	public static ForkJoinPool getCurrentDefaultFastutilExecutor() {
		return defaultExecutor;
	}

	/** Set the current default executor that will be used by Fastutil's parallel operations not already in a {@link ForkJoinPool}.
	 * 
	 * <p>Note that this is static for the whole program. To override the pool to use for just a
	 * single operation, call the method in a task in the {@link ForkJoinPool} to use, for example,
	 * "{@code poolToParallelSortIn.invoke(() -> parallelQuickSort(arrayToSort))}".
	 *
	 * @throws NullPointerException if given {@code null}. Pass {@link ForkJoinPool#commonPool()}
	 * if you want to reset the behavior back to the default.
	 */
	public static void setDefaultFastutilExecutor(ForkJoinPool newDefault) {
		defaultExecutor = java.util.Objects.requireNonNull(newDefault);
	}
}
