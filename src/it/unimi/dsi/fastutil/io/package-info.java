/**
 * Classes and static methods that make object and primitive-type I/O easier and faster.
 *
 * <p>
 * Classes in this package provide very efficient, unsynchronized buffered
 * {@linkplain it.unimi.dsi.fastutil.io.FastBufferedInputStream input} and
 * {@linkplain it.unimi.dsi.fastutil.io.FastBufferedOutputStream output} streams (with support for
 * {@linkplain it.unimi.dsi.fastutil.io.RepositionableStream repositioning}, too) and
 * {@linkplain it.unimi.dsi.fastutil.io.FastByteArrayInputStream fast streams} based on byte arrays.
 *
 * <p>
 * Static containers provide instead a wealth of methods that can be used to
 * {@linkplain BinIO#storeObject(Object, CharSequence) serialize} or
 * {@linkplain BinIO#loadObject(CharSequence) deserialize} very easily objects and
 * {@linkplain BinIO#storeInts(int[], CharSequence) arrays}, even
 * {@linkplain TextIO#storeInts(int[], CharSequence) in text form}.
 */
package it.unimi.dsi.fastutil.io;
