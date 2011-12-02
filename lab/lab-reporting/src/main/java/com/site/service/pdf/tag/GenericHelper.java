package com.site.service.pdf.tag;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.site.kernel.logging.Log;

public class GenericHelper {
	private GenericContainer m_generic;

	private static Log LOG = Log.getLog(GenericHelper.class);

	public void start(String tag, Stack parents, Attributes attrs) {
		GenericContainer generic;

		if (tag.equals("table")) {
			generic = new TableBo();
		} else if (tag.equals("tr")) {
			generic = new TrBo();
		} else if (tag.equals("td")) {
			generic = new TdBo();
		} else if (tag.equals("th")) {
			generic = new ThBo();
		} else if (tag.equals("p")) {
			generic = new PBo();
		} else if (tag.equals("br")) {
			generic = new BrBo();
		} else if (tag.equals("img")) {
			generic = new ImgBo();
		} else if (tag.equals("font")) {
			generic = new FontBo();
		} else if (tag.equals("header")) {
			generic = new HeaderBo();
		} else if (tag.equals("footer")) {
			generic = new FooterBo();
		} else {
			throw new RuntimeException("Unsupported tag(" + tag + ") found");
		}

		generic.loadAttributes(attrs);
		generic.setReady(parents);

		m_generic = generic;
	}

	public void end(String tag, Stack parents) throws DocumentException {
		Object obj = parents.peek();

		if (obj instanceof GenericHelper) {
			GenericHelper parent = (GenericHelper) obj;

			parent.getGenericContainer().addChild(m_generic);
		} else if (obj instanceof PageBo) {
			PageBo page = (PageBo) obj;

			page.addChild(m_generic);
		} else {
			throw new RuntimeException(obj + " can't contain " + m_generic);
		}
	}

	public GenericContainer getGenericContainer() {
		return m_generic;
	}

	public void addText(char[] ch, int start, int length) {
		try {
			m_generic.addChild(new TextBo(ch, start, length));
		} catch (Exception e) {
			// ignore it
			LOG.log(e);
		}
	}

	public static int getAlignment(String align) {
		if ("left".equalsIgnoreCase(align)) {
			return Element.ALIGN_LEFT;
		} else if ("right".equalsIgnoreCase(align)) {
			return Element.ALIGN_RIGHT;
		} else if ("center".equalsIgnoreCase(align)) {
			return Element.ALIGN_CENTER;
		} else if ("top".equalsIgnoreCase(align)) {
			return Element.ALIGN_TOP;
		} else if ("bottom".equalsIgnoreCase(align)) {
			return Element.ALIGN_BOTTOM;
		} else if ("middle".equalsIgnoreCase(align)) {
			return Element.ALIGN_MIDDLE;
		} else if ("baseline".equalsIgnoreCase(align)) {
			return Element.ALIGN_BASELINE;
		} else if ("justified".equalsIgnoreCase(align)) {
			return Element.ALIGN_JUSTIFIED;
		} else if ("justinfied-all".equalsIgnoreCase(align)) {
			return Element.ALIGN_JUSTIFIED_ALL;
		} else {
			return Element.ALIGN_UNDEFINED;
		}
	}

	public String toString() {
		return "GenericHelper[" + m_generic.toString() + "]";
	}
}
