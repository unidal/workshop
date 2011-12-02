package com.site.media.mp3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.site.media.mp3.ID3v2;
import com.site.media.mp3.ID3v2Frame;
import com.site.media.mp3.ID3v2NoSuchFrameException;

/**
 * Tests for class ID3v2 (ID3v2 specific tests)
 */
public class ID3v2Test extends MP3TestCase {

	/**
	 * Create a standard set of frames as an expectation. These frames are
	 * stored in the Vector expectFramesSet for direct comparison if all frames
	 * are read and in a HashTable for individual access (using the frame id as
	 * key, only one frame created per key)
	 */
	protected void setUp() throws Exception {
		super.setUp();

		expectFramesSet = new Vector<ID3v2Frame>();
		expectFrames = new Hashtable<String, ID3v2Frame>();

		ID3v2Frame tit2 = new ID3v2Frame("TIT2", "\0Title".getBytes("Cp437"),
				false, false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0,
				(byte) 0);
		expectFramesSet.add(tit2);
		expectFrames.put("TIT2", tit2);

		ID3v2Frame tpe1 = new ID3v2Frame("TPE1", "\0Artist".getBytes("Cp437"),
				false, false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0,
				(byte) 0);
		expectFramesSet.add(tpe1);
		expectFrames.put("TPE1", tpe1);

		ID3v2Frame talb = new ID3v2Frame("TALB", "\0Album".getBytes("Cp437"),
				false, false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0,
				(byte) 0);
		expectFramesSet.add(talb);
		expectFrames.put("TALB", talb);

		ID3v2Frame tyer = new ID3v2Frame("TYER", "\0002003".getBytes("Cp437"),
				false, false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0,
				(byte) 0);
		expectFramesSet.add(tyer);
		expectFrames.put("TYER", tyer);

		ID3v2Frame trck = new ID3v2Frame("TRCK", "\0001".getBytes("Cp437"),
				false, false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0,
				(byte) 0);
		expectFramesSet.add(trck);
		expectFrames.put("TRCK", trck);

		ID3v2Frame tcon = new ID3v2Frame("TCON", "\0Rock".getBytes("Cp437"),
				false, false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0,
				(byte) 0);
		expectFramesSet.add(tcon);
		expectFrames.put("TCON", tcon);

		ID3v2Frame comm = new ID3v2Frame("COMM", "\0engShort\0Long comment"
				.getBytes("Cp437"), false, false, false,
				ID3v2Frame.NO_COMPRESSION, (byte) 0, (byte) 0);
		expectFramesSet.add(comm);
		expectFrames.put("COMM", comm);

		// The XBIN frame is not expected, but it is used in some tests
		// => only put in expectFrames, not in expectFramesSet
		byte[] binContent = { (byte) 42, (byte) 10, (byte) 2, (byte) 12,
				(byte) 255, (byte) 0, (byte) 17 };
		ID3v2Frame xbin = new ID3v2Frame("XBIN", binContent, false, false,
				false, ID3v2Frame.NO_COMPRESSION, (byte) 0, (byte) 0);
		expectFrames.put("XBIN", xbin);
	}

	protected Vector<ID3v2Frame> expectFramesSet;

	protected Hashtable<String, ID3v2Frame> expectFrames;

	/**
	 * Tests using the class with an InputStream.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testID3v2InputStream() throws Exception {
		FileInputStream fis = new FileInputStream((File) testFiles
				.get("short_v2reg"));
		ID3v2 id3v2 = new ID3v2(fis);
		fis.close();

		// try to read a frame => should succeed
		Vector<ID3v2Frame> res = id3v2.getFrame("TIT2");
		Vector<ID3v2Frame> expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TIT2"));
		assertEquals(expect, res);

		// id3v2 is read-only, but no changed => update succeeds
		id3v2.update();

		// do a modification => update throws an IOException
		id3v2.removeFrame("TIT2");
		try {
			id3v2.update();
			fail("Expected IOException not thrown");
		} catch (IOException e) {
		}

	}

	// No need to test ID3v2(File) as it is used in almost all
	// other tests

	/**
	 * Tests the synchronize method.
	 */
	public void testSynchronize() {
		// no sync
		byte[] test = { (byte) 10, (byte) 255, (byte) 30, (byte) 10, (byte) 12,
				(byte) 42, (byte) 42, (byte) 2 };
		byte[] res = ID3v2.synchronize(test);
		assertEquals(null, res);

		// sync
		byte[] test2 = { (byte) 10, (byte) 255, (byte) 30, (byte) 10,
				(byte) 255, (byte) 0, (byte) 42, (byte) 2 };
		res = ID3v2.synchronize(test2);
		byte[] expect2 = { (byte) 10, (byte) 255, (byte) 30, (byte) 10,
				(byte) 255, (byte) 42, (byte) 2 };
		assertEqualsByteArray(expect2, res);
	}

