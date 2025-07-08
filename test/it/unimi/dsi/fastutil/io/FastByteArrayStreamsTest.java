package it.unimi.dsi.fastutil.io;

import it.unimi.dsi.fastutil.bytes.ByteArrays;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UTFDataFormatException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 @see FastByteArrayOutputStream
 @see FastByteArrayInputStream
 @see DataOutputStream
 @see ByteArrayOutputStream
 @see ByteArrayInputStream
 */
@SuppressWarnings({"resource", "IOResourceOpenedButNotSafelyClosed"})
public class FastByteArrayStreamsTest {
	/// todo put into {@link it.unimi.dsi.fastutil.bytes.ByteArrays} ?
	/// @see java.util.HexFormat
	private static final short[] HEX_TABLE = new short[256];
	static {
		final byte[] hex = "0123456789abcdef".getBytes(StandardCharsets.ISO_8859_1);
		for (int i = 0; i < 256; i ++){
			HEX_TABLE[i] = (short)((hex[i >>> 4 & 0xF] << 8) | (hex[i & 0xF] & 0xFF));
		}
	}

	@SuppressWarnings("deprecation")
	public static String toHexString (byte[] buffer, int fromIndex, int length) {
		if (length <= 0){ return ""; }
		length = Math.min(length, buffer.length - fromIndex);

		byte[] buf = new byte[length << 1];// buffer.length * 2

		for (int dstIdx = 0, endIndex = fromIndex + length; fromIndex < endIndex; fromIndex++){
			int nextByte = buffer[fromIndex] & 0xFF;
			short byteAsTwoHex = HEX_TABLE[nextByte];
			buf[dstIdx++] = (byte)(byteAsTwoHex >> 8);
			buf[dstIdx++] = (byte)(byteAsTwoHex & 0xFF);
		}

		return new String(buf, 0/*hi*/, 0, buf.length);
	}

	public static String toHexString (byte[] buffer) {
		return toHexString(buffer, 0, buffer.length);
	}

	@Test
	public void testHex () {
		assertEquals("", toHexString(null, -1, -1));
		assertEquals("", toHexString(new byte[]{0, 1, 2, -1}, -1, -1));
		assertEquals("000102ff", toHexString(new byte[]{0, 1, 2, -1}, 0, 100));
		assertEquals("0102ff", toHexString(new byte[]{0, 1, 2, -1}, 1, 100));
		assertEquals("0102", toHexString(new byte[]{0, 1, 2, -1}, 1, 2));
	}

	@FunctionalInterface
	interface ThrowingConsumer<T> {
		void accept (T value) throws Throwable;
	}

	static final class OpenDataOutputStream extends DataOutputStream {
		public OpenDataOutputStream (){ super(new ByteArrayOutputStream()); }
		public ByteArrayOutputStream out (){ return (ByteArrayOutputStream) out; }
	}

	static class OpenByteArrayInputStream extends ByteArrayInputStream {
		public OpenByteArrayInputStream (){ super(ByteArrays.EMPTY_ARRAY); }
		public void setArray (byte[] array) {
			this.buf = array;
			this.pos = 0;
			this.count = buf.length;
		}
	}

	static final class OpenDataInputStream extends DataInputStream {
		public OpenDataInputStream (){ super(new OpenByteArrayInputStream()); }
		public OpenByteArrayInputStream in (){ return (OpenByteArrayInputStream) in; }
	}

	private void reset (DataOutput w) {
		if (w instanceof FastByteArrayOutputStream x){
			x.reset();
		} else {
			((OpenDataOutputStream) w).out().reset();
		}
	}

	private byte[] toByteArray (DataOutput w) {
		if (w instanceof FastByteArrayOutputStream x){
			return x.toByteArray();
		} else {
			return ((OpenDataOutputStream) w).out().toByteArray();
		}
	}

	private void setArray (DataInput r, byte[] array) {
		if (r instanceof FastByteArrayInputStream x){
			x.array = array;
			x.offset = 0;
			x.position(0);
			x.length = array.length;

		} else {
			((OpenDataInputStream) r).in().setArray(array);
		}
	}


