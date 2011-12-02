package com.site.kernel.dal.model;

public class XmlModelKey {
	private XmlModelField[] m_fields;
	
	public XmlModelKey(XmlModelField[] fields) {
		m_fields = fields;
	}

	public XmlModelField[] getFields() {
		return m_fields;
	}
}
