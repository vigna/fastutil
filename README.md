# Welcome to fastutil

[fastutil](http://fastutil.di.unimi.it/) extends the Java Collections
Framework by providing type-specific maps, sets, lists, and queues with a
small memory footprint and fast access and insertion; it provides also big
(64-bit) arrays, sets, and lists, sorting algorithms, fast, practical I/O
classes for binary and text files, and facilities for memory mapping large
files.

Since version 8.5.5, fastutil is split into two jars for convenience:

- `fastutil-core.jar` contains data structures based on integers, longs,
  doubles, and objects;

- `fastutil.jar` is the classic distribution, containing all classes.

Note that core classes are duplicated in the standard jar, so if you are
depending on both (for example, because of transitive dependencies) you
should exclude the core jar.

Previous split versions would provide different classes in different jars,
but managing sensibly dependencies turned out to be impossible.

You can also create a small, customized fastutil jar (which you can put in
your repo, local maven repo, etc.) using the `find-deps.sh` shell script.
It has mild prerequisites, as only the `jdeps` tool is required (bundled
with JDK 8). It can be used to identify all fastutil classes your project
uses and build a minimized jar only containing the necessary classes.

## Building

First, you have to `make sources` to get the actual Java sources.
After that, `ant jar` will generate a single jar file; `ant javadoc` will
generate the API documentation; `ant junit` will run the unit tests.

If you want to obtain the three jars above, you have to run the script
`split.sh`, and then `ant osgi-rest`.

The Java sources are generated using a C preprocessor. The `gencsource.sh`
script reads in a driver file, that is, a Java source that uses some
preprocessor-defined symbols and some conditional compilation, and produces a
(fake) C source, which includes the driver code and some definitions that
customize the environment.

* seba (<mailto:sebastiano.vigna@unimi.it>)
* https://groups.google.com/g/fastutil