	/// us→us, us→jdk, jdk→us, jdk→jdk (to be 100% sure)
	void x (String hex, ThrowingConsumer<DataOutput> write, ThrowingConsumer<DataInput> readAndVerify) {
		hex = hex.replaceAll("[\\s_]+", "").trim();
		try {
			for (DataOutput w : new DataOutput[]{new FastByteArrayOutputStream(), new OpenDataOutputStream()}){
				for (DataInput r : new DataInput[]{new FastByteArrayInputStream(ByteArrays.EMPTY_ARRAY), new OpenDataInputStream()}){
					reset(w);

					write.accept(w);//1

					byte[] array = toByteArray(w);
					assertEquals(hex, toHexString(array));
					assertEquals(hex.length(), array.length * 2);

					setArray(r, array);

					readAndVerify.accept(r);//2
				}
			}
		} catch (Throwable e){
			throw new AssertionError(e);
		}
	}

	@Test
	public void testEOF () throws UTFDataFormatException {
		FastByteArrayInputStream r = new FastByteArrayInputStream(ByteArrays.EMPTY_ARRAY);
		assertEquals(0, r.available());
		assertEquals(-1, r.read());
		assertEquals(-1, r.readByte());
		assertEquals(0xff, r.readUnsignedByte());
		assertTrue(r.readBoolean());//?
		assertEquals(-1, r.readShort());
		assertEquals(0xFFFF, r.readUnsignedShort());
		assertEquals(0xFFFF, r.readChar());
		assertEquals(-1, r.readInt());
		assertEquals(-1, r.readLong());
		assertEquals(Float.NaN, r.readFloat(), 0.001);
		assertEquals(Double.NaN, r.readDouble(), 0.001);
		assertNull(r.readLine());
		assertNull(r.readUTF());
	}

	@Test
	public void testWriteDef () {
		x("00 7f ff_ff",
				w->{
					w.write(0);
					w.write(0x7f);
					w.write(-1);
					w.write(-1);
				},
				r -> {
					assertEquals(0, r.readUnsignedByte());
					assertEquals(0x7f, r.readUnsignedByte());
					assertEquals(0xff, r.readUnsignedByte());
					assertEquals(-1, r.readByte());
				}
		);
	}

	@Test
	public void testWriteArray1 () {
		x("00 7f ff_ff",
				w->{
					w.write(new byte[]{0, 0x7f, -1, -1}, 0, 4);
				},
				r -> {
					byte[] b = new byte[4];
					r.readFully(b, 0, 0);
					r.readFully(b, 0, 4);
					assertEquals(0, b[0]);
					assertEquals(0x7f, b[1]);
					assertEquals(-1, b[2]);
					assertEquals(-1, b[3]);
				}
		);
	}

	@Test
	public void testWriteArray2 () {
		x("00 7f ff_ff",
				w->{
					w.write(new byte[]{0, 0x7f, -1, -1});
				},
				r -> {
					byte[] b = new byte[4];
					r.readFully(b);
					assertEquals(0, b[0]);
					assertEquals(0x7f, b[1]);
					assertEquals(-1, b[2]);
					assertEquals(-1, b[3]);
				}
		);
	}

	@Test
	public void testWriteBoolean () {
		x("01 00 01",
				w->{
					w.writeBoolean(true);
					w.writeBoolean(false);
					w.writeBoolean(true);
				},
				r -> {
					assertTrue(r.readBoolean());
					assertFalse(r.readBoolean());
					assertTrue(r.readBoolean());
				}
		);
	}

	@Test
	public void testWriteByte () {
		x("00 7f ff_ff",
				w->{
					w.writeByte(0);
					w.writeByte(0x7f);
					w.writeByte(-1);
					w.writeByte(-1);
				},
				r -> {
					assertEquals(0, r.readUnsignedByte());
					assertEquals(0x7f, r.readUnsignedByte());
					assertEquals(0xff, r.readUnsignedByte());
					assertEquals(-1, r.readByte());
				}
		);
	}

	@Test
	public void testWriteShort () {
		x("0000 8000 1234  7fff 8000 7f56",
				w->{
					w.writeShort(0);
					w.writeShort(Short.MIN_VALUE);
					w.writeShort(0x1234);

					w.writeShort(Short.MAX_VALUE);
					w.writeShort(Short.MIN_VALUE);
					w.writeShort(0x7f56);
				},
				r -> {
					assertEquals(0, r.readUnsignedShort());
					assertEquals(0x8000, r.readUnsignedShort());
					assertEquals(0x1234, r.readUnsignedShort());

					assertEquals(Short.MAX_VALUE, r.readShort());
					assertEquals(Short.MIN_VALUE, r.readShort());
					assertEquals(0x7f56, r.readShort());
				}
		);
	}

