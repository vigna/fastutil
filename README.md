Welcome to fastutil.
--------------------

[fastutil](http://fastutil.di.unimi.it/) is a collection of type-specific
Java classes that extend the Java Collections Framework by providing
several containers, such as maps, sets, lists and prority queues,
implementing the interfaces of the java.util package; it provides also big
(64-bit) arrays, sets and lists, and fast, practical I/O classes for
binary and text files.

fastutil provides a huge collection of specialized classes generated
starting from a parameterized version; the classes are much more compact
and much faster than the general ones. Please read the package
documentation for more information.

The compiled code is contained in the jar file, and should be installed
where you keep Java extensions. Note that the jar file is huge, due to the
large number of classes: if you plan to ship your own jar with some
fastutil classes included, you should look at AutoJar or similar tools to
extract automatically the necessary classes.

You have to "make sources" to get the actual Java sources; finally, "ant
jar" and "ant javadoc" will generate the jar file and the API
documentation.

The Java sources are generated using a C preprocessor. The gencsource.sh
script reads in a driver file, that is, a Java source that uses some
preprocessor-defined symbols and some conditional compilation, and
produces a (fake) C source, which includes the driver code and some
definitions that customize the environment.


* seba (<mailto:sebastiano.vigna@unimi.it>)
* <mailto:fastutil@googlegroups.com>
