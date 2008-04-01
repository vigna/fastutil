include build.properties

SOURCEDIR = java/it/unimi/dsi/fastutil
DOCSDIR = docs

APIURL=http://java.sun.com/j2se/5.0/docs/api # External URLs in the docs will point here

.SUFFIXES: .java .j

.PHONY: all clean depend install docs jar tar jsources csources

.SECONDARY: $(JSOURCES)

#  The capitalized types used to build class and method names; boolean and object types are not listed.
TYPE_NOBOOL_NOOBJ=Byte Short Int Long Char Float Double

#  The capitalized types used to build class and method names; boolean and reference are not listed.
TYPE_NOBOOL_NOREF=$(TYPE_NOBOOL_NOOBJ) Object

#  The capitalized types used to build class and method names; object types are not listed.
TYPE_NOOBJ=Boolean $(TYPE_NOBOOL_NOOBJ)

#  The capitalized types used to build class and method names; references are not listed.
TYPE_NOREF=$(TYPE_NOOBJ) Object

#  The capitalized types used to build class and method names; boolean is not listed.
TYPE_NOBOOL=$(TYPE_NOBOOL_NOREF) Reference

# The capitalized types used to build class and method names; now references appear as Reference.
TYPE=$(TYPE_NOREF) Reference

# These variables are used as an associative array (using computed names).
PACKAGE_Boolean = booleans
PACKAGE_Byte = bytes
PACKAGE_Short = shorts
PACKAGE_Int = ints
PACKAGE_Long = longs
PACKAGE_Char = chars
PACKAGE_Float= floats
PACKAGE_Double = doubles
PACKAGE_Object = objects
PACKAGE_Reference = objects

explain:
	@echo -e "\nTo build fastutil, you must first use \"make sources\""
	@echo -e "to obtain the actual Java files. Then, you can build the jar"
	@echo -e "file using \"ant jar\", or the documentation using \"ant javadoc\".\n"
	@echo -e "If you set the make variable TEST (e.g., make sources TEST=1), you"
	@echo -e "will compile behavioral and speed tests into the classes.\n"
	@echo -e "If you set the make variable ASSERTS (e.g., make sources ASSERTS=1), you"
	@echo -e "will compile assertions into the classes.\n\n"

