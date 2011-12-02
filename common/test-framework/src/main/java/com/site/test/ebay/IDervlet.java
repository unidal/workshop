package com.site.test.ebay;

import javax.servlet.Servlet;

/**
 * A Dervlet is a Servlet with a specified mapping path
 */
public interface IDervlet extends Servlet {
	String getPath();
}