	@Test
	public void testWriteChar () {
		x("0000 8000 1234  7fff 8000 7f56",
				w->{
					w.writeChar(0);
					w.writeChar(Short.MIN_VALUE);
					w.writeChar(0x1234);

					w.writeChar(Short.MAX_VALUE);
					w.writeChar(Short.MIN_VALUE);
					w.writeChar(0x7f56);
				},
				r -> {
					assertEquals(0, r.readChar());
					assertEquals(0x8000, r.readChar());
					assertEquals(0x1234, r.readChar());

					assertEquals(Short.MAX_VALUE, r.readChar());
					assertEquals(0x8000, r.readChar());
					assertEquals(0x7f56, r.readChar());
				}
		);
	}

	@Test
	public void testWriteInt () {
		x("0000_0000 0012_3456 ffff_8000   8000_0000 0000_7fff  7fff_ffff",
				w->{
					w.writeInt(0);
					w.writeInt(0x123456);
					w.writeInt(Short.MIN_VALUE);

					w.writeInt(Integer.MIN_VALUE);
					w.writeInt(Short.MAX_VALUE);
					w.writeInt(Integer.MAX_VALUE);
				},
				r -> {
					assertEquals(0, r.readInt());
					assertEquals(0x123456, r.readInt());
					assertEquals(Short.MIN_VALUE, r.readInt());

					assertEquals(Integer.MIN_VALUE, r.readInt());
					assertEquals(Short.MAX_VALUE, r.readInt());
					assertEquals(Integer.MAX_VALUE, r.readInt());
				}
		);
	}

	@Test
	public void testWriteLong () {
		x("0000_0000_0000_0000 0000_0000_0012_3456 ffff_ffff_ffff_8000   ffff_ffff_8000_0000 0000_0000_0000_7fff 0000_0000_7fff_ffff   8000_0000_0000_0000 7fff_ffff_ffff_ffff",
				w->{
					w.writeLong(0);
					w.writeLong(0x123456);
					w.writeLong(Short.MIN_VALUE);

					w.writeLong(Integer.MIN_VALUE);
					w.writeLong(Short.MAX_VALUE);
					w.writeLong(Integer.MAX_VALUE);

					w.writeLong(Long.MIN_VALUE);
					w.writeLong(Long.MAX_VALUE);
				},
				r -> {
					assertEquals(0, r.readLong());
					assertEquals(0x123456, r.readLong());
					assertEquals(Short.MIN_VALUE, r.readLong());

					assertEquals(Integer.MIN_VALUE, r.readLong());
					assertEquals(Short.MAX_VALUE, r.readLong());
					assertEquals(Integer.MAX_VALUE, r.readLong());

					assertEquals(Long.MIN_VALUE, r.readLong());
					assertEquals(Long.MAX_VALUE, r.readLong());
				}
		);
	}

	@Test
	public void testWriteFloat () {
		x("00000000 4991a2b0 c7000000 cf000000 46fffe00 4f000000 40490fdb",
				w->{
					w.writeFloat(0);
					w.writeFloat(0x123456);
					w.writeFloat(Short.MIN_VALUE);

					w.writeFloat(Integer.MIN_VALUE);
					w.writeFloat(Short.MAX_VALUE);
					w.writeFloat(Integer.MAX_VALUE);

					w.writeFloat(3.14159265f);
				},
				r -> {
					assertEquals(0, r.readFloat(), 0.001);
					assertEquals(0x123456, r.readFloat(), 0.001);
					assertEquals(Short.MIN_VALUE, r.readFloat(), 0.001);

					assertEquals(Integer.MIN_VALUE, r.readFloat(), 0.001);
					assertEquals(Short.MAX_VALUE, r.readFloat(), 0.001);
					assertEquals(Integer.MAX_VALUE, r.readFloat(), 10);

					assertEquals(3.14159265f, r.readFloat(), 0.001);
				}
		);
	}

