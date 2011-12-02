package com.site.kernel;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import com.site.kernel.common.BaseTestCase;

public class SystemInjectionTest extends BaseTestCase {
	public void testInjectionProperties() {
		List<String> names = new ArrayList<String>();
		List<List<String>> values = new ArrayList<List<String>>();
		Sample sample = new Sample();

		setNames(names);
		setValues(values);
		SystemInjection.injectProperties(sample, names, values);
		assertValues(sample);
	}

	private void setNames(List<String> names) {
		names.add("byte");
		names.add("char");
		names.add("short");
		names.add("int");
		names.add("long");
		names.add("float");
		names.add("double");
		names.add("boolean");
		names.add("string");
		names.add("file");
		names.add("url");
		names.add("timeZone");
		names.add("intArray");
		names.add("stringArray");
		names.add("list");
		names.add("set");
		names.add("iterator");
	}

	private void setValues(List<List<String>> values) {
		values.add(Arrays.asList(new String[] { "1" })); // byte
		values.add(Arrays.asList(new String[] { "1" })); // char
		values.add(Arrays.asList(new String[] { "1" })); // short
		values.add(Arrays.asList(new String[] { "1" })); // int
		values.add(Arrays.asList(new String[] { "1" })); // long
		values.add(Arrays.asList(new String[] { "1" })); // float
		values.add(Arrays.asList(new String[] { "1" })); // double
		values.add(Arrays.asList(new String[] { "true" })); // boolean
		values.add(Arrays.asList(new String[] { "s" })); // string
		values.add(Arrays.asList(new String[] { "." })); // file
		values.add(Arrays.asList(new String[] { "http://www.test.com" })); // url
		values.add(Arrays.asList(new String[] { "GMT-8" })); // timeZone
		values.add(Arrays.asList(new String[] { "1", "2", "3" })); // intArray
		values.add(Arrays.asList(new String[] { "s1", "s2", "s3" })); // stringArray
		values.add(Arrays.asList(new String[] { "s1", "s2", "s3" })); // list
		values.add(Arrays.asList(new String[] { "s1", "s2", "s3" })); // set
		values.add(Arrays.asList(new String[] { "s1", "s2", "s3" })); // iterator
	}

	private void assertValues(Sample sample) {
		assertTrue(0 != sample.getByte());
		assertTrue(0 != sample.getChar());
		assertTrue(0 != sample.getShort());
		assertTrue(0 != sample.getInt());
		assertTrue(0 != sample.getLong());
		assertTrue(0 != sample.getFloat());
		assertTrue(0 != sample.getDouble());
		assertTrue(false != sample.isBoolean());
		assertTrue(null != sample.getString());
		assertTrue(null != sample.getFile());
		assertTrue(null != sample.getUrl());
		assertTrue(null != sample.getTimeZone());
		assertTrue(null != sample.getIntArray());
		assertTrue(null != sample.getStringArray());
		assertTrue(null != sample.getList());
		assertTrue(null != sample.getSet());
		assertTrue(null != sample.getIterator());
	}

	private static final class Sample {
		private byte m_byte;

		private char m_char;

		private short m_short;

		private int m_int;

		private long m_long;

		private float m_float;

		private double m_double;

		private boolean m_boolean;

		private String m_string;

		private File m_file;

		private URL m_url;

		private TimeZone m_timeZone;

		private int[] m_intArray;

		private String[] m_stringArray;

		private List m_list;

		private Set m_set;

		private Iterator m_iterator;

		public boolean isBoolean() {
			return m_boolean;
		}

		public void setBoolean(boolean b) {
			m_boolean = b;
		}

		public byte getByte() {
			return m_byte;
		}

		public void setByte(byte b) {
			m_byte = b;
		}

		public char getChar() {
			return m_char;
		}

		public void setChar(char c) {
			m_char = c;
		}

		public double getDouble() {
			return m_double;
		}

		public void setDouble(double d) {
			m_double = d;
		}

		public File getFile() {
			return m_file;
		}

		public void setFile(File file) {
			m_file = file;
		}

		public float getFloat() {
			return m_float;
		}

		public void setFloat(float f) {
			m_float = f;
		}

		public int getInt() {
			return m_int;
		}

		public void setInt(int i) {
			m_int = i;
		}

		public int[] getIntArray() {
			return m_intArray;
		}

		public void setIntArray(int[] intArray) {
			m_intArray = intArray;
		}

		public Iterator getIterator() {
			return m_iterator;
		}

		public void setIterator(Iterator iterator) {
			m_iterator = iterator;
		}

		public List getList() {
			return m_list;
		}

		public void setList(List list) {
			m_list = list;
		}

		public long getLong() {
			return m_long;
		}

		public void setLong(long l) {
			m_long = l;
		}

		public Set getSet() {
			return m_set;
		}

		public void setSet(Set set) {
			m_set = set;
		}

		public short getShort() {
			return m_short;
		}

		public void setShort(short s) {
			m_short = s;
		}

		public String getString() {
			return m_string;
		}

		public void setString(String string) {
			m_string = string;
		}

		public String[] getStringArray() {
			return m_stringArray;
		}

		public void setStringArray(String[] stringArray) {
			m_stringArray = stringArray;
		}

		public TimeZone getTimeZone() {
			return m_timeZone;
		}

		public void setTimeZone(TimeZone timeZone) {
			m_timeZone = timeZone;
		}

		public URL getUrl() {
			return m_url;
		}

		public void setUrl(URL url) {
			m_url = url;
		}

	}
}
