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

import com.thebuzzmedia.exiftool.core.StandardFormat;
import com.thebuzzmedia.exiftool.core.cache.VersionCacheFactory;
import com.thebuzzmedia.exiftool.core.handlers.TagHandler;
import com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.CommandExecutor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.isReadable;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.isWritable;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notBlank;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notEmpty;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notNull;
import static com.thebuzzmedia.exiftool.core.handlers.StopHandler.stopHandler;

/**
 * Class used to provide a Java-like interface to Phil Harvey's excellent,
 * Perl-based <a href="http://www.sno.phy.queensu.ca/~phil/exiftool">ExifTool</a>.
 *
 * There are a number of other basic Java wrappers to ExifTool available online,
 * but most of them only abstract out the actual Java-external-process execution
 * logic and do no additional work to make integration with the external
 * ExifTool any easier or intuitive from the perspective of the Java application
 * written to make use of ExifTool.
 *
 * This class was written in order to make integration with ExifTool inside of a
 * Java application seamless and performant with the goal being that the
 * developer can treat ExifTool as if it were written in Java, garnering all of
 * the benefits with none of the added headache of managing an external native
 * process from Java.
 *
 * Phil Harvey's ExifTool is written in Perl and runs on all major platforms
 * (including Windows) so no portability issues are introduced into your
 * application by utilizing this class.
 *
 * <h3>Usage</h3>
 *
 * Assuming ExifTool is installed on the host system correctly and either in the
 * system path, using this class to communicate with ExifTool is as simple as
 * creating an instance using {@link ExifToolBuilder}:
 *
 * <pre><code>
 *     ExifTool tool = new ExifToolBuilder().build();
 * </code></pre>
 *
 * This mode assume that:
 * <ul>
 *   <li>Path is set as an environment variable (i.e. {@code -Dexiftool.withPath=/usr/local/exiftool/bin/exiftool}).</li>
 *   <li>Or globally available.</li>
 * </ul>
 *
 * If you want to set the path of {@code ExifTool}, you can also specify it during creation:
 *
 * <pre><code>
 *     ExifTool tool = new ExifToolBuilder()
 *         .withPath("/usr/local/exiftool/bin/exiftool")
 *         .build();
 * </code></pre>
 *
 * Once created, usage is as simple as making calls to {@link #getImageMeta(File, Collection)} or
 * {@link #getImageMeta(File, Format, Collection)} with a list of {@link Tag} you want to pull
 * values for from the given image.
 *
 * In this default mode, calls to {@link #getImageMeta} will automatically
 * start an external ExifTool process to handle the request. After ExifTool has
 * parsed the tag values from the file, the external process exits and this
 * class parses the result before returning it to the caller.
 *
 * Results from calls to {@link #getImageMeta} are returned in a {@link Map}
 * with the {@link com.thebuzzmedia.exiftool.core.StandardTag} values as the keys and {@link String} values for every
 * tag that had a value in the image file as the values. {@link com.thebuzzmedia.exiftool.core.StandardTag}s with no
 * value found in the image are omitted from the result map.
 *
 * While each {@link com.thebuzzmedia.exiftool.core.StandardTag} provides a hint at which format the resulting value
 * for that tag is returned as from ExifTool (see {@link com.thebuzzmedia.exiftool.Tag#parse(String)}), that
 * only applies to values returned with an output format of
 * {@link com.thebuzzmedia.exiftool.core.StandardFormat#NUMERIC} and it is ultimately up to the caller to decide how
 * best to parse or convert the returned values.
 *
 * The {@link com.thebuzzmedia.exiftool.core.StandardTag} Enum provides the {@link com.thebuzzmedia.exiftool.Tag#parse(String)}}
 * convenience method for parsing given `String` values according to
 * the Tag hint automatically for you if that is what you plan on doing,
 * otherwise feel free to handle the return values anyway you want.
 *
 * <h3>ExifTool -stay_open Support</h3>
 *
 * ExifTool <a href="http://u88.n24.queensu.ca/exiftool/forum/index.php/topic,1402.msg12933.html#msg12933">8.36</a>
 * added a new persistent-process feature that allows ExifTool to stay
 * running in a daemon mode and continue accepting commands via a file or stdin.
 *
 * This new mode is controlled via the {@code -stay_open True/False}
 * command line argument and in a busy system that is making thousands of calls
 * to ExifTool, can offer speed improvements of up to <strong>60x</strong> (yes,
 * really that much).
 *
 * This feature was added to ExifTool shortly after user
 * <a href="http://www.christian-etter.de/?p=458">Christian Etter discovered</a> the overhead
 * for starting up a new Perl interpreter each time ExifTool is loaded accounts for
 * roughly <a href="http://u88.n24.queensu.ca/exiftool/forum/index.php/topic,1402.msg6121.html#msg6121">98.4% of the total runtime</a>.
 *
 * Support for using ExifTool in daemon mode is enabled by explicitly calling
 * {@link ExifToolBuilder#enableStayOpen()} method.
 * Calling this method will create an instance of {@link ExifTool} with {@link com.thebuzzmedia.exiftool.core.strategies.StayOpenStrategy} execution strategy.
 *
 * Because this feature requires ExifTool 8.36 or later, this class will
 * actually verify support for the feature in the version of ExifTool
 * before successfully instantiating the class and will notify you via
 * an {@link com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException} if the native
 * ExifTool doesn't support the requested feature.
 *
 * In the event of an {@link com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException}, the caller can either
 * upgrade the native ExifTool upgrade to the version required or simply avoid
 * using that feature to work around the exception.
 *
 * <h3>Automatic Resource Cleanup</h3>
 *
 * When {@code stay_open} mode is used, there is the potential for
 * leaking both host OS processes (native {@code exiftool} processes) as well as the
 * read/write streams used to communicate with it unless {@link #close()} is
 * called to clean them up when done. <strong>Fortunately</strong>, this library
 * provides an automatic cleanup mechanism that runs, by default, after 10 minutes
 * of inactivity to clean up those stray resources.
 *
 * The inactivity period can be controlled by modifying the
 * {@code exifTool.processCleanupDelay} system variable. A value of <code>0</code> or
 * less disabled the automatic cleanup process and requires you to cleanup
 * ExifTool instances on your own by calling {@link #close()} manually.
 *
 * You can also set this delay manually using {@link com.thebuzzmedia.exiftool.ExifToolBuilder}:
 * <pre><code>
 *     ExifTool exifTool = new ExifToolBuilder()
 *         .enableStayOpen(60000) // Try to clean resources once per minutes.
 *         .build();
 * </code></pre>
 *
 * Any class activity by way of calls to <code>getImageMeta</code> will always
 * reset the inactivity timer, so in a busy system the cleanup thread could
 * potentially never run, leaving the original host ExifTool process running
 * forever (which is fine).
 *
 * This design was chosen to help make using the class and not introducing
 * memory leaks and bugs into your code easier as well as making very inactive
 * instances of this class light weight while not in-use by cleaning up after
 * themselves.
 *
 * The only overhead incurred when opening the process back up is a 250-500ms
 * lag while launching the VM interpreter again on the first call (depending on
 * host machine speed and load).
 *
 * <h3>Reusing a "closed" ExifTool Instance</h3>
 *
 * If you or the cleanup thread have called {@link #close()} on an instance of
 * this class, cleaning up the host process and read/write streams, the instance
 * of this class can still be safely used. Any followup calls to
 * <code>getImageMeta</code> will simply re-instantiate all the required
 * resources necessary to service the call.
 *
 * This can be handy behavior to be aware of when writing scheduled processing
 * jobs that may wake up every hour and process thousands of pictures then go
 * back to sleep. In order for the process to execute as fast as possible, you
 * would want to use ExifTool in daemon mode (use {@link ExifToolBuilder#enableStayOpen})
 * and when done, instead of {@link #close()}-ing the instance of this class and throwing it
 * out, you can keep the reference around and re-use it again when the job executes again an hour later.
 *
 * <h3>Performance</h3>
 *
 * Extra care is taken to ensure minimal object creation or unnecessary CPU
 * overhead while communicating with the external process.
 *
 * {@link Pattern}s used to split the responses from the process are explicitly
 * compiled and reused, string concatenation is minimized, Tag name lookup is
 * done via a <code>static final</code> {@link Map} shared by all instances and
 * so on.
 *
 * Additionally, extra care is taken to utilize the most optimal code paths when
 * initiating and using the external process, for example, the
 * {@link ProcessBuilder#command(List)} method is used to avoid the copying of
 * array elements when {@link ProcessBuilder#command(String...)} is used and
 * avoiding the (hidden) use of {@link StringTokenizer} when
 * {@link Runtime#exec(String)} is called.
 *
 * All of this effort was done to ensure that imgscalr and its supporting
 * classes continue to provide best-of-breed performance and memory utilization
 * in long running/high performance environments (e.g. web applications).
 *
 * <h3>Thread Safety</h3>
 *
 * Instances of this class <strong>are Thread-safe</strong> (note that version 1.1 of exiftool
 * was not Thread-safe):
 *
 * <ul>
 *   <li>If {@code stay_open} is disabled, then a one-shot process is used for each command.</li>
 *   <li>
 *     Otherwise a single process is open and read/write operations are streamed to this process.
 *     In this case, each operation will be synchronized to ensure thread-safety.
 *   </li>
 * </ul>
 *
 * If you want to use ExifTool in a multi-threaded environment, I strongly suggest you to
 * use a pool size: this is available out of the box. With this configuration, you will get at most
 * a number of open process equal to the size of the pool. If a thread is trying to parse an image and no process
 * is available, then ExifTool will wait for a process to be available.
 *
 * Here is the configuration to get a pool:
 *
 * <pre><code>
 *     ExifTool exifTool = new ExifToolBuilder()
 *         .withPoolSize(10) // Allow 10 exiftool process in parallel
 *         .build();
 * </code></pre>
 *
 * <h3>Why ExifTool?</h3>
 *
 * <a href="http://www.sno.phy.queensu.ca/~phil/exiftool">ExifTool</a> is
 * written in Perl and requires an external process call from Java to make use
 * of.
 *
 * While this would normally preclude a piece of software from inclusion into
 * the imgscalr library (more complex integration), there is no other image
 * metadata piece of software available as robust, complete and well-tested as
 * ExifTool. In addition, ExifTool already runs on all major platforms
 * (including Windows), so there was not a lack of portability introduced by
 * providing an integration for it.
 *
 * Allowing it to be used from Java is a boon to any Java project that needs the
 * ability to read/write image-metadata from almost
 * <a href="http://www.sno.phy.queensu.ca/~phil/exiftool/#supported">any image or video file</a> format.
 *
 * <h3>Alternatives</h3>
 *
 * If integration with an external Perl process is something your app cannot do
 * and you still need image metadata-extraction capability, Drew Noakes has
 * written the 2nd most robust image metadata library I have come
 * across: <a href="http://drewnoakes.com/drewnoakes.com/code/exif/">Metadata Extractor</a>
 * that you might want to look at.
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @author Mickael Jeanroy (mickael.jeanroy@gmail.com)
 * @since 1.1
 */