	/**
	 * Tests the unsynchronize method.
	 */
	public void testUnynchronize() {
		// array does not contain any 0xff
		byte[] test = { (byte) 10, (byte) 240, (byte) 30, (byte) 10, (byte) 12,
				(byte) 42, (byte) 42, (byte) 2 };
		byte[] res = ID3v2.unsynchronize(test);
		assertEquals(null, res);

		// array does contain 0xff, but not followed by
		// either 0x00 or 111xxxxx
		byte[] test2 = { (byte) 10, (byte) 255, (byte) 30, (byte) 10,
				(byte) 12, (byte) 42, (byte) 42, (byte) 2 };
		res = ID3v2.unsynchronize(test2);
		assertEquals(null, res);

		// array does contain 0xff 0x00
		byte[] test3 = { (byte) 10, (byte) 255, (byte) 0, (byte) 10, (byte) 12,
				(byte) 42, (byte) 42, (byte) 2 };
		res = ID3v2.unsynchronize(test3);
		byte[] expect3 = { (byte) 10, (byte) 255, (byte) 0, (byte) 0,
				(byte) 10, (byte) 12, (byte) 42, (byte) 42, (byte) 2 };
		assertEqualsByteArray(expect3, res);

		// array does contain 0xff 111xxxxx
		byte[] test4 = { (byte) 10, (byte) 255, (byte) 224, (byte) 10,
				(byte) 12, (byte) 42, (byte) 42, (byte) 2 };
		res = ID3v2.unsynchronize(test4);
		byte[] expect4 = { (byte) 10, (byte) 255, (byte) 0, (byte) 224,
				(byte) 10, (byte) 12, (byte) 42, (byte) 42, (byte) 2 };
		assertEqualsByteArray(expect4, res);

		// multiple unsynchs
		byte[] test5 = { (byte) 10, (byte) 255, (byte) 0, (byte) 10, (byte) 12,
				(byte) 255, (byte) 230, (byte) 2 };
		res = ID3v2.unsynchronize(test5);
		byte[] expect5 = { (byte) 10, (byte) 255, (byte) 0, (byte) 0,
				(byte) 10, (byte) 12, (byte) 255, (byte) 0, (byte) 230,
				(byte) 2 };
		assertEqualsByteArray(expect5, res);
	}

