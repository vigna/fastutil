Criteria for the revision of fastutil's code base
=================================================

General rules
-------------

	* All methods overriding some other method must sport
	  the @Override annotation.

	* All implementation of deprecated methods must
	  propagate the deprecation annotation and documentation.

	* Annotation must be in the order @Deprecated, @Override,
	  @SuppressWarnings.

Type-specific methods
---------------------

For each new type-specific method, the following must happen:

	* The method documentation must contain only a short
	  title (more or less copied from the non-specific JDK
	  documentation) and a `@see` pointing at the corresponding 
	  non-type-specific method.

	* The corresponding non-type-specific method must be
	  redeclared as @Deprecated with the following documentation
	  and annotation.

```java
	 /** {@inheritDoc}
	  * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
```

	* Ideally, one should provide an abstract class which implements
	  the non-type-specific methods by delegating the argument
	  to the type-specific method, possibly treating separately
	  the `null` case. Documentation and annotation should be

```java
	 /** {@inheritDoc}
	  * <p>Delegates to the corresponding type-specific method.
	  * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
```

	* Ideally, the abstract class should implement the type-specific
	  methods following the example of the Java Collections Framework.
	  For example,
```java
	 /** {@inheritDoc}
	  * <p>This implementation just throws an {@link UnsupportedOperationException}. */
	 @Override
```

Obsolete type-specific methods
------------------------------

Obsolete methods such as `intIterator()` should be marked in interfaces
as deprecated and documented with a pointer
```java
	/** Returns a type-specific iterator on this elements of this collection.
	 *
	 * @see #iterator()
	 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #iterator()}.
	 */
	@Deprecated
```

Whenever these methods are implemented, the deprecation must be propagated.

```java
	/**  @{inheritDoc}
	 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #iterator()}. */
	@Deprecated
	@Override
```

Unnecessary methods
-------------------

Over the years, a number of duplicate methods have been implemented
(e.g., standard versions of type-specific methods). For clarity
and simplicity, no unnecessary method should be implemented. In doubt,
follow the example of the JDK.