source:
	-rm -f fastutil-$(version)
	ln -s . fastutil-$(version)
	tar zcvf fastutil-$(version)-src.tar.gz --owner=0 --group=0 \
		fastutil-$(version)/*.drv \
		fastutil-$(version)/build.xml \
		fastutil-$(version)/build.properties \
		fastutil-$(version)/gencsource.sh \
		fastutil-$(version)/CHANGES \
		fastutil-$(version)/README \
		fastutil-$(version)/COPYING.LIB \
		fastutil-$(version)/makefile \
		$(foreach f, $(SOURCES), fastutil-$(version)/$(f)) \
		fastutil-$(version)/$(SOURCEDIR)/{boolean,byte,char,short,int,long,float,double,object}s/package.html \
		fastutil-$(version)/$(SOURCEDIR)/io/package.html \
		fastutil-$(version)/java/overview.html
	rm fastutil-$(version)

bin:
	make -s clean sources
	ant jar javadoc
	-rm -f fastutil-$(version)
	ln -s . fastutil-$(version)
	tar zcvf fastutil-$(version)-bin.tar.gz --owner=0 --group=0 \
		fastutil-$(version)/CHANGES \
		fastutil-$(version)/README \
		fastutil-$(version)/COPYING.LIB \
		fastutil-$(version)/docs \
		fastutil-$(version)/fastutil-$(version).jar
	rm fastutil-$(version)


LinkedOpenHashSet.drv: OpenHashSet.drv
	ln -sf OpenHashSet.drv $@

LinkedOpenHashMap.drv: OpenHashMap.drv
	ln -sf OpenHashMap.drv $@

OpenCustomHashSet.drv: OpenHashSet.drv
	ln -sf OpenHashSet.drv $@

OpenCustomHashMap.drv: OpenHashMap.drv
	ln -sf OpenHashMap.drv $@

LinkedOpenCustomHashSet.drv: OpenHashSet.drv
	ln -sf OpenHashSet.drv $@

LinkedOpenCustomHashMap.drv: OpenHashMap.drv
	ln -sf OpenHashMap.drv $@

CSOURCES := 

#
# Interfaces
#

ITERABLES := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Iterable.c)
$(ITERABLES): Iterable.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ITERABLES)

COLLECTIONS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Collection.c)
$(COLLECTIONS): Collection.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(COLLECTIONS)

SETS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Set.c)
$(SETS): Set.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SETS)

SORTED_SETS := $(foreach k,$(TYPE_NOBOOL), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)SortedSet.c)
$(SORTED_SETS): SortedSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SORTED_SETS)

FUNCTIONS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)Function.c))
$(FUNCTIONS): Function.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(FUNCTIONS)

MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)Map.c))
$(MAPS): Map.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(MAPS)

SORTED_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)SortedMap.c))
$(SORTED_MAPS): SortedMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SORTED_MAPS)

LISTS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)List.c)
$(LISTS): List.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LISTS)

STACKS := $(foreach k,$(TYPE_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Stack.c)
$(STACKS): Stack.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(STACKS)

PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)PriorityQueue.c)
$(PRIORITY_QUEUES): PriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(PRIORITY_QUEUES)

INDIRECT_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)IndirectPriorityQueue.c)
$(INDIRECT_PRIORITY_QUEUES): IndirectPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(INDIRECT_PRIORITY_QUEUES)

INDIRECT_DOUBLE_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)IndirectDoublePriorityQueue.c)
$(INDIRECT_DOUBLE_PRIORITY_QUEUES): IndirectDoublePriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(INDIRECT_DOUBLE_PRIORITY_QUEUES)

COMPARATORS := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Comparator.c)
$(COMPARATORS): Comparator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(COMPARATORS)

ITERATORS := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Iterator.c)
$(ITERATORS): Iterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ITERATORS)

BIDIRECTIONAL_ITERATORS := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)BidirectionalIterator.c)
$(BIDIRECTIONAL_ITERATORS): BidirectionalIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(BIDIRECTIONAL_ITERATORS)

LIST_ITERATORS := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)ListIterator.c)
$(LIST_ITERATORS): ListIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LIST_ITERATORS)

#
# Abstract implementations
#

ABSTRACT_COLLECTIONS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)Collection.c)
$(ABSTRACT_COLLECTIONS): AbstractCollection.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_COLLECTIONS)

ABSTRACT_SETS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)Set.c)
$(ABSTRACT_SETS): AbstractSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_SETS)

ABSTRACT_SORTED_SETS := $(foreach k,$(TYPE_NOBOOL), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)SortedSet.c)
$(ABSTRACT_SORTED_SETS): AbstractSortedSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_SORTED_SETS)

ABSTRACT_FUNCTIONS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)2$(v)Function.c))
$(ABSTRACT_FUNCTIONS): AbstractFunction.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_FUNCTIONS)

ABSTRACT_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)2$(v)Map.c))
$(ABSTRACT_MAPS): AbstractMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_MAPS)

ABSTRACT_SORTED_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)2$(v)SortedMap.c))
$(ABSTRACT_SORTED_MAPS): AbstractSortedMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_SORTED_MAPS)

ABSTRACT_LISTS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)List.c)
$(ABSTRACT_LISTS): AbstractList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_LISTS)

ABSTRACT_STACKS := $(foreach k,$(TYPE_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)Stack.c)
$(ABSTRACT_STACKS): AbstractStack.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_STACKS)

ABSTRACT_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)PriorityQueue.c)
$(ABSTRACT_PRIORITY_QUEUES): AbstractPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_PRIORITY_QUEUES)

ABSTRACT_COMPARATORS := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)Comparator.c)
$(ABSTRACT_COMPARATORS): AbstractComparator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_COMPARATORS)

ABSTRACT_ITERATORS := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)Iterator.c)
$(ABSTRACT_ITERATORS): AbstractIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_ITERATORS)

ABSTRACT_BIDIRECTIONAL_ITERATORS := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)BidirectionalIterator.c)
$(ABSTRACT_BIDIRECTIONAL_ITERATORS): AbstractBidirectionalIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_BIDIRECTIONAL_ITERATORS)

ABSTRACT_LIST_ITERATORS := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/Abstract$(k)ListIterator.c)
$(ABSTRACT_LIST_ITERATORS): AbstractListIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_LIST_ITERATORS)

#
# Concrete implementations
#

OPEN_HASH_SETS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)OpenHashSet.c)
$(OPEN_HASH_SETS): OpenHashSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_HASH_SETS)

LINKED_OPEN_HASH_SETS := $(foreach k,$(TYPE_NOBOOL), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)LinkedOpenHashSet.c)
$(LINKED_OPEN_HASH_SETS): LinkedOpenHashSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LINKED_OPEN_HASH_SETS)

OPEN_CUSTOM_HASH_SETS := $(SOURCEDIR)/objects/ObjectOpenCustomHashSet.c
$(OPEN_CUSTOM_HASH_SETS): OpenCustomHashSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_CUSTOM_HASH_SETS)

LINKED_OPEN_CUSTOM_HASH_SETS := $(SOURCEDIR)/objects/ObjectLinkedOpenCustomHashSet.c
$(LINKED_OPEN_CUSTOM_HASH_SETS): LinkedOpenCustomHashSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LINKED_OPEN_CUSTOM_HASH_SETS)

ARRAY_SETS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)ArraySet.c)
$(ARRAY_SETS): ArraySet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_SETS)

AVL_TREE_SETS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)AVLTreeSet.c)
$(AVL_TREE_SETS): AVLTreeSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(AVL_TREE_SETS)

RB_TREE_SETS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)RBTreeSet.c)
$(RB_TREE_SETS): RBTreeSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(RB_TREE_SETS)

OPEN_HASH_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)OpenHashMap.c))
$(OPEN_HASH_MAPS): OpenHashMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_HASH_MAPS)

LINKED_OPEN_HASH_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)LinkedOpenHashMap.c))
$(LINKED_OPEN_HASH_MAPS): LinkedOpenHashMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LINKED_OPEN_HASH_MAPS)

OPEN_CUSTOM_HASH_MAPS := $(foreach v,$(TYPE), $(SOURCEDIR)/objects/Object2$(v)OpenCustomHashMap.c)
$(OPEN_CUSTOM_HASH_MAPS): OpenCustomHashMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_CUSTOM_HASH_MAPS)

LINKED_OPEN_CUSTOM_HASH_MAPS := $(foreach v,$(TYPE), $(SOURCEDIR)/objects/Object2$(v)LinkedOpenCustomHashMap.c)
$(LINKED_OPEN_CUSTOM_HASH_MAPS): LinkedOpenCustomHashMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LINKED_OPEN_CUSTOM_HASH_MAPS)

ARRAY_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)ArrayMap.c))
$(ARRAY_MAPS): ArrayMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_MAPS)

AVL_TREE_MAPS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)AVLTreeMap.c))
$(AVL_TREE_MAPS): AVLTreeMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(AVL_TREE_MAPS)

RB_TREE_MAPS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)RBTreeMap.c))
$(RB_TREE_MAPS): RBTreeMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(RB_TREE_MAPS)

ARRAY_LISTS := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)ArrayList.c)
$(ARRAY_LISTS): ArrayList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_LISTS)

FRONT_CODED_LISTS := $(foreach k, Byte Short Int Long Char, $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)ArrayFrontCodedList.c)
$(FRONT_CODED_LISTS): ArrayFrontCodedList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(FRONT_CODED_LISTS)

HEAP_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)HeapPriorityQueue.c)
$(HEAP_PRIORITY_QUEUES): HeapPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAP_PRIORITY_QUEUES)

ARRAY_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)ArrayPriorityQueue.c)
$(ARRAY_PRIORITY_QUEUES): ArrayPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_PRIORITY_QUEUES)

HEAP_SEMI_INDIRECT_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)HeapSemiIndirectPriorityQueue.c)
$(HEAP_SEMI_INDIRECT_PRIORITY_QUEUES): HeapSemiIndirectPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAP_SEMI_INDIRECT_PRIORITY_QUEUES)

HEAP_INDIRECT_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)HeapIndirectPriorityQueue.c)
$(HEAP_INDIRECT_PRIORITY_QUEUES): HeapIndirectPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAP_INDIRECT_PRIORITY_QUEUES)

ARRAY_INDIRECT_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)ArrayIndirectPriorityQueue.c)
$(ARRAY_INDIRECT_PRIORITY_QUEUES): ArrayIndirectPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_INDIRECT_PRIORITY_QUEUES)

HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)HeapSesquiIndirectDoublePriorityQueue.c)
$(HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUES): HeapSesquiIndirectDoublePriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUES)

HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)HeapIndirectDoublePriorityQueue.c)
$(HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUES): HeapIndirectDoublePriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUES)

ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)ArrayIndirectDoublePriorityQueue.c)
$(ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUES): ArrayIndirectDoublePriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUES)

#
# Static containers
#

ITERATORS_STATIC := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Iterators.c)
$(ITERATORS_STATIC): Iterators.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ITERATORS_STATIC)


COLLECTIONS_STATIC := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Collections.c)
$(COLLECTIONS_STATIC): Collections.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(COLLECTIONS_STATIC)


SETS_STATIC := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Sets.c)
$(SETS_STATIC): Sets.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SETS_STATIC)


SORTED_SETS_STATIC := $(foreach k,$(TYPE_NOBOOL), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)SortedSets.c)
$(SORTED_SETS_STATIC): SortedSets.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SORTED_SETS_STATIC)


LISTS_STATIC := $(foreach k,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Lists.c)
$(LISTS_STATIC): Lists.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LISTS_STATIC)


ARRAYS_STATIC := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Arrays.c)
$(ARRAYS_STATIC): Arrays.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAYS_STATIC)


PRIORITY_QUEUES_STATIC := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)PriorityQueues.c)
$(PRIORITY_QUEUES_STATIC): PriorityQueues.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(PRIORITY_QUEUES_STATIC)


HEAPS_STATIC := $(foreach k,$(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Heaps.c)
$(HEAPS_STATIC): Heaps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAPS_STATIC)


SEMI_INDIRECT_HEAPS_STATIC := $(foreach k,$(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)SemiIndirectHeaps.c)
$(SEMI_INDIRECT_HEAPS_STATIC): SemiIndirectHeaps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SEMI_INDIRECT_HEAPS_STATIC)


INDIRECT_HEAPS_STATIC := $(foreach k,$(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)IndirectHeaps.c)
$(INDIRECT_HEAPS_STATIC): IndirectHeaps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(INDIRECT_HEAPS_STATIC)


FUNCTIONS_STATIC := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)Functions.c))
$(FUNCTIONS_STATIC): Functions.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(FUNCTIONS_STATIC)


MAPS_STATIC := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)Maps.c))
$(MAPS_STATIC): Maps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(MAPS_STATIC)


SORTED_MAPS_STATIC := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)2$(v)SortedMaps.c))
$(SORTED_MAPS_STATIC): SortedMaps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SORTED_MAPS_STATIC)


COMPARATORS_STATIC := $(foreach k,$(TYPE_NOBOOL_NOREF), $(SOURCEDIR)/$(PACKAGE_$(k))/$(k)Comparators.c)
$(COMPARATORS_STATIC): Comparators.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(COMPARATORS_STATIC)

#
# Fragmented stuff
#

BINIO_FRAGMENTS := $(foreach k,$(TYPE_NOREF), $(SOURCEDIR)/io/$(k)BinIOFragment.h)
$(BINIO_FRAGMENTS): BinIOFragment.drv; ./gencsource.sh $< $@ >$@

CFRAGMENTS += $(BINIO_FRAGMENTS)

$(SOURCEDIR)/io/BinIO.c: BinIO.drv $(BINIO_FRAGMENTS)
	./gencsource.sh BinIO.drv $@ >$@

CSOURCES += $(SOURCEDIR)/io/BinIO.c


TEXTIO_FRAGMENTS := $(foreach k,$(TYPE_NOOBJ), $(SOURCEDIR)/io/$(k)TextIOFragment.h)
$(TEXTIO_FRAGMENTS): TextIOFragment.drv; ./gencsource.sh $< $@ >$@

CFRAGMENTS += $(TEXTIO_FRAGMENTS)

$(SOURCEDIR)/io/TextIO.c: TextIO.drv $(TEXTIO_FRAGMENTS)
	./gencsource.sh TextIO.drv $@ >$@

CSOURCES += $(SOURCEDIR)/io/TextIO.c




JSOURCES = $(CSOURCES:.c=.java) # The list of generated Java source files

SOURCES = \
	$(SOURCEDIR)/Function.java \
	$(SOURCEDIR)/Hash.java \
	$(SOURCEDIR)/HashCommon.java \
	$(SOURCEDIR)/BidirectionalIterator.java \
	$(SOURCEDIR)/Stack.java \
	$(SOURCEDIR)/PriorityQueue.java \
	$(SOURCEDIR)/IndirectPriorityQueue.java \
	$(SOURCEDIR)/IndirectDoublePriorityQueue.java \
	$(SOURCEDIR)/Maps.java \
	$(SOURCEDIR)/Arrays.java \
	$(SOURCEDIR)/PriorityQueues.java \
	$(SOURCEDIR)/IndirectPriorityQueues.java \
	$(SOURCEDIR)/IndirectDoublePriorityQueues.java \
	$(SOURCEDIR)/AbstractPriorityQueue.java \
	$(SOURCEDIR)/AbstractIndirectPriorityQueue.java \
	$(SOURCEDIR)/AbstractIndirectDoublePriorityQueue.java \
	$(SOURCEDIR)/AbstractStack.java \
	$(SOURCEDIR)/io/FastByteArrayInputStream.java \
	$(SOURCEDIR)/io/FastByteArrayOutputStream.java \
	$(SOURCEDIR)/io/FastMultiByteArrayInputStream.java \
	$(SOURCEDIR)/io/FastBufferedInputStream.java \
	$(SOURCEDIR)/io/FastBufferedOutputStream.java \
	$(SOURCEDIR)/io/MeasurableInputStream.java \
	$(SOURCEDIR)/io/RepositionableStream.java # These are True Java Sources instead

# We pass each generated Java source through the preprocessor. TEST compiles in the test code,
# whereas ASSERTS compiles in some assertions (whose testing, of course, must be enabled in the JVM).

$(JSOURCES): %.java: %.c
	gcc -w -I. -ftabstop=4 $(if $(TEST),-DTEST,) $(if $(ASSERTS),-DASSERTS_CODE,) -DASSERTS_VALUE=$(if $(ASSERTS),true,false) -E -C -P $< >$@


clean: 
	@find . -name \*.class -exec rm {} \;  
	@find . -name \*.java~ -exec rm {} \;  
	@find . -name \*.html~ -exec rm {} \;  
	@rm -f $(SOURCEDIR)/{booleans,bytes,shorts,chars,ints,longs,floats,doubles,objects}/*.java
	@rm -f $(SOURCEDIR)/*.{c,h,j} $(SOURCEDIR)/*/*.{c,h,j}
	@rm -fr $(DOCSDIR)/*


sources: $(JSOURCES)

csources: $(CSOURCES)
