package com.site.service.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.site.kernel.common.BaseTestCase;
import com.site.kernel.util.ClazzLoader;
import com.site.service.pdf.tag.DataParser;

public class PdfReportTest extends BaseTestCase {
	public void testAirfare() throws SAXException, IOException {
		File pdfFile = new File("target/Airfare.pdf");
		FileOutputStream fos = new FileOutputStream(pdfFile);
		URL url = ClazzLoader.getResource("sample_report.xml");
		InputSource inputSource = new InputSource(url.openStream());
		DataParser parser = new DataParser();

		parser.setOutputStream(fos);
		parser.parse(inputSource);
		fos.close();
	}

	public void testNestedTables() throws FileNotFoundException, DocumentException {
		// step1
		Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);

		// step2
		PdfWriter.getInstance(document, new FileOutputStream("target/NestedTables.pdf"));

		// step3
		document.open();

		// step4
		PdfPTable table = new PdfPTable(4);
		PdfPTable nested1 = new PdfPTable(2);

		nested1.addCell("1.1");
		nested1.addCell("1.2");

		PdfPTable nested2 = new PdfPTable(1);

		nested2.addCell("2.1");
		nested2.addCell("2.2");

		for (int k = 0; k < 24; ++k) {
			if (k == 1) {
				table.addCell(nested1);
			} else if (k == 20) {
				table.addCell(nested2);
			} else {
				table.addCell("cell " + k);
			}
		}

		document.add(table);
		// step 5: we close the document
		document.close();

	}
}
