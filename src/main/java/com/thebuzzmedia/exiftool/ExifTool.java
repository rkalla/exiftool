/**   
 * Copyright 2011 The Buzz Media, LLC
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * Class used to provide a Java-like interface to Phil Harvey's excellent,
 * Perl-based <a
 * href="http://www.sno.phy.queensu.ca/~phil/exiftool">ExifTool</a>.
 * <p/>
 * There are a number of other basic Java wrappers to ExifTool available online,
 * but most of them only abstract out the actual Java-external-process execution
 * logic and do no additional work to make integration with the external
 * ExifTool any easier or intuitive from the perspective of the Java application
 * written to make use of ExifTool.
 * <p/>
 * This class was written in order to make integration with ExifTool inside of a
 * Java application seamless and performant with the goal being that the
 * developer can treat ExifTool as if it were written in Java, garnering all of
 * the benefits with none of the added headache of managing an external native
 * process from Java.
 * <p/>
 * Phil Harvey's ExifTool is written in Perl and runs on all major platforms
 * (including Windows) so no portability issues are introduced into your
 * application by utilizing this class.
 * <h3>Usage</h3>
 * Assuming ExifTool is installed on the host system correctly and either in the
 * system path or pointed to by {@link #EXIF_TOOL_PATH}, using this class to
 * communicate with ExifTool is as simple as creating an instance (
 * <code>ExifTool tool = new ExifTool()</code>) and then making calls to
 * {@link #getImageMeta(File, Tag...)} or
 * {@link #getImageMeta(File, Format, Tag...)} with a list of {@link Tag}s you
 * want to pull values for from the given image.
 * <p/>
 * In this default mode, calls to <code>getImageMeta</code> will automatically
 * start an external ExifTool process to handle the request. After ExifTool has
 * parsed the tag values from the file, the external process exits and this
 * class parses the result before returning it to the caller.
 * <p/>
 * Results from calls to <code>getImageMeta</code> are returned in a {@link Map}
 * with the {@link Tag} values as the keys and {@link String} values for every
 * tag that had a value in the image file as the values. {@link Tag}s with no
 * value found in the image are omitted from the result map.
 * <p/>
 * While each {@link Tag} provides a hint at which format the resulting value
 * for that tag is returned as from ExifTool (see {@link Tag#getType()}), that
 * only applies to values returned with an output format of
 * {@link Format#NUMERIC} and it is ultimately up to the caller to decide how
 * best to parse or convert the returned values.
 * <p/>
 * The {@link Tag} Enum provides the {@link Tag#parseValue(Tag, String)}
 * convenience method for parsing given <code>String</code> values according to
 * the Tag hint automatically for you if that is what you plan on doing,
 * otherwise feel free to handle the return values anyway you want.
 * <h3>ExifTool -stay_open Support</h3>
 * ExifTool <a href=
 * "http://u88.n24.queensu.ca/exiftool/forum/index.php/topic,1402.msg12933.html#msg12933"
 * >8.36</a> added a new persistent-process feature that allows ExifTool to stay
 * running in a daemon mode and continue accepting commands via a file or stdin.
 * <p/>
 * This new mode is controlled via the <code>-stay_open True/False</code>
 * command line argument and in a busy system that is making thousands of calls
 * to ExifTool, can offer speed improvements of up to <strong>60x</strong> (yes,
 * really that much).
 * <p/>
 * This feature was added to ExifTool shortly after user <a
 * href="http://www.christian-etter.de/?p=458">Christian Etter discovered</a>
 * the overhead for starting up a new Perl interpreter each time ExifTool is
 * loaded accounts for roughly <a href=
 * "http://u88.n24.queensu.ca/exiftool/forum/index.php/topic,1402.msg6121.html#msg6121"
 * >98.4% of the total runtime</a>.
 * <p/>
 * Support for using ExifTool in daemon mode is enabled by passing
 * {@link Feature#STAY_OPEN} to the constructor of the class when creating an
 * instance of this class and then simply using the class as you normally would.
 * This class will manage a single ExifTool process running in daemon mode in
 * the background to service all future calls to the class.
 * <p/>
 * Because this feature requires ExifTool 8.36 or later, this class will
 * actually verify support for the feature in the version of ExifTool pointed at
 * by {@link #EXIF_TOOL_PATH} before successfully instantiating the class and
 * will notify you via an {@link UnsupportedFeatureException} if the native
 * ExifTool doesn't support the requested feature.
 * <p/>
 * In the event of an {@link UnsupportedFeatureException}, the caller can either
 * upgrade the native ExifTool upgrade to the version required or simply avoid
 * using that feature to work around the exception.
 * <h3>Automatic Resource Cleanup</h3>
 * When {@link Feature#STAY_OPEN} mode is used, there is the potential for
 * leaking both host OS processes (native 'exiftool' processes) as well as the
 * read/write streams used to communicate with it unless {@link #close()} is
 * called to clean them up when done. <strong>Fortunately</strong>, this class
 * provides an automatic cleanup mechanism that runs, by default, after 10mins
 * of inactivity to clean up those stray resources.
 * <p/>
 * The inactivity period can be controlled by modifying the
 * {@link #PROCESS_CLEANUP_DELAY} system variable. A value of <code>0</code> or
 * less disabled the automatic cleanup process and requires you to cleanup
 * ExifTool instances on your own by calling {@link #close()} manually.
 * <p/>
 * Any class activity by way of calls to <code>getImageMeta</code> will always
 * reset the inactivity timer, so in a busy system the cleanup thread could
 * potentially never run, leaving the original host ExifTool process running
 * forever (which is fine).
 * <p/>
 * This design was chosen to help make using the class and not introducing
 * memory leaks and bugs into your code easier as well as making very inactive
 * instances of this class light weight while not in-use by cleaning up after
 * themselves.
 * <p/>
 * The only overhead incurred when opening the process back up is a 250-500ms
 * lag while launching the VM interpreter again on the first call (depending on
 * host machine speed and load).
 * <h3>Reusing a "closed" ExifTool Instance</h3>
 * If you or the cleanup thread have called {@link #close()} on an instance of
 * this class, cleaning up the host process and read/write streams, the instance
 * of this class can still be safely used. Any followup calls to
 * <code>getImageMeta</code> will simply re-instantiate all the required
 * resources necessary to service the call (honoring any {@link Feature}s set).
 * <p/>
 * This can be handy behavior to be aware of when writing scheduled processing
 * jobs that may wake up every hour and process thousands of pictures then go
 * back to sleep. In order for the process to execute as fast as possible, you
 * would want to use ExifTool in daemon mode (pass {@link Feature#STAY_OPEN} to
 * the constructor of this class) and when done, instead of {@link #close()}-ing
 * the instance of this class and throwing it out, you can keep the reference
 * around and re-use it again when the job executes again an hour later.
 * <h3>Performance</h3>
 * Extra care is taken to ensure minimal object creation or unnecessary CPU
 * overhead while communicating with the external process.
 * <p/>
 * {@link Pattern}s used to split the responses from the process are explicitly
 * compiled and reused, string concatenation is minimized, Tag name lookup is
 * done via a <code>static final</code> {@link Map} shared by all instances and
 * so on.
 * <p/>
 * Additionally, extra care is taken to utilize the most optimal code paths when
 * initiating and using the external process, for example, the
 * {@link ProcessBuilder#command(List)} method is used to avoid the copying of
 * array elements when {@link ProcessBuilder#command(String...)} is used and
 * avoiding the (hidden) use of {@link StringTokenizer} when
 * {@link Runtime#exec(String)} is called.
 * <p/>
 * All of this effort was done to ensure that imgscalr and its supporting
 * classes continue to provide best-of-breed performance and memory utilization
 * in long running/high performance environments (e.g. web applications).
 * <h3>Thread Safety</h3>
 * Instances of this class are <strong>not</strong> Thread-safe. Both the
 * instance of this class and external ExifTool process maintain state specific
 * to the current operation. Use of instances of this class need to be
 * synchronized using an external mechanism or in a highly threaded environment
 * (e.g. web application), instances of this class can be used along with
 * {@link ThreadLocal}s to ensure Thread-safe, highly parallel use.
 * <h3>Why ExifTool?</h3>
 * <a href="http://www.sno.phy.queensu.ca/~phil/exiftool">ExifTool</a> is
 * written in Perl and requires an external process call from Java to make use
 * of.
 * <p/>
 * While this would normally preclude a piece of software from inclusion into
 * the imgscalr library (more complex integration), there is no other image
 * metadata piece of software available as robust, complete and well-tested as
 * ExifTool. In addition, ExifTool already runs on all major platforms
 * (including Windows), so there was not a lack of portability introduced by
 * providing an integration for it.
 * <p/>
 * Allowing it to be used from Java is a boon to any Java project that needs the
 * ability to read/write image-metadata from almost <a
 * href="http://www.sno.phy.queensu.ca/~phil/exiftool/#supported">any image or
 * video file</a> format.
 * <h3>Alternatives</h3>
 * If integration with an external Perl process is something your app cannot do
 * and you still need image metadata-extraction capability, Drew Noakes has
 * written the 2nd most robust image metadata library I have come across: <a
 * href="http://drewnoakes.com/drewnoakes.com/code/exif/">Metadata Extractor</a>
 * that you might want to look at.
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @since 1.1
 */
