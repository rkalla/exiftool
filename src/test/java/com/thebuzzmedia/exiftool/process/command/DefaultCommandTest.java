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

package com.thebuzzmedia.exiftool.process.command;

import com.thebuzzmedia.exiftool.process.Command;
import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultCommandTest {

	@Test
	public void it_should_create_command() {
		Command command = new DefaultCommand("exiftool", Collections.<String>emptyList());
		assertThat(command.toString()).isEqualTo("exiftool");
		assertThat(command.getArguments()).isEqualTo(asList("exiftool"));
	}

	@Test
	public void it_should_create_command_and_add_argument() {
		Command command = new DefaultCommand("exiftool", asList("-ver", "-stay_open"));
		assertThat(command.toString()).isEqualTo("exiftool -ver -stay_open");
		assertThat(command.getArguments()).isEqualTo(asList("exiftool", "-ver", "-stay_open"));
	}
}
