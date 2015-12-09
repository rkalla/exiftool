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

package com.thebuzzmedia.exiftool.process.executor;

import com.thebuzzmedia.exiftool.process.OutputHandler;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableList;

/**
 * Composite handler.
 *
 * <p />
 *
 * Run each internal handler and return false if one of them returns
 * false during line processing.
 *
 * This class should only be used internally (to compose handlers during
 * command execution).
 *
 * <p />
 *
 * <strong>Note:</strong> this class is thread safe if (and only if)
 * internal handlers are thread safe.
 */
class CompositeHandler implements OutputHandler {

	/**
	 * List of internal handlers.
	 * Each one will be processed in order.
	 * Once created, this list is unmodifiable.
	 */
	private final List<OutputHandler> handlers;

	/**
	 * Create handler.
	 *
	 * @param handlers List of handlers.
	 */
	CompositeHandler(OutputHandler... handlers) {
		List<OutputHandler> list = new ArrayList<OutputHandler>(handlers.length);
		if (handlers.length > 0) {
			addAll(list, handlers);
		}

		// Create immutable list.
		this.handlers = unmodifiableList(list);
	}

	@Override
	public boolean readLine(String line) {
		boolean hasNext = true;
		for (OutputHandler handler : handlers) {
			if (!handler.readLine(line)) {
				hasNext = false;
			}
		}

		return hasNext;
	}
}
