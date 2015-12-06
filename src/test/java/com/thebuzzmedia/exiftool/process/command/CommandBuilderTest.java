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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

public class CommandBuilderTest {

	@Rule
	public ExpectedException thrown = none();

	@Test
	public void it_should_build_command() {
		Command cmd = CommandBuilder.builder("exiftool").build();
		assertThat(cmd.toString()).isEqualTo("exiftool");
	}

	@Test
	public void it_should_build_command_with_arguments() {
		Command cmd = CommandBuilder.builder("exiftool")
			.addArgument("-ver")
			.build();

		assertThat(cmd.toString()).isEqualTo("exiftool -ver");
	}

	@Test
	public void it_should_build_command_with_several_arguments() {
		Command cmd = CommandBuilder.builder("exiftool")
			.addArgument("-ver", "-stay_open")
			.build();

		assertThat(cmd.toString()).isEqualTo("exiftool -ver -stay_open");
	}

	@Test
	public void it_should_not_build_command_if_executable_is_null() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Command line executable should be defined");
		CommandBuilder.builder(null);
	}

	@Test
	public void it_should_not_build_command_if_executable_is_empty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Command line executable should be defined");
		CommandBuilder.builder("");
	}

	@Test
	public void it_should_not_build_command_if_executable_is_blank() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Command line executable should be defined");
		CommandBuilder.builder(" ");
	}

	@Test
	public void it_should_not_build_command_if_argument_is_null() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Command line argument should be defined if set");
		CommandBuilder.builder("exiftool").addArgument(null);
	}

	@Test
	public void it_should_not_build_command_if_argument_is_empty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Command line argument should be defined if set");
		CommandBuilder.builder("exiftool").addArgument("");
	}

	@Test
	public void it_should_not_build_command_if_argument_is_blank() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Command line argument should be defined if set");
		CommandBuilder.builder("exiftool").addArgument(" ");
	}

	@Test
	public void it_should_not_build_command_if_one_of_argument_is_null() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Command line argument should be defined if set");
		CommandBuilder.builder("exiftool").addArgument("-ver", new String[] { null });
	}

	@Test
	public void it_should_not_build_command_if_one_of_argument_is_empty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Command line argument should be defined if set");
		CommandBuilder.builder("exiftool").addArgument("-ver", "");
	}

	@Test
	public void it_should_not_build_command_if_one_of_argument_is_blank() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Command line argument should be defined if set");
		CommandBuilder.builder("exiftool").addArgument("-ver", " ");
	}
}
