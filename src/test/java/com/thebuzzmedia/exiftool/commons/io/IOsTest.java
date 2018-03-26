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

package com.thebuzzmedia.exiftool.commons.io;

import com.thebuzzmedia.exiftool.commons.io.IOs;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import static com.thebuzzmedia.exiftool.tests.TestConstants.BR;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IOsTest {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private String charset;

	@Before
	public void setUp() {
		charset = System.getProperty("file.encoding");
	}

	@After
	public void tearDown() throws Exception {
		System.setProperty("file.encoding", charset);
		resetDefaultCharset();
	}

	@Test
	public void it_should_read_input_stream() throws Exception {
		String firstLine = "first-line";
		String secondLine = "second-line";
		String output = firstLine + BR + secondLine;
		InputStream is = spy(new ByteArrayInputStream(output.getBytes(UTF_8)));

		OutputHandler handler = mock(OutputHandler.class);
		when(handler.readLine(anyString())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				String str = (String) invocation.getArguments()[0];
				return str != null;
			}
		});

		IOs.readInputStream(is, handler);

		verify(handler).readLine(firstLine);
		verify(handler).readLine(secondLine);
		verify(is).close();
	}

	@Test
	public void it_should_read_input_stream_and_stop_without_closing_stream() throws Exception {
		String firstLine = "first-line";
		String secondLine = "second-line";
		String output = firstLine + BR + secondLine;
		InputStream is = spy(new ByteArrayInputStream(output.getBytes(UTF_8)));

		OutputHandler handler = mock(OutputHandler.class);
		when(handler.readLine(anyString())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return false;
			}
		});

		IOs.readInputStream(is, handler);

		verify(handler, times(1)).readLine(anyString());
		verify(handler).readLine(firstLine);
		verify(is, never()).close();
	}

	@Test
	public void it_should_close_closeable_instance() throws Exception {
		Closeable closeable = mock(Closeable.class);
		IOs.closeQuietly(closeable);
		verify(closeable).close();
	}

	@Test
	public void it_should_close_closeable_instance_and_do_not_fail() throws Exception {
		Closeable closeable = mock(Closeable.class);
		doThrow(IOException.class).when(closeable).close();

		IOs.closeQuietly(closeable);

		verify(closeable).close();
	}

	@Test
	public void it_should_read_file_with_utf8_charset() throws Exception {
		String output = "line-with-accent: àéê";
		byte[] bytes = output.getBytes(UTF_8);
		InputStream is = new ByteArrayInputStream(bytes);

		OutputHandler handler = mock(OutputHandler.class);
		when(handler.readLine(anyString())).thenReturn(false);

		System.setProperty("file.encoding", "CP1252");
		resetDefaultCharset();

		IOs.readInputStream(is, handler);

		verify(handler).readLine("line-with-accent: àéê");
	}

	private void resetDefaultCharset() throws Exception {
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null, null);
	}
}