public class ExifTool implements AutoCloseable {

	/**
	 * Internal Logger.
	 * Will used slf4j, log4j or internal implementation.
	 */
	private static final Logger log = LoggerFactory.getLogger(ExifTool.class);

	/**
	 * Cache used to store {@code exiftool} version:
	 *
	 * <ul>
	 *   <li>Key is the path to the {@code exiftool} executable</li>
	 *   <li>Value is the associated version.</li>
	 * </ul>
	 */
	private static final VersionCache cache = VersionCacheFactory.newCache();

	/**
	 * Command Executor.
	 * This withExecutor will be used to execute exiftool process and commands.
	 */
	private final CommandExecutor executor;

	/**
	 * Exiftool Path.
	 * Path is first read from `exiftool.withPath` system property,
	 * otherwise `exiftool` must be globally available.
	 */
	private final String path;

	/**
	 * This is the version detected on exiftool executable.
	 * This version depends on executable given on instantiation.
	 */
	private final Version version;

	/**
	 * ExifTool execution strategy.
	 * This strategy implement how exiftool is effectively used (as one-shot
	 * process or with `stay_open` flag).
	 */
	private final ExecutionStrategy strategy;

	/**
	 * Create new ExifTool instance.
	 * When exiftool is created, it will try to activate some features.
	 * If feature is not available on this specific exiftool version, then
	 * an it an {@link UnsupportedFeatureException} will be thrown.
	 *
	 * @param path ExifTool withPath.
	 * @param executor Executor used to handle command line.
	 * @param strategy Execution strategy.
	 */
	ExifTool(String path, CommandExecutor executor, ExecutionStrategy strategy) {
		this.executor = notNull(executor, "Executor should not be null");
		this.path = notBlank(path, "ExifTool path should not be null");
		this.strategy = notNull(strategy, "Execution strategy should not be null");
		this.version = cache.load(path, executor);

		// Check if this instance may be used safely.
		if (!strategy.isSupported(version)) {
			throw new UnsupportedFeatureException(path, version);
		}
	}

