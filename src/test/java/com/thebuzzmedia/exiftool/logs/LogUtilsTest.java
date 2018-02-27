package com.thebuzzmedia.exiftool.logs;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogUtilsTest {

	@Test
	public void it_should_translate_slf4j_string_to_string_format() {
		assertThat(LogUtils.fromSlf4jStyle("")).isEqualTo("");
		assertThat(LogUtils.fromSlf4jStyle("Test")).isEqualTo("Test");
		assertThat(LogUtils.fromSlf4jStyle("Test {}")).isEqualTo("Test %s");
		assertThat(LogUtils.fromSlf4jStyle("Test {} {}")).isEqualTo("Test %s %s");
	}

	@Test
	public void it_should_serialize_stack_trace_to_string() {
		RuntimeException ex = new RuntimeException("message");
		String stackTrace = LogUtils.getStackTrace(ex);
		assertThat(stackTrace).isNotEmpty().startsWith("java.lang.RuntimeException: message");
	}
}