public class ExifTool {
	/**
	 * Flag used to indicate if debugging output has been enabled by setting the
	 * "<code>exiftool.debug</code>" system property to <code>true</code>. This
	 * value will be <code>false</code> if the " <code>exiftool.debug</code>"
	 * system property is undefined or set to <code>false</code>.
	 * <p/>
	 * This system property can be set on startup with:<br/>
	 * <code>
	 * -Dexiftool.debug=true
	 * </code> or by calling {@link System#setProperty(String, String)} before
	 * this class is loaded.
	 * <p/>
	 * Default value is <code>false</code>.
	 */
	public static final Boolean DEBUG = Boolean.getBoolean("exiftool.debug");

	/**
	 * Prefix to every log message this library logs. Using a well-defined
	 * prefix helps make it easier both visually and programmatically to scan
	 * log files for messages produced by this library.
	 * <p/>
	 * The value is "<code>[exiftool] </code>" (including the space).
	 */
	public static final String LOG_PREFIX = "[exiftool] ";

	/**
	 * The absolute path to the ExifTool executable on the host system running
	 * this class as defined by the "<code>exiftool.path</code>" system
	 * property.
	 * <p/>
	 * If ExifTool is on your system path and running the command "exiftool"
	 * successfully executes it, leaving this value unchanged will work fine on
	 * any platform. If the ExifTool executable is named something else or not
	 * in the system path, then this property will need to be set to point at it
	 * before using this class.
	 * <p/>
	 * This system property can be set on startup with:<br/>
	 * <code>
	 * -Dexiftool.path=/path/to/exiftool
	 * </code> or by calling {@link System#setProperty(String, String)} before
	 * this class is loaded.
	 * <p/>
	 * On Windows be sure to double-escape the path to the tool, for example:
	 * <code>
	 * -Dexiftool.path=C:\\Tools\\exiftool.exe
	 * </code>
	 * <p/>
	 * Default value is "<code>exiftool</code>".
	 * <h3>Relative Paths</h3>
	 * Relative path values (e.g. "bin/tools/exiftool") are executed with
	 * relation to the base directory the VM process was started in. Essentially
	 * the directory that <code>new File(".").getAbsolutePath()</code> points at
	 * during runtime.
	 */
	public static final String EXIF_TOOL_PATH = System.getProperty(
			"exiftool.path", "exiftool");

	/**
	 * Interval (in milliseconds) of inactivity before the cleanup thread wakes
	 * up and cleans up the daemon ExifTool process and the read/write streams
	 * used to communicate with it when the {@link Feature#STAY_OPEN} feature is
	 * used.
	 * <p/>
	 * Ever time a call to <code>getImageMeta</code> is processed, the timer
	 * keeping track of cleanup is reset; more specifically, this class has to
	 * experience no activity for this duration of time before the cleanup
	 * process is fired up and cleans up the host OS process and the stream
	 * resources.
	 * <p/>
	 * Any subsequent calls to <code>getImageMeta</code> after a cleanup simply
	 * re-initializes the resources.
	 * <p/>
	 * This system property can be set on startup with:<br/>
	 * <code>
	 * -Dexiftool.processCleanupDelay=600000
	 * </code> or by calling {@link System#setProperty(String, String)} before
	 * this class is loaded.
	 * <p/>
	 * Setting this value to 0 disables the automatic cleanup thread completely
	 * and the caller will need to manually cleanup the external ExifTool
	 * process and read/write streams by calling {@link #close()}.
	 * <p/>
	 * Default value is <code>600,000</code> (10 minutes).
	 */
	public static final long PROCESS_CLEANUP_DELAY = Long.getLong(
			"exiftool.processCleanupDelay", 600000);

	/**
	 * Name used to identify the (optional) cleanup {@link Thread}.
	 * <p/>
	 * This is only provided to make debugging and profiling easier for
	 * implementors making use of this class such that the resources this class
	 * creates and uses (i.e. Threads) are readily identifiable in a running VM.
	 * <p/>
	 * Default value is "<code>ExifTool Cleanup Thread</code>".
	 */
	protected static final String CLEANUP_THREAD_NAME = "ExifTool Cleanup Thread";

