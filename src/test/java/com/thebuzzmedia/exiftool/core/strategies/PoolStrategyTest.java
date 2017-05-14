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

import static com.thebuzzmedia.exiftool.tests.MockitoUtils.anyListOf;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.thebuzzmedia.exiftool.ExecutionStrategy;
import com.thebuzzmedia.exiftool.Version;
import com.thebuzzmedia.exiftool.exceptions.PoolIOException;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.junit.Before;
import org.junit.Test;

public class PoolStrategyTest {

	private CommandExecutor executor;
	private String exifTool;
	private List<String> arguments;
	private OutputHandler handler;

	@Before
	public void setUp() {
		executor = mock(CommandExecutor.class);
		exifTool = "exiftool";
		arguments = singletonList("-ver");
		handler = mock(OutputHandler.class);
	}

	@Test
	public void it_should_create_pool() {
		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		ExecutionStrategy s2 = mock(ExecutionStrategy.class);
		ExecutionStrategy s3 = mock(ExecutionStrategy.class);
		Collection<ExecutionStrategy> strategies = asList(s1, s2, s3);

		PoolStrategy pool = new PoolStrategy(strategies);

		assertThat(pool.isRunning()).isFalse();
	}

	@Test
	public void it_should_execute_a_strategy() throws Exception {
		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		ExecutionStrategy s2 = mock(ExecutionStrategy.class);
		ExecutionStrategy s3 = mock(ExecutionStrategy.class);
		Collection<ExecutionStrategy> strategies = asList(s1, s2, s3);

		PoolStrategy pool = new PoolStrategy(strategies);

		pool.execute(executor, exifTool, arguments, handler);

		verify(s1).execute(executor, exifTool, arguments, handler);
	}

	@Test
	public void it_should_check_that_version_is_not_supported() throws Exception {
		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		ExecutionStrategy s2 = mock(ExecutionStrategy.class);
		Collection<ExecutionStrategy> strategies = asList(s1, s2);

		PoolStrategy pool = new PoolStrategy(strategies);

		Version version = mock(Version.class);
		when(s1.isSupported(version)).thenReturn(true);
		when(s2.isSupported(version)).thenReturn(false);

		assertThat(pool.isSupported(version)).isFalse();
	}

	@Test
	public void it_should_check_that_version_is_supported() throws Exception {
		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		ExecutionStrategy s2 = mock(ExecutionStrategy.class);
		Collection<ExecutionStrategy> strategies = asList(s1, s2);

		PoolStrategy pool = new PoolStrategy(strategies);

		Version version = mock(Version.class);
		when(s1.isSupported(version)).thenReturn(true);
		when(s2.isSupported(version)).thenReturn(true);

		assertThat(pool.isSupported(version)).isTrue();
	}

	@Test
	public void it_should_execute_strategies_in_parallel() throws Exception {
		CountDownLatch executionLock = new CountDownLatch(1);

		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		doAnswer(new LockAnswer(1, executionLock))
			.when(s1)
			.execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		ExecutionStrategy s2 = mock(ExecutionStrategy.class);
		doAnswer(new LockAnswer(1, executionLock))
			.when(s2)
			.execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		PoolStrategy pool = new PoolStrategy(asList(s1, s2));

		CountDownLatch lock = new CountDownLatch(1);

		ExecuteTask r1 = new ExecuteTask(1, lock, pool, executor, exifTool, arguments, handler);
		Thread t1 = new Thread(r1);
		t1.start();

		ExecuteTask r2 = new ExecuteTask(2, lock, pool, executor, exifTool, arguments, handler);
		Thread t2 = new Thread(r2);
		t2.start();

		// Unblock threads
		lock.countDown();
		sleep(1000);

		// Check that strategy have been executed
		verify(s1).execute(executor, exifTool, arguments, handler);
		verify(s2).execute(executor, exifTool, arguments, handler);

		// Unblock strategies
		executionLock.countDown();
		sleep(2000);

		assertThat(r1.getThrown()).isNull();
		assertThat(r2.getThrown()).isNull();
		assertThat(pool.isRunning()).isFalse();
	}

