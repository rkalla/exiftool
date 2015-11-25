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
import com.thebuzzmedia.exiftool.Format;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.tests.mocks.ReadCommandResultAnswer;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.mockito.ArgumentCaptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExifTool_getImageMeta_Test extends AbstractExifTool_getImageMeta_Test {

	@Override
	protected ExifTool createExifTool() {
		return new ExifTool();
	}

	@Override
	protected void mockExecutor() throws Exception {
		// Mock Executor
		String firstLine = format("%s: %s", Tag.ARTIST.getName(), "foobar");
		String secondLine = format("%s: %s", Tag.COMMENT.getName(), "foo");
		ReadCommandResultAnswer readAnswer = new ReadCommandResultAnswer(firstLine, secondLine);
		when(executor.execute(any(Command.class), any(OutputHandler.class))).thenAnswer(readAnswer);
	}

	@Override
	protected void verifyExecution(Format format, Map<Tag, String> results) throws Exception {
		ArgumentCaptor<Command> cmdCaptor = ArgumentCaptor.forClass(Command.class);
		verify(executor).execute(cmdCaptor.capture(), any(OutputHandler.class));

		Command cmd = cmdCaptor.getValue();
		assertThat(cmd.getArguments())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(buildArgumentsList(format));

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.hasSize(2)
			.contains(
				entry(Tag.ARTIST, "foobar"),
				entry(Tag.COMMENT, "foo")
			);
	}

	private List<String> buildArgumentsList(Format format) {
		List<String> args = new LinkedList<String>();
		args.add("exiftool");

		if (format == Format.NUMERIC) {
			args.add("-n");
		}

		args.add("-S");
		args.add("-" + Tag.ARTIST.getName());
		args.add("-" + Tag.COMMENT.getName());
		args.add("/tmp/foo.png");
		args.add("-execute");

		return args;
	}
}