	/**
	 * Compiled {@link Pattern} of ": " used to split compact output from
	 * ExifTool evenly into name/value pairs.
	 */
	protected static final Pattern TAG_VALUE_PATTERN = Pattern.compile(": ");

	/**
	 * Map shared across all instances of this class that maintains the state of
	 * {@link Feature}s and if they are supported or not (supported=true,
	 * unsupported=false) by the underlying native ExifTool process being used
	 * in conjunction with this class.
	 * <p/>
	 * If a {@link Feature} is missing from the map (has no <code>true</code> or
	 * <code>false</code> flag associated with it, but <code>null</code>
	 * instead) then that means that feature has not been checked for support
	 * yet and this class will know to call
	 * {@link #checkFeatureSupport(Feature...)} on it to determine its supported
	 * state.
	 * <p/>
	 * For efficiency reasons, individual {@link Feature}s are checked for
	 * support one time during each run of the VM and never again during the
	 * session of that running VM.
	 */
	protected static final Map<Feature, Boolean> FEATURE_SUPPORT_MAP = new HashMap<ExifTool.Feature, Boolean>();

	/**
	 * Static list of args used to execute ExifTool using the '-ver' flag in
	 * order to get it to print out its version number. Used by the
	 * {@link #checkFeatureSupport(Feature...)} method to check all the required
	 * feature versions.
	 * <p/>
	 * Defined here as a <code>static final</code> list because it is used every
	 * time and never changes.
	 */
	private static final List<String> VERIFY_FEATURE_ARGS = new ArrayList<String>(
			2);

	static {
		VERIFY_FEATURE_ARGS.add(EXIF_TOOL_PATH);
		VERIFY_FEATURE_ARGS.add("-ver");
	}

	/**
	 * Used to determine if the given {@link Feature} is supported by the
	 * underlying native install of ExifTool pointed at by
	 * {@link #EXIF_TOOL_PATH}.
	 * <p/>
	 * If support for the given feature has not been checked for yet, this
	 * method will automatically call out to ExifTool and ensure the requested
	 * feature is supported in the current local install.
	 * <p/>
	 * The external call to ExifTool to confirm feature support is only ever
	 * done once per JVM session and stored in a <code>static final</code>
	 * {@link Map} that all instances of this class share.
	 *
	 * @param feature
	 *            The feature to check support for in the underlying ExifTool
	 *            install.
	 *
	 * @return <code>true</code> if support for the given {@link Feature} was
	 *         confirmed to work with the currently installed ExifTool or
	 *         <code>false</code> if it is not supported.
	 *
	 * @throws IllegalArgumentException
	 *             if <code>feature</code> is <code>null</code>.
	 * @throws RuntimeException
	 *             if any exception occurs while attempting to start the
	 *             external ExifTool process to verify feature support.
	 */
	public static boolean isFeatureSupported(Feature feature)
			throws IllegalArgumentException, RuntimeException {
		if (feature == null)
			throw new IllegalArgumentException("feature cannot be null");

		Boolean supported = FEATURE_SUPPORT_MAP.get(feature);

		/*
		 * If there is no Boolean flag for the feature, support for it hasn't
		 * been checked yet with the native ExifTool install, so we need to do
		 * that.
		 */
		if (supported == null) {
			log("\tSupport for feature %s has not been checked yet, checking...");
			checkFeatureSupport(feature);

			// Re-query for the supported state
			supported = FEATURE_SUPPORT_MAP.get(feature);
		}

		return supported;
	}

	/**
	 * Helper method used to ensure a message is loggable before it is logged
	 * and then pre-pend a universal prefix to all log messages generated by
	 * this library to make the log entries easy to parse visually or
	 * programmatically.
	 * <p/>
	 * If a message cannot be logged (logging is disabled) then this method
	 * returns immediately.
	 * <p/>
	 * <strong>NOTE</strong>: Because Java will auto-box primitive arguments
	 * into Objects when building out the <code>params</code> array, care should
	 * be taken not to call this method with primitive values unless
	 * {@link #DEBUG} is <code>true</code>; otherwise the VM will be spending
	 * time performing unnecessary auto-boxing calculations.
	 * 
	 * @param message
	 *            The log message in <a href=
	 *            "http://download.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax"
	 *            >format string syntax</a> that will be logged.
	 * @param params
	 *            The parameters that will be swapped into all the place holders
	 *            in the original messages before being logged.
	 * 
	 * @see #LOG_PREFIX
	 */
	protected static void log(String message, Object... params) {
		if (DEBUG)
			System.out.printf(LOG_PREFIX + message + '\n', params);
	}

	/**
	 * Used to verify the version of ExifTool installed is a high enough version
	 * to support the given features.
	 * <p/>
	 * This method runs the command "<code>exiftool -ver</code>" to get the
	 * version of the installed ExifTool and then compares that version to the
	 * least required version specified by the given features (see
	 * {@link Feature#getVersion()}).
	 * 
	 * @param features
	 *            The features whose required versions will be checked against
	 *            the installed ExifTool for support.
	 * 
	 * @throws RuntimeException
	 *             if any exception occurs communicating with the external
	 *             ExifTool process spun up in order to check its version.
	 */
	protected static void checkFeatureSupport(Feature... features)
			throws RuntimeException {
		// Ensure there is work to do.
		if (features == null || features.length == 0)
			return;

		log("\tChecking %d feature(s) for support in the external ExifTool install...",
				features.length);

		for (int i = 0; i < features.length; i++) {
			String ver = null;
			Boolean supported;
			Feature feature = features[i];

			log("\t\tChecking feature %s for support, requires ExifTool version %s or higher...",
					feature, feature.version);

			// Execute 'exiftool -ver'
			IOStream streams = startExifToolProcess(VERIFY_FEATURE_ARGS);

			try {
				// Read the single-line reply (version number)
				ver = streams.reader.readLine();

				// Close r/w streams to exited process.
				streams.close();
			} catch (Exception e) {
				/*
				 * no-op, while it is important to know that we COULD launch the
				 * ExifTool process (i.e. startExifToolProcess call worked) but
				 * couldn't communicate with it, the context with which this
				 * method is called is from the constructor of this class which
				 * would just wrap this exception and discard it anyway if it
				 * failed.
				 * 
				 * the caller will realize there is something wrong with the
				 * ExifTool process communication as soon as they make their
				 * first call to getImageMeta in which case whatever was causing
				 * the exception here will popup there and then need to be
				 * corrected.
				 * 
				 * This is an edge case that should only happen in really rare
				 * scenarios, so making this method easier to use is more
				 * important that robust IOException handling right here.
				 */
			}

			// Ensure the version found is >= the required version.
			if (ver != null && ver.compareTo(feature.version) >= 0) {
				supported = Boolean.TRUE;
				log("\t\tFound ExifTool version %s, feature %s is SUPPORTED.",
						ver, feature);
			} else {
				supported = Boolean.FALSE;
				log("\t\tFound ExifTool version %s, feature %s is NOT SUPPORTED.",
						ver, feature);
			}

			// Update feature support map
			FEATURE_SUPPORT_MAP.put(feature, supported);
		}
	}

