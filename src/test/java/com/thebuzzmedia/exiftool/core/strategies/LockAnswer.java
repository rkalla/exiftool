/**
 * Copyright 2011 The Buzz Media, LLC
 * Copyright 2016 Mickael Jeanroy <mickael.jeanroy@gmail.com>
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

package com.thebuzzmedia.exiftool.core.strategies;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Implementation of Mockito Answer.
 * The answer method will block until internal lock is released.
 */
class LockAnswer implements Answer<Void> {
	private static final Logger log = LoggerFactory.getLogger(LockAnswer.class);

	/**
	 * Id of answer, used for debug logging.
	 */
	private final int id;

	/**
	 * Internal lock.
	 */
	private final CountDownLatch lock;

	/**
	 * Create answer.
	 *
	 * @param id Answer id.
	 * @param lock Internal lock.
	 */
	LockAnswer(int id, CountDownLatch lock) {
		this.lock = lock;
		this.id = id;
	}

	@Override
	public Void answer(InvocationOnMock invocation) throws Throwable {
		log.debug("Waiting for lock on answer #{}", id);
		lock.await();
		return null;
	}
}
