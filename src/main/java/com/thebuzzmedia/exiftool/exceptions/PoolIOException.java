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

package com.thebuzzmedia.exiftool.exceptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

public class PoolIOException extends IOException {

	private final Collection<Exception> thrownExceptions;

	public PoolIOException(String message, Collection<Exception> thrownExceptions) {
		super(message);
		this.thrownExceptions = new ArrayList<>(thrownExceptions);
	}

	public Collection<Exception> getThrownExceptions() {
		return unmodifiableCollection(thrownExceptions);
	}
}
