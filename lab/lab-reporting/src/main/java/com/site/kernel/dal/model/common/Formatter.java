package com.site.kernel.dal.model.common;

import com.site.kernel.dal.model.helpers.FormatterContext;

public interface Formatter {
	String EMPTY = "";
	String ONE_INDENT = "   ";

	String format(Object value, boolean indented) throws FormatingException;

	void handleEnd(FormatterContext context, Event e) throws ParsingException;

	void handleStart(FormatterContext context, Event e) throws ParsingException;

	void handleText(FormatterContext context, Event e) throws ParsingException;

	boolean ignoreWhiteSpaces();
}
