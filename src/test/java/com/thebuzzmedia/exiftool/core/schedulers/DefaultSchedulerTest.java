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

package com.thebuzzmedia.exiftool.core.schedulers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.writePrivateField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSchedulerTest {

	@Rule
	public ExpectedException thrown = none();

	@Mock
	private ScheduledThreadPoolExecutor executor;

	@Test
	public void it_should_not_create_default_scheduler_with_negative_delay() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Delay should be a strictly positive value");
		new DefaultScheduler(-1);
	}

	@Test
	public void it_should_not_create_default_scheduler_with_zero_delay() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Delay should be a strictly positive value");
		new DefaultScheduler(0);
	}

	@Test
	public void it_should_not_create_default_scheduler_with_null_time_unit() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Time Unit should not be null");
		new DefaultScheduler(1, null);
	}

	@Test
	public void it_should_create_default_scheduler() throws Exception {
		long delay = 10000;
		DefaultScheduler scheduler = new DefaultScheduler(delay);

		assertThat(readPrivateField(scheduler, "delay", Long.class)).isEqualTo(delay);
		assertThat(readPrivateField(scheduler, "timeUnit", TimeUnit.class)).isEqualTo(TimeUnit.MILLISECONDS);
		assertThat(readPrivateField(scheduler, "executor", ScheduledThreadPoolExecutor.class)).isNotNull();
	}

	@Test
	public void it_should_create_default_scheduler_with_specific_time_unit() throws Exception {
		long delay = 1;
		TimeUnit timeUnit = TimeUnit.HOURS;

		DefaultScheduler scheduler = new DefaultScheduler(delay, timeUnit);

		assertThat(readPrivateField(scheduler, "delay", Long.class)).isEqualTo(delay);
		assertThat(readPrivateField(scheduler, "timeUnit", TimeUnit.class)).isEqualTo(timeUnit);
		assertThat(readPrivateField(scheduler, "executor", ScheduledThreadPoolExecutor.class)).isNotNull();
	}

	@Test
	public void it_should_start_scheduler() throws Exception {
		int delay = 10000;
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		DefaultScheduler scheduler = new DefaultScheduler(delay, timeUnit);
		writePrivateField(scheduler, "executor", executor);

		RunnableFuture<?> runnable = mock(RunnableFuture.class);
		scheduler.start(runnable);

		verify(executor).schedule((Runnable) runnable, delay, timeUnit);
	}

	@Test
	public void it_should_stop_scheduler() throws Exception {
		int delay = 10000;
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		DefaultScheduler scheduler = new DefaultScheduler(delay, timeUnit);
		writePrivateField(scheduler, "executor", executor);

		RunnableFuture<?> r1 = mock(RunnableFuture.class);
		RunnableFuture<?> r2 = mock(RunnableFuture.class);
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
		queue.add(r1);
		queue.add(r2);

		when(executor.getQueue()).thenReturn(queue);

		scheduler.stop();

		verify(r1).cancel(false);
		verify(r2).cancel(false);
		verify(executor).purge();
	}

	@Test
	public void it_should_finalize_scheduler() throws Throwable {
		int delay = 10000;
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		DefaultScheduler scheduler = new DefaultScheduler(delay, timeUnit);
		writePrivateField(scheduler, "executor", executor);

		RunnableFuture<?> r1 = mock(RunnableFuture.class);
		RunnableFuture<?> r2 = mock(RunnableFuture.class);
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
		queue.add(r1);
		queue.add(r2);

		when(executor.getQueue()).thenReturn(queue);

		scheduler.finalize();

		verify(r1).cancel(false);
		verify(r2).cancel(false);
		verify(executor).purge();
		verify(executor).shutdownNow();
	}
}
