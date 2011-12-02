package com.site.media.mp3;

import com.site.media.mp3.Parser;

/**
 * Test case for class Parser
 */
public class ParserTest extends MP3TestCase {

	/**
	 * Tests that the getEncoding method returns the correct encoding and
	 * advances the position.
	 */
	public void testParseEncoding() {
		byte[] testIso = { 0, 84, 101, 115, 116 };
		Parser parser = new Parser(testIso, true);
		assertEquals(Parser.ISO, parser.getEncoding());
		assertEquals(1, parser.getPosition());

		byte[] testUnicode = { 1, -2, -1, 0, 84, 0, 101, 0, 115, 0, 116 };
		parser = new Parser(testUnicode, true);
		assertEquals(Parser.UNICODE, parser.getEncoding());
		assertEquals(1, parser.getPosition());
	}

	/**
	 * Tests the correct parsing of text in ISO encoding.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testParseTextByteIso() throws Exception {
		byte[] test = { 84, 101, 115, 116 };
		Parser parser = new Parser(test, false);
		String out = parser.parseText(Parser.ISO);

		assertEquals("Test", out);
	}

	/**
	 * Tests the correct parsing of text in UTF-16 big-endian encoding.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testParseTextByteUTF16BE() throws Exception {
		byte[] test = { -2, -1, 0, 84, 0, 101, 0, 115, 0, 116 };
		Parser parser = new Parser(test, false);
		String out = parser.parseText(Parser.UNICODE);

		assertEquals("Test", out);
	}

	/**
	 * Tests the correct parsing of text in UTF-16 little-endian encoding.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testParseTextByteUTF16LE() throws Exception {
		byte[] test = { -1, -2, 84, 0, 101, 0, 115, 0, 116, 0 };
		Parser parser = new Parser(test, false);
		String out = parser.parseText(Parser.UNICODE);

		assertEquals("Test", out);
	}

	/**
	 * Tests the correct parsing of text in UTF-16 big-endian encoding when the
	 * text is followed by additional bytes.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testParseTextByteUTF16BEMoreInput() throws Exception {
		byte[] test = { -2, -1, 0, 84, 0, 101, 0, 115, 0, 116, 0, 0, 1, 2 };
		Parser parser = new Parser(test, false);
		String out = parser.parseText(Parser.UNICODE);

		assertEquals("Test", out);
	}

	/**
	 * Tests the correct parsing of text in UTF-16 little-endian encoding when
	 * the text is followed by additional bytes.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testParseTextByteUTF16LEMoreInput() throws Exception {
		byte[] test = { -1, -2, 84, 0, 101, 0, 115, 0, 116, 0, 0, 0, 1, 2 };
		Parser parser = new Parser(test, false);
		String out = parser.parseText(Parser.UNICODE);

		assertEquals("Test", out);
	}

	/**
	 * Tests the correct parsing of binary content.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testParseBinary() throws Exception {
		byte[] test = { -1, -2, 84, 0, 101, 0, 115, 0, 116, 0, 0, 0, 1, 2 };
		Parser parser = new Parser(test, false);
		byte[] out = parser.parseBinary();

		assertEqualsByteArray(test, out);
	}

	/**
	 * Tests the correct parsing of binary content when a length is given.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testParseBinaryLength() throws Exception {
		byte[] test = { -1, -2, 84, 0, 101, 0, 115, 0, 116, 0, 0, 0, 1, 2 };
		Parser parser = new Parser(test, false);
		byte[] out = parser.parseBinary(6);

		byte[] expect = { -1, -2, 84, 0, 101, 0 };
		assertEqualsByteArray(expect, out);
	}

	/**
	 * Tests the correct parsing of binary content when a length and a starting
	 * position is given.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testParseBinaryLengthWithPosition() throws Exception {
		byte[] test = { -1, -2, 84, 0, 101, 0, 115, 0, 116, 0, 0, 0, 1, 2 };
		Parser parser = new Parser(test, false);
		parser.setPosition(2);
		byte[] out = parser.parseBinary(6);

		byte[] expect = { 84, 0, 101, 0, 115, 0 };
		assertEqualsByteArray(expect, out);
	}

}
