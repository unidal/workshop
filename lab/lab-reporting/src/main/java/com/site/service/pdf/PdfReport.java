package com.site.service.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.site.kernel.common.BaseXslTransformer;
import com.site.kernel.util.ClazzLoader;
import com.site.service.pdf.tag.DataParser;

public class PdfReport {
	public static void generateReport(XmlFeeder feeder, String xslTemplate, OutputStream output)
			throws TransformerConfigurationException, IOException, TransformerException, SAXException {

		URL template = ClazzLoader.getResource(xslTemplate);

		if (template == null) {
			throw new RuntimeException("Resource(" + xslTemplate + ") could not be found, please check your CLASSPATH.");
		}

		String xmlSource = feeder.buildXml();
		Reader reader = new StringReader(xmlSource);
		String xmlResult = new Transformer().transform(template, reader);
		DataParser parser = new DataParser();

		parser.setOutputStream(output);
		parser.parse(new InputSource(new StringReader(xmlResult)));
	}

	public static abstract class XmlFeeder {
		public abstract String buildXml();
	}

	private static final class Transformer extends BaseXslTransformer {
		public String transform(URL xslTemplate, Reader reader) throws TransformerConfigurationException, IOException,
				TransformerException {

			return doTransform(xslTemplate, reader, null);
		}
	}
}
