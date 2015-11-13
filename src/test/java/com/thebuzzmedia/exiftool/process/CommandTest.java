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

package com.thebuzzmedia.exiftool.process;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

public class CommandTest {

	@Rule
	public ExpectedException thrown = none();

	@Test
	public void it_should_not_create_command_if_executable_is_null() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Exiftool path should be defined");
		new Command(null);
	}

	@Test
	public void it_should_not_create_command_if_executable_is_empty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Exiftool path should be defined");
		new Command("");
	}

	@Test
	public void it_should_not_create_command_if_executable_is_blank() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Exiftool path should be defined");
		new Command("  ");
	}

	@Test
	public void it_should_create_command() {
		Command command = new Command("exiftool");
		assertThat(command.toString())
			.isEqualTo("exiftool");
	}

	@Test
	public void it_should_create_command_and_add_argument() {
		Command command = new Command("exiftool");
		command.addArgument("-ver");
		command.addArgument("-stay-open");

		assertThat(command.toString())
			.isEqualTo("exiftool -ver -stay-open");
	}

	@Test
	public void it_should_not_add_null_argument() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Exiftool argument must be defined if set");
		Command command = new Command("exiftool");
		command.addArgument(null);
	}

	@Test
	public void it_should_not_add_empty_argument() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Exiftool argument must be defined if set");
		Command command = new Command("exiftool");
		command.addArgument("");
	}

	@Test
	public void it_should_not_add_blank_argument() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Exiftool argument must be defined if set");
		Command command = new Command("exiftool");
		command.addArgument("  ");
	}
}
