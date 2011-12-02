package com.site.media.mp3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import junit.framework.TestCase;

/**
 * General super class for all tests using MP3 files. The main task of this
 * class is to provide a common implementation for the setUp() and tearDown()
 * methods which creates and removes the MP3 test files.
 * 
 * @author Jens Vonderheide <vonderheide@redlink.de>
 */
public abstract class MP3TestCase extends TestCase {

	/**
	 * Create a lot of test MP3 files.
	 */
	protected void setUp() throws Exception {
		super.setUp();

		prepareFrames();
		defV1 = ("TAG"
				+ "Title\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
				+ "Artist\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
				+ "Album\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
				+ "2003"
				+ "Comment\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
				+ "\7").getBytes("Cp437");
		defV11 = ("TAG"
				+ "Title\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
				+ "Artist\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
				+ "Album\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
				+ "2003" + "Comment\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
				+ "\0\4" + "\7").getBytes("Cp437");
		defV1Spaces = ("TAG" + "Title                         "
				+ "Artist                        "
				+ "Album                         " + "2003"
				+ "Comment                       " + "\7").getBytes("Cp437");

		// V2 header without unsynchronisation
		defV2 = ("ID3\3\0\0\0\0\0\173" + "TIT2\0\0\0\6\0\0\0" + "Title" + // 16
				"TPE1\0\0\0\7\0\0\0" + "Artist" + // 17
				"TALB\0\0\0\6\0\0\0" + "Album" + // 16
				"TYER\0\0\0\5\0\0\0" + "2003" + // 15
				"TRCK\0\0\0\2\0\0\0" + "1" + // 12
				"TCON\0\0\0\5\0\0\0" + "Rock" + // 15
				"COMM\0\0\0\26\0\0\0" + "engShort\0Long comment")
				.getBytes("Cp437"); // 33

		createMP3("short_none", null, null, 3);
		createMP3("short_v1reg", defV1, null, 3);
		createMP3("short_v11reg", defV11, null, 3);
		createMP3("short_v1spaces", defV1Spaces, null, 3);
		createMP3("short_v2reg", null, defV2, 3);
		createMP3("short_v12reg", defV11, defV2, 3);
	}

	/**
	 * Remove all test files contained in testFiles.
	 */
	protected void tearDown() throws Exception {
		for (Enumeration<File> e = testFiles.elements(); e.hasMoreElements();) {
			File file = (File) e.nextElement();
			file.delete();
		}
	}

	/**
	 * Convenience method for asserting that two byte arrays are equal.
	 * 
	 * @param expect the expected byte array
	 * @param val the byte array to assert
	 */
	public static void assertEqualsByteArray(byte[] expect, byte[] val) {
		if (expect.length != val.length) {
			fail("Length differs, expected " + expect.length + ", got "
					+ val.length);
		}
		for (int i = 0; i < expect.length; i++) {
			if (expect[i] != val[i]) {
				fail("Arrays differ at index " + i + ", expected " + expect[i]
						+ ", got " + val[i]);
			}
		}
	}

