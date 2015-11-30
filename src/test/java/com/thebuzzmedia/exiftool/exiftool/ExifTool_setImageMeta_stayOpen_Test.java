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
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static com.thebuzzmedia.exiftool.tests.TestConstants.BR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExifTool_setImageMeta_stayOpen_Test extends AbstractExifTool_setImageMeta_Test {

	@Override
	protected ExifTool createExifTool() throws Exception {
		return new ExifTool(Feature.STAY_OPEN);
	}

	@Override
	protected void mockExecutor(CommandExecutor executor) throws Exception {
		// Mock Executor
		CommandProcess process = mock(CommandProcess.class);
		when(process.read(any(OutputHandler.class))).thenReturn(null);
		when(executor.start(any(Command.class))).thenReturn(process);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void verifyExecution(ExifTool exifTool, CommandExecutor executor, File image, Format format, Map<Tag, String> tags) throws Exception {
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

		CommandProcess process = readPrivateField(exifTool, "process", CommandProcess.class);

		ArgumentCaptor<List> argsCaptor = ArgumentCaptor.forClass(List.class);

		InOrder inOrder = inOrder(process);
		inOrder.verify(process).write(argsCaptor.capture());
		inOrder.verify(process).flush();
		inOrder.verify(process).read(any(OutputHandler.class));

		List<String> expectedArgs = createArgumentsList(image, format, tags);
		List<String> args = argsCaptor.getValue();
		assertThat(args)
			.isNotNull()
			.isNotEmpty()
			.hasSameSizeAs(expectedArgs)
			.isEqualTo(expectedArgs);

		verify(process, never()).close();
	}

	private List<String> createArgumentsList(File file, Format format, Map<Tag, String> tags) {
		List<String> args = new LinkedList<String>();

		if (format == Format.NUMERIC) {
			args.add("-n" + BR);
		}

		args.add("-S" + BR);

		for (Map.Entry<Tag, String> entry : tags.entrySet()) {
			args.add("-" + entry.getKey().getName() + "=" + entry.getValue() + BR);
		}

		args.add(file.getAbsolutePath() + BR);
		args.add("-execute" + BR);

		return args;
	}
}