	protected static IOStream startExifToolProcess(List<String> args)
			throws RuntimeException {
		Process proc = null;
		IOStream streams = null;

		log("\tAttempting to start external ExifTool process using args: %s",
				args);

		try {
			proc = new ProcessBuilder(args).start();
			log("\t\tSuccessful");
		} catch (Exception e) {
			String message = "Unable to start external ExifTool process using the execution arguments: "
					+ args
					+ ". Ensure ExifTool is installed correctly and runs using the command path '"
					+ EXIF_TOOL_PATH
					+ "' as specified by the 'exiftool.path' system property.";

			log(message);
			throw new RuntimeException(message, e);
		}

		log("\tSetting up Read/Write streams to the external ExifTool process...");

		// Setup read/write streams to the new process.
		streams = new IOStream(new BufferedReader(new InputStreamReader(
				proc.getInputStream())), new OutputStreamWriter(
				proc.getOutputStream()));

		log("\t\tSuccessful, returning streams to caller.");
		return streams;
	}

	/**
	 * Simple class used to house the read/write streams used to communicate
	 * with an external ExifTool process as well as the logic used to safely
	 * close the streams when no longer needed.
	 * <p/>
	 * This class is just a convenient way to group and manage the read/write
	 * streams as opposed to making them dangling member variables off of
	 * ExifTool directly.
	 * 
	 * @author Riyad Kalla (software@thebuzzmedia.com)
	 * @since 1.1
	 */
	private static class IOStream {
		BufferedReader reader;
		OutputStreamWriter writer;

		public IOStream(BufferedReader reader, OutputStreamWriter writer) {
			this.reader = reader;
			this.writer = writer;
		}

		public void close() {
			try {
				log("\tClosing Read stream...");
				reader.close();
				log("\t\tSuccessful");
			} catch (Exception e) {
				// no-op, just try to close it.
			}

			try {
				log("\tClosing Write stream...");
				writer.close();
				log("\t\tSuccessful");
			} catch (Exception e) {
				// no-op, just try to close it.
			}

			// Null the stream references.
			reader = null;
			writer = null;

			log("\tRead/Write streams successfully closed.");
		}
	}

	/**
	 * Enum used to define the different kinds of features in the native
	 * ExifTool executable that this class can help you take advantage of.
	 * <p/>
	 * These flags are different from {@link Tag}s in that a "feature" is
	 * determined to be a special functionality of the underlying ExifTool
	 * executable that requires a different code-path in this class to take
	 * advantage of; for example, <code>-stay_open True</code> support.
	 * 
	 * @author Riyad Kalla (software@thebuzzmedia.com)
	 * @since 1.1
	 */
	public enum Feature {
		/**
		 * Enum used to specify that you wish to launch the underlying ExifTool
		 * process with <code>-stay_open True</code> support turned on that this
		 * class can then take advantage of.
		 * <p/>
		 * Required ExifTool version is <code>8.36</code> or higher.
		 */
		STAY_OPEN("8.36");

		/**
		 * Used to get the version of ExifTool required by this feature in order
		 * to work.
		 * 
		 * @return the version of ExifTool required by this feature in order to
		 *         work.
		 */
		public String getVersion() {
			return version;
		}

		private String version;

		private Feature(String version) {
			this.version = version;
		}
	}

	/**
	 * Enum used to define the 2 different output formats that {@link Tag}
	 * values can be returned in: numeric or human-readable text.
	 * <p/>
	 * ExifTool, via the <code>-n</code> command line arg, is capable of
	 * returning most values in their raw numeric form (e.g.
	 * Aperture="2.8010323841") as well as a more human-readable/friendly format
	 * (e.g. Aperture="2.8").
	 * <p/>
	 * While the {@link Tag}s defined on this class do provide a hint at the
	 * type of the result (see {@link Tag#getType()}), that hint only applies
	 * when the {@link Format#NUMERIC} form of the value is returned.
	 * <p/>
	 * If the caller finds the human-readable format easier to process,
	 * {@link Format#HUMAN_READABLE} can be specified when calling
	 * {@link ExifTool#getImageMeta(File, Format, Tag...)} and the returned
	 * {@link String} values processed manually by the caller.
	 * <p/>
	 * In order to see the types of values that are returned when
	 * {@link Format#HUMAN_READABLE} is used, you can check the comprehensive <a
	 * href="http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html">
	 * ExifTool Tag Guide</a>.
	 * <p/>
	 * This makes sense with some values like Aperture that in
	 * {@link Format#NUMERIC} format end up returning as 14-decimal-place, high
	 * precision values that are near the intended value (e.g.
	 * "2.79999992203711" instead of just returning "2.8"). On the other hand,
	 * other values (like Orientation) are easier to parse when their numeric
	 * value (1-8) is returned instead of a much longer friendly name (e.g.
	 * "Mirror horizontal and rotate 270 CW").
	 * 
	 * @author Riyad Kalla (software@thebuzzmedia.com)
	 * @since 1.1
	 */
	public enum Format {
		NUMERIC, HUMAN_READABLE;
	}

