# Welcome to fastutil

[fastutil](http://fastutil.di.unimi.it/) is a collection of type-specific Java
classes that extend the Java Collections Framework by providing several
containers, such as maps, sets, lists and prority queues, implementing the
interfaces of the java.util package; it also provides big (64-bit) arrays,
sets, lists, and fast, practical I/O classes for binary and text files.

fastutil provides a huge collection of specialized classes generated starting
from a parametrized version; the classes are much more compact and much faster
than the general ones. Please read the package documentation for more
information.

Note that the jar file is huge, due to the large number of classes. To
create a small, customized fastutil jar (which you can put in your repo,
local maven, etc.), we provide the `find-deps.sh` shell script. It has
mild prerequisites, as only the `jdeps` tool is required (bundled with JDK
8). It can be used to identify all fastutil classes your project uses and
build a minimized jar only containing the necessary classes. You can also
have a look at AutoJar or similar tools to extract automatically the
necessary classes.

## Building

You have to "make sources" to get the actual Java sources; finally, "ant
jar" will generate the jar file; "ant javadoc" will generate the API
documentation; "ant junit" will run the unit tests.

The Java sources are generated using a C preprocessor. The `gencsource.sh`
script reads in a driver file, that is, a Java source that uses some
preprocessor-defined symbols and some conditional compilation, and produces a
(fake) C source, which includes the driver code and some definitions that
customize the environment.

* seba (<mailto:sebastiano.vigna@unimi.it>)
* https://groups.google.com/g/fastutil
