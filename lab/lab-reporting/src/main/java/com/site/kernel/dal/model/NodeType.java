package com.site.kernel.dal.model;

import com.site.kernel.common.BaseEnum;


public class NodeType extends BaseEnum {
	public static final NodeType ATTRIBUTE = new NodeType(1, "ATTRIBUTE");
	public static final NodeType ELEMENT = new NodeType(2, "ELEMENT");
	public static final NodeType ELEMENT_CDATA = new NodeType(3, "ELEMENT_CDATA");
	public static final NodeType TEXT = new NodeType(4, "TEXT");
	public static final NodeType TEXT_CDATA = new NodeType(5, "TEXT_CDATA");
	public static final NodeType MODEL = new NodeType(6, "MODEL");

	protected NodeType(int id, String name) {
		super(id, name);
	}
}
