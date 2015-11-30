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

package com.thebuzzmedia.exiftool.tests.builders;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Create a mock instance of {@link java.io.File}.
 */
public class FileBuilder {

	/**
	 * File name (this is the name, not the full path).
	 * Must be initialized on construction.
	 */
	private final String name;

	/**
	 * Full path of file.
	 * Default is `/tmp`.
	 */
	private String path;

	/**
	 * Flag to know if file is readable.
	 * Default is `true`.
	 */
	private boolean canRead;

	/**
	 * Flag to know if file is writable.
	 * Default is `true`.
	 */
	private boolean canWrite;

	/**
	 * Flag to know if file exists.
	 * Default is `true`.
	 */
	private boolean exists;

	public FileBuilder(String name) {
		this.name = name;
		this.canRead = true;
		this.canWrite = true;
		this.exists = true;
		this.path = "/tmp";
	}

	/**
	 * Update `write` flag.
	 *
	 * @param canWrite Value of `write` flag.
	 * @return Current builder.
	 */
	public FileBuilder canWrite(boolean canWrite) {
		this.canWrite = canWrite;
		return this;
	}

	/**
	 * Update `read` flag.
	 *
	 * @param canRead Value of `read` flag.
	 * @return Current builder.
	 */
	public FileBuilder canRead(boolean canRead) {
		this.canRead = canRead;
		return this;
	}

	/**
	 * Update file `path`.
	 *
	 * @param path New path.
	 * @return Current builder.
	 */
	public FileBuilder path(String path) {
		this.path = path;
		return this;
	}

	/**
	 * Update `exists` flag.
	 *
	 * @param exists New value of `exists` flag.
	 * @return Current builder.
	 */
	public FileBuilder exists(boolean exists) {
		this.exists = exists;
		return this;
	}

	/**
	 * Create file instance.
	 * **NOTE:** A mock (created with mockito) is returned, not a real file.
	 *
	 * @return Mock of file.
	 */
	public File build() {
		File file = mock(File.class);
		when(file.getName()).thenReturn(name);
		when(file.getAbsolutePath()).thenReturn(path + "/" + name);
		when(file.getPath()).thenReturn(path + "/" + name);
		when(file.canRead()).thenReturn(canRead);
		when(file.canWrite()).thenReturn(canWrite);
		when(file.exists()).thenReturn(exists);
		when(file.toString()).thenCallRealMethod();
		return file;
	}
}
