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

package com.thebuzzmedia.exiftool.exiftool;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.Feature;
import com.thebuzzmedia.exiftool.Format;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import com.thebuzzmedia.exiftool.tests.mocks.ReadStringResultAnswer;
import org.mockito.ArgumentCaptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExifTool_getImageMeta_stayOpen_Test extends AbstractExifTool_getImageMeta_Test {

	@Override
	protected ExifTool createExifTool() {
		return new ExifTool(Feature.STAY_OPEN);
	}

	@Override
	protected void mockExecutor() throws Exception  {
		// Mock Executor
		String firstLine = format("%s: %s", Tag.ARTIST.getName(), "foobar");
		String secondLine = format("%s: %s", Tag.COMMENT.getName(), "foo");
		ReadStringResultAnswer readAnswer = new ReadStringResultAnswer(firstLine, secondLine);

		CommandProcess process = mock(CommandProcess.class);
		when(process.read(any(OutputHandler.class))).thenAnswer(readAnswer);
		when(executor.start(any(Command.class))).thenReturn(process);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void verifyExecution(Format format, Map<Tag, String> results) throws Exception {
		ArgumentCaptor<Command> cmdCaptor = ArgumentCaptor.forClass(Command.class);
		verify(executor).start(cmdCaptor.capture());

		Command cmd = cmdCaptor.getValue();
		assertThat(cmd.getArguments())
			.isNotNull()
			.isNotEmpty()
			.containsExactly(
				"exiftool",
				"-stay_open",
				"True",
				"-@",
				"-"
			);

		ArgumentCaptor<List> argsCaptor = ArgumentCaptor.forClass(List.class);
		CommandProcess commandProcess = readPrivateField(exifTool, "process", CommandProcess.class);
		verify(commandProcess).write(argsCaptor.capture());

		List<String> args = argsCaptor.getValue();
		assertThat(args)
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(buildArgumentsList(format));

		verify(commandProcess).flush();
		verify(commandProcess, never()).close();
	}

	private List<String> buildArgumentsList(Format format) {
		List<String> args = new LinkedList<String>();

		if (format == Format.NUMERIC) {
			args.add("-n\n");
		}

		args.add("-S\n");
		args.add("-" + Tag.ARTIST.getName() + "\n");
		args.add("-" + Tag.COMMENT.getName() + "\n");
		args.add("/tmp/foo.png\n");
		args.add("-execute\n");
		return args;
	}
}