	/**
	 * This method should be used to clean previous execution.
	 *
	 * <br>
	 *
	 * <strong>NOTE: Calling this method prevent this instance of {@link ExifTool} from being re-used.</strong>
	 *
	 * @throws Exception If an error occurred while closing exiftool client.
	 */
	@Override
	public void close() throws Exception {
		strategy.shutdown();
	}

	/**
	 * Stop `ExifTool` client.
	 *
	 * <strong>NOTE</strong>: Calling this method does not preclude this
	 * instance of {@link ExifTool} from being re-used, it merely disposes of
	 * the native and internal resources until the next call to
	 * {@code getImageMeta} causes them to be re-instantiated.
	 *
	 * @throws Exception If an error occurred while stopping exiftool client.
	 */
	public void stop() throws Exception {
		strategy.close();
	}

	/**
	 * This method is used to determine if there is currently a running
	 * ExifTool process associated with this class.
	 *
	 * <br>
	 *
	 * Any dependent processes and streams can be shutdown using
	 * {@link #close()} and this class will automatically re-create them on the
	 * next call to {@link #getImageMeta} if necessary.
	 *
	 * @return {@code true} if there is an external ExifTool process is still
	 * running otherwise returns {@code false}.
	 */
	public boolean isRunning() {
		return strategy.isRunning();
	}

