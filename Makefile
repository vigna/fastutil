DOCSDIR=docs
APIURL=http://java.sun.com/j2se/1.4/docs/api # External URLs in the docs will point here
VERSION=2.52

.SUFFIXES: .java .class

CSOURCES = $(wildcard java/it/unimi/dsi/fastutil/*.c)	# The list of C source files
SOURCES = $(CSOURCES:.c=.java)	# The list of Java generated source files
CLASSES = $(SOURCES:.java=.class)		# The list of respective class files

.PHONY: all clean depend install docs jar tar jsources
.SECONDARY: $(SOURCES)

explain:
	@echo -e "\nTo build fastutil, you must first use the gencsources.sh shell"
	@echo "script to generate the fake C sources that will be preprocessed"
	@echo "to obtain the actual Java files. Then, you can build the jar"
	@echo -e "file using \"make jar\", or the documentation using \"make docs\".\n"
	@echo -e "Note that you need ant (http://jakarta.apache.org/ant).\n"
	@echo -e "If you set the make variable TEST (e.g., make jar TEST=1), you"
	@echo -e "will compile regression and speed tests into the classes.\n\n"

jar: jsources
	export ANT_OPTS="-Xmx128M -Xms128M"
	ant jar

tar: jar
	-rm -f fastutil-$(VERSION)
	ln -s . fastutil-$(VERSION)
	tar zcvf fastutil-$(VERSION).tar.gz --owner=root --group=root \
		fastutil-$(VERSION)/*.drv \
		fastutil-$(VERSION)/build.xml \
		fastutil-$(VERSION)/gencsources.sh \
		fastutil-$(VERSION)/CHANGES \
		fastutil-$(VERSION)/README \
		fastutil-$(VERSION)/COPYING.LIB \
		fastutil-$(VERSION)/Makefile \
		fastutil-$(VERSION)/docs \
		fastutil-$(VERSION)/fastutil-$(VERSION).jar \
		fastutil-$(VERSION)/java/it/unimi/dsi/fastutil/{BidirectionalIterator.java,HashCommon.java,Hash.java,package.html}
	rm fastutil-$(VERSION)

source:
	-rm -f fastutil-$(VERSION)
	ln -s . fastutil-$(VERSION)
	tar zcvf fastutil-$(VERSION).tar.gz \
		fastutil-$(VERSION)/*.drv \
		fastutil-$(VERSION)/build.xml \
		fastutil-$(VERSION)/gencsources.sh \
		fastutil-$(VERSION)/CHANGES \
		fastutil-$(VERSION)/README \
		fastutil-$(VERSION)/COPYING.LIB \
		fastutil-$(VERSION)/Makefile \
		fastutil-$(VERSION)/java/it/unimi/dsi/fastutil/{BidirectionalIterator.java,HashCommon.java,Hash.java,package.html}
	rm fastutil-$(VERSION)

jsources: $(SOURCES)

clean: 
	@find . -name \*.class -exec rm {} \;  
	@find . -name \*.java~ -exec rm {} \;  
	@find . -name \*.html~ -exec rm {} \;  
	@rm -f */*/*/*/*/*Set.java */*/*/*/*/*Map.java */*/*/*/*/*Collection.java */*/*/*/*/*ListIterator.java */*/*/*/*/{Boolean,Byte,Short,Int,Long,Char,Float,Double}*Iterator.java */*/*/*/*/*Comparator.java
	@rm -f */*/*/*/*/*.c
	@rm -fr $(DOCSDIR)/*


PACKAGES = it.unimi.dsi.fastutil

docs: jsources
	-mkdir -p $(DOCSDIR)
	-rm -fr $(DOCSDIR)/*
	javadoc -J-Xmx256M -d $(DOCSDIR) -public -windowtitle "fastutil $(VERSION)" -link $(APIURL) -sourcepath java $(PACKAGES)
	chmod -R a+rX $(DOCSDIR)


tags:
	etags build.xml Makefile README gencsources.sh *.drv java/it/unimi/dsi/fastutil/Hash.java java/it/unimi/dsi/fastutil/BidirectionalIterator.java java/it/unimi/dsi/fastutil/HashCommon.java java/it/unimi/dsi/fastutil/package.html

# Implicit rule for making Java class files from Java 
# source files. 
.c.java:
ifdef TEST
	gcc -I. -DTEST -E -C -P $< > $@
else
	gcc -I. -E -C -P $< > $@
endif
