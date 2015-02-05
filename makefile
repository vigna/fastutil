include build.properties

TAR=tar
PKG_PATH = it/unimi/dsi/fastutil
SOURCEDIR = src/$(PKG_PATH)
GEN_SRCDIR ?= src
export GEN_SRCDIR
DOCSDIR = docs

APIURL=http://java.sun.com/j2se/5.0/docs/api # External URLs in the docs will point here

.SUFFIXES: .java .j

.PHONY: all clean depend install docs jar tar jsources csources dirs

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

#  The capitalized types used to build class and method names; only types for which big structures are built are listed.
TYPE_BIG=Int Long Float Double Object Reference


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
	$(TAR) zcvf fastutil-$(version)-src.tar.gz --owner=0 --group=0 \
		fastutil-$(version)/drv/*.drv \
		fastutil-$(version)/build.xml \
		fastutil-$(version)/ivy.xml \
		fastutil-$(version)/fastutil.bnd \
		fastutil-$(version)/pom.xml \
		fastutil-$(version)/build.properties \
		fastutil-$(version)/gencsource.sh \
		fastutil-$(version)/CHANGES \
		fastutil-$(version)/README \
		fastutil-$(version)/LICENSE-2.0 \
		fastutil-$(version)/makefile \
		$(foreach f, $(SOURCES), fastutil-$(version)/$(f)) \
		fastutil-$(version)/$(SOURCEDIR)/{boolean,byte,char,short,int,long,float,double,object}s/package.html \
		fastutil-$(version)/$(SOURCEDIR)/io/package.html \
		fastutil-$(version)/src/overview.html \
		fastutil-$(version)/src/stylesheet.css \
		$$(find fastutil-$(version)/test -iname \*.java)
	rm fastutil-$(version)

binary:
	make -s clean sources
	ant clean osgi javadoc
	-rm -f fastutil-$(version)
	ln -s . fastutil-$(version)
	cp dist/lib/fastutil-$(version).jar .
	$(TAR) zcvf fastutil-$(version)-bin.tar.gz --owner=0 --group=0 \
		fastutil-$(version)/CHANGES \
		fastutil-$(version)/README \
		fastutil-$(version)/LICENSE-2.0 \
		fastutil-$(version)/docs \
		fastutil-$(version)/fastutil-$(version).jar
	rm fastutil-$(version)

stage:
	(sed -e s/VERSION/$$(grep version build.properties | cut -d= -f2)/ <pom-model.xml >pom.xml)
	(unset LOCAL_IVY_SETTINGS; ant stage)


dirs:
	mkdir -p $(GEN_SRCDIR)/$(PKG_PATH)
	mkdir -p $(GEN_SRCDIR)/$(PKG_PATH)/io
	mkdir -p $(foreach k, $(sort $(TYPE)), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k)))

#
# Interfaces
#

ITERABLES := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Iterable.c)
$(ITERABLES): drv/Iterable.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ITERABLES)

COLLECTIONS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Collection.c)
$(COLLECTIONS): drv/Collection.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(COLLECTIONS)

SETS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Set.c)
$(SETS): drv/Set.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SETS)

HASHES := $(foreach k,$(TYPE_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Hash.c)
$(HASHES): drv/Hash.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HASHES)

SORTED_SETS := $(foreach k,$(TYPE_NOBOOL), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)SortedSet.c)
$(SORTED_SETS): drv/SortedSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SORTED_SETS)

FUNCTIONS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)Function.c))
$(FUNCTIONS): drv/Function.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(FUNCTIONS)

MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)Map.c))
$(MAPS): drv/Map.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(MAPS)

SORTED_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)SortedMap.c))
$(SORTED_MAPS): drv/SortedMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SORTED_MAPS)

LISTS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)List.c)
$(LISTS): drv/List.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LISTS)

STACKS := $(foreach k,$(TYPE_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Stack.c)
$(STACKS): drv/Stack.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(STACKS)

PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)PriorityQueue.c)
$(PRIORITY_QUEUES): drv/PriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(PRIORITY_QUEUES)

INDIRECT_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)IndirectPriorityQueue.c)
$(INDIRECT_PRIORITY_QUEUES): drv/IndirectPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(INDIRECT_PRIORITY_QUEUES)

COMPARATORS := $(foreach k,$(TYPE_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Comparator.c)
$(COMPARATORS): drv/Comparator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(COMPARATORS)

ITERATORS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Iterator.c)
$(ITERATORS): drv/Iterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ITERATORS)

BIDIRECTIONAL_ITERATORS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)BidirectionalIterator.c)
$(BIDIRECTIONAL_ITERATORS): drv/BidirectionalIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(BIDIRECTIONAL_ITERATORS)

LIST_ITERATORS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)ListIterator.c)
$(LIST_ITERATORS): drv/ListIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LIST_ITERATORS)

BIG_LISTS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)BigList.c)
$(BIG_LISTS): drv/BigList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(BIG_LISTS)

BIG_LIST_ITERATORS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)BigListIterator.c)
$(BIG_LIST_ITERATORS): drv/BigListIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(BIG_LIST_ITERATORS)

#
# Abstract implementations
#

ABSTRACT_COLLECTIONS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)Collection.c)
$(ABSTRACT_COLLECTIONS): drv/AbstractCollection.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_COLLECTIONS)

ABSTRACT_SETS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)Set.c)
$(ABSTRACT_SETS): drv/AbstractSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_SETS)

ABSTRACT_SORTED_SETS := $(foreach k,$(TYPE_NOBOOL), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)SortedSet.c)
$(ABSTRACT_SORTED_SETS): drv/AbstractSortedSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_SORTED_SETS)

ABSTRACT_FUNCTIONS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)2$(v)Function.c))
$(ABSTRACT_FUNCTIONS): drv/AbstractFunction.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_FUNCTIONS)

ABSTRACT_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)2$(v)Map.c))
$(ABSTRACT_MAPS): drv/AbstractMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_MAPS)

ABSTRACT_SORTED_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)2$(v)SortedMap.c))
$(ABSTRACT_SORTED_MAPS): drv/AbstractSortedMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_SORTED_MAPS)

ABSTRACT_LISTS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)List.c)
$(ABSTRACT_LISTS): drv/AbstractList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_LISTS)

ABSTRACT_BIG_LISTS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)BigList.c)
$(ABSTRACT_BIG_LISTS): drv/AbstractBigList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_BIG_LISTS)

ABSTRACT_STACKS := $(foreach k,$(TYPE_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)Stack.c)
$(ABSTRACT_STACKS): drv/AbstractStack.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_STACKS)

ABSTRACT_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)PriorityQueue.c)
$(ABSTRACT_PRIORITY_QUEUES): drv/AbstractPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_PRIORITY_QUEUES)

ABSTRACT_COMPARATORS := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)Comparator.c)
$(ABSTRACT_COMPARATORS): drv/AbstractComparator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_COMPARATORS)

ABSTRACT_ITERATORS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)Iterator.c)
$(ABSTRACT_ITERATORS): drv/AbstractIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_ITERATORS)

ABSTRACT_BIDIRECTIONAL_ITERATORS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)BidirectionalIterator.c)
$(ABSTRACT_BIDIRECTIONAL_ITERATORS): drv/AbstractBidirectionalIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_BIDIRECTIONAL_ITERATORS)

ABSTRACT_LIST_ITERATORS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)ListIterator.c)
$(ABSTRACT_LIST_ITERATORS): drv/AbstractListIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_LIST_ITERATORS)

ABSTRACT_BIG_LIST_ITERATORS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Abstract$(k)BigListIterator.c)
$(ABSTRACT_BIG_LIST_ITERATORS): drv/AbstractBigListIterator.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ABSTRACT_BIG_LIST_ITERATORS)

#
# Concrete implementations
#

OPEN_HASH_SETS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)OpenHashSet.c)
$(OPEN_HASH_SETS): drv/OpenHashSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_HASH_SETS)

OPEN_HASH_BIG_SETS := $(foreach k,$(TYPE_BIG), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)OpenHashBigSet.c)
$(OPEN_HASH_BIG_SETS): drv/OpenHashBigSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_HASH_BIG_SETS)

LINKED_OPEN_HASH_SETS := $(foreach k,$(TYPE_NOBOOL), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)LinkedOpenHashSet.c)
$(LINKED_OPEN_HASH_SETS): drv/LinkedOpenHashSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LINKED_OPEN_HASH_SETS)

OPEN_CUSTOM_HASH_SETS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)OpenCustomHashSet.c)
$(OPEN_CUSTOM_HASH_SETS): drv/OpenCustomHashSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_CUSTOM_HASH_SETS)

LINKED_OPEN_CUSTOM_HASH_SETS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)LinkedOpenCustomHashSet.c)
$(LINKED_OPEN_CUSTOM_HASH_SETS): drv/LinkedOpenCustomHashSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LINKED_OPEN_CUSTOM_HASH_SETS)

ARRAY_SETS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)ArraySet.c)
$(ARRAY_SETS): drv/ArraySet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_SETS)

AVL_TREE_SETS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)AVLTreeSet.c)
$(AVL_TREE_SETS): drv/AVLTreeSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(AVL_TREE_SETS)

RB_TREE_SETS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)RBTreeSet.c)
$(RB_TREE_SETS): drv/RBTreeSet.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(RB_TREE_SETS)

OPEN_HASH_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)OpenHashMap.c))
$(OPEN_HASH_MAPS): drv/OpenHashMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_HASH_MAPS)

LINKED_OPEN_HASH_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)LinkedOpenHashMap.c))
$(LINKED_OPEN_HASH_MAPS): drv/LinkedOpenHashMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LINKED_OPEN_HASH_MAPS)

OPEN_CUSTOM_HASH_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)OpenCustomHashMap.c))
$(OPEN_CUSTOM_HASH_MAPS): drv/OpenCustomHashMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(OPEN_CUSTOM_HASH_MAPS)

LINKED_OPEN_CUSTOM_HASH_MAPS := $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/objects/Object2$(v)LinkedOpenCustomHashMap.c)
$(LINKED_OPEN_CUSTOM_HASH_MAPS): drv/LinkedOpenCustomHashMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LINKED_OPEN_CUSTOM_HASH_MAPS)

#STRIPED_OPEN_HASH_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/Striped$(k)2$(v)OpenHashMap.c))
#$(STRIPED_OPEN_HASH_MAPS): drv/StripedOpenHashMap.drv; ./gencsource.sh $< $@ >$@

#CSOURCES += $(STRIPED_OPEN_HASH_MAPS)

ARRAY_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)ArrayMap.c))
$(ARRAY_MAPS): drv/ArrayMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_MAPS)

AVL_TREE_MAPS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)AVLTreeMap.c))
$(AVL_TREE_MAPS): drv/AVLTreeMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(AVL_TREE_MAPS)

RB_TREE_MAPS := $(foreach k,$(TYPE_NOBOOL_NOREF), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)RBTreeMap.c))
$(RB_TREE_MAPS): drv/RBTreeMap.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(RB_TREE_MAPS)

ARRAY_LISTS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)ArrayList.c)
$(ARRAY_LISTS): drv/ArrayList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_LISTS)

BIG_ARRAY_BIG_LISTS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)BigArrayBigList.c)
$(BIG_ARRAY_BIG_LISTS): drv/BigArrayBigList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(BIG_ARRAY_BIG_LISTS)

FRONT_CODED_LISTS := $(foreach k, Byte Short Int Long Char, $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)ArrayFrontCodedList.c)
$(FRONT_CODED_LISTS): drv/ArrayFrontCodedList.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(FRONT_CODED_LISTS)

HEAP_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)HeapPriorityQueue.c)
$(HEAP_PRIORITY_QUEUES): drv/HeapPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAP_PRIORITY_QUEUES)

ARRAY_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)ArrayPriorityQueue.c)
$(ARRAY_PRIORITY_QUEUES): drv/ArrayPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_PRIORITY_QUEUES)

ARRAY_FIFO_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)ArrayFIFOQueue.c)
$(ARRAY_FIFO_QUEUES): drv/ArrayFIFOQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_FIFO_QUEUES)

HEAP_SEMI_INDIRECT_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)HeapSemiIndirectPriorityQueue.c)
$(HEAP_SEMI_INDIRECT_PRIORITY_QUEUES): drv/HeapSemiIndirectPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAP_SEMI_INDIRECT_PRIORITY_QUEUES)

HEAP_INDIRECT_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)HeapIndirectPriorityQueue.c)
$(HEAP_INDIRECT_PRIORITY_QUEUES): drv/HeapIndirectPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAP_INDIRECT_PRIORITY_QUEUES)

ARRAY_INDIRECT_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)ArrayIndirectPriorityQueue.c)
$(ARRAY_INDIRECT_PRIORITY_QUEUES): drv/ArrayIndirectPriorityQueue.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAY_INDIRECT_PRIORITY_QUEUES)


#
# Static containers
#

ITERATORS_STATIC := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Iterators.c)
$(ITERATORS_STATIC): drv/Iterators.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ITERATORS_STATIC)


BIG_LIST_ITERATORS_STATIC := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)BigListIterators.c)
$(BIG_LIST_ITERATORS_STATIC): drv/BigListIterators.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(BIG_LIST_ITERATORS_STATIC)


COLLECTIONS_STATIC := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Collections.c)
$(COLLECTIONS_STATIC): drv/Collections.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(COLLECTIONS_STATIC)


SETS_STATIC := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Sets.c)
$(SETS_STATIC): drv/Sets.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SETS_STATIC)


SORTED_SETS_STATIC := $(foreach k,$(TYPE_NOBOOL), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)SortedSets.c)
$(SORTED_SETS_STATIC): drv/SortedSets.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SORTED_SETS_STATIC)


LISTS_STATIC := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Lists.c)
$(LISTS_STATIC): drv/Lists.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(LISTS_STATIC)


BIG_LISTS_STATIC := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)BigLists.c)
$(BIG_LISTS_STATIC): drv/BigLists.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(BIG_LISTS_STATIC)


ARRAYS_STATIC := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Arrays.c)
$(ARRAYS_STATIC): drv/Arrays.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(ARRAYS_STATIC)


BIG_ARRAYS_STATIC := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)BigArrays.c)
$(BIG_ARRAYS_STATIC): drv/BigArrays.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(BIG_ARRAYS_STATIC)


PRIORITY_QUEUES_STATIC := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)PriorityQueues.c)
$(PRIORITY_QUEUES_STATIC): drv/PriorityQueues.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(PRIORITY_QUEUES_STATIC)


HEAPS_STATIC := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Heaps.c)
$(HEAPS_STATIC): drv/Heaps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(HEAPS_STATIC)


SEMI_INDIRECT_HEAPS_STATIC := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)SemiIndirectHeaps.c)
$(SEMI_INDIRECT_HEAPS_STATIC): drv/SemiIndirectHeaps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SEMI_INDIRECT_HEAPS_STATIC)


INDIRECT_HEAPS_STATIC := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)IndirectHeaps.c)
$(INDIRECT_HEAPS_STATIC): drv/IndirectHeaps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(INDIRECT_HEAPS_STATIC)


FUNCTIONS_STATIC := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)Functions.c))
$(FUNCTIONS_STATIC): drv/Functions.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(FUNCTIONS_STATIC)


MAPS_STATIC := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)Maps.c))
$(MAPS_STATIC): drv/Maps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(MAPS_STATIC)


SORTED_MAPS_STATIC := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)SortedMaps.c))
$(SORTED_MAPS_STATIC): drv/SortedMaps.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(SORTED_MAPS_STATIC)


COMPARATORS_STATIC := $(foreach k,$(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)Comparators.c)
$(COMPARATORS_STATIC): drv/Comparators.drv; ./gencsource.sh $< $@ >$@

CSOURCES += $(COMPARATORS_STATIC)

#
# Fragmented stuff
#

BINIO_FRAGMENTS := $(foreach k,$(TYPE_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/io/$(k)BinIOFragment.h)
$(BINIO_FRAGMENTS): drv/BinIOFragment.drv; ./gencsource.sh $< $@ >$@

CFRAGMENTS += $(BINIO_FRAGMENTS)

$(GEN_SRCDIR)/$(PKG_PATH)/io/BinIO.c: drv/BinIO.drv $(BINIO_FRAGMENTS)
	./gencsource.sh drv/BinIO.drv $@ >$@

CSOURCES += $(GEN_SRCDIR)/$(PKG_PATH)/io/BinIO.c


TEXTIO_FRAGMENTS := $(foreach k,$(TYPE_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/io/$(k)TextIOFragment.h)
$(TEXTIO_FRAGMENTS): drv/TextIOFragment.drv; ./gencsource.sh $< $@ >$@

CFRAGMENTS += $(TEXTIO_FRAGMENTS)

$(GEN_SRCDIR)/$(PKG_PATH)/io/TextIO.c: drv/TextIO.drv $(TEXTIO_FRAGMENTS)
	./gencsource.sh drv/TextIO.drv $@ >$@

CSOURCES += $(GEN_SRCDIR)/$(PKG_PATH)/io/TextIO.c

#
# Old sources, generated only with the old target
#

OPEN_DOUBLE_HASH_SETS := $(foreach k,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)OpenDoubleHashSet.c)
$(OPEN_DOUBLE_HASH_SETS): drv/OpenDoubleHashSet.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(OPEN_DOUBLE_HASH_SETS)

LINKED_OPEN_DOUBLE_HASH_SETS := $(foreach k,$(TYPE_NOBOOL), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)LinkedOpenDoubleHashSet.c)
$(LINKED_OPEN_DOUBLE_HASH_SETS): drv/LinkedOpenDoubleHashSet.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(LINKED_OPEN_DOUBLE_HASH_SETS)

OPEN_DOUBLE_CUSTOM_HASH_SETS := $(GEN_SRCDIR)/$(PKG_PATH)/objects/ObjectOpenCustomDoubleHashSet.c
$(OPEN_DOUBLE_CUSTOM_HASH_SETS): drv/OpenCustomDoubleHashSet.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(OPEN_DOUBLE_CUSTOM_HASH_SETS)

LINKED_OPEN_DOUBLE_CUSTOM_HASH_SETS := $(GEN_SRCDIR)/$(PKG_PATH)/objects/ObjectLinkedOpenCustomDoubleHashSet.c
$(LINKED_OPEN_DOUBLE_CUSTOM_HASH_SETS): drv/LinkedOpenCustomDoubleHashSet.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(LINKED_OPEN_CUSTOM_DOUBLE_HASH_SETS)

OPEN_DOUBLE_HASH_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)OpenDoubleHashMap.c))
$(OPEN_DOUBLE_HASH_MAPS): drv/OpenDoubleHashMap.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(OPEN_DOUBLE_HASH_MAPS)

LINKED_OPEN_DOUBLE_HASH_MAPS := $(foreach k,$(TYPE_NOBOOL), $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)2$(v)LinkedOpenDoubleHashMap.c))
$(LINKED_OPEN_DOUBLE_HASH_MAPS): drv/LinkedOpenDoubleHashMap.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(LINKED_OPEN_DOUBLE_HASH_MAPS)

OPEN_CUSTOM_DOUBLE_HASH_MAPS := $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/objects/Object2$(v)OpenCustomDoubleHashMap.c)
$(OPEN_CUSTOM_DOUBLE_HASH_MAPS): drv/OpenCustomDoubleHashMap.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(OPEN_CUSTOM_DOUBLE_HASH_MAPS)

LINKED_OPEN_CUSTOM_DOUBLE_HASH_MAPS := $(foreach v,$(TYPE), $(GEN_SRCDIR)/$(PKG_PATH)/objects/Object2$(v)LinkedOpenCustomDoubleHashMap.c)
$(LINKED_OPEN_CUSTOM_DOUBLE_HASH_MAPS): drv/LinkedOpenCustomDoubleHashMap.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(LINKED_OPEN_CUSTOM_DOUBLE_HASH_MAPS)

INDIRECT_DOUBLE_PRIORITY_QUEUES := $(foreach k,$(TYPE_NOBOOL_NOOBJ), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)IndirectDoublePriorityQueue.c)
$(INDIRECT_DOUBLE_PRIORITY_QUEUES): drv/IndirectDoublePriorityQueue.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(INDIRECT_DOUBLE_PRIORITY_QUEUES)

HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)HeapSesquiIndirectDoublePriorityQueue.c)
$(HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUES): drv/HeapSesquiIndirectDoublePriorityQueue.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUES)

HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)HeapIndirectDoublePriorityQueue.c)
$(HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUES): drv/HeapIndirectDoublePriorityQueue.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUES)

ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUES := $(foreach k, $(TYPE_NOBOOL_NOREF), $(GEN_SRCDIR)/$(PKG_PATH)/$(PACKAGE_$(k))/$(k)ArrayIndirectDoublePriorityQueue.c)
$(ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUES): drv/ArrayIndirectDoublePriorityQueue.drv; ./gencsource.sh $< $@ >$@

OLDCSOURCES += $(ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUES)


JSOURCES = $(CSOURCES:.c=.java) # The list of generated Java source files
OLDJSOURCES = $(OLDCSOURCES:.c=.java) 


SOURCES = \
	$(SOURCEDIR)/Function.java \
	$(SOURCEDIR)/Hash.java \
	$(SOURCEDIR)/HashCommon.java \
	$(SOURCEDIR)/BidirectionalIterator.java \
	$(SOURCEDIR)/Stack.java \
	$(SOURCEDIR)/BigList.java \
	$(SOURCEDIR)/BigListIterator.java \
	$(SOURCEDIR)/BigArrays.java \
	$(SOURCEDIR)/PriorityQueue.java \
	$(SOURCEDIR)/IndirectPriorityQueue.java \
	$(SOURCEDIR)/Maps.java \
	$(SOURCEDIR)/Arrays.java \
	$(SOURCEDIR)/Swapper.java \
	$(SOURCEDIR)/BigSwapper.java \
	$(SOURCEDIR)/Size64.java \
	$(SOURCEDIR)/PriorityQueues.java \
	$(SOURCEDIR)/IndirectPriorityQueues.java \
	$(SOURCEDIR)/AbstractPriorityQueue.java \
	$(SOURCEDIR)/AbstractIndirectPriorityQueue.java \
	$(SOURCEDIR)/AbstractStack.java \
	$(SOURCEDIR)/io/FastByteArrayInputStream.java \
	$(SOURCEDIR)/io/FastByteArrayOutputStream.java \
	$(SOURCEDIR)/io/FastMultiByteArrayInputStream.java \
	$(SOURCEDIR)/io/FastBufferedInputStream.java \
	$(SOURCEDIR)/io/FastBufferedOutputStream.java \
	$(SOURCEDIR)/io/InspectableFileCachedInputStream.java \
	$(SOURCEDIR)/io/MeasurableInputStream.java \
	$(SOURCEDIR)/io/MeasurableOutputStream.java \
	$(SOURCEDIR)/io/MeasurableStream.java \
	$(SOURCEDIR)/io/RepositionableStream.java # These are True Java Sources instead

OLDSOURCES = \
	$(SOURCEDIR)/IndirectDoublePriorityQueue.java \
	$(SOURCEDIR)/IndirectDoublePriorityQueues.java \
	$(SOURCEDIR)/AbstractIndirectDoublePriorityQueue.java


# We pass each generated Java source through the preprocessor. TEST compiles in the test code,
# whereas ASSERTS compiles in some assertions (whose testing, of course, must be enabled in the JVM).

$(JSOURCES) $(OLDJSOURCES): %.java: %.c
	gcc -w -I. -ftabstop=4 $(if $(TEST),-DTEST,) $(if $(ASSERTS),-DASSERTS_CODE,) -DASSERTS_VALUE=$(if $(ASSERTS),true,false) -E -C -P $< >$@


clean: 
	@find build . -name \*.class -exec rm {} \;  
	@find . -name \*.java~ -exec rm {} \;  
	@find . -name \*.html~ -exec rm {} \;  
	@rm -f $(GEN_SRCDIR)/$(PKG_PATH)/{booleans,bytes,shorts,chars,ints,longs,floats,doubles,objects}/*.java
	@rm -f $(GEN_SRCDIR)/$(PKG_PATH)/*.{c,h,j} $(GEN_SRCDIR)/$(PKG_PATH)/*/*.{c,h,j}
	@rm -fr $(DOCSDIR)/*


sources: $(JSOURCES)

oldsources: $(OLDJSOURCES)

csources: $(CSOURCES)

oldcsources: $(OLDCSOURCES)