	/**
	 * Exiftool version pointed by this instance.
	 *
	 * @return Version.
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * Parse image metadata.
	 * Output format is numeric.
	 *
	 * @param image Image.
	 * @param tags List of tags to extract.
	 * @return Pair of tag associated with the value.
	 * @throws IOException If something bad happen during I/O operations.
	 * @throws NullPointerException If one parameter is null.
	 * @throws IllegalArgumentException If list of tag is empty.
	 * @throws com.thebuzzmedia.exiftool.exceptions.UnreadableFileException If image cannot be read.
	 */
	public Map<Tag, String> getImageMeta(File image, Collection<Tag> tags) throws IOException {
		return getImageMeta(image, StandardFormat.NUMERIC, tags);
	}

	/**
	 * Parse image metadata.
	 *
	 * @param image Image.
	 * @param format Output format.
	 * @param tags List of tags to extract.
	 * @return Pair of tag associated with the value.
	 * @throws IOException If something bad happen during I/O operations.
	 * @throws NullPointerException If one parameter is null.
	 * @throws IllegalArgumentException If list of tag is empty.
	 * @throws com.thebuzzmedia.exiftool.exceptions.UnreadableFileException If image cannot be read.
	 */
	public Map<Tag, String> getImageMeta(File image, Format format, Collection<? extends Tag> tags) throws IOException {
		notNull(image, "Image cannot be null and must be a valid stream of image data.");
		notNull(format, "Format cannot be null.");
		notEmpty(tags, "Tags cannot be null and must contain 1 or more Tag to query the image for.");
		isReadable(image, "Unable to read the given image [%s], ensure that the image exists at the given withPath and that the executing Java process has permissions to read it.", image);

		log.debug("Querying {} tags from image: {}", tags.size(), image);

		// Create a result map big enough to hold results for each of the tags
		// and avoid collisions while inserting.
		TagHandler tagHandler = new TagHandler(tags);

		// Build list of exiftool arguments.
		List<String> args = getImageMetaArguments(format, image, tags);

		// Execute ExifTool command
		strategy.execute(executor, path, args, tagHandler);

		// Add some debugging log
		log.debug("Image Meta Processed [queried {}, found {} values]", tagHandler.size(), tagHandler.size());

		return tagHandler.getTags();
	}

