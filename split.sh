#!/bin/bash

TMPFILE=$(mktemp)

\ls -1 build/it/unimi/dsi/fastutil/*.class build/it/unimi/dsi/fastutil/io/*.class >$TMPFILE
find build/it/unimi/dsi/fastutil/{ints,longs,doubles,objects} -iname \*.class -not -iname \*Short\* -not -iname \*Char\* -not -iname \*Boolean\* -not -iname \*Byte\* -not -iname \*Float\* >>$TMPFILE
jdeps -v -R -e 'it\.unimi\.dsi\.fastutil($|.*)' -classpath build $(cat $TMPFILE) | awk '/build -> build/ {exit} { if (NF == 4) print "build/" gensub(/\./, "/", "g", $3) ".class" }' >>$TMPFILE
sort -u $TMPFILE >fastutil-core.txt

find build/it/unimi/dsi/fastutil/{bytes,chars} -iname \*.class -not -iname \*Short\* -not -iname \*Boolean\* -not -iname \*Float\* >$TMPFILE
jdeps -v -R -e 'it\.unimi\.dsi\.fastutil($|.*)' -classpath build $(cat $TMPFILE) | awk '/build -> build/ {exit} { if (NF == 4) print "build/" gensub(/\./, "/", "g", $3) ".class" }' >>$TMPFILE
comm -2 -3 <(sort -u $TMPFILE) fastutil-core.txt >fastutil-bytechar.txt

comm -2 -3 <(find build/it/unimi/dsi/fastutil/ -iname \*.class | sort) <(sort -u fastutil-core.txt fastutil-bytechar.txt) >fastutil-boolshortfloat.txt
