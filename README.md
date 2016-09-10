## ExifTool - Enhanced Java Integration for Phil Harvey's ExifTool.

[![Build Status](https://travis-ci.org/mjeanroy/exiftool.svg?branch=master)](https://travis-ci.org/mjeanroy/exiftool)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/exiftool-lib/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/exiftool-lib)

### Description

*The goal and main features of this fork did not changed, so this description is the same as the original library*

This project represents the most robust Java integrations with Phil Harvey's
excellent ExifTool available.

The goal of this project was to provide such a tight, well designed and performant
integration with ExifTool that any Java developer using the class would have no
idea that they weren't simply calling into a standard Java library while still
being able to leverage the unmatched robustness of ExifTool.

All concepts of external process launching, management, communication, tag
extraction, value conversion and resource cleanup are abstracted out by this
project and all handled automatically for the caller.

Even when using ExifTool in "daemon mode" via the `-stay_open True` command line
argument, this project hides all the details required to make that work,
automatically re-using the daemon process as well as eventually cleaning it up
automatically along with supporting resources after a defined interval of
inactivity so as to avoid resource leaks.

The set of EXIF tags supported out of the box is based on the EXIF tags supported
by the most popular mobile devices (iPhone, Android, BlackBerry, etc.) as well
as some of the most popular cameras on the market (Canon point and shoot as well
as DSLR).

And lastly, to ensure that integration with the external ExifTool project is as
robust and seamless as possible, this class also offers extensive pre-condition
checking and error reporting during instantiation and use.

For example, if you specify that you want to use `stay_open` support, the
ExifTool class will actually check the native ExifTool executable for support
for that feature before allowing the feature to be turned on and report the
problem to the caller along with potential work-arounds if necessary.

Additionally, all external calls to the process are safely wrapped and reported
with detailed exceptions if problems arise instead of just letting unknown
exceptions bubble up from the unknown system depths to the caller.

All the exceptions and exceptional scenarios are well-documented in the Javadoc
along with extensive implementation details for anyone wanting to know more about
the project.


### History

This library is a fork of [https://github.com/thebuzzmedia/exiftool](https://github.com/thebuzzmedia/exiftool) adding new features:
- Use Java executor framework (available with JDK6), instead of legacy `java.utilTimer`.
- ExifTool instance now implement `Closeable` interface (and can be used with Java 7 `try-with-resource`).
- Allow extensions: extracting custom tag is now allowed (just need to implement `Tag` interface, most common tags are still available out of the box).
- Use cache to extract ExifTool version when an instance is created.
- ExifTool is now thread-safe.
- Integration with `slf4j` and `log4j`.

More informations below.

### Support

This library is tested against Java >= 7, Linux and Windows.


### Installation

This library is available on maven repository:

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>exiftool-lib</artifactId>
  <version>2.1.0</version>
</dependency>
```

### Breaking Changes

- Tag class is now `com.thebuzzmedia.exiftool.Tag`, commons tags available in `com.thebuzzmedia.exiftool.core.StandardTag` enum class.
- Format class is now `com.thebuzzmedia.exiftool.Format`, commons formats (`NUMERIC`, `HUMAN_READABLE`) available in `com.thebuzzmedia.exiftool.core.StandardFormat` enum class.
- UnsupportedFeatureException class is now `com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException`.
- Feature class has been removed, use `enableStayOpen` method on `ExifToolBuilder` class.
- Introduction of `ExifToolBuilder` to create instances of `ExifTool`.
- Use Java executor framework:
  - Thread name cannot be defined (previously named as `ExifTool Cleanup Thread`).
  - Old timer implementation is still here, but this is not the default used implementation.
- Thread Safety.

### Examples

#### Parsing tags

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;

import java.io.File;
import java.util.Map;

import static java.util.Arrays.asList;

public class ExifParser {

    private static final Logger log = LoggerFactory.getLogger(ExifParser.class);

    public static Map<String, Tag> parse(File image) throws Exception {
        // ExifTool path must be defined as a system property (`exiftool.path`),
        // but path can be set using `withPath` method.
        try (ExifTool exifTool = new ExifToolBuilder().build()) {
            return exifTool.getImageMeta(image, asList(
                StandardTag.ISO,
                StandardTag.X_RESOLUTION,
                StandardTag.Y_RESOLUTION
            ));

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        for (String image : args) {
            System.out.println("Tags: ", ExifParser.parse(new File(image)));
        }
    }
}
```

#### Stay Open

If you want to reuse your exiftool process, you may want to activate the `stay_open` feature: note that an
instance of `UnsupportedFeatureException` will be thrown your exiftool version is too old.

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException;

import java.io.File;
import java.util.Map;

import static java.util.Arrays.asList;

public class ExifParser {

    private static final Logger log = LoggerFactory.getLogger(ExifParser.class);

    private static final ExifTool exifTool;

    static {
        try {
            exifTool = new ExifToolBuilder().enableStayOpen().build();
        } catch (UnsupportedFeatureException ex) {
            // Fallback to simple exiftool instance.
            exifTool = new ExifToolBuilder().build();
        }
    }

    public static Map<String, Tag> parse(File image) {
        return exifTool.getImageMeta(image, asList(
            StandardTag.ISO,
            StandardTag.X_RESOLUTION,
            StandardTag.Y_RESOLUTION
        ));
    }

    public static void main(String[] args) throws Exception {
        try {
            for (String image : args) {
                System.out.println("Tags: ", ExifParser.parse(new File(image)));
            }
        } finally {
            exifTool.close();
        }
    }
}
```

#### Multithreading

ExifTool is completely thread-safe. It means that if you use a "stay open" process, each access (get / set meta-data)
will be synchronized. This can be a big problem if you need to manipulate images in parallel. In this case, a pool
can be configured to allow a maximum number of `exiftool` to be open.

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException;

import java.io.File;
import java.util.Map;

import static java.util.Arrays.asList;

public class ExifParser {

    private static final Logger log = LoggerFactory.getLogger(ExifParser.class);

    public static void main(String[] args) throws Exception {
        ExifTool exifTool = new ExifToolBuilder()
            .withPoolSize(10)  // Allow 10 process
            .enableStayOpen()
            .build();

        // Start 10 threads and use exifTool in parallel.
        // ...
    }
}
```

### Performance

You can benchmark the performance of this ExifTool library on your machine by
running the Benchmark class under the /test/java repository.

Here is an example output on my Core2 Duo 3.0Ghz E6850 w/ 12GB of Ram:

Benchmark [tags=49, images=10, iterations=25]
	250 ExifTool process calls, 12250 total operations.

	[-stay_open False]
		Elapsed Time: 97823 ms (97.823 secs)
	[-stay_open True]
		Elapsed Time: 4049 ms (4.049 secs - 24.159792x faster)

You can see that utilizing the -stay_open functionality provided in ExifTool
you can realize magnitudes times more performance.

Also the bigger of a test you run (more iterations) the bigger the performance
margin increases.

### Troubleshooting

Below are a few common scenarios you might run into and proposed workarounds for
them.

- I keep getting `UnsupportedFeatureException` exceptions when running ExifTool `stay_open` support.

This exception will only be raised when you attempt to use a feature that
the underlying ExifTool doesn't support. This means you either need to upgrade
your install of ExifTool or skip using the feature.

- I downloaded the newest version of ExifTool, but I keep getting `UnsupportedFeatureExceptions`.

What is probably happening is that your host system already had ExifTool
installed and the default EXIF_TOOL_PATH is simply running the command "exiftool"
which executes the one in the system path, not the newest version you may have
just downloaded.

You can confirm this by typing 'which exiftool' to see which one is getting
launched. You can also point the ExifTool class at the correct version by
setting the path of the exiftool executable to use.

- Can the ExifTool class support parsing `InputStreams` instead of File representations of images?

No. Phil has mentioned that enabling daemon mode disables the ability to
stream bytes to ExifTool to process for EXIF data (because ExifTool listens
on the same input stream for processing commands and a terminating -execute
sequence, it can't also listen for image byte[] data).

Because of this and because of the expectation that ExifTool in daemon mode
will be the primary use-case for this class, limited support for `InputStream`
parsing was designed out of this class.

- Do I need to manually call `close()` to cleanup a daemon ExifTool?

This is done automatically for you when `exifTool` instance is garbage collected or
via the cleanup thread the class employs when a daemon instance of ExifTool is created.
Unless you modified the delay of the cleanup thread (and set it to 0 or less), the
automatic cleanup thread is enabled and will clean up those resources for
you after the specified amount of inactivity.

Nevetheless, I suggest you to use exiftool with a `try-with-resource` and to force `close`
operation when your program stops.

### Reference

ExifTool by Phil Harvey - http://www.sno.phy.queensu.ca/~phil/exiftool/
imgscalr - http://www.thebuzzmedia.com/software/imgscalr-java-image-scaling-library/

### License

This library is released under the Apache 2 License. See LICENSE.
