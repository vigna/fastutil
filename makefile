# Driver files for sorted maps.
SORTED_MAP=SortedMap AVLTreeMap RBTreeMap

# Driver files for unsorted maps.
UNSORTED_MAP=Map OpenHashMap LinkedOpenHashMap

# Driver files for abstract maps.
ABSTRACT_MAP=Map

# Driver files for abstract lists.
ABSTRACT_LIST=List

# Driver files for lists.
LIST=List ArrayList

# Driver files for abstract stacks.
ABSTRACT_STACK=Stack

# Driver files for stacks.
STACK=Stack

# Driver files for abstract collections.
ABSTRACT_COLLECTION=Collection Set

# Driver files for sorted sets.
SORTED_COLLECTION=SortedSet AVLTreeSet RBTreeSet

# Driver files for unsorted sets.
UNSORTED_COLLECTION=Collection Set OpenHashSet LinkedOpenHashSet

# Driver files for abstract comparators.
ABSTRACT_COMPARATOR=Comparator

# Driver files for comparators.
COMPARATOR=Comparator

# Driver files for abstract iterators.
ITERATOR=Iterator ListIterator BidirectionalIterator

# Driver files for iterators.
ABSTRACT_ITERATOR=Iterator ListIterator BidirectionalIterator

# Driver files for fragments
FRAGMENT=Iterators-Fragment 

# The types we specialise to these are actual Java types, so references appear here as Object.
TYPE=boolean byte short int long char float double Object Object

# The same types, but in lower case and plural to build package names; singular forms are reserved keywords.
TYPE_PACK=booleans bytes shorts ints longs chars floats doubles objects objects

#  The capitalised types used to build class and method names; booleans object types are not listed.
TYPE_CAP_NOBOOL_NOOBJ=Byte Short Int Long Char Float Double

#  The capitalised types used to build class and method names; object types are not listed.
TYPE_CAP_NOBOOL_NOREF=$(TYPE_CAP_NOBOOL_NOOBJ) Object

#  The capitalised types used to build class and method names; object types are not listed.
TYPE_CAP_NOOBJ=Boolean $(TYPE_CAP_NOBOOL_NOOBJ)

#  The capitalised types used to build class and method names; references are not listed.
TYPE_CAP_NOREF=$(TYPE_CAP_NOOBJ) Object

# The capitalised types used to build class and method names; now references appear as Reference.
TYPE_CAP=$(TYPE_CAP_NOREF) Reference

# The downcased types used to build method names.
TYPE_LC=boolean byte short int long char float double object reference

# The corresponding classes in few cases, there are differences with $TYPE_CAP.
CLASS=Boolean Byte Short Integer Long Character Float Double Object Reference

CSOURCE:=$(foreach m,$(ABSTRACT_MAP), $(foreach k,$(TYPE_CAP), $(foreach v,$(TYPE_CAP), Abstract$(k)2$(v)$(m).c)))
CSOURCE:=$(foreach m,$(UNSORTED_MAP), $(foreach k,$(TYPE_CAP), $(foreach v,$(TYPE_CAP), $(k)2$(v)$(m).c)))
CSOURCE+=$(foreach m,$(SORTED_MAP), $(foreach k,$(TYPE_CAP_NOBOOL_NOREF), $(foreach v,$(TYPE_CAP), $(k)2$(v)$(m).c)))

CSOURCE+=$(foreach s,$(ABSTRACT_COLLECTION), $(foreach k,$(TYPE_CAP), Abstract$(k)$(s).c))
CSOURCE+=$(foreach s,$(UNSORTED_COLLECTION), $(foreach k,$(TYPE_CAP), $(k)$(s).c))
CSOURCE+=$(foreach s,$(SORTED_COLLECTION), $(foreach k,$(TYPE_CAP_NOREF), $(k)$(s).c))

CSOURCE+=$(foreach s,$(ABSTRACT_LIST), $(foreach k,$(TYPE_CAP), Abstract$(k)$(s).c))
CSOURCE+=$(foreach s,$(LIST), $(foreach k,$(TYPE_CAP), $(k)$(s).c))

CSOURCE+=$(foreach s,$(ABSTRACT_STACK), $(foreach k,$(TYPE_CAP_NOOBJ), Abstract$(k)$(s).c))
CSOURCE+=$(foreach s,$(STACK), $(foreach k,$(TYPE_CAP_NOOBJ), $(k)$(s).c))

CSOURCE+=$(foreach i,$(ABSTRACT_ITERATOR), $(foreach k,$(TYPE_CAP_NOBOOL_NOOBJ), Abstract$(k)$(i).c))
CSOURCE+=$(foreach i,$(ITERATOR), $(foreach k,$(TYPE_CAP_NOBOOL_NOOBJ), $(k)$(i).c))

CSOURCE+=$(foreach c,$(ABSTRACT_COMPARATOR), $(foreach k,$(TYPE_CAP_NOBOOL_NOOBJ), Abstract$(k)$(c).c))
CSOURCE+=$(foreach c,$(COMPARATOR), $(foreach k,$(TYPE_CAP_NOBOOL_NOOBJ), $(k)$(c).c))

CSOURCE+=$(foreach f,$(FRAGMENT), $(foreach k,$(TYPE_CAP_NOOBJ), $(k)$(f).h))

prova:
	echo $(CSOURCE)