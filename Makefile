DOCSDIR=docs
APIURL=http://java.sun.com/j2se/1.4/docs/api # External URLs in the docs will point here
VERSION=3.2

.SUFFIXES: .java .j

SRC = java/it/unimi/dsi/fastutil
CSOURCES = $(wildcard $(SRC)/*/*.c $(SRC)/*.c) # The list of C source files
CFRAGMENTS = $(wildcard $(SRC)/*.h) # The list of C fragments
JSOURCES = $(CSOURCES:.c=.java) # The list of generated Java source files
FRAGMENTS = $(CFRAGMENTS:.h=.j) # The list of generated Java fragments
SOURCES = $(SRC)/Hash.java $(SRC)/BidirectionalIterator.java $(SRC)/HashCommon.java $(SRC)/Stack.java $(SRC)/AbstractStack.java # These are True Java Sources instead

ifdef ASSERTS
	ASSERTS_VALUE = true
else
	ASSERTS_VALUE = false
endif

.PHONY: all clean depend install docs jar tar jsources
.SECONDARY: $(JSOURCES)

explain:
	@echo -e "\nTo build fastutil, you must first use the gencsources.sh shell"
	@echo "script to generate the fake C sources that will be preprocessed"
	@echo "to obtain the actual Java files. Then, you can build the jar"
	@echo -e "file using \"make jar\", or the documentation using \"make docs\".\n"
	@echo -e "Note that you need ant (http://jakarta.apache.org/ant).\n"
	@echo -e "If you set the make variable TEST (e.g., make jar TEST=1), you"
	@echo -e "will compile behavioural and speed tests into the classes.\n\n"

jar: jsources
	ant jar

bin: jar docs
	-rm -f fastutil-$(VERSION)
	ln -s . fastutil-$(VERSION)
	tar zcvf fastutil-$(VERSION)-bin.tar.gz --owner=root --group=root \
		fastutil-$(VERSION)/CHANGES \
		fastutil-$(VERSION)/README \
		fastutil-$(VERSION)/COPYING.LIB \
		fastutil-$(VERSION)/docs \
		fastutil-$(VERSION)/fastutil-$(VERSION).jar
	rm fastutil-$(VERSION)

source:
	-rm -f fastutil-$(VERSION)
	ln -s . fastutil-$(VERSION)
	tar zcvf fastutil-$(VERSION)-src.tar.gz \
		fastutil-$(VERSION)/*.drv \
		fastutil-$(VERSION)/build.xml \
		fastutil-$(VERSION)/gencsources.sh \
		fastutil-$(VERSION)/CHANGES \
		fastutil-$(VERSION)/README \
		fastutil-$(VERSION)/COPYING.LIB \
		fastutil-$(VERSION)/Makefile \
		fastutil-$(VERSION)/$(SRC)/{BidirectionalIterator.java,HashCommon.java,Hash.java,Stack.java,AbstractStack.java} \
		fastutil-$(VERSION)/$(SRC)/{boolean,byte,char,short,int,long,float,double,object}s/package.html \
		fastutil-$(VERSION)/java/overview.html
	rm fastutil-$(VERSION)


jsources: $(JSOURCES)

$(JSOURCES): $(FRAGMENTS)

clean: 
	@find . -name \*.class -exec rm {} \;  
	@find . -name \*.java~ -exec rm {} \;  
	@find . -name \*.html~ -exec rm {} \;  
	@rm -f $(SRC)/*/*.java
	@rm -f $(SRC)/*.{c,h,j} $(SRC)/*/*.{c,h,j}
	@rm -fr $(DOCSDIR)/*


docs: jsources
	-mkdir -p $(DOCSDIR)
	-rm -fr $(DOCSDIR)/*
	javadoc -J-Xmx256M -source 1.4 -d $(DOCSDIR) -public -windowtitle "fastutil $(VERSION)" -overview java/overview.html -link $(APIURL) -sourcepath java -subpackages it.unimi.dsi.fastutil
	chmod -R a+rX $(DOCSDIR)


tags:
	etags build.xml Makefile README gencsources.sh *.drv java/overview.html $(SRC)/Hash.java $(SRC)/BidirectionalIterator.java $(SRC)/HashCommon.java  $(SRC)/Stack.java  $(SRC)/AbstractStack.java


.h.j:
ifdef TEST
	gcc -I. -ftabstop=4 -DTEST -DASSERTS=$(ASSERTS_VALUE) -E -C -P $< > $@
else
	gcc -I. -ftabstop=4 -DASSERTS=$(ASSERTS_VALUE) -E -C -P $< > $@
endif

.c.java:
ifdef TEST
	gcc -I. -ftabstop=4 -DTEST -DASSERTS=$(ASSERTS_VALUE) -E -C -P $< > $@
else
	gcc -I. -ftabstop=4 -DASSERTS=$(ASSERTS_VALUE) -E -C -P $< > $@
endif
