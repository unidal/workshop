package com.site.kernel.dal.model.common;

import org.xml.sax.Attributes;

/**
 * @author qwu
 */
public class Event {
	private static final int START_ELEMENT = 1;
	private static final int TEXT = 2;
	private static final int END_ELEMENT = 3;

	private Attributes m_attributes;
	private String m_localName;
	private String m_namespaceURI;
	private String m_rawName;
	private String m_text;
	private int m_type;

	public Event(String namespaceURI, String localName, String rawName, Attributes attributes) {
		m_namespaceURI = namespaceURI;
		m_localName = localName;
		m_rawName = rawName;
		m_attributes = attributes;

		if (attributes != null) {
			m_type = START_ELEMENT;
		} else {
			m_type = END_ELEMENT;
		}
	}

	public Event(String text) {
		m_text = text;
		m_type = TEXT;
	}

	public Attributes getAttributes() {
		return m_attributes;
	}

	public String getLocalName() {
		return m_localName;
	}

	public String getNamespaceURI() {
		return m_namespaceURI;
	}

	public String getRawName() {
		return m_rawName;
	}

	public String getText() {
		return m_text;
	}

	public boolean isStartElement() {
		return m_type == START_ELEMENT;
	}

	public boolean isEndElement() {
		return m_type == END_ELEMENT;
	}

	public boolean isText() {
		return m_type == TEXT;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Event[");

		switch (m_type) {
		case START_ELEMENT:
			sb.append("<").append(m_localName).append(">");
			break;
		case END_ELEMENT:
			sb.append("</").append(m_localName).append(">");
			break;
		case TEXT:
			sb.append(m_text);
			break;
		}

		sb.append("]");

		return sb.toString();
	}

}
