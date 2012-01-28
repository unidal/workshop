package com.site.web.mvc.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.site.lookup.ComponentTestCase;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.NormalAction;
import com.site.web.mvc.Page;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.mvc.annotation.PayloadMeta;

public class AnnotationMatrixTest extends ComponentTestCase {
	@SuppressWarnings("unchecked")
	public void testNormal() throws Exception {
		AnnotationMatrix matrix = lookup(AnnotationMatrix.class);
		Method method = Mock.class.getMethod("inboundAction1");
		Class<? extends Annotation>[] classes = new Class[] { InboundActionMeta.class, PayloadMeta.class, OutboundActionMeta.class };

		matrix.addToMatrix(InboundActionMeta.class, PayloadMeta.class, 1, 0);
		matrix.addToMatrix(InboundActionMeta.class, OutboundActionMeta.class, -1);
		matrix.checkMatrix(method, classes);
	}

	@SuppressWarnings("unchecked")
	public void testConflict() throws Exception {
		AnnotationMatrix matrix = lookup(AnnotationMatrix.class);
		Method method = Mock.class.getMethod("inboundAction2");
		Class<? extends Annotation>[] classes = new Class[] { InboundActionMeta.class, PayloadMeta.class, OutboundActionMeta.class };

		matrix.addToMatrix(InboundActionMeta.class, PayloadMeta.class, 1, 0);
		matrix.addToMatrix(InboundActionMeta.class, OutboundActionMeta.class, -1);

		try {
			matrix.checkMatrix(method, classes);
			fail("RuntimeException should be thrown.");
		} catch (Exception e) {
			// expected
		}
	}

	public static final class Mock {
		@InboundActionMeta(name = "action1")
		@PayloadMeta(MockPayload.class)
		public void inboundAction1() {
		}

		@OutboundActionMeta(name = "action2")
		@InboundActionMeta(name = "action2")
		@PayloadMeta(MockPayload.class)
		public void inboundAction2() {
		}
	}

	static class DummyPage implements Page {
		public String getName() {
			return null;
		}

		public String getPath() {
		   return null;
		}
	}

	public static final class MockPayload implements ActionPayload<DummyPage, NormalAction> {
		public DummyPage getPage() {
			return null;
		}

		public void setPage(String action) {
		}

		public NormalAction getAction() {
			return null;
		}

		public void setAction(String action) {
		}

		@Override
		public void validate(ActionContext<?> ctx) {
		}
	}
}
