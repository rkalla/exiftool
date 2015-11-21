/**
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

import com.thebuzzmedia.exiftool.exceptions.ProcessException;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultCommandProcessTest {

	private static final String BR = System.getProperty("line.separator");

	@Rule
	public ExpectedException thrown = none();

	@Test
	public void it_should_fail_with_null_input() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Input stream should not be null");
		new DefaultCommandProcess(null, mock(OutputStream.class));
	}

	@Test
	public void it_should_fail_with_null_output() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Output stream should not be null");
		new DefaultCommandProcess(mock(InputStream.class), null);
	}

	@Test
	public void it_should_close_process() throws Exception {
		OutputStream os = mock(OutputStream.class);
		InputStream is = mock(InputStream.class);

		DefaultCommandProcess process = new DefaultCommandProcess(is, os);
		assertThat(process.isClosed()).isFalse();
		assertThat(process.isRunning()).isTrue();

		process.close();

		verify(is).close();
		verify(os).close();

		assertThat(process.isClosed()).isTrue();
		assertThat(process.isRunning()).isFalse();
	}

	@Test
	public void it_should_close_process_with_first_failure() throws Exception {
		OutputStream os = mock(OutputStream.class);
		InputStream is = mock(InputStream.class);

		IOException ex = new IOException("fail");
		doThrow(ex).when(os).close();

		DefaultCommandProcess process = new DefaultCommandProcess(is, os);
		assertThat(process.isClosed()).isFalse();
		assertThat(process.isRunning()).isTrue();

		try {
			process.close();
			failBecauseExceptionWasNotThrown(IOException.class);
		}
		catch (ProcessException e) {
			assertThat(e.getCause()).isSameAs(ex);

			verify(is).close();
			verify(os).close();

			assertThat(process.isClosed()).isTrue();
			assertThat(process.isRunning()).isFalse();
		}
	}

	@Test
	public void it_should_close_process_with_second_failure() throws Exception {
		OutputStream os = mock(OutputStream.class);
		InputStream is = mock(InputStream.class);

		IOException ex = new IOException("fail");
		doThrow(ex).when(is).close();

		DefaultCommandProcess process = new DefaultCommandProcess(is, os);
		assertThat(process.isClosed()).isFalse();
		assertThat(process.isRunning()).isTrue();

		try {
			process.close();
			failBecauseExceptionWasNotThrown(IOException.class);
		}
		catch (ProcessException e) {
			assertThat(e.getCause()).isSameAs(ex);

			verify(is).close();
			verify(os).close();

			assertThat(process.isClosed()).isTrue();
			assertThat(process.isRunning()).isFalse();
		}
	}

	@Test
	public void it_should_close_process_with_both_failure() throws Exception {
		OutputStream os = mock(OutputStream.class);
		InputStream is = mock(InputStream.class);

		IOException ex1 = new IOException("fail1");
		doThrow(ex1).when(os).close();

		IOException ex2 = new IOException("fail2");
		doThrow(ex2).when(is).close();

		DefaultCommandProcess process = new DefaultCommandProcess(is, os);
		assertThat(process.isClosed()).isFalse();
		assertThat(process.isRunning()).isTrue();

		try {
			process.close();
			failBecauseExceptionWasNotThrown(IOException.class);
		}
		catch (ProcessException e) {
			assertThat(e.getCause()).isSameAs(ex1);

			verify(is).close();
			verify(os).close();

			assertThat(process.isClosed()).isTrue();
			assertThat(process.isRunning()).isFalse();
		}
	}

	@Test
	public void it_should_read_from_input() {
		String firstLine = "first-line";
		String secondLine = "second-line";
		String output = firstLine + BR + secondLine;
		InputStream stream = new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8));

		DefaultCommandProcess process = new DefaultCommandProcess(stream, mock(OutputStream.class));
		String out = process.read();

		assertThat(out)
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(output);
	}

	@Test
	public void it_should_not_read_from_closed_process() throws Exception {
		thrown.expect(ProcessException.class);
		thrown.expectMessage("Cannot read from closed process");
		DefaultCommandProcess process = new DefaultCommandProcess(mock(InputStream.class), mock(OutputStream.class));
		process.close();
		process.read();
	}

	@Test
	public void it_should_read_from_input_and_use_handler() {
		String firstLine = "first-line";
		String secondLine = "{ready}";
		String thirdLine = "third-line";
		String output = firstLine + BR + secondLine + BR + thirdLine;
		InputStream stream = new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8));

		OutputHandler handler = mock(OutputHandler.class);
		when(handler.readLine(anyString())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				String line = (String) invocation.getArguments()[0];
				return !line.equals("{ready}");
			}
		});

		DefaultCommandProcess process = new DefaultCommandProcess(stream, mock(OutputStream.class));
		String out = process.read(handler);

		assertThat(out)
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(firstLine + BR + secondLine);

		verify(handler, times(2)).readLine(anyString());
		verify(handler).readLine(firstLine);
		verify(handler).readLine(secondLine);
		verify(handler, never()).readLine(thirdLine);
	}

	@Test
	public void it_should_write_from_output() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String message = "foo";

		DefaultCommandProcess process = new DefaultCommandProcess(mock(InputStream.class), os);
		process.write(message);

		assertThat(os.toString())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(message);
	}

	@Test
	public void it_should_catch_write_failure() throws Exception {
		thrown.expect(ProcessException.class);

		OutputStream os = mock(OutputStream.class);

		IOException ex = new IOException("fail");
		doThrow(ex).when(os).write(any(byte[].class));

		DefaultCommandProcess process = new DefaultCommandProcess(mock(InputStream.class), os);
		process.write("foo");
	}

	@Test
	public void it_should_not_write_null_output() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Write input should not be null");
		DefaultCommandProcess process = new DefaultCommandProcess(mock(InputStream.class), mock(OutputStream.class));
		process.write((String) null);
	}

	@Test
	public void it_should_not_write_null_outputs() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Write inputs should not be empty");
		DefaultCommandProcess process = new DefaultCommandProcess(mock(InputStream.class), mock(OutputStream.class));
		process.write((Iterable<String>) null);
	}

	@Test
	public void it_should_not_write_empty_outputs() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Write inputs should not be empty");
		List<String> inputs = emptyList();
		DefaultCommandProcess process = new DefaultCommandProcess(mock(InputStream.class), mock(OutputStream.class));
		process.write(inputs);
	}

	@Test
	public void it_should_not_write_from_closed_process() throws Exception {
		thrown.expect(ProcessException.class);
		thrown.expectMessage("Cannot write from closed process");
		DefaultCommandProcess process = new DefaultCommandProcess(mock(InputStream.class), mock(OutputStream.class));
		process.close();
		process.write("foo");
	}

	@Test
	public void it_should_write_inputs() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String msg1 = "foo";
		String msg2 = "bar";

		DefaultCommandProcess process = new DefaultCommandProcess(mock(InputStream.class), os);
		process.write(asList(msg1, msg2));

		assertThat(os.toString())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(msg1 + msg2);
	}
}
