package com.site.kernel.dal.model;

import com.site.kernel.common.BaseEnum;


public abstract class Namespace extends BaseEnum {
	public static final String NO_ALIAS = "";

	private String m_alias;

	protected Namespace(int id, String alias, String namespaceURI) {
		super(id, namespaceURI);

		m_alias = (alias == null ? "" : alias);

		if (namespaceURI == null) {
			throw new IllegalArgumentException("namespaceURI should not be null, use empty string instead");
		}
	}

	public String getAlias() {
		return m_alias;
	}

	public boolean hasAlias() {
		return !NO_ALIAS.equals(m_alias);
	}

	public boolean hasNamespaceURI() {
		return getName().length() > 0;
	}
}
