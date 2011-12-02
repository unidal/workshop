package org.unidal.ezsell.view;

import java.io.ByteArrayOutputStream;

import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.EbayPage;

import com.site.lookup.annotation.Inject;
import com.site.lookup.util.ByteArrayUtils;
import com.site.web.mvc.Action;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.view.BaseFileViewer;

public class ReportViewer extends BaseFileViewer<EbayPage, Action, EbayContext, EbayModel> {
	@Inject
	private ReportGenerator m_generator;

	@Override
	protected String getFileName(EbayContext ctx, EbayModel model) {
		ReportAction reportAction = model.getReportAction();

		return reportAction.getFileName(model.getReportId());
	}

	@Override
	protected String getJspFilePath(EbayContext ctx, EbayModel model) {
		ReportAction reportAction = model.getReportAction();

		return reportAction.getPath();
	}

	@Override
	protected String getMimeType(EbayContext ctx, EbayModel model) {
		return "application/pdf";
	}

	@Override
	protected byte[] process(EbayContext ctx, EbayModel model, byte[] content) {
		try {
			byte[] trimmedContent = ByteArrayUtils.trim(content);
			ByteArrayOutputStream pdfContent = new ByteArrayOutputStream();
			String xmlContent = new String(trimmedContent, "utf-8");

			m_generator.generate(xmlContent, pdfContent);
			return pdfContent.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();

			ctx.addError(new ErrorObject("pdf.generate.error", e));
		}

		return null;
	}
}
