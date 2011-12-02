package com.site.media.mp3;

import java.io.File;

import com.site.media.mp3.MP3File;
import com.site.media.mp3.MP3Properties;
import com.site.media.mp3.TagContent;

/**
 * Test case for class MP3File
 */
public class MP3FileTest extends MP3TestCase {

	/**
	 * Create some more test files with different MPEG frame properties. These
	 * files are only used in this test case, there is no need to create them im
	 * MP3TestCase.
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// mpeg1 layer1 bitrate 160 frequency 44100
		byte[] leadinM1L1B160 = new byte[silenceLeadin.length];
		System.arraycopy(silenceLeadin, 0, leadinM1L1B160, 0,
				silenceLeadin.length);
		leadinM1L1B160[1] = (byte) 0xfe;
		leadinM1L1B160[2] = (byte) 0x50;
		byte[] frameM1L1B160 = new byte[silenceFrame.length];
		System
				.arraycopy(silenceFrame, 0, frameM1L1B160, 0,
						silenceFrame.length);
		frameM1L1B160[1] = (byte) 0xfe;
		frameM1L1B160[2] = (byte) 0x50;
		createMP3("M1L1B160", null, null, 3, leadinM1L1B160, frameM1L1B160);

		// mpeg1 layer2 bitrate 160 frequency 48000
		byte[] leadinM1L2B160 = new byte[silenceLeadin.length];
		System.arraycopy(silenceLeadin, 0, leadinM1L2B160, 0,
				silenceLeadin.length);
		leadinM1L2B160[1] = (byte) 0xfc;
		leadinM1L2B160[2] = (byte) 0x94;
		byte[] frameM1L2B160 = new byte[silenceFrame.length];
		System
				.arraycopy(silenceFrame, 0, frameM1L2B160, 0,
						silenceFrame.length);
		frameM1L2B160[1] = (byte) 0xfc;
		frameM1L2B160[2] = (byte) 0x94;
		createMP3("M1L2B160", null, null, 3, leadinM1L2B160, frameM1L2B160);

		// mpeg1 layer3 bitrate 160 frequency 32000
		byte[] leadinM1L3B160 = new byte[silenceLeadin.length];
		System.arraycopy(silenceLeadin, 0, leadinM1L3B160, 0,
				silenceLeadin.length);
		leadinM1L3B160[1] = (byte) 0xfb;
		leadinM1L3B160[2] = (byte) 0xa8;
		byte[] frameM1L3B160 = new byte[silenceFrame.length];
		System
				.arraycopy(silenceFrame, 0, frameM1L3B160, 0,
						silenceFrame.length);
		frameM1L3B160[1] = (byte) 0xfb;
		frameM1L3B160[2] = (byte) 0xa8;
		createMP3("M1L3B160", null, null, 3, leadinM1L3B160, frameM1L3B160);

		// mpeg2 layer1 bitrate 160 frequency 22050
		byte[] leadinM2L1B160 = new byte[silenceLeadin.length];
		System.arraycopy(silenceLeadin, 0, leadinM2L1B160, 0,
				silenceLeadin.length);
		leadinM2L1B160[1] = (byte) 0xf6;
		leadinM2L1B160[2] = (byte) 0x50;
		byte[] frameM2L1B160 = new byte[silenceFrame.length];
		System
				.arraycopy(silenceFrame, 0, frameM2L1B160, 0,
						silenceFrame.length);
		frameM2L1B160[1] = (byte) 0xf6;
		frameM2L1B160[2] = (byte) 0x50;
		createMP3("M2L1B160", null, null, 3, leadinM2L1B160, frameM2L1B160);

		// mpeg2 layer2 bitrate 160 frequency 24000
		byte[] leadinM2L2B160 = new byte[silenceLeadin.length];
		System.arraycopy(silenceLeadin, 0, leadinM2L2B160, 0,
				silenceLeadin.length);
		leadinM2L2B160[1] = (byte) 0xf4;
		leadinM2L2B160[2] = (byte) 0x94;
		byte[] frameM2L2B160 = new byte[silenceFrame.length];
		System
				.arraycopy(silenceFrame, 0, frameM2L2B160, 0,
						silenceFrame.length);
		frameM2L2B160[1] = (byte) 0xf4;
		frameM2L2B160[2] = (byte) 0x94;
		createMP3("M2L2B160", null, null, 3, leadinM2L2B160, frameM2L2B160);

		// mpeg2 layer3 bitrate 160 frequency 16000
		byte[] leadinM2L3B160 = new byte[silenceLeadin.length];
		System.arraycopy(silenceLeadin, 0, leadinM2L3B160, 0,
				silenceLeadin.length);
		leadinM2L3B160[1] = (byte) 0xf2;
		leadinM2L3B160[2] = (byte) 0xa8;
		byte[] frameM2L3B160 = new byte[silenceFrame.length];
		System
				.arraycopy(silenceFrame, 0, frameM2L3B160, 0,
						silenceFrame.length);
		frameM2L3B160[1] = (byte) 0xf2;
		frameM2L3B160[2] = (byte) 0xa8;
		createMP3("M2L3B160", null, null, 3, leadinM2L3B160, frameM2L3B160);

	}

	/**
	 * Test updating a larger MP3 file in a way that requires using a temporary
	 * file.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testUpdateLargeFileWithTemp() throws Exception {
		createMP3("larger", null, null, 3000);
		MP3File mp3 = new MP3File(((File) testFiles.get("larger"))
				.getAbsolutePath());
		mp3.setWriteID3(true);
		mp3.setWriteID3v2(true);
		long origLength = mp3.length();

		TagContent t = new TagContent();

		t.setContent("Artist");
		mp3.setArtist(t);
		mp3.update();

		assertTrue("File is larger after update (" + mp3.length() + ">"
				+ origLength, mp3.length() > origLength);
	}

	/**
	 * Test updating an MP3 file which already contains an ID3v2 area large
	 * enough to take the necessary updates (i.e. no temporary file is used)
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testUpdateFileWithEnoughPadding() throws Exception {
		byte[] v2Padding = new byte[2048];
		System.arraycopy(defV2, 0, v2Padding, 0, defV2.length);
		for (int i = defV2.length; i < 2048; i++) {
			v2Padding[i] = 0;
		}
		v2Padding[8] = (byte) 0xf;
		v2Padding[9] = (byte) 0x76;
		createMP3("withpadding", null, v2Padding, 3000);

		MP3File mp3 = new MP3File(((File) testFiles.get("withpadding"))
				.getAbsolutePath());

		// there is no exact way to test that the file is left
		// intact (TODO: think of one)
		// Compare the filesize before and after the update
		long origLength = mp3.length();

		mp3.setWriteID3(true);
		mp3.setWriteID3v2(true);
		mp3.setUsePadding(true);

		TagContent t = new TagContent();

		t.setContent("Artist");
		mp3.setArtist(t);

		t.setContent("Album");
		mp3.setAlbum(t);

		t.setContent("Title");
		mp3.setTitle(t);

		t.setContent("4");
		mp3.setTrack(t);

		t.setContent("Pop");
		mp3.setGenre(t);

		mp3.update();

		assertTrue("File is larger after update (" + mp3.length() + ">"
				+ origLength, mp3.length() > origLength);
	}

	/**
	 * Tests the getMPEGLevel method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetMPEGLevel() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(1, mp3.getMPEGLevel());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(1, mp3.getMPEGLevel());

		mp3 = new MP3File(((File) testFiles.get("M1L1B160")).getAbsolutePath());
		assertEquals(1, mp3.getMPEGLevel());

		mp3 = new MP3File(((File) testFiles.get("M1L2B160")).getAbsolutePath());
		assertEquals(1, mp3.getMPEGLevel());

		mp3 = new MP3File(((File) testFiles.get("M1L3B160")).getAbsolutePath());
		assertEquals(1, mp3.getMPEGLevel());

		mp3 = new MP3File(((File) testFiles.get("M2L1B160")).getAbsolutePath());
		assertEquals(2, mp3.getMPEGLevel());

		mp3 = new MP3File(((File) testFiles.get("M2L2B160")).getAbsolutePath());
		assertEquals(2, mp3.getMPEGLevel());

		mp3 = new MP3File(((File) testFiles.get("M2L3B160")).getAbsolutePath());
		assertEquals(2, mp3.getMPEGLevel());
	}

	/**
	 * Tests the getLayer method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetLayer() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(3, mp3.getLayer());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(3, mp3.getLayer());

		mp3 = new MP3File(((File) testFiles.get("M1L1B160")).getAbsolutePath());
		assertEquals(1, mp3.getLayer());

		mp3 = new MP3File(((File) testFiles.get("M1L2B160")).getAbsolutePath());
		assertEquals(2, mp3.getLayer());

		mp3 = new MP3File(((File) testFiles.get("M1L3B160")).getAbsolutePath());
		assertEquals(3, mp3.getLayer());

		mp3 = new MP3File(((File) testFiles.get("M2L1B160")).getAbsolutePath());
		assertEquals(1, mp3.getLayer());

		mp3 = new MP3File(((File) testFiles.get("M2L2B160")).getAbsolutePath());
		assertEquals(2, mp3.getLayer());

		mp3 = new MP3File(((File) testFiles.get("M2L3B160")).getAbsolutePath());
		assertEquals(3, mp3.getLayer());
	}

	/**
	 * Tests the getBitrate method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetBitrate() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(64, mp3.getBitrate());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(64, mp3.getBitrate());

		mp3 = new MP3File(((File) testFiles.get("M1L1B160")).getAbsolutePath());
		assertEquals(160, mp3.getBitrate());

		mp3 = new MP3File(((File) testFiles.get("M1L2B160")).getAbsolutePath());
		assertEquals(160, mp3.getBitrate());

		mp3 = new MP3File(((File) testFiles.get("M1L3B160")).getAbsolutePath());
		assertEquals(160, mp3.getBitrate());

		mp3 = new MP3File(((File) testFiles.get("M2L1B160")).getAbsolutePath());
		assertEquals(160, mp3.getBitrate());

		mp3 = new MP3File(((File) testFiles.get("M2L2B160")).getAbsolutePath());
		assertEquals(160, mp3.getBitrate());

		mp3 = new MP3File(((File) testFiles.get("M2L3B160")).getAbsolutePath());
		assertEquals(160, mp3.getBitrate());
	}

	/**
	 * Tests the getSamplerate method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetSamplerate() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(32000, mp3.getSamplerate());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(32000, mp3.getSamplerate());

		mp3 = new MP3File(((File) testFiles.get("M1L1B160")).getAbsolutePath());
		assertEquals(44100, mp3.getSamplerate());

		mp3 = new MP3File(((File) testFiles.get("M1L2B160")).getAbsolutePath());
		assertEquals(48000, mp3.getSamplerate());

		mp3 = new MP3File(((File) testFiles.get("M1L3B160")).getAbsolutePath());
		assertEquals(32000, mp3.getSamplerate());

		mp3 = new MP3File(((File) testFiles.get("M2L1B160")).getAbsolutePath());
		assertEquals(22050, mp3.getSamplerate());

		mp3 = new MP3File(((File) testFiles.get("M2L2B160")).getAbsolutePath());
		assertEquals(24000, mp3.getSamplerate());

		mp3 = new MP3File(((File) testFiles.get("M2L3B160")).getAbsolutePath());
		assertEquals(16000, mp3.getSamplerate());
	}

	/**
	 * Tests the getMode method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetMode() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(MP3Properties.MODE_MONO, mp3.getMode());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(MP3Properties.MODE_MONO, mp3.getMode());

		// TODO Test different modes
	}

	/**
	 * Tests the getEmphasis method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetEmphasis() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(MP3Properties.EMPHASIS_NONE, mp3.getEmphasis());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(MP3Properties.EMPHASIS_NONE, mp3.getEmphasis());

		// TODO Test different emphasises
	}

	/**
	 * Tests the getProtection method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetProtection() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(false, mp3.getProtection());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(false, mp3.getProtection());

		// TODO Test protection on
	}

	/**
	 * Tests the getPrivate method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetPrivate() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(false, mp3.getPrivate());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(false, mp3.getPrivate());

		// TODO Test private on
	}

	/**
	 * Tests the getPadding method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetPadding() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(false, mp3.getPadding());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(false, mp3.getPadding());

		// TODO Test padding on
	}

	/**
	 * Tests the getCopyright method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetCopyright() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(false, mp3.getCopyright());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(false, mp3.getCopyright());

		// TODO Test copyright on
	}

	/**
	 * Tests the getOriginal method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetOriginal() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(false, mp3.getOriginal());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(false, mp3.getOriginal());

		// TODO Test original on
	}

	/**
	 * Tests the getLength method.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetLength() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());
		assertEquals(0L, mp3.getLength());

		mp3 = new MP3File(((File) testFiles.get("short_v12reg"))
				.getAbsolutePath());
		assertEquals(0L, mp3.getLength());

		// TODO Test long file
	}

	/**
	 * Tests setting and getting various information.
	 * 
	 * @exception Exception if an exception occurred
	 */
	public void testGetSetInfo() throws Exception {
		MP3File mp3 = new MP3File(((File) testFiles.get("short_none"))
				.getAbsolutePath());

		byte[] bin = { (byte) 7, (byte) 10, (byte) 99 };

		TagContent content = new TagContent();
		content.setContent("Artist");
		mp3.setArtist(content);
		assertEquals("Artist", mp3.getArtist().getTextContent());

		content = new TagContent();
		content.setContent("Album");
		mp3.setAlbum(content);
		assertEquals("Album", mp3.getAlbum().getTextContent());

		content = new TagContent();
		content.setContent("Title");
		mp3.setTitle(content);
		assertEquals("Title", mp3.getTitle().getTextContent());

		content = new TagContent();
		content.setContent("4");
		mp3.setTrack(content);
		assertEquals("4", mp3.getTrack().getTextContent());

		content = new TagContent();
		content.setContent("Other (12)");
		mp3.setTrack(content);
		assertEquals("Other (12)", mp3.getTrack().getTextContent());

		content = new TagContent();
		content.setContent("2004");
		mp3.setYear(content);
		assertEquals("2004", mp3.getYear().getTextContent());

		content = new TagContent();
		content.setContent("120");
		mp3.setBPM(content);
		assertEquals("120", mp3.getBPM().getTextContent());

		content = new TagContent();
		content.setContent("Composer");
		mp3.setComposer(content);
		assertEquals("Composer", mp3.getComposer().getTextContent());

		content = new TagContent();
		content.setContent("CopyrightText");
		mp3.setCopyrightText(content);
		assertEquals("CopyrightText", mp3.getCopyrightText().getTextContent());

		content = new TagContent();
		content.setContent("0212");
		mp3.setDate(content);
		assertEquals("0212", mp3.getDate().getTextContent());

		content = new TagContent();
		content.setContent("12");
		mp3.setPlaylistDelay(content);
		assertEquals("12", mp3.getPlaylistDelay().getTextContent());

		content = new TagContent();
		content.setContent("Encoded by");
		mp3.setEncodedBy(content);
		assertEquals("Encoded by", mp3.getEncodedBy().getTextContent());

		content = new TagContent();
		content.setContent("Lyricist");
		mp3.setLyricist(content);
		assertEquals("Lyricist", mp3.getLyricist().getTextContent());

		content = new TagContent();
		content.setContent("File type");
		mp3.setFileType(content);
		assertEquals("File type", mp3.getFileType().getTextContent());

		content = new TagContent();
		content.setContent("1215");
		mp3.setTime(content);
		assertEquals("1215", mp3.getTime().getTextContent());

		content = new TagContent();
		content.setContent("Content group");
		mp3.setContentGroup(content);
		assertEquals("Content group", mp3.getContentGroup().getTextContent());

		content = new TagContent();
		content.setContent("Subtitle");
		mp3.setSubtitle(content);
		assertEquals("Subtitle", mp3.getSubtitle().getTextContent());

		content = new TagContent();
		content.setContent("C#m");
		mp3.setInitialKey(content);
		assertEquals("C#m", mp3.getInitialKey().getTextContent());

		content = new TagContent();
		content.setContent("eng");
		mp3.setLanguage(content);
		assertEquals("eng", mp3.getLanguage().getTextContent());

		content = new TagContent();
		content.setContent("200000");
		mp3.setLengthInTag(content);
		assertEquals("200000", mp3.getLengthInTag().getTextContent());

		content = new TagContent();
		content.setContent("Media type");
		mp3.setMediaType(content);
		assertEquals("Media type", mp3.getMediaType().getTextContent());

		content = new TagContent();
		content.setContent("Original title");
		mp3.setOriginalTitle(content);
		assertEquals("Original title", mp3.getOriginalTitle().getTextContent());

		content = new TagContent();
		content.setContent("Original filename");
		mp3.setOriginalFilename(content);
		assertEquals("Original filename", mp3.getOriginalFilename()
				.getTextContent());

		content = new TagContent();
		content.setContent("Original lyricist");
		mp3.setOriginalLyricist(content);
		assertEquals("Original lyricist", mp3.getOriginalLyricist()
				.getTextContent());

		content = new TagContent();
		content.setContent("Original artist");
		mp3.setOriginalArtist(content);
		assertEquals("Original artist", mp3.getOriginalArtist()
				.getTextContent());

		content = new TagContent();
		content.setContent("2001");
		mp3.setOriginalYear(content);
		assertEquals("2001", mp3.getOriginalYear().getTextContent());

		content = new TagContent();
		content.setContent("File owner");
		mp3.setFileOwner(content);
		assertEquals("File owner", mp3.getFileOwner().getTextContent());

		content = new TagContent();
		content.setContent("Band");
		mp3.setBand(content);
		assertEquals("Band", mp3.getBand().getTextContent());

		content = new TagContent();
		content.setContent("Conductor");
		mp3.setConductor(content);
		assertEquals("Conductor", mp3.getConductor().getTextContent());

		content = new TagContent();
		content.setContent("Remixer");
		mp3.setRemixer(content);
		assertEquals("Remixer", mp3.getRemixer().getTextContent());

		content = new TagContent();
		content.setContent("1/2");
		mp3.setPartOfSet(content);
		assertEquals("1/2", mp3.getPartOfSet().getTextContent());

		content = new TagContent();
		content.setContent("Publisher");
		mp3.setPublisher(content);
		assertEquals("Publisher", mp3.getPublisher().getTextContent());

		content = new TagContent();
		content.setContent("Recording dates");
		mp3.setRecordingDate(content);
		assertEquals("Recording dates", mp3.getRecordingDates()
				.getTextContent());

		content = new TagContent();
		content.setContent("Internet radio station name");
		mp3.setInternetRadioStationName(content);
		assertEquals("Internet radio station name", mp3
				.getInternetRadioStationName().getTextContent());

		content = new TagContent();
		content.setContent("Internet radio station owner");
		mp3.setInternetRadioStationOwner(content);
		assertEquals("Internet radio station owner", mp3
				.getInternetRadioStationOwner().getTextContent());

		content = new TagContent();
		content.setContent("12000");
		mp3.setFilesize(content);
		assertEquals("12000", mp3.getFilesize().getTextContent());

		content = new TagContent();
		content.setContent("1234567890ab");
		mp3.setISRC(content);
		assertEquals("1234567890ab", mp3.getISRC().getTextContent());

		content = new TagContent();
		content.setContent("Commercial information");
		mp3.setCommercialInformation(content);
		assertEquals("Commercial information", mp3.getCommercialInformation()
				.getTextContent());

		content = new TagContent();
		content.setContent("http://www.gnu.org/lgpl.html");
		mp3.setCopyrightWebpage(content);
		assertEquals("http://www.gnu.org/lgpl.html", mp3.getCopyrightWebpage()
				.getTextContent());

		content = new TagContent();
		content.setContent("http://www.web.org/audio.html");
		mp3.setAudioFileWebpage(content);
		assertEquals("http://www.web.org/audio.html", mp3.getAudioFileWebpage()
				.getTextContent());

		content = new TagContent();
		content.setContent("http://www.web.org/artist.html");
		mp3.setArtistWebpage(content);
		assertEquals("http://www.web.org/artist.html", mp3.getArtistWebpage()
				.getTextContent());

		content = new TagContent();
		content.setContent("http://www.web.org/source.html");
		mp3.setAudioSourceWebpage(content);
		assertEquals("http://www.web.org/source.html", mp3
				.getAudioSourceWebpage().getTextContent());

		content = new TagContent();
		content.setContent("http://www.web.org/radio.html");
		mp3.setInternetRadioStationWebpage(content);
		assertEquals("http://www.web.org/radio.html", mp3
				.getInternetRadioStationWebpage().getTextContent());

		content = new TagContent();
		content.setContent("http://www.web.org/pay.html");
		mp3.setPaymentWebpage(content);
		assertEquals("http://www.web.org/pay.html", mp3.getPaymentWebpage()
				.getTextContent());

		content = new TagContent();
		content.setContent("http://www.web.org/publisher.html");
		mp3.setPublishersWebpage(content);
		assertEquals("http://www.web.org/publisher.html", mp3
				.getPublishersWebpage().getTextContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setEventTimingCodes(content);
		assertEquals(bin, mp3.getEventTimingCodes().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setLookupTable(content);
		assertEquals(bin, mp3.getLookupTable().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setSynchronizedTempoCodes(content);
		assertEquals(bin, mp3.getSynchronizedTempoCodes().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setSynchronizedLyrics(content);
		assertEquals(bin, mp3.getSynchronizedLyrics().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setRelativeVolumeAdjustment(content);
		assertEquals(bin, mp3.getRelativeVolumenAdjustment().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setEqualisation(content);
		assertEquals(bin, mp3.getEqualisation().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setReverb(content);
		assertEquals(bin, mp3.getReverb().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setPlayCounter(content);
		assertEquals(bin, mp3.getPlayCounter().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setPopularimeter(content);
		assertEquals(bin, mp3.getPopularimeter().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setRecommendedBufferSize(content);
		assertEquals(bin, mp3.getRecommendedBufferSize().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setPositionSynchronization(content);
		assertEquals(bin, mp3.getPositionSynchronization().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setOwnership(content);
		assertEquals(bin, mp3.getOwnership().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setCommercial(content);
		assertEquals(bin, mp3.getCommercial().getBinaryContent());

		content = new TagContent();
		content.setContent(bin);
		mp3.setCDIdentifier(content);
		assertEquals(bin, mp3.getCDIdentifier().getBinaryContent());
	}
}