	/**
	 * Enum used to pre-define a convenient list of tags that can be easily
	 * extracted from images using this class with an external install of
	 * ExifTool.
	 * <p/>
	 * Each tag defined also includes a type hint for the parsed value
	 * associated with it when the default {@link Format#NUMERIC} value format
	 * is used.
	 * <p/>
	 * All replies from ExifTool are parsed as {@link String}s and using the
	 * type hint from each {@link Tag} can easily be converted to the correct
	 * data format by using the provided {@link Tag#parseValue(Tag, String)}
	 * method.
	 * <p/>
	 * This class does not make an attempt at converting the value automatically
	 * in case the caller decides they would prefer tag values returned in
	 * {@link Format#HUMAN_READABLE} format and to avoid any compatibility
	 * issues with future versions of ExifTool if a tag's return value is
	 * changed. This approach to leaving returned tag values as strings until
	 * the caller decides they want to parse them is a safer and more robust
	 * approach.
	 * <p/>
	 * The types provided by each tag are merely a hint based on the <a
	 * href="http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html"
	 * >ExifTool Tag Guide</a> by Phil Harvey; the caller is free to parse or
	 * process the returned {@link String} values any way they wish.
	 * <h3>Tag Support</h3>
	 * ExifTool is capable of parsing almost every tag known to man (1000+), but
	 * this class makes an attempt at pre-defining a convenient list of the most
	 * common tags for use.
	 * <p/>
	 * This list was determined by looking at the common metadata tag values
	 * written to images by popular mobile devices (iPhone, Android) as well as
	 * cameras like simple point and shoots as well as DSLRs. As an additional
	 * source of input the list of supported/common EXIF formats that Flickr
	 * supports was also reviewed to ensure the most common/useful tags were
	 * being covered here.
	 * <p/>
	 * Please email me or <a
	 * href="https://github.com/thebuzzmedia/imgscalr/issues">file an issue</a>
	 * if you think this list is missing a commonly used tag that should be
	 * added to it.
	 * 
	 * @author Riyad Kalla (software@thebuzzmedia.com)
	 * @since 1.1
	 */
	public enum Tag {
		ISO("ISO", Integer.class), APERTURE("ApertureValue", Double.class), WHITE_BALANCE(
				"WhiteBalance", Integer.class), CONTRAST("Contrast",
				Integer.class), SATURATION("Saturation", Integer.class), SHARPNESS(
				"Sharpness", Integer.class), SHUTTER_SPEED("ShutterSpeedValue",
				Double.class), DIGITAL_ZOOM_RATIO("DigitalZoomRatio",
				Double.class), IMAGE_WIDTH("ImageWidth", Integer.class), IMAGE_HEIGHT(
				"ImageHeight", Integer.class), X_RESOLUTION("XResolution",
				Double.class), Y_RESOLUTION("YResolution", Double.class), FLASH(
				"Flash", Integer.class), METERING_MODE("MeteringMode",
				Integer.class), FOCAL_LENGTH("FocalLength", Double.class), FOCAL_LENGTH_35MM(
				"FocalLengthIn35mmFormat", Integer.class), EXPOSURE_TIME(
				"ExposureTime", Double.class), EXPOSURE_COMPENSATION(
				"ExposureCompensation", Double.class), EXPOSURE_PROGRAM(
				"ExposureProgram", Integer.class), ORIENTATION("Orientation",
				Integer.class), COLOR_SPACE("ColorSpace", Integer.class), SENSING_METHOD(
				"SensingMethod", Integer.class), SOFTWARE("Software",
				String.class), MAKE("Make", String.class), MODEL("Model",
				String.class), LENS_MAKE("LensMake", String.class), LENS_MODEL(
				"LensModel", String.class), OWNER_NAME("OwnerName",
				String.class), TITLE("XPTitle", String.class), AUTHOR(
				"XPAuthor", String.class), SUBJECT("XPSubject", String.class), KEYWORDS(
				"XPKeywords", String.class), COMMENT("XPComment", String.class), RATING(
				"Rating", Integer.class), RATING_PERCENT("RatingPercent",
				Integer.class), DATE_TIME_ORIGINAL("DateTimeOriginal",
				String.class), CREATION_DATE("CreationDate", String.class), GPS_LATITUDE(
				"GPSLatitude", Double.class), GPS_LATITUDE_REF(
				"GPSLatitudeRef", String.class), GPS_LONGITUDE("GPSLongitude",
				Double.class), GPS_LONGITUDE_REF("GPSLongitudeRef",
				String.class), GPS_ALTITUDE("GPSAltitude", Double.class), GPS_ALTITUDE_REF(
				"GPSAltitudeRef", Integer.class), GPS_SPEED("GPSSpeed",
				Double.class), GPS_SPEED_REF("GPSSpeedRef", String.class), GPS_PROCESS_METHOD(
				"GPSProcessingMethod", String.class), GPS_BEARING(
				"GPSDestBearing", Double.class), GPS_BEARING_REF(
				"GPSDestBearingRef", String.class), GPS_TIMESTAMP(
				"GPSTimeStamp", String.class), ROTATION("Rotation",Integer.class),
				EXIF_VERSION("ExifVersion",String.class), LENS_ID("LensID",String.class),
				COPYRIGHT("Copyright", String.class), ARTIST("Artist", String.class),
                SUB_SEC_TIME_ORIGINAL("SubSecTimeOriginal", Integer.class),
                OBJECT_NAME("ObjectName", String.class), CAPTION_ABSTRACT("Caption-Abstract",
                String.class), CREATOR("Creator", String.class), IPTC_KEYWORDS("Keywords",
                String.class), COPYRIGHT_NOTICE("CopyrightNotice", String.class),
                FILE_TYPE("FileType", String.class), AVG_BITRATE("AvgBitrate", String.class),
                MIME_TYPE("MIMEType", String.class);

		private static final Map<String, Tag> TAG_LOOKUP_MAP;

		/**
		 * Initializer used to init the <code>static final</code> tag/name
		 * lookup map used by all instances of this class.
		 */
		static {
			Tag[] values = Tag.values();
			TAG_LOOKUP_MAP = new HashMap<String, ExifTool.Tag>(
					values.length * 3);

			for (int i = 0; i < values.length; i++) {
				Tag tag = values[i];
				TAG_LOOKUP_MAP.put(tag.name, tag);
			}
		}

		/**
		 * Used to get the {@link Tag} identified by the given, case-sensitive,
		 * tag name.
		 * 
		 * @param name
		 *            The case-sensitive name of the tag that will be searched
		 *            for.
		 * 
		 * @return the {@link Tag} identified by the given, case-sensitive, tag
		 *         name or <code>null</code> if one couldn't be found.
		 */
		public static Tag forName(String name) {
			return TAG_LOOKUP_MAP.get(name);
		}

