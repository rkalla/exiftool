package com.thebuzzmedia.exiftool.process;

import java.io.IOException;

public interface CommandProcess extends AutoCloseable {

	/**
	 * Read output until a null line is read.
	 *
	 * Since command process will not be closed, a simple string
	 * is returned (an exit status cannot be computed).
	 *
	 * @return Command result.
	 */
	String read();

	/**
	 * Read output until:
	 * - A null line is read.
	 * - Handler returns false when line is read.
	 *
	 * Since command process will not be closed, a simple string
	 * is returned (an exit status cannot be computed).
	 *
	 * @param handler Output handler.
	 * @return Full output.
	 */
	String read(OutputHandler handler);

	/**
	 * Write input string to the current process.
	 * If write operation failed (or is not possible), an instance
	 * of {@link com.thebuzzmedia.exiftool.exceptions.ProcessException} should be thrown.
	 *
	 * @param input Input.
	 * @param others Other inputs.
	 */
	void write(String input, String... others);

	/**
	 * Write set of inputs to the current process.
	 * If write operation failed (or is not possible), an instance
	 * of {@link com.thebuzzmedia.exiftool.exceptions.ProcessException} should be thrown.
	 *
	 * @param inputs Collection of inputs.
	 */
	void write(Iterable<String> inputs);

	/**
	 * Flush pending write operations.
	 */
	void flush() throws IOException;

	/**
	 * Check if current process is still opened.
	 * If this method returns {@code true}, then {@link #isClosed()} should return {@code false}.
	 *
	 * @return True if process is open, false otherwise.
	 */
	boolean isRunning();

	/**
	 * Check if current process has been closed.
	 * If this method returns {@code true}, then {@link #isRunning()} should return {@code false}.
	 *
	 * @return True if process is closed, false otherwise.
	 */
	boolean isClosed();
}
