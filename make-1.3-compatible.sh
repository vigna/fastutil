#
# Strips all references to 1.4 features, that is, RandomAccess and asserts.
# The resulting files are compilable using JDK 1.3.
# Kindly donated by Aaron Kardell.
#
for javafile in $(find $JAVADIR -name "*.java")
do
	cat $javafile | sed 's/^import java.util.RandomAccess;/\/\/import java.util.RandomAccess;/' | sed 's/implements RandomAccess, /implements /' | sed 's/\([^\/]\)\(if ( asserts ) assert\)/\1\/\/\2/' | sed 's/AssertionError/Error/g' | sed 's/    assert /    \/\/assert /g' > /tmp/JDK13Tmp.java
	cp /tmp/JDK13Tmp.java $javafile
	rm -f /tmp/JDK13Tmp.java
done