	@Test
	public void it_should_execute_strategies_in_parallel_and_block_until_one_become_available() throws Exception {
		CountDownLatch execLock1 = new CountDownLatch(1);
		CountDownLatch execLock2 = new CountDownLatch(1);

		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		doAnswer(new LockAnswer(1, execLock1))
			.when(s1)
			.execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		ExecutionStrategy s2 = mock(ExecutionStrategy.class);
		doAnswer(new LockAnswer(2, execLock2))
			.when(s2)
			.execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		PoolStrategy pool = new PoolStrategy(asList(s1, s2));

		CountDownLatch lock = new CountDownLatch(1);

		ExecuteTask r1 = new ExecuteTask(1, lock, pool, executor, exifTool, arguments, handler);
		Thread t1 = new Thread(r1);
		t1.start();

		ExecuteTask r2 = new ExecuteTask(2, lock, pool, executor, exifTool, arguments, handler);
		Thread t2 = new Thread(r2);
		t2.start();

		// Unblock threads.
		lock.countDown();
		sleep(1000);

		// Test that first two are executed.
		verify(s1, times(1)).execute(executor, exifTool, arguments, handler);
		verify(s2, times(1)).execute(executor, exifTool, arguments, handler);

		reset(s1);
		reset(s2);

		// Start new thread.
		ExecuteTask r3 = new ExecuteTask(3, lock, pool, executor, exifTool, arguments, handler);
		Thread t3 = new Thread(r3);
		t3.start();

		verify(s1, never()).execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));
		verify(s2, never()).execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		// Unblock first strategy.
		execLock1.countDown();
		sleep(1000);

		// Check that the third has been executed.
		verify(s1).execute(executor, exifTool, arguments, handler);

		// Unblock last one.
		execLock2.countDown();

		assertThat(r1.getThrown()).isNull();
		assertThat(r2.getThrown()).isNull();
		assertThat(r3.getThrown()).isNull();
	}

	@Test
	public void it_should_be_running_when_a_strategy_is_running() throws Exception {
		CountDownLatch execLock1 = new CountDownLatch(1);
		CountDownLatch execLock2 = new CountDownLatch(1);

		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		doAnswer(new LockAnswer(1, execLock1))
			.when(s1)
			.execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		ExecutionStrategy s2 = mock(ExecutionStrategy.class);
		doAnswer(new LockAnswer(2, execLock2))
			.when(s2)
			.execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		PoolStrategy pool = new PoolStrategy(asList(s1, s2));

		CountDownLatch lock = new CountDownLatch(1);

		ExecuteTask r1 = new ExecuteTask(1, lock, pool, executor, exifTool, arguments, handler);
		Thread t1 = new Thread(r1);
		t1.start();

		ExecuteTask r2 = new ExecuteTask(2, lock, pool, executor, exifTool, arguments, handler);
		Thread t2 = new Thread(r2);
		t2.start();

		assertThat(pool.isRunning()).isFalse();

		// Unblock threads.
		lock.countDown();
		sleep(1000);

		assertThat(pool.isRunning()).isTrue();

		// Start new thread.
		ExecuteTask r3 = new ExecuteTask(3, lock, pool, executor, exifTool, arguments, handler);
		Thread t3 = new Thread(r3);
		t3.start();

		assertThat(pool.isRunning()).isTrue();

		execLock1.countDown();
		execLock2.countDown();
		sleep(1000);

		assertThat(pool.isRunning()).isFalse();
	}

	@Test
	public void it_should_close_inner_strategies() throws Exception {
		CountDownLatch execLock = new CountDownLatch(1);
		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		doAnswer(new LockAnswer(1, execLock))
			.when(s1)
			.execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		ExecutionStrategy s2 = mock(ExecutionStrategy.class);

		PoolStrategy pool = new PoolStrategy(asList(s1, s2));

		// Start a thread with strategy #1
		CountDownLatch lock = new CountDownLatch(1);
		Thread t1 = new Thread(new ExecuteTask(1, lock, pool, executor, exifTool, arguments, handler));
		t1.start();

		// Unblock threads.
		lock.countDown();
		sleep(1000);

		// Test that first one is executed.
		assertThat(pool.isRunning()).isTrue();
		verify(s1).execute(executor, exifTool, arguments, handler);
		verify(s2, never()).execute(executor, exifTool, arguments, handler);

		// Start new thread used to close pool.
		Thread t2 = new Thread(new CloseTask(1, pool));
		t2.start();

		verify(s1, never()).close();
		verify(s2, never()).close();

		// Unblock first strategy.
		execLock.countDown();
		sleep(1000);

		verify(s1).close();
		verify(s2).close();
	}

	@Test
	public void it_should_close_inner_strategies_and_collect_exceptions() throws Exception {
		CountDownLatch execLock = new CountDownLatch(1);
		ExecutionStrategy s1 = mock(ExecutionStrategy.class);
		doAnswer(new LockAnswer(1, execLock))
			.when(s1)
			.execute(any(CommandExecutor.class), anyString(), anyListOf(String.class), any(OutputHandler.class));

		ExecutionStrategy s2 = mock(ExecutionStrategy.class);

		PoolStrategy pool = new PoolStrategy(asList(s1, s2));

		// Start a thread with strategy #1
		CountDownLatch lock = new CountDownLatch(1);
		Thread t1 = new Thread(new ExecuteTask(1, lock, pool, executor, exifTool, arguments, handler));
		t1.start();

		// Unblock threads.
		lock.countDown();
		sleep(1000);

		// Test that first one is executed.
		assertThat(pool.isRunning()).isTrue();
		verify(s1).execute(executor, exifTool, arguments, handler);
		verify(s2, never()).execute(executor, exifTool, arguments, handler);

		// Start new thread used to close pool.
		CloseTask task = new CloseTask(1, pool);
		Thread t2 = new Thread(task);
		t2.start();

		assertThat(pool.isRunning()).isTrue();
		verify(s1, never()).close();
		verify(s2, never()).close();

		IOException ex1 = new IOException("1");
		IOException ex2 = new IOException("2");

		doThrow(ex1).when(s1).close();
		doThrow(ex2).when(s2).close();

		// Unblock first strategy.
		execLock.countDown();
		sleep(1000);

		verify(s1).close();
		verify(s2).close();

		assertThat(task.getThrown()).isNotNull();
		assertThat(task.getThrown()).isInstanceOf(PoolIOException.class);

		Collection<Exception> exceptions = ((PoolIOException) task.getThrown()).getThrownExceptions();
		assertThat(exceptions)
			.isNotNull()
			.isNotEmpty()
			.hasSize(2)
			.contains(ex1, ex2);
	}
}
