/**
 * Copyright 2011 The Buzz Media, LLC
 * Copyright 2015 Mickael Jeanroy <mickael.jeanroy@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thebuzzmedia.exiftool;

import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.executor.CommandExecutors;

import java.util.HashSet;
import java.util.Set;

import static com.thebuzzmedia.exiftool.process.executor.CommandExecutors.newExecutor;

/**
 * Builder for {link ExifTool instance}.
 * This builder should be used to create instance of {@link com.thebuzzmedia.exiftool.ExifTool}.
 *
 * ### Settings
 *
 * #### Path
 *
 * Set the absolute path to the ExifTool executable on the host system running
 * this class as defined by the `exiftool.path` system property.
 *
 * If set, value will be used, otherwise, path will be read:
 * - From system property. This system property can be set on startup
 *   with `-Dexiftool.path=/path/to/exiftool` or by
 *   calling {@link System#setProperty(String, String)} before
 *   this class is loaded.
 * - Default value is `exiftool`. In this case, `exiftool` command must
 *   be globally available.
 *
 * If ExifTool is on your system path and running the command `exiftool`
 * successfully executes it, leaving this value unchanged will work fine on
 * any platform. If the ExifTool executable is named something else or not
 * in the system path, then this property will need to be set to point at it
 * before using this class.
 *
 * On Windows be sure to double-escape the path to the tool,
 * for example: `-Dexiftool.path=C:\\Tools\\exiftool.exe`.
 *
 * Default value is `exiftool`.
 *
 * Relative path values (e.g. `bin/tools/exiftool`) are executed with
 * relation to the base directory the VM process was started in. Essentially
 * the directory that `new File(".").getAbsolutePath()` points at
 * during runtime.
 *
 * #### Executor
 *
 * Executor is the component responsible for executing command line on the
 * system. Most of the time, the default should be fine, but if you want to tune
 * the used executor, then this property is for you.
 * Custom executor must implement {@link com.thebuzzmedia.exiftool.process.CommandExecutor} interface.
 *
 * #### Stay Open Feature
 *
 * ExifTool <a href="http://u88.n24.queensu.ca/exiftool/forum/index.php/topic,1402.msg12933.html#msg12933">8.36</a>
 * added a new persistent-process feature that allows ExifTool to stay
 * running in a daemon mode and continue accepting commands via a file or stdin.
 * This feature is disabled by default.
 *
 * **NOTE:** If `stay_open` flag is enabled, then an instance of {@link com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException}
 * may be thrown during ExifTool creation.
 * If this exception occurs, then you should probably:
 * - Update your ExifTool version.
 * - Create new ExifTool without this feature:
 *
 * ```java
 *     final ExifTool exifTool;
 *     try {
 *         exifTool = new ExifToolBuilder()
 *             .enableStayOpen()
 *             .build();
 *     }
 *     catch (UnsupportedFeatureException ex) {
 *         exifTool = new ExifToolBuilder()
 *             .disableStayOpen()
 *             .build();
 *     }
 * ```
 */
public class ExifToolBuilder {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(ExifToolBuilder.class);

	/**
	 * Function to get default path value.
	 */
	private static final PathFunction PATH = new PathFunction();

	/**
	 * Function to get default executor environment.
	 */
	private static final ExecutorFunction EXECUTOR = new ExecutorFunction();

	/**
	 * ExifTool path.
	 */
	private String path;

	/**
	 * ExifTool executor.
	 */
	private CommandExecutor executor;

	/**
	 * Set of active features.
	 */
	private final Set<Feature> features;

	public ExifToolBuilder() {
		this.features = new HashSet<Feature>();
	}

	/**
	 * Set ExifTool path.
	 *
	 * @param path New path.
	 * @return Current builder.
	 */
	public ExifToolBuilder path(String path) {
		log.debug("Set path: %s", path);
		this.path = path;
		return this;
	}

	/**
	 * Set ExifTool executor.
	 *
	 * @param executor New executor.
	 * @return Current builder.
	 */
	public ExifToolBuilder executor(CommandExecutor executor) {
		log.debug("Set executor: %s", executor);
		this.executor = executor;
		return this;
	}

	/**
	 * Enable `stay_open` feature.
	 *
	 * @return Current builder.
	 */
	public ExifToolBuilder enableStayOpen() {
		log.debug("Enable 'stay_open' feature");
		this.features.add(Feature.STAY_OPEN);
		return this;
	}

	/**
	 * Disable `stay_open` feature.
	 *
	 * @return Current builder.
	 */
	public ExifToolBuilder disableStayOpen() {
		log.debug("Disable 'stay_open' feature");
		this.features.remove(Feature.STAY_OPEN);
		return this;
	}

	/**
	 * Create exiftool instance with previous settings.
	 *
	 * @return Exiftool instance.
	 */
	public ExifTool build() {
		String path = firstNonNull(this.path, PATH);
		CommandExecutor executor = firstNonNull(this.executor, EXECUTOR);

		// Add some debugging information
		if (log.isDebugEnabled()) {
			log.debug("Create ExifTool instance");
			log.debug(" - Path: %s", path);
			log.debug(" - Executor: %s", executor);
			log.debug(" - Features: %s", features);
		}

		return new ExifTool(path, executor, features);
	}

	/**
	 * Return first non null value:
	 * - If first parameter is not null, then it is returned.
	 * - Otherwise, result of function is returned.
	 *
	 * @param value First value.
	 * @param factory Function used to get non null value.
	 * @param <T> Type of values.
	 * @return Non null value.
	 */
	private static <T> T firstNonNull(T value, FactoryFunction<T> factory) {
		return value == null ? factory.apply() : value;
	}

	/**
	 * Interface to return values.
	 * This interface should be used by builder to lazily create
	 * default settings parameters.
	 *
	 * @param <T> Type of settings.
	 */
	private static interface FactoryFunction<T> {
		T apply();
	}

	/**
	 * Return the absolute path to the ExifTool executable on the host system running
	 * this class as defined by the `exiftool.path` system property.
	 *
	 * If ExifTool is on your system path and running the command `exiftool`
	 * successfully executes it, leaving this value unchanged will work fine on
	 * any platform. If the ExifTool executable is named something else or not
	 * in the system path, then this property will need to be set to point at it
	 * before using this class.
	 *
	 * This system property can be set on startup with `-Dexiftool.path=/path/to/exiftool`
	 * or by calling {@link System#setProperty(String, String)} before
	 * this class is loaded.
	 *
	 * On Windows be sure to double-escape the path to the tool,
	 * for example: `-Dexiftool.path=C:\\Tools\\exiftool.exe`.
	 *
	 * Default value is `exiftool`.
	 *
	 * ### Relative Paths
	 *
	 * Relative path values (e.g. `bin/tools/exiftool`) are executed with
	 * relation to the base directory the VM process was started in. Essentially
	 * the directory that `new File(".").getAbsolutePath()` points at
	 * during runtime.
	 */
	private static class PathFunction implements FactoryFunction<String> {
		@Override
		public String apply() {
			return System.getProperty("exiftool.path", "exiftool");
		}
	}

	/**
	 * Returns the default executor environment.
	 * Default executor is the result of {@link CommandExecutors#newExecutor()} method.
	 */
	private static class ExecutorFunction implements FactoryFunction<CommandExecutor> {
		@Override
		public CommandExecutor apply() {
			return newExecutor();
		}
	}
}