		/**
		 * Convenience method used to convert the given string Tag value
		 * (returned from the external ExifTool process) into the type described
		 * by the associated {@link Tag}.
		 * 
		 * @param <T>
		 *            The type of the returned value.
		 * @param tag
		 *            The {@link Tag} whose value this is. The tag's type hint
		 *            will be queried to determine how to convert this string
		 *            value.
		 * @param value
		 *            The {@link String} representation of the tag's value as
		 *            parsed from the image.
		 * 
		 * @return the given string value converted to a native Java type (e.g.
		 *         Integer, Double, etc.).
		 * 
		 * @throws IllegalArgumentException
		 *             if <code>tag</code> is <code>null</code>.
		 * @throws NumberFormatException
		 *             if any exception occurs while trying to parse the given
		 *             <code>value</code> to any of the supported numeric types
		 *             in Java via calls to the respective <code>parseXXX</code>
		 *             methods defined on all the numeric wrapper classes (e.g.
		 *             {@link Integer#parseInt(String)} ,
		 *             {@link Double#parseDouble(String)} and so on).
		 * @throws ClassCastException
		 *             if the type defined by <code>T</code> is incompatible
		 *             with the type defined by {@link Tag#getType()} returned
		 *             by the <code>tag</code> argument passed in. This class
		 *             performs an implicit/unchecked cast to the type
		 *             <code>T</code> before returning the parsed result of the
		 *             type indicated by {@link Tag#getType()}. If the types do
		 *             not match, a <code>ClassCastException</code> will be
		 *             generated by the VM.
		 */
		@SuppressWarnings("unchecked")
		public static <T> T parseValue(Tag tag, String value)
				throws IllegalArgumentException, NumberFormatException {
			if (tag == null)
				throw new IllegalArgumentException("tag cannot be null");

			T result = null;

			// Check that there is work to do first.
			if (value != null) {
				Class<?> type = tag.type;

				if (Boolean.class.isAssignableFrom(type))
					result = (T) Boolean.valueOf(value);
				else if (Byte.class.isAssignableFrom(type))
					result = (T) Byte.valueOf(Byte.parseByte(value));
				else if (Integer.class.isAssignableFrom(type))
					result = (T) Integer.valueOf(Integer.parseInt(value));
				else if (Short.class.isAssignableFrom(type))
					result = (T) Short.valueOf(Short.parseShort(value));
				else if (Long.class.isAssignableFrom(type))
					result = (T) Long.valueOf(Long.parseLong(value));
				else if (Float.class.isAssignableFrom(type))
					result = (T) Float.valueOf(Float.parseFloat(value));
				else if (Double.class.isAssignableFrom(type))
					result = (T) Double.valueOf(Double.parseDouble(value));
				else if (Character.class.isAssignableFrom(type))
					result = (T) Character.valueOf(value.charAt(0));
				else if (String.class.isAssignableFrom(type))
					result = (T) value;
			}

			return result;
		}

		/**
		 * Used to get the name of the tag (e.g. "Orientation", "ISO", etc.).
		 * 
		 * @return the name of the tag (e.g. "Orientation", "ISO", etc.).
		 */
		public String getName() {
			return name;
		}

		/**
		 * Used to get a hint for the native type of this tag's value as
		 * specified by Phil Harvey's <a href=
		 * "http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html"
		 * >ExifTool Tag Guide</a>.
		 * 
		 * @return a hint for the native type of this tag's value.
		 */
		public Class<?> getType() {
			return type;
		}

		private String name;
		private Class<?> type;

		private Tag(String name, Class<?> type) {
			this.name = name;
			this.type = type;
		}
	}

	private Timer cleanupTimer;
	private TimerTask currentCleanupTask;

	private IOStream streams;
	private List<String> args;

	private Set<Feature> featureSet;

	public ExifTool() {
		this((Feature[]) null);
	}

	public ExifTool(Feature... features) throws UnsupportedFeatureException {
		featureSet = new HashSet<ExifTool.Feature>();

		if (features != null && features.length > 0) {
			/*
			 * Process all features to ensure we checked them for support in the
			 * installed version of ExifTool. If the feature has already been
			 * checked before, this method will return immediately.
			 */
			checkFeatureSupport(features);

			/*
			 * Now we need to verify that all the features requested for this
			 * instance of ExifTool to use WERE supported after all.
			 */
			for (int i = 0; i < features.length; i++) {
				Feature f = features[i];

				/*
				 * If the Feature was supported, record it in the local
				 * featureSet so this instance knows what features are being
				 * turned on by the caller.
				 * 
				 * If the Feature was not supported, throw an exception
				 * reporting it to the caller so they know it cannot be used.
				 */
				if (FEATURE_SUPPORT_MAP.get(f).booleanValue())
					featureSet.add(f);
				else
					throw new UnsupportedFeatureException(f);
			}
		}

		args = new ArrayList<String>(64);

		/*
		 * Now that initialization is done, init the cleanup timer if we are
		 * using STAY_OPEN and the delay time set is non-zero.
		 */
		if (isFeatureEnabled(Feature.STAY_OPEN) && PROCESS_CLEANUP_DELAY > 0) {
			this.cleanupTimer = new Timer(CLEANUP_THREAD_NAME, true);

			// Start the first cleanup task counting down.
			resetCleanupTask();
		}
	}

	/**
	 * Used to shutdown the external ExifTool process and close the read/write
	 * streams used to communicate with it when {@link Feature#STAY_OPEN} is
	 * enabled.
	 * <p/>
	 * <strong>NOTE</strong>: Calling this method does not preclude this
	 * instance of {@link ExifTool} from being re-used, it merely disposes of
	 * the native and internal resources until the next call to
	 * <code>getImageMeta</code> causes them to be re-instantiated.
	 * <p/>
	 * The cleanup thread will automatically call this after an interval of
	 * inactivity defined by {@link #PROCESS_CLEANUP_DELAY}.
	 * <p/>
	 * Calling this method on an instance of this class without
	 * {@link Feature#STAY_OPEN} support enabled has no effect.
	 */
	public void close() {
		/*
		 * no-op if the underlying process and streams have already been closed
		 * OR if stayOpen was never used in the first place in which case
		 * nothing is open right now anyway.
		 */
		if (streams == null)
			return;

		/*
		 * If ExifTool was used in stayOpen mode but getImageMeta was never
		 * called then the streams were never initialized and there is nothing
		 * to shut down or destroy, otherwise we need to close down all the
		 * resources in use.
		 */
		if (streams == null) {
			log("\tThis ExifTool instance was never used so no external process or streams were ever created (nothing to clean up, we will just exit).");
		} else {
			try {
				log("\tAttempting to close ExifTool daemon process, issuing '-stay_open\\nFalse\\n' command...");

				// Tell the ExifTool process to exit.
				streams.writer.write("-stay_open\nFalse\n");
				streams.writer.flush();

				log("\t\tSuccessful");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				streams.close();
			}
		}

		streams = null;
		log("\tExifTool daemon process successfully terminated.");
	}

	/**
	 * For {@link ExifTool} instances with {@link Feature#STAY_OPEN} support
	 * enabled, this method is used to determine if there is currently a running
	 * ExifTool process associated with this class.
	 * <p/>
	 * Any dependent processes and streams can be shutdown using
	 * {@link #close()} and this class will automatically re-create them on the
	 * next call to <code>getImageMeta</code> if necessary.
	 * 
	 * @return <code>true</code> if there is an external ExifTool process in
	 *         daemon mode associated with this class utilizing the
	 *         {@link Feature#STAY_OPEN} feature, otherwise returns
	 *         <code>false</code>.
	 */
	public boolean isRunning() {
		return (streams != null);
	}

