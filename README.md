# Welcome to `fastutil`!

# Introduction

`fastutil` extends the [Java™ Collections
Framework](http://download.oracle.com/javase/1.5.0/docs/guide/collections/)
by providing type-specific maps, sets, lists and queues with a small
memory footprint and fast access and insertion; it also provides big
(64-bit) arrays, sets and lists, and fast, practical I/O classes for
binary and text files. It is free software distributed under the [Apache
License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

The classes implement their standard counterpart interface (e.g., `Map`
for maps) and can be plugged into existing code. Moreover, they provide
additional features (such as bidirectional iterators) that are not
available in the standard classes.

Besides objects and primitive types, `fastutil` classes provide support
for _references_, that is, objects that are compared using the equality
operator rather than the `equals()` method.

The sources are generated using a C preprocessor, starting from a set of
driver files. You can peek at the `javadoc`-generated
[documentation](docs). In particular, the overview explains the design
choices used in `fastutil`.

# Core jar

If the standard `fastutil` jar is too large, there is a _core_ jar
containing only data structures specific for integers, longs and doubles.
Note that those classes are duplicated in the standard jar, so if you are
depending on both (for example, because of transitive dependencies) you
should exclude the core jar.

You can also create a small, customized fastutil jar (which you can put in
your repo, local maven repo, etc.) using the `find-deps.sh` shell script.
It has mild prerequisites, as only the `jdeps` tool is required (bundled
with JDK 8). It can be used to identify all fastutil classes your project
uses and build a minimized jar only containing the necessary classes.

## Building

First, you have to `make sources` to get the actual Java sources.
After that, `ant jar` will generate a single jar file; `ant javadoc` will
generate the API documentation; `ant junit` will run the unit tests.

If you want to obtain the two jars above, you have to run the script
`split.sh`, and then `ant osgi-rest`.

The Java sources are generated using a C preprocessor. The `gencsource.sh`
script reads in a driver file, that is, a Java source that uses some
preprocessor-defined symbols and some conditional compilation, and produces a
(fake) C source, which includes the driver code and some definitions that
customize the environment.

# Speed

`fastutil` provides in many cases the fastest implementations available.
You can find many other implementations of primitive collections (e.g.,
[HPPC](http://labs.carrotsearch.com/hppc.html),
[Koloboke](https://github.com/leventov/Koloboke), etc.). Sometimes authors
are a little bit quick in defining their implementations the “fastest
available“: the truth is, you have to take decisions in any
implementation. These decisions make your implementation faster or slower
in different scenarios. I suggest to _always_ test speed within your own
application, rather than relying on general benchmarks, and ask the
authors for suggestions about how to use the libraries in an optimal way.
In particular, when testing hash-based data structures you should always
set explicitly the load factor, as speed is strongly dependent on the
length of collision chains.

# Big data structures

With `fastutil` 6, a new set of classes makes it possible to handle very
large collections: in particular, collections whose size exceeds
2<sup>31</sup>. [Big arrays](docs/it/unimi/dsi/fastutil/BigArrays.html)
are arrays-of-arrays handled by a wealth of static methods that act on
them as if they were monodimensional arrays with 64-bit indices, and [big
lists](docs/it/unimi/dsi/fastutil/BigList.html) provide 64-bit list
access. The size of a [hash big
set](docs/it/unimi/dsi/fastutil/ints/IntOpenHashBigSet.html) is limited
only by the amount of core memory. The usual methods from
[`java.util.Arrays`](http://download.oracle.com/javase/1.5.0/docs/api/java/util/Arrays.html)
and similar classes have been extended to big arrays: have a look at the
Javadoc documentation of
[`BigArrays`](docs/it/unimi/dsi/fastutil/BigArrays.html) and
[`IntBigArrays`](docs/it/unimi/dsi/fastutil/ints/IntBigArrays.html) to get
an idea of the generic and type-specific methods available.

# History and Motivation

`fastutil` came up as a necessity during the development of
[UbiCrawler](http://law.di.unimi.it/index.php?option=com_content&task=view&id=34&Itemid=42),
as we needed to manage structures with dozens of millions of items very
efficiently. The same reasons led to the development of the
high-performance classes of [`dsiutils`](http://dsiutils.di.unimi.it/) and
[MG4J](http://mg4j.di.unimi.it/) (e.g.,
[`MutableString`](http://dsiutils.di.unimi.it/docs/it/unimi/dsi/lang/MutableString.html)).

# Discussion

There is a [discussion group](http://groups.google.com/group/fastutil)
about `fastutil`. You can join or [send a
message](mailto:fastutil@googlegroups.com).
