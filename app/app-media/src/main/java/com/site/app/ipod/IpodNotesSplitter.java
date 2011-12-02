package com.site.app.ipod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class IpodNotesSplitter {
	private int m_sectionSize = 3900;

	private File m_notesDir;

	private File m_sourceFile;

	private String m_basename;

	private static final String CRLF = "\r\n";

	public IpodNotesSplitter(File ipodRoot, File sourceFile) {
		m_notesDir = new File(ipodRoot, "Notes");
		m_sourceFile = sourceFile;

		if (!m_notesDir.exists()) {
			throw new IllegalArgumentException(m_notesDir + " does not exist.");
		} else if (!m_sourceFile.exists()) {
			throw new IllegalArgumentException(m_sourceFile
					+ " does not exist.");
		}

		String filename = sourceFile.getName();
		int pos = filename.lastIndexOf('.');
		String basename;

		if (pos >= 0) {
			basename = filename.substring(0, pos);
		} else {
			basename = filename;
		}

		m_basename = basename.replace('.', '-').replace(' ', '-');
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			showHelp();
			return;
		}
		String ipodRoot = (args.length > 0 ? args[0] : null);
		String sourceFile = (args.length > 1 ? args[1] : null);

		try {
			IpodNotesSplitter splitter = new IpodNotesSplitter(new File(
					ipodRoot), new File(sourceFile));

			splitter.split();
		} catch (Exception e) {
			e.printStackTrace();

			System.exit(1);
		}
	}

	private void split() throws IOException {
		long fileLength = m_sourceFile.length();
		int sections = (int) (fileLength - 1) / m_sectionSize + 1;

		createToc(sections);
		createSections(sections);
	}

	private void createSections(int sections) throws IOException {
		String content = getContent(new FileReader(m_sourceFile));
		File sectionDir = new File(m_notesDir, m_basename + ".files");
		StringBuilder sb = new StringBuilder(4096);

		sectionDir.mkdirs();

		for (int i = 0; i < sections; i++) {
			String lastSectionName = m_basename + "." + i;
			String nextSectionName = m_basename + "." + (i + 2);

			sb.setLength(0);

			if (i != 0) {
				sb.append("<a href=\"").append(lastSectionName).append(
						"\">LAST</a>").append("   ");
				sb.append("<a href=\"/").append(m_basename)
						.append("\">TOC</a>");
				sb.append(CRLF).append(CRLF);
			}

			int begin = i * m_sectionSize;
			int end = Math.min(begin + m_sectionSize, content.length());

			sb.append(content.substring(begin, end)).append(CRLF);

			if (i != sections - 1) {
				sb.append(CRLF).append(CRLF);
				sb.append("<a href=\"/").append(m_basename)
						.append("\">TOC</a>").append("   ");
				sb.append("<a href=\"").append(nextSectionName).append(
						"\">NEXT</a>");
			}

			File sectionFile = new File(sectionDir, m_basename + "." + (i + 1));
			Writer writer = new FileWriter(sectionFile);
			writer.write(sb.toString());
			writer.close();
			System.out.println("Section " + sectionFile + " generated");
		}
	}

	private String getContent(Reader reader) throws IOException {
		StringBuilder sb = new StringBuilder(16 * 1024);

		final char[] buffer = new char[4096];

		while (true) {
			int size = reader.read(buffer);

			if (size == -1) {
				break;
			} else {
				sb.append(buffer, 0, size);
			}
		}

		return sb.toString();
	}

	private void createToc(int sections) throws IOException {
		File tocFile = new File(m_notesDir, m_basename);
		StringBuilder sb = new StringBuilder(1024);

		sb.append("Table of Contents").append(CRLF).append(CRLF);

		sb.append("<a href=\"/\">BACK</a>").append(CRLF);

		for (int i = 0; i < sections; i++) {
			String sectionName = m_basename + ".files/" + m_basename + "."
					+ (i + 1);

			sb.append("<a href=\"").append(sectionName).append("\">Section ")
					.append(i + 1).append("</a>").append(CRLF);
		}

		Writer writer = new FileWriter(tocFile);

		writer.write(sb.toString());
		writer.close();
		System.out.println("TOC " + tocFile + " generated");
	}

	private static void showHelp() {
		System.out.println("java -jar app-ipod.jar ipod-root (source-file)+");
		System.out.println("   ipod-root      Root directory in iPod");
		System.out
				.println("   source-file    Source text file to be splitted.");
	}
}
