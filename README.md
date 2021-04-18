# Welcome to fastutil

[fastutil](http://fastutil.di.unimi.it/) is a collection of type-specific
Java classes that extend the Java Collections Framework by providing
several containers, such as maps, sets, lists and prority queues,
implementing the interfaces of the java.util package; it also provides big
(64-bit) arrays, sets, lists, and fast, practical I/O classes for binary
and text files.

fastutil provides a huge collection of specialized classes generated
starting from a parameterized version; the classes are much more compact
and much faster than the general ones. Please read the package
documentation for more information.

Since version 8.5.4, fastutil is split into three jars for convenience:

- `fastutil-core.jar` contains data structures based on integers, longs,
  doubles, and objects;

- `fastutil-extra.jar` adds data structures  based on references, bytes,
  and characters;

- `fastutil.jar` adds the remaining data structures: booleans, shorts, and
  floats.

Each jar depends on the previous one.

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