	/**
	 * Tests the hasTag method
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testHasTag() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_none"));
		assertFalse(id3v2.hasTag());
		id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		assertTrue(id3v2.hasTag());
	}

	/**
	 * Tests the getFrames method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetFrames() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		Vector<ID3v2Frame> res = id3v2.getFrames();
		assertEquals(expectFramesSet, res);
	}

	/**
	 * Tests the getFrame method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetFrame() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));

		Vector<ID3v2Frame> res = id3v2.getFrame("TIT2");
		Vector<ID3v2Frame> expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TIT2"));
		assertEquals(expect, res);

		res = id3v2.getFrame("TPE1");
		expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TPE1"));
		assertEquals(expect, res);

		res = id3v2.getFrame("TALB");
		expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TALB"));
		assertEquals(expect, res);

		res = id3v2.getFrame("TYER");
		expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TYER"));
		assertEquals(expect, res);

		res = id3v2.getFrame("TRCK");
		expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TRCK"));
		assertEquals(expect, res);

		res = id3v2.getFrame("TCON");
		expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TCON"));
		assertEquals(expect, res);

		res = id3v2.getFrame("COMM");
		expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("COMM"));
		assertEquals(expect, res);
	}

	/**
	 * Tests the addFrame method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testAddFrame() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_none"));
		ID3v2Frame add = (ID3v2Frame) expectFrames.get("TPE1");
		id3v2.addFrame(add);

		Vector<ID3v2Frame> res = id3v2.getFrame("TPE1");
		Vector<ID3v2Frame> expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TPE1"));
		assertEquals(expect, res);

		id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		add = new ID3v2Frame("TPE1", "\0Artist2".getBytes("Cp437"), false,
				false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0, (byte) 0);
		id3v2.addFrame(add);

		res = id3v2.getFrame("TPE1");
		expect = new Vector<ID3v2Frame>();
		expect.add(expectFrames.get("TPE1"));
		expect.add(new ID3v2Frame("TPE1", "\0Artist2".getBytes("Cp437"), false,
				false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0, (byte) 0));
		assertEquals(expect, res);

	}

	/**
	 * Tests that a custom frame (i.e. a frame starting with X) is added
	 * correctly. This test also writes the file and re-reads it.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testAddCustomFrame() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_none"));

		ID3v2Frame frame = new ID3v2Frame("XZZZ", "\0Custom".getBytes("Cp437"),
				false, false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0,
				(byte) 0);
		id3v2.addFrame(frame);

		Vector<ID3v2Frame> res = id3v2.getFrame("XZZZ");
		Vector<ID3v2Frame> expect = new Vector<ID3v2Frame>();
		expect.add(frame);
		assertEquals(expect, res);

		id3v2.update();

		ID3v2 reread = new ID3v2((File) testFiles.get("short_none"));

		res = reread.getFrame("XZZZ");
		expect = new Vector<ID3v2Frame>();
		expect.add(frame);
		assertEquals(expect, res);
	}

	/**
	 * Tests removing an ID3v2 frame (removeFrame(ID3v2Frame)).
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testRemoveFrameID3v2Frame() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		ID3v2Frame rem = (ID3v2Frame) expectFrames.get("TPE1");
		id3v2.removeFrame(rem);

		try {
			id3v2.getFrame("TPE1");
			fail("Expected ID3v2NoSuchFrameException not thrown");
		} catch (ID3v2NoSuchFrameException e) {
		}
	}

	/**
	 * Tests removing an ID3v2 frame by type (removeFrame(String)).
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testRemoveFrameString() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		id3v2.removeFrame("TPE1");

		try {
			id3v2.getFrame("TPE1");
			fail("Expected ID3v2NoSuchFrameException not thrown");
		} catch (ID3v2NoSuchFrameException e) {
		}
	}

	/**
	 * Tests the removeFrame method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testRemoveFrameStringint() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		id3v2.removeFrame("TPE1", 0);

		try {
			id3v2.getFrame("TPE1");
			fail("Expected ID3v2NoSuchFrameException not thrown");
		} catch (ID3v2NoSuchFrameException e) {
		}
	}

	/**
	 * Tests the removeFrames method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testRemoveFrames() throws Exception {
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		id3v2.removeFrames();

		Vector<ID3v2Frame> res = id3v2.getFrames();
		Vector<ID3v2Frame> expect = new Vector<ID3v2Frame>();
		assertEquals(expect, res);
	}

	/**
	 * Tests updating the ID3v2 tag in a file which does not have a tag.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testUpdateNone() throws Exception {
		// Add frames to a file without any tags
		// Don't use any options (compression, padding, unsynch)
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_none"));
		id3v2.setUseCRC(false);
		id3v2.setUsePadding(false);
		id3v2.setUseUnsynchronization(false);
		for (Enumeration<ID3v2Frame> e = expectFramesSet.elements(); e.hasMoreElements();) {
			id3v2.addFrame((ID3v2Frame) e.nextElement());
		}
		id3v2.update();

		id3v2 = new ID3v2((File) testFiles.get("short_none"));
		Vector<ID3v2Frame> res = id3v2.getFrames();
		assertEquals(expectFramesSet, res);
	}

	/**
	 * Tests updating the ID3v2 tag in a file which does not have a tag, using
	 * padding.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testUpdateNonePadding() throws Exception {
		// Add frames to a file without any tags
		// Use padding, but no other options
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_none"));
		id3v2.setUseCRC(false);
		id3v2.setUsePadding(true);
		id3v2.setUseUnsynchronization(false);
		for (Enumeration<ID3v2Frame> e = expectFramesSet.elements(); e.hasMoreElements();) {
			id3v2.addFrame((ID3v2Frame) e.nextElement());
		}
		id3v2.update();

		id3v2 = new ID3v2((File) testFiles.get("short_none"));
		Vector<ID3v2Frame> res = id3v2.getFrames();
		assertEquals(expectFramesSet, res);

		// check that padding was used (file length > 2048)
		assertTrue("Padding was used", ((File) testFiles.get("short_none"))
				.length() > 2048);
	}

	/**
	 * Tests updating the ID3v2 tag in a file with an existing tag, using
	 * padding.
	 * 
	 * @exception Exception if an exception occurred
	 */
	@SuppressWarnings("unchecked")
   public void testUpdateExPadding() throws Exception {
		// Add frames to a file with tags
		// Use padding, but no other options
		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		id3v2.setUseCRC(false);
		id3v2.setUsePadding(true);
		id3v2.setUseUnsynchronization(false);
		ID3v2Frame add = new ID3v2Frame("TPE1", "\0Artist2".getBytes("Cp437"),
				false, false, false, ID3v2Frame.NO_COMPRESSION, (byte) 0,
				(byte) 0);
		id3v2.addFrame(add);
		id3v2.update();

		id3v2 = new ID3v2((File) testFiles.get("short_v2reg"));
		Vector<ID3v2Frame> res = id3v2.getFrames();
		Vector<ID3v2Frame> expect = (Vector<ID3v2Frame>) expectFramesSet.clone();
		expect.add(add);
		assertEquals(expect, res);

		// check that padding was used (file length > 2048)
		assertTrue("Padding was used", ((File) testFiles.get("short_v2reg"))
				.length() > 2048);
	}

