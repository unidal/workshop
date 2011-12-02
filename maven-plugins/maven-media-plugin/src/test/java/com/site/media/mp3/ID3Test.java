package com.site.media.mp3;

import java.io.File;
import java.io.FileInputStream;

import com.site.media.mp3.ID3;
import com.site.media.mp3.NoID3TagException;

/**
 * Tests for class ID3 (ID3v1 and ID3v11 specific tests)
 */
public class ID3Test extends MP3TestCase {

	/**
	 * Tests that an exception is thrown if the file does not contain a title
	 * tag and the title is requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetTitleNoTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		try {
			id3.getTitle();
			fail("Expected NoID3TagException not thrown");
		} catch (NoID3TagException e) {
		}
	}

	/**
	 * Tests reading the title from a file containing only ID3v1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetTitleV1() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1reg"));
		assertEquals("Title", id3.getTitle());
	}

	/**
	 * Tests reading the title from a file containing only ID3v1.1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetTitleV11() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v11reg"));
		assertEquals("Title", id3.getTitle());
	}

	/**
	 * Tests reading the title from a file containing only ID3v1 data with
	 * spaces.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetTitleV1Space() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1spaces"));
		assertEquals("Title", id3.getTitle());
	}

	/**
	 * Tests that an exception is thrown if the file does not contain an artist
	 * tag and the artist is requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetArtistNoTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		try {
			id3.getArtist();
			fail("Expected NoID3TagException not thrown");
		} catch (NoID3TagException e) {
		}
	}

	/**
	 * Tests reading the artist from a file containing only ID3v1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetArtistV1() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1reg"));
		assertEquals("Artist", id3.getArtist());
	}

	/**
	 * Tests reading the title from a file containing only ID3v1.1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetArtistV11() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v11reg"));
		assertEquals("Artist", id3.getArtist());
	}

	/**
	 * Tests reading the title from a file containing only ID3v1 data with
	 * spaces.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetArtistSpace() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1spaces"));
		assertEquals("Artist", id3.getArtist());
	}

	/**
	 * Tests that an exception is thrown if the file does not contain an album
	 * tag and the album is requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetAlbumNoTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		try {
			id3.getAlbum();
			fail("Expected NoID3TagException not thrown");
		} catch (NoID3TagException e) {
		}
	}

	/**
	 * Tests reading the album from a file containing only ID3v1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetAlbumV1() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1reg"));
		assertEquals("Album", id3.getAlbum());
	}

	/**
	 * Tests reading the album from a file containing only ID3v1.1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetAlbumV11() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v11reg"));
		assertEquals("Album", id3.getAlbum());
	}

	/**
	 * Tests reading the album from a file containing only ID3v1 data with
	 * spaces.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetAlbumV1Space() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1spaces"));
		assertEquals("Album", id3.getAlbum());
	}

	/**
	 * Tests that an exception is thrown if the file does not contain an year
	 * tag and the year is requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetYearNoTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		try {
			id3.getYear();
			fail("Expected NoID3TagException not thrown");
		} catch (NoID3TagException e) {
		}
	}

	/**
	 * Tests reading the year from a file containing only ID3v1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetYearV1() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1reg"));
		assertEquals("2003", id3.getYear());
	}

	/**
	 * Tests reading the year from a file containing only ID3v1.1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetYearV11() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v11reg"));
		assertEquals("2003", id3.getYear());
	}

	/**
	 * Tests reading the year from a file containing only ID3v1 data with
	 * spaces.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetYearV1Space() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1spaces"));
		assertEquals("2003", id3.getYear());
	}

	/**
	 * Tests that an exception is thrown if the file does not contain a genre
	 * tag and the genre is requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetGenreNoTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		try {
			id3.getGenre();
			fail("Expected NoID3TagException not thrown");
		} catch (NoID3TagException e) {
		}
	}

	/**
	 * Tests reading the genre from a file containing only ID3v1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetGenreV1() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1reg"));
		assertEquals(7, id3.getGenre());
	}

	/**
	 * Tests reading the genre from a file containing only ID3v1.1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetGenreV11() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v11reg"));
		assertEquals(7, id3.getGenre());
	}

	/**
	 * Tests reading the genre from a file containing only ID3v1 data with
	 * spaces.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetGenreV1Space() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1spaces"));
		assertEquals(7, id3.getGenre());
	}

	/**
	 * Tests that an exception is thrown if the file does not contain a comment
	 * tag and the comment is requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetCommentNoTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		try {
			id3.getComment();
			fail("Expected NoID3TagException not thrown");
		} catch (NoID3TagException e) {
		}
	}

	/**
	 * Tests reading the comment from a file containing only ID3v1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetCommentV1() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1reg"));
		assertEquals("Comment", id3.getComment());
	}

	/**
	 * Tests reading the comment from a file containing only ID3v1.1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetCommentV11() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v11reg"));
		assertEquals("Comment", id3.getComment());
	}

	/**
	 * Tests reading the comment from a file containing only ID3v1 data with
	 * spaces.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetCommentV1Space() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1spaces"));
		assertEquals("Comment", id3.getComment());
	}

	/**
	 * Tests that an exception is thrown if the file does not contain a track
	 * tag and the track is requested.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetTrackNoTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		try {
			id3.getTrack();
			fail("Expected NoID3TagException not thrown");
		} catch (NoID3TagException e) {
		}
	}

	/**
	 * Tests reading the track from a file containing only ID3v1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetTrackV1() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1reg"));
		assertEquals(0, id3.getTrack());
	}

	/**
	 * Tests reading the track from a file containing only ID3v1.1 data.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetTrackV11() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v11reg"));
		assertEquals(4, id3.getTrack());
	}

	/**
	 * Tests reading the track from a file containing only ID3v1 data with
	 * spaces.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testGetTrackV1Space() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_v1spaces"));
		assertEquals(0, id3.getTrack());
	}

	/**
	 * Tests setting the title.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testSetTitle() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		id3.setTitle("Title");
		assertEquals("Title", id3.getTitle());
	}

	/**
	 * Tests setting the artist.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testSetArtist() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		id3.setArtist("Artist");
		assertEquals("Artist", id3.getArtist());
	}

	/**
	 * Tests setting the album.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testSetAlbum() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		id3.setAlbum("Album");
		assertEquals("Album", id3.getAlbum());
	}

	/**
	 * Tests setting the year.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testSetYear() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		id3.setYear("2004");
		assertEquals("2004", id3.getYear());
	}

	/**
	 * Tests setting the comment.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testSetComment() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		id3.setComment("Comment");
		assertEquals("Comment", id3.getComment());
	}

	/**
	 * Tests setting the track.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testSetTrack() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		id3.setTrack(6);
		assertEquals(6, id3.getTrack());
	}

	/**
	 * Tests setting the genre.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testSetGenre() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		id3.setGenre(11);
		assertEquals(11, id3.getGenre());
	}

	/**
	 * Tests updating the file with ID3v1 information if it contains no tag yet.
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testWriteTagNoTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		id3.setAlbum("Album");
		id3.setArtist("Artist");
		id3.setComment("Comment");
		id3.setGenre(7);
		id3.setTitle("Title");
		id3.setTrack(4);
		id3.setYear("2003");

		id3.writeTag();

		id3 = new ID3((File) testFiles.get("short_none"));
		assertEquals("Album", id3.getAlbum());
		assertEquals("Artist", id3.getArtist());
		assertEquals("Comment", id3.getComment());
		assertEquals(7, id3.getGenre());
		assertEquals("Title", id3.getTitle());
		assertEquals(4, id3.getTrack());
		assertEquals("2003", id3.getYear());

		// additional (redundant) check:
		// The contents of "short_none" must now be identical to the ones of
		// "short_v11reg"
		byte[] contN = new byte[992];
		FileInputStream fis = new FileInputStream((File) testFiles
				.get("short_none"));
		fis.read(contN);
		fis.close();
		byte[] contR = new byte[992];
		fis = new FileInputStream((File) testFiles.get("short_v11reg"));
		fis.read(contR);
		fis.close();
		MP3TestCase.assertEqualsByteArray(contR, contN);
	}

	/**
	 * Tests ID3.checkForTag().
	 * 
	 * @throws Exception if an exception occurred
	 */
	public void testCheckForTag() throws Exception {
		ID3 id3 = new ID3((File) testFiles.get("short_none"));
		assertFalse(id3.checkForTag());
		id3 = new ID3((File) testFiles.get("short_v1reg"));
		assertTrue(id3.checkForTag());
		id3 = new ID3((File) testFiles.get("short_v11reg"));
		assertTrue(id3.checkForTag());
		id3 = new ID3((File) testFiles.get("short_v1spaces"));
		assertTrue(id3.checkForTag());
	}

}