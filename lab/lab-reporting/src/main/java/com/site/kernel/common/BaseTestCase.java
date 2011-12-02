package com.site.kernel.common;

import java.util.Stack;

import junit.framework.TestCase;

import com.site.kernel.Module;
import com.site.kernel.initialization.BaseModule;

public abstract class BaseTestCase extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();

      Module.FULL.doInitialization(null, new Stack<BaseModule>(), true);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected void printMethodHeader() {
		Exception testee = new Exception();
		String methodName = testee.getStackTrace()[1].getMethodName();

		out("-------------------------------------------------------");
		out("Visual output for " + methodName + ":");
		out("-------------------------------------------------------");
	}

	protected void out(Object message) {
		if (message == null) {
			message = "null";
		}

		System.out.println(message.toString());
	}

	protected void out(String message) {
		System.out.println(message);
	}
}
