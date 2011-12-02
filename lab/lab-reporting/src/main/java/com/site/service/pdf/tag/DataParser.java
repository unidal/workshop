package com.site.service.pdf.tag;

import java.io.OutputStream;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.site.kernel.common.BaseXmlHandler;

public class DataParser extends BaseXmlHandler {
	private Stack m_objs = new Stack(); // ELEMENT object

	private Stack m_tags = new Stack(); // ELEMENT tag

	private OutputStream m_outputStream;

	private FontManager m_fontManager = FontManager.getInstance();

	public void setOutputStream(OutputStream outputStream) {
		m_outputStream = outputStream;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		Object obj = m_objs.peek();

		if (obj instanceof GenericHelper) {
			GenericHelper generic = (GenericHelper) obj;

			generic.addText(ch, start, length);
		}
	}

	public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs)
			throws SAXException {
		String tag = localName;

		try {
			if (tag.equals("document")) {
				DocumentBo document = new DocumentBo();

				document.loadAttributes(attrs);

				if (m_tags.isEmpty()) {
					document.createDocument(m_outputStream);
				} else {
					throw new SAXException("document(" + document + ") must be the root element");
				}

				m_objs.push(document);
			} else if (tag.equals("base-font")) {
				BaseFontBo baseFont = new BaseFontBo();

				baseFont.loadAttributes(attrs);

				String peek = (String) m_tags.peek();

				if (peek.equals("document")) {
					baseFont.start(m_objs);
				} else {
					throw new SAXException("base-font(" + baseFont + ") must go under document");
				}

				m_objs.push(baseFont);
			} else if (tag.equals("page")) {
				PageBo page = new PageBo();

				page.loadAttributes(attrs);

				String peek = (String) m_tags.peek();

				if (peek.equals("document")) {
					page.start(m_objs);
				} else {
					throw new SAXException("page(" + page + ") must go under document");
				}

				m_objs.push(page);
			} else {
				GenericHelper generic = new GenericHelper();

				generic.start(tag, m_objs, attrs);
				m_objs.push(generic);

				if (tag.equals("font")) {
					m_fontManager.pushFontBo((FontBo) generic.getGenericContainer());
				}
			}
		} catch (Exception e) {
			SAXException saxe = new SAXException(e);

			saxe.setStackTrace(e.getStackTrace());
			throw saxe;
		}

		m_tags.push(tag);
	}

	public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
		Object obj = m_objs.pop();
		String tag = (String) m_tags.pop();

		try {
			if (tag.equals("document")) {
				DocumentBo document = (DocumentBo) obj;

				document.closeDocument();
			} else if (tag.equals("base-font")) {
				BaseFontBo baseFont = (BaseFontBo) obj;

				baseFont.end(m_objs);
			} else if (tag.equals("page")) {
				PageBo page = (PageBo) obj;

				page.end(m_objs);
			} else {
				GenericHelper generic = (GenericHelper) obj;

				generic.end(tag, m_objs);

				if (tag.equals("font")) {
					m_fontManager.popFontBo();
				}
			}
		} catch (Exception e) {
			SAXException saxe = new SAXException(e);

			saxe.setStackTrace(e.getStackTrace());
			throw saxe;
		}
	}

}
