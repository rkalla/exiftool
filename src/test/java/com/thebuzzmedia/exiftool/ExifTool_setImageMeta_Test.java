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

import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class ExifTool_setImageMeta_Test extends AbstractExifTool_setImageMeta_Test {

	@Override
	protected ExifTool createExifTool(CommandExecutor executor) throws Exception {
		return new ExifTool("exiftool", executor, Collections.<Feature>emptySet());
	}

	@Override
	protected void mockExecutor(CommandExecutor executor) throws Exception {
	}

	@Override
	protected void verifyExecution(ExifTool exifTool, CommandExecutor executor, File image, Format format, Map<Tag, String> tags) throws Exception {
		ArgumentCaptor<Command> cmdCaptor = ArgumentCaptor.forClass(Command.class);
		verify(executor).execute(cmdCaptor.capture(), any(OutputHandler.class));

		List<String> expectedArgs = createArgumentsList(image, format, tags);
		Command cmd = cmdCaptor.getValue();
		assertThat(cmd.getArguments())
			.isNotNull()
			.isNotEmpty()
			.hasSameSizeAs(expectedArgs)
			.isEqualTo(expectedArgs);
	}

	private List<String> createArgumentsList(File file, Format format, Map<Tag, String> tags) {
		List<String> args = new LinkedList<String>();
		args.add("exiftool");

		if (format == Format.NUMERIC) {
			args.add("-n");
		}

		args.add("-S");

		for (Map.Entry<Tag, String> entry : tags.entrySet()) {
			args.add("-" + entry.getKey().getName() + "=" + entry.getValue());
		}

		args.add(file.getAbsolutePath());
		args.add("-execute");

		return args;
	}
}