	/**
	 * Used to determine if the given {@link Feature} has been enabled for this
	 * particular instance of {@link ExifTool}.
	 * <p/>
	 * This method is different from {@link #isFeatureSupported(Feature)}, which
	 * checks if the given feature is supported by the underlying ExifTool
	 * install where as this method tells the caller if the given feature has
	 * been enabled for use in this particular instance.
	 * 
	 * @param feature
	 *            The feature to check if it has been enabled for us or not on
	 *            this instance.
	 * 
	 * @return <code>true</code> if the given {@link Feature} is currently
	 *         enabled on this instance of {@link ExifTool}, otherwise returns
	 *         <code>false</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>feature</code> is <code>null</code>.
	 */
	public boolean isFeatureEnabled(Feature feature)
			throws IllegalArgumentException {
		if (feature == null)
			throw new IllegalArgumentException("feature cannot be null");

		return featureSet.contains(feature);
	}

	public Map<Tag, String> getImageMeta(File image, Tag... tags)
			throws IllegalArgumentException, SecurityException, IOException {
		return getImageMeta(image, Format.NUMERIC, tags);
	}

	public Map<Tag, String> getImageMeta(File image, Format format, Tag... tags)
			throws IllegalArgumentException, SecurityException, IOException {
		if (image == null)
			throw new IllegalArgumentException(
					"image cannot be null and must be a valid stream of image data.");
		if (format == null)
			throw new IllegalArgumentException("format cannot be null");
		if (tags == null || tags.length == 0)
			throw new IllegalArgumentException(
					"tags cannot be null and must contain 1 or more Tag to query the image for.");
		if (!image.canRead())
			throw new SecurityException(
					"Unable to read the given image ["
							+ image.getAbsolutePath()
							+ "], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		long startTime = System.currentTimeMillis();

		/*
		 * Create a result map big enough to hold results for each of the tags
		 * and avoid collisions while inserting.
		 */
		Map<Tag, String> resultMap = new HashMap<ExifTool.Tag, String>(
				tags.length * 3);

		if (DEBUG)
			log("Querying %d tags from image: %s", tags.length,
					image.getAbsolutePath());

		long exifToolCallElapsedTime = 0;

		/*
		 * Using ExifTool in daemon mode (-stay_open True) executes different
		 * code paths below. So establish the flag for this once and it is
		 * reused a multitude of times later in this method to figure out where
		 * to branch to.
		 */
		boolean stayOpen = featureSet.contains(Feature.STAY_OPEN);

		// Clear process args
		args.clear();

		if (stayOpen) {
			log("\tUsing ExifTool in daemon mode (-stay_open True)...");

			// Always reset the cleanup task.
			resetCleanupTask();

			/*
			 * If this is our first time calling getImageMeta with a stayOpen
			 * connection, set up the persistent process and run it so it is
			 * ready to receive commands from us.
			 */
			if (streams == null) {
				log("\tStarting daemon ExifTool process and creating read/write streams (this only happens once)...");

				args.add(EXIF_TOOL_PATH);
				args.add("-stay_open");
				args.add("True");
				args.add("-@");
				args.add("-");

				// Begin the persistent ExifTool process.
				streams = startExifToolProcess(args);
			}

			log("\tStreaming arguments to ExifTool process...");

			if (format == Format.NUMERIC)
				streams.writer.write("-n\n"); // numeric output

			streams.writer.write("-S\n"); // compact output

			for (int i = 0; i < tags.length; i++) {
				streams.writer.write('-');
				streams.writer.write(tags[i].name);
				streams.writer.write("\n");
			}

			streams.writer.write(image.getAbsolutePath());
			streams.writer.write("\n");

			log("\tExecuting ExifTool...");

			// Begin tracking the duration ExifTool takes to respond.
			exifToolCallElapsedTime = System.currentTimeMillis();

			// Run ExifTool on our file with all the given arguments.
			streams.writer.write("-execute\n");
			streams.writer.flush();
		} else {
			log("\tUsing ExifTool in non-daemon mode (-stay_open False)...");

			/*
			 * Since we are not using a stayOpen process, we need to setup the
			 * execution arguments completely each time.
			 */
			args.add(EXIF_TOOL_PATH);

			if (format == Format.NUMERIC)
				args.add("-n"); // numeric output

			args.add("-S"); // compact output

			for (int i = 0; i < tags.length; i++)
				args.add("-" + tags[i].name);

			args.add(image.getAbsolutePath());

			// Run the ExifTool with our args.
			streams = startExifToolProcess(args);

			// Begin tracking the duration ExifTool takes to respond.
			exifToolCallElapsedTime = System.currentTimeMillis();
		}

		log("\tReading response back from ExifTool...");

		String line = null;

		while ((line = streams.reader.readLine()) != null) {
			String[] pair = TAG_VALUE_PATTERN.split(line);

			if (pair != null && pair.length == 2) {
				// Determine the tag represented by this value.
				Tag tag = Tag.forName(pair[0]);

				/*
				 * Store the tag and the associated value in the result map only
				 * if we were able to map the name back to a Tag instance. If
				 * not, then this is an unknown/unexpected tag return value and
				 * we skip it since we cannot translate it back to one of our
				 * supported tags.
				 */
				if (tag != null) {
					resultMap.put(tag, pair[1]);
					log("\t\tRead Tag [name=%s, value=%s]", tag.name, pair[1]);
				}
			}

			/*
			 * When using a persistent ExifTool process, it terminates its
			 * output to us with a "{ready}" clause on a new line, we need to
			 * look for it and break from this loop when we see it otherwise
			 * this process will hang indefinitely blocking on the input stream
			 * with no data to read.
			 */
			if (stayOpen && line.equals("{ready}"))
				break;
		}

		// Print out how long the call to external ExifTool process took.
		log("\tFinished reading ExifTool response in %d ms.",
				(System.currentTimeMillis() - exifToolCallElapsedTime));

		/*
		 * If we are not using a persistent ExifTool process, then after running
		 * the command above, the process exited in which case we need to clean
		 * our streams up since it no longer exists. If we were using a
		 * persistent ExifTool process, leave the streams open for future calls.
		 */
		if (!stayOpen)
			streams.close();

		if (DEBUG)
			log("\tImage Meta Processed in %d ms [queried %d tags and found %d values]",
					(System.currentTimeMillis() - startTime), tags.length,
					resultMap.size());

		return resultMap;
	}
        
        public void setImageMeta(File image, Map<Tag, String> tags) 
                        throws IllegalArgumentException, SecurityException, IOException {
		setImageMeta(image, Format.NUMERIC, tags);
	}
        
        public void setImageMeta(File image, Format format, Map<Tag, String> tags )
			throws IllegalArgumentException, SecurityException, IOException {
		if (image == null)
			throw new IllegalArgumentException(
					"image cannot be null and must be a valid stream of image data.");
		if (format == null)
			throw new IllegalArgumentException("format cannot be null");
		if (tags == null || tags.size() == 0)
			throw new IllegalArgumentException(
					"tags cannot be null and must contain 1 or more Tag to query the image for.");
		if (!image.canWrite())
			throw new SecurityException(
					"Unable to read the given image ["
							+ image.getAbsolutePath()
							+ "], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		long startTime = System.currentTimeMillis();

		if (DEBUG)
			log("Writing %d tags to image: %s", tags.size(),
					image.getAbsolutePath());

		long exifToolCallElapsedTime = 0;

		/*
		 * Using ExifTool in daemon mode (-stay_open True) executes different
		 * code paths below. So establish the flag for this once and it is
		 * reused a multitude of times later in this method to figure out where
		 * to branch to.
		 */
		boolean stayOpen = featureSet.contains(Feature.STAY_OPEN);

		// Clear process args
		args.clear();

		if (stayOpen) {
			log("\tUsing ExifTool in daemon mode (-stay_open True)...");

			// Always reset the cleanup task.
			resetCleanupTask();

			/*
			 * If this is our first time calling getImageMeta with a stayOpen
			 * connection, set up the persistent process and run it so it is
			 * ready to receive commands from us.
			 */
			if (streams == null) {
				log("\tStarting daemon ExifTool process and creating read/write streams (this only happens once)...");

				args.add(EXIF_TOOL_PATH);
				args.add("-stay_open");
				args.add("True");
				args.add("-@");
				args.add("-");

				// Begin the persistent ExifTool process.
				streams = startExifToolProcess(args);
			}

			log("\tStreaming arguments to ExifTool process...");

			if (format == Format.NUMERIC)
				streams.writer.write("-n\n"); // numeric output

			streams.writer.write("-S\n"); // compact output

                        for ( Entry<Tag,String> entry :tags.entrySet() ) {
				streams.writer.write('-');
				streams.writer.write(entry.getKey().name);
				streams.writer.write("='");
				streams.writer.write(entry.getValue());
				streams.writer.write("'\n");
			}

			streams.writer.write(image.getAbsolutePath());
			streams.writer.write("\n");

			log("\tExecuting ExifTool...");

			// Begin tracking the duration ExifTool takes to respond.
			exifToolCallElapsedTime = System.currentTimeMillis();

			// Run ExifTool on our file with all the given arguments.
			streams.writer.write("-execute\n");
			streams.writer.flush();
		} else {
			log("\tUsing ExifTool in non-daemon mode (-stay_open False)...");

			/*
			 * Since we are not using a stayOpen process, we need to setup the
			 * execution arguments completely each time.
			 */
			args.add(EXIF_TOOL_PATH);

			if (format == Format.NUMERIC)
				args.add("-n"); // numeric output

			args.add("-S"); // compact output

			for ( Entry<Tag,String> entry :tags.entrySet() )
				args.add("-" + entry.getKey().name + "='" + entry.getValue() + "'" );

			args.add(image.getAbsolutePath());

			// Run the ExifTool with our args.
			streams = startExifToolProcess(args);

			// Begin tracking the duration ExifTool takes to respond.
			exifToolCallElapsedTime = System.currentTimeMillis();
		}

		log("\tReading response back from ExifTool...");

		String line = null;

		while ((line = streams.reader.readLine()) != null) {
			/*
			 * When using a persistent ExifTool process, it terminates its
			 * output to us with a "{ready}" clause on a new line, we need to
			 * look for it and break from this loop when we see it otherwise
			 * this process will hang indefinitely blocking on the input stream
			 * with no data to read.
			 */
			if (stayOpen && line.equals("{ready}"))
				break;
		}

		// Print out how long the call to external ExifTool process took.
		log("\tFinished reading ExifTool response in %d ms.",
				(System.currentTimeMillis() - exifToolCallElapsedTime));

		/*
		 * If we are not using a persistent ExifTool process, then after running
		 * the command above, the process exited in which case we need to clean
		 * our streams up since it no longer exists. If we were using a
		 * persistent ExifTool process, leave the streams open for future calls.
		 */
		if (!stayOpen)
			streams.close();

		if (DEBUG)
			log("\tImage Meta Processed in %d ms [write %d tags]",
					(System.currentTimeMillis() - startTime), tags.size());
	}

	/**
	 * Helper method used to make canceling the current task and scheduling a
	 * new one easier.
	 * <p/>
	 * It is annoying that we cannot just reset the timer on the task, but that
	 * isn't the way the java.util.Timer class was designed unfortunately.
	 */
	private void resetCleanupTask() {
		// no-op if the timer was never created.
		if (cleanupTimer == null)
			return;

		log("\tResetting cleanup task...");

		// Cancel the current cleanup task if necessary.
		if (currentCleanupTask != null)
			currentCleanupTask.cancel();

		// Schedule a new cleanup task.
		cleanupTimer.schedule(
				(currentCleanupTask = new CleanupTimerTask(this)),
				PROCESS_CLEANUP_DELAY, PROCESS_CLEANUP_DELAY);

		log("\t\tSuccessful");
	}

	/**
	 * Class used to represent the {@link TimerTask} used by the internal auto
	 * cleanup {@link Timer} to call {@link ExifTool#close()} after a specified
	 * interval of inactivity.
	 * 
	 * @author Riyad Kalla (software@thebuzzmedia.com)
	 * @since 1.1
	 */
	private class CleanupTimerTask extends TimerTask {
		private ExifTool owner;

		public CleanupTimerTask(ExifTool owner) throws IllegalArgumentException {
			if (owner == null)
				throw new IllegalArgumentException(
						"owner cannot be null and must refer to the ExifTool instance creating this task.");

			this.owner = owner;
		}

		@Override
		public void run() {
			log("\tAuto cleanup task running...");
			owner.close();
		}
	}

	/**
	 * Class used to define an exception that occurs when the caller attempts to
	 * use a {@link Feature} that the underlying native ExifTool install does
	 * not support (i.e. the version isn't new enough).
	 * 
	 * @author Riyad Kalla (software@thebuzzmedia.com)
	 * @since 1.1
	 */
	public class UnsupportedFeatureException extends RuntimeException {
		private static final long serialVersionUID = -1332725983656030770L;

		private Feature feature;

		public UnsupportedFeatureException(Feature feature) {
			super(
					"Use of feature ["
							+ feature
							+ "] requires version "
							+ feature.version
							+ " or higher of the native ExifTool program. The version of ExifTool referenced by the system property 'exiftool.path' is not high enough. You can either upgrade the install of ExifTool or avoid using this feature to workaround this exception.");
		}

		public Feature getFeature() {
			return feature;
		}
	}
}