	/**
	 * Test that no unsynchronisation is done even if necessary if
	 * unsynchronisation is not requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testUpdateNoUnsynch() throws Exception {
		// Add frame which needs unsynch to a file without any tags
		// These test does not check all variants of unsynch
		// as there is a separate test for that.
		ID3v2Frame frame = (ID3v2Frame) expectFrames.get("XBIN");
		Vector<ID3v2Frame> expect = new Vector<ID3v2Frame>();
		expect.add(frame);

		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_none"));
		id3v2.setUseCRC(false);
		id3v2.setUsePadding(false);
		id3v2.setUseUnsynchronization(false);
		id3v2.addFrame(frame);
		id3v2.update();

		id3v2 = new ID3v2((File) testFiles.get("short_none"));
		Vector<ID3v2Frame> res = id3v2.getFrames();
		assertEquals(expect, res);

		// Check that no unsynch was used. As unsynch is hidden
		// by ID3v2, the only way is to directly read the file
		FileInputStream fis = new FileInputStream((File) testFiles
				.get("short_none"));
		fis.skip(0x23);
		byte[] b = new byte[2];
		fis.read(b);
		fis.close();
		assertTrue((byte) 0 == b[0]);
		assertTrue((byte) 0 != b[1]);

		long length = ((File) testFiles.get("short_none")).length();
		assertTrue("File length is 901 (" + length + ")", length == 901L);
	}

	/**
	 * Test that unsynchronisation is done if necessary and requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testUpdateUnsynch() throws Exception {
		ID3v2Frame frame = (ID3v2Frame) expectFrames.get("XBIN");
		Vector<ID3v2Frame> expect = new Vector<ID3v2Frame>();
		expect.add(frame);

		ID3v2 id3v2 = new ID3v2((File) testFiles.get("short_none"));
		id3v2.setUseCRC(false);
		id3v2.setUsePadding(false);
		id3v2.setUseUnsynchronization(true);
		id3v2.addFrame(frame);
		id3v2.update();

		id3v2 = new ID3v2((File) testFiles.get("short_none"));
		Vector<ID3v2Frame> res = id3v2.getFrames();
		assertEquals(expect, res);

		// Check that unsynch was used
		FileInputStream fis = new FileInputStream((File) testFiles
				.get("short_none"));
		fis.skip(0x23);
		byte[] b = new byte[2];
		fis.read(b);
		fis.close();
		assertTrue((byte) 0 == b[0]);
		assertTrue((byte) 0 == b[1]);

		assertTrue(((File) testFiles.get("short_none")).length() == 902L);
	}

	/**
	 * Tests that if the constructor is called with an InputStream, this stream
	 * is not closed.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testConstuctorInputStreamNotClosed() throws Exception {
		// test for a file with no id3v2 header
		File file = (File) testFiles.get("short_none");
		FileInputStream stream = new FileInputStream(file);
		new ID3v2(stream);
		try {
			stream.read(); // this will throw an exception if the stream is
							// closed
		} catch (IOException e) {
			fail("Stream is unexpectedly closed");
		}
		stream.close();

		// test for a file with an id3v2 header
		file = (File) testFiles.get("short_v2reg");
		stream = new FileInputStream(file);
		new ID3v2(stream);
		try {
			stream.read(); // this will throw an exception if the stream is
							// closed
		} catch (IOException e) {
			fail("Stream is unexpectedly closed");
		}
		stream.close();
	}

	/**
	 * Tests that if the constructor is called with a File, the internal input
	 * stream is closed.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testConstuctorFileClosed() throws Exception {
		// test for a file with no id3v2 header
		File file = (File) testFiles.get("short_none");
		new ID3v2(file);

		// if the internal InputStream is still open, the file cannot be deleted
		// (This is a bad test as it depends on the operating system, but
		// I don't see any other way.)
		if (false == file.delete()) {
			fail("Could not delete file as expected");
		}

		// test for a file with an id3v2 header
		file = (File) testFiles.get("short_v2reg");
		new ID3v2(file);
		// if the internal InputStream is still open, the file cannot be deleted
		if (false == file.delete()) {
			fail("Could not delete file as expected");
		}
	}

}