	/**
	 * Write image metadata.
	 * Default format is numeric.
	 *
	 * @param image Image.
	 * @param tags Tags to write.
	 * @throws IOException If an error occurs during write operation.
	 */
	public void setImageMeta(File image, Map<? extends Tag, String> tags) throws IOException {
		setImageMeta(image, StandardFormat.NUMERIC, tags);
	}

	/**
	 * Write image metadata in a specific format.
	 *
	 * @param image Image.
	 * @param format Specified format.
	 * @param tags Tags to write.
	 * @throws IOException If an error occurs during write operation.
	 */
	public void setImageMeta(File image, Format format, Map<? extends Tag, String> tags) throws IOException {
		notNull(image, "Image cannot be null and must be a valid stream of image data.");
		notNull(format, "Format cannot be null.");
		notEmpty(tags, "Tags cannot be null and must contain 1 or more Tag to query the image for.");
		isWritable(image, "Unable to read the given image [%s], ensure that the image exists at the given withPath and that the executing Java process has permissions to read it.", image);

		log.debug("Writing {} tags to image: {}", tags.size(), image);

		long startTime = System.currentTimeMillis();

		// Get arguments
		List<String> args = setImageMetaArguments(format, image, tags);

		// Execute ExifTool command
		strategy.execute(executor, path, args, stopHandler());

		log.debug("Image Meta Processed in {} ms [write {} tags]", System.currentTimeMillis() - startTime, tags.size());
	}

	/**
	 * Build argument list to parse image metadata using exiftool command
	 * line.
	 *
	 * @param format Output format.
	 * @param image Image.
	 * @param tags List of tags.
	 * @return List of associated arguments.
	 */
	private List<String> getImageMetaArguments(Format format, File image, Collection<? extends Tag> tags) {
		// Create list of arguments: deduce expected number of arguments.
		List<String> formatArgs = format.getArgs();
		int nbArgs = tags.size() + formatArgs.size() + 3;
		List<String> args = new ArrayList<>(nbArgs);

		// Format output.
		args.addAll(formatArgs);

		// Compact output.
		args.add("-S");

		// Add tags arguments.
		for (Tag tag : tags) {
			args.add("-" + tag.getName());
		}

		// Add image argument.
		args.add(image.getAbsolutePath());

		// Add last argument.
		// This argument will only be used by exiftool if stay_open flag has been set.
		args.add("-execute");

		return args;
	}

	/**
	 * Build argument list to parse image metadata using exiftool command
	 * line.
	 *
	 * @param format Output format.
	 * @param image Image.
	 * @param tags List of tags.
	 * @return List of associated arguments.
	 */
	private List<String> setImageMetaArguments(Format format, File image, Map<? extends Tag, String> tags) {
		List<String> formatArgs = format.getArgs();
		int nbArgs = tags.size() + formatArgs.size() + 3;
		List<String> args = new ArrayList<>(nbArgs);

		// Format output.
		args.addAll(formatArgs);

		// Compact output.
		args.add("-S");

		// Add tags arguments.
		for (Map.Entry<? extends Tag, String> entry : tags.entrySet()) {
			args.add("-" + entry.getKey().getName() + "=" + entry.getValue());
		}

		// Add image argument.
		args.add(image.getAbsolutePath());

		// Add last argument.
		// This argument will only be used by exiftool if stay_open flag has been set.
		args.add("-execute");

		return args;
	}
}