	/**
	 * Create the two MP3 frames containing silence that are used in createMP3.
	 */
	private void prepareFrames() {
		// create a lead-in frame and a regular frame containing silence
		// most bytes of both frames contain 0xff, so it is easier
		// to prefill the whole byte array with 0xff and then
		// change the values that differ
		silenceLeadin = new byte[288];
		silenceFrame = new byte[288];

		for (int i = 0; i < 288; i++) {
			silenceLeadin[i] = (byte) 0xff;
			silenceFrame[i] = (byte) 0xff;
		}
		silenceLeadin[1] = (byte) 0xfb;
		silenceLeadin[2] = (byte) 0x58;
		silenceLeadin[3] = (byte) 0xc0;
		silenceLeadin[4] = (byte) 0x00;
		silenceLeadin[5] = (byte) 0x00;
		silenceLeadin[6] = (byte) 0x00;
		silenceLeadin[7] = (byte) 0x00;
		silenceLeadin[8] = (byte) 0x01;
		silenceLeadin[9] = (byte) 0xa4;
		silenceLeadin[10] = (byte) 0x00;
		silenceLeadin[11] = (byte) 0x00;
		silenceLeadin[12] = (byte) 0x00;
		silenceLeadin[13] = (byte) 0x00;
		silenceLeadin[14] = (byte) 0x00;
		silenceLeadin[15] = (byte) 0x00;
		silenceLeadin[16] = (byte) 0x34;
		silenceLeadin[17] = (byte) 0x80;
		silenceLeadin[18] = (byte) 0x00;
		silenceLeadin[19] = (byte) 0x00;
		silenceLeadin[20] = (byte) 0x00;

		silenceFrame[1] = (byte) 0xfb;
		silenceFrame[2] = (byte) 0x58;
		silenceFrame[3] = (byte) 0xc4;
		silenceFrame[4] = (byte) 0x80;
		silenceFrame[5] = (byte) 0x02;
		silenceFrame[6] = (byte) 0xe0;
		silenceFrame[7] = (byte) 0x00;
		silenceFrame[8] = (byte) 0x01;
		silenceFrame[9] = (byte) 0xa4;
		silenceFrame[10] = (byte) 0x00;
		silenceFrame[11] = (byte) 0x00;
		silenceFrame[12] = (byte) 0x00;
		silenceFrame[13] = (byte) 0x00;
		silenceFrame[14] = (byte) 0x00;
		silenceFrame[15] = (byte) 0x00;
		silenceFrame[16] = (byte) 0x34;
		silenceFrame[17] = (byte) 0x80;
		silenceFrame[18] = (byte) 0x00;
		silenceFrame[19] = (byte) 0x00;
		silenceFrame[20] = (byte) 0x00;
	}

	/**
	 * Create a new MP3 file with the given number of frames, prepending the
	 * given ID3v2 bytes and appending the given ID3v1 bytes.
	 * 
	 * The file is created in the directory contained in TESTFILE_PATH. After
	 * creation, a reference to a File object identifying the test file is added
	 * to testFiles using the given handle as key.
	 * 
	 * @param hande a unique identifier for this test file
	 * @param v1 the bytes to append
	 * @param v2 the bytes to prepend
	 * @param frames the number of MP3 frames to create
	 */
	protected void createMP3(String handle, byte[] v1, byte[] v2, int frames)
			throws FileNotFoundException, IOException {
		createMP3(handle, v1, v2, frames, silenceLeadin, silenceFrame);
	}

	/**
	 * Create a new MP3 file using the given leadin and regular frames.
	 * 
	 * @param handle
	 * @param v1
	 * @param v2
	 * @param frames
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void createMP3(String handle, byte[] v1, byte[] v2, int frames,
			byte[] leadin, byte[] regular) throws FileNotFoundException,
			IOException {

		// create byte array to hold the full file including
		// prepends and appends
		int contentLength = frames * regular.length;
		if (v1 != null) {
			contentLength += v1.length;
		}
		if (v2 != null) {
			contentLength += v2.length;
		}
		byte[] content = new byte[contentLength];

		// create content
		int offset = 0;
		if (v2 != null) {
			System.arraycopy(v2, 0, content, 0, v2.length);
			offset += v2.length;
		}
		System.arraycopy(leadin, 0, content, offset, leadin.length);
		offset += leadin.length;
		for (int i = 0; i < frames - 1; i++) {
			System.arraycopy(regular, 0, content, offset, regular.length);
			offset += regular.length;
		}
		if (v1 != null) {
			System.arraycopy(v1, 0, content, offset, v1.length);
			offset += v1.length;
		}

		File file = new File(TESTFILE_PATH + File.separator + handle + ".mp3");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(content);
		fos.close();

//		System.out.println(file.getCanonicalPath());
		testFiles.put(handle, file);
	}

	/**
	 * A collection of all test files created in the setUp() method.
	 */
	protected Hashtable<String, File> testFiles = new Hashtable<String, File>();

	protected final String TESTFILE_PATH = ".";

	protected byte[] silenceFrame;

	protected byte[] silenceLeadin;

	protected byte[] defV1;
	protected byte[] defV11;
	protected byte[] defV1Spaces;
	protected byte[] defV2;

}