DOCSDIR=doc
APIURL=http://java.sun.com/j2se/1.4/docs/api # External URLs in the docs will point here
VERSION=2.0

.SUFFIXES: .java .class

CSOURCES = $(wildcard src/it/unimi/dsi/fastUtil/*.c)	# The list of C source files
SOURCES = $(CSOURCES:.c=.java)	# The list of Java generated source files
CLASSES = $(SOURCES:.java=.class)		# The list of respective class files

.PHONY: all clean depend install docs jar tar jsources
.SECONDARY: $(SOURCES)

explain:
	@echo -e "\nTo build fastUtil, you must first use the gencsources.sh shell"
	@echo "script to generate the fake C sources that will be preprocessed"
	@echo "to obtain the actual Java files. Then, you can build the jar"
	@echo -e "file using \"make jar\", or the documentation using \"make docs\".\n"
	@echo -e "Note that you need ant (http://jakarta.apache.org/ant).\n"
	@echo -e "If you set the make variable TEST (e.g., make jar TEST=1), you"
	@echo -e "will compile regression and speed tests into the classes.\n\n"

jar: jsources
	ant dist

tar: jar
	tar zhcvf fastUtil-$(VERSION).tgz fastUtil-*.*/

jsources: $(SOURCES)

clean: 
	@find . -name \*.class -exec rm {} \;  
	@find . -name \*.java~ -exec rm {} \;  
	@find . -name \*.html~ -exec rm {} \;  
	@rm -f */*/*/*/*/*Set.java */*/*/*/*/*Map.java */*/*/*/*/*Collection.java */*/*/*/*/*ListIterator.java */*/*/*/*/{Boolean,Byte,Short,Int,Long,Char,Float,Double}*Iterator.java */*/*/*/*/*Comparator.java
	@rm -f */*/*/*/*/*.c
	@rm -fr $(DOCSDIR)/*


PACKAGES = it.unimi.dsi.fastUtil

docs: jsources
	-mkdir -p $(DOCSDIR)
	-rm -fr $(DOCSDIR)/*
	javadoc -J-Xmx256M -d $(DOCSDIR) -windowtitle "fastUtil $(VERSION)" -link $(APIURL) -sourcepath src $(PACKAGES)
	chmod -R a+rX $(DOCSDIR)


tags:
	etags Makefile README gencsources.sh *.drv src/it/unimi/dsi/fastUtil/Hash.java src/it/unimi/dsi/fastUtil/HashCommon.java src/it/unimi/dsi/fastUtil/package.html

# Implicit rule for making Java class files from Java 
# source files. 
.c.java:
ifdef TEST
	gcc -I. -DTEST -E -C -P $< > $@
else
	gcc -I. -E -C -P $< > $@
endif
