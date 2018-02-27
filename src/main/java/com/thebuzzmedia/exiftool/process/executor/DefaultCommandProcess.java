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

package com.thebuzzmedia.exiftool.process.executor;

import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.OutputHandler;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.thebuzzmedia.exiftool.commons.io.IOs.readInputStream;
import static com.thebuzzmedia.exiftool.commons.lang.Objects.firstNonNull;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notEmpty;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notNull;

/**
 * Default implementation for {@link CommandProcess} interface.
 *
 * <br>
 *
 * This implementation used instance of {@link InputStream} to handle
 * read operation and instance of {@link OutputStream} to handle write
 * operation. These streams may come from instance of {@link Process} for instance.
 *
 * <br>
 *
 * <strong>Note:</strong> This implementation is not thread safe.
 */
public class DefaultCommandProcess implements CommandProcess {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(DefaultCommandProcess.class);

	/**
	 * Instance of {@link InputStream}.
	 * This stream will be used to handle read operation.
	 */
	private final InputStream is;

	/**
	 * Output stream.
	 * This stream will be used to handle write operation.
	 */
	private final OutputStream os;

	/**
	 * Error Stream.
	 */
	private final InputStream err;

	/**
	 * Flag to know if a given process has been closed.
	 */
	private boolean close;

	/**
	 * Create process.
	 * @param is Input stream.
	 * @param os Output stream.
	 * @param err Error stream.
	 */
	public DefaultCommandProcess(InputStream is, OutputStream os, InputStream err) {
		this.is = notNull(is, "Input stream should not be null");
		this.os = notNull(os, "Output stream should not be null");
		this.err = notNull(err, "Error stream should not be null");
		this.close = false;
	}

	@Override
	public String read() throws IOException {
		return doRead(null);
	}

	@Override
	public String read(OutputHandler handler) throws IOException {
		return doRead(notNull(handler, "Handler should not be null"));
	}

	@Override
	public void write(String input, String... others) throws IOException {
		doWrite(input);

		// Write other inputs.
		for (String o : others) {
			doWrite(o);
		}
	}

	@Override
	public void write(Iterable<String> inputs) throws IOException {
		notEmpty(inputs, "Write inputs should not be empty");
		for (String input : inputs) {
			doWrite(input);
		}
	}

	@Override
	public void flush() throws IOException {
		os.flush();
	}

	@Override
	public boolean isRunning() {
		return !isClosed();
	}

	@Override
	public boolean isClosed() {
		return close;
	}

	@Override
	public void close() throws Exception {
		IOException ex1 = close(os);
		IOException ex2 = close(is);
		IOException ex3 = close(err);

		close = true;

		// Throw exception if something bad happened
		if (ex1 != null || ex2 != null || ex3 != null) {
			throw firstNonNull(ex1, ex2, ex3);
		}
	}

	private IOException close(Closeable closeable) {
		try {
			closeable.close();
			return null;
		}
		catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			return ex;
		}
	}

	private String doRead(OutputHandler h) throws IOException {
		if (isClosed()) {
			throw new IllegalStateException("Cannot read from closed process");
		}

		log.debug("Read command output");

		// Create result handler, and wrap it in a composite
		// handler if one is specified in parameter.
		final ResultHandler out = new ResultHandler();
		final OutputHandler handler = h == null ? out : new CompositeHandler(out, h);

		// Read output stream until the end
		readInputStream(is, handler);

		// We can return the output
		return out.getOutput();
	}

	private void doWrite(String input) throws IOException {
		if (isClosed()) {
			throw new IllegalStateException("Cannot write from closed process");
		}

		// Check valid input.
		notNull(input, "Write input should not be null");

		// Just log some debug information
		log.debug("Send command input: {}", input);

		try {
			os.write(input.getBytes());
		}
		catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}
	}
}