	@Test
	public void testWriteDouble () {
		x("00000000000000004132345600000000c0e0000000000000c1e000000000000040dfffc00000000041dfffffffc00000c3e000000000000043e0000000000000400921fb54442d18",
				w->{
					w.writeDouble(0);
					w.writeDouble(0x123456);
					w.writeDouble(Short.MIN_VALUE);

					w.writeDouble(Integer.MIN_VALUE);
					w.writeDouble(Short.MAX_VALUE);
					w.writeDouble(Integer.MAX_VALUE);

					w.writeDouble(Long.MIN_VALUE);
					w.writeDouble(Long.MAX_VALUE);

					w.writeDouble(Math.PI);
				},
				r -> {
					assertEquals(0, r.readDouble(), 0.001);
					assertEquals(0x123456, r.readDouble(), 0.001);
					assertEquals(Short.MIN_VALUE, r.readDouble(), 0.001);

					assertEquals(Integer.MIN_VALUE, r.readDouble(), 0.001);
					assertEquals(Short.MAX_VALUE, r.readDouble(), 0.001);
					assertEquals(Integer.MAX_VALUE, r.readDouble(), 0.001);

					assertEquals(Long.MIN_VALUE, r.readDouble(), 0.001);
					assertEquals(Long.MAX_VALUE, r.readDouble(), 0.001);

					assertEquals(Math.PI, r.readDouble(), 0.001);
				}
		);
	}

	@Test
	public void testWriteBytes () {
		String s = "ISO_8859_1 is equal to Unicode.left(256)!";
		x("4049534f5f383835395f3120697320657175616c20746f20556e69636f64652e6c6566742832353629210d0a",
				w->{
					w.writeBytes('@'+ s+ "\r\n");
				},
				r -> {
					assertEquals('@', r.readByte());
					String z = r.readLine();
					assertEquals(s, z);
				}
		);
	}

	@Test
	public void testWriteChars () {
		String s = "Chars in UTF-16BE 🚀💯!";
		x("00400043006800610072007300200069006e0020005500540046002d00310036004200450020d83dde80d83ddcaf0021000000170043006800610072007300200069006e0020005500540046002d00310036004200450020d83dde80d83ddcaf0021",
				w->{
					w.writeChars('@'+ s+ "\0");// c string
					w.writeShort(s.length());
					w.writeChars(s);
				},
				r -> {
					assertEquals('@', r.readChar());

					StringBuilder sb = new StringBuilder();
					char c;
					while ((c = r.readChar())!=0)
						sb.append(c);
					assertEquals(s, sb.toString());

					int len = r.readUnsignedShort();
					sb.setLength(0);
					while (len-- >0)
						sb.append(r.readChar());
					assertEquals(s, sb.toString());
				}
		);
	}

	@Test
	public void testWriteUTF8 () throws UTFDataFormatException {
		String s = "\0\r\nChars in UTF-8 🚀💯!";
		assertEquals(23, s.length());
		assertEquals(27, s.getBytes(StandardCharsets.UTF_8).length);
		x("0020c0800d0a436861727320696e205554462d3820eda0bdedba80eda0bdedb2af21",
				w->{
					w.writeUTF(s);
				},
				r -> {
					assertEquals(s, r.readUTF());
				}
		);

		FastByteArrayOutputStream w = new FastByteArrayOutputStream();
		w.writeUTF(s.repeat(100));
		FastByteArrayInputStream r = new FastByteArrayInputStream(w.toByteArray());
		assertEquals(s.repeat(100), r.readUTF());
	}

	@Test
	public void testSkipBytes () {
		x("007fff0203",
				w->{
					w.writeByte(0);
					w.writeByte(0x7f);
					w.writeByte(-1);
					w.writeByte(2);
					w.writeByte(3);
				},
				r -> {
					assertEquals(1, r.skipBytes(1));
					assertEquals(0x7f, r.readUnsignedByte());
					assertEquals(2, r.skipBytes(2));
					assertEquals(3, r.readByte());
				}
		);
	}

	@Test
	public void testObjectStreams () throws IOException, ClassNotFoundException {
		FastByteArrayOutputStream w = new FastByteArrayOutputStream();

		String s = "Simple Java Object";
		w.writeObject(s);

		w.writeObject(Math.PI);

		Map<? extends Serializable,? extends Serializable> m = Map.of("field1", 42, 'c', 17, boolean.class, true);
		w.writeObject(m);

		FastByteArrayInputStream r = new  FastByteArrayInputStream(w.toByteArray());
		assertEquals(s, r.readObject());
		assertEquals(Math.PI, r.readObject());
		assertEquals(m, r.readObject());
	}
}