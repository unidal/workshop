package org.unidal.ezsell.view;

import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.EbayPage;

import com.site.web.mvc.Action;
import com.site.web.mvc.view.BaseFileViewer;

public class EmailViewer extends BaseFileViewer<EbayPage, Action, EbayContext, EbayModel> {
	@Override
	protected String getJspFilePath(EbayContext ctx, EbayModel model) {
		return model.getEmailId().getPath();
	}

	@Override
	protected String getFileName(EbayContext ctx, EbayModel model) {
		return null;
	}

	@Override
	protected String getMimeType(EbayContext ctx, EbayModel model) {
		return null;
	}

	@Override
	protected byte[] process(EbayContext ctx, EbayModel model, byte[] content) {
		// EmailMessage message = new EmailMessage("register_confirm", false);
		// Email email = (Email)
		// ctx.getHttpServletRequest().getAttribute("email");
		//
		// try {
		// message.setSubject(email.getSubject());
		// message.setTextMessage(email.getTextContent());
		// message.setHtmlMessage(email.getHtmlContent());
		// message.setFrom("qmwu2000@gmail.com", "Unidal.org");
		// message.addTo("qmwu2009@gmail.com", "Frankie");
		// message.setSmtpHost("smtp.google.com");
		// message.setSmtpPort(25);
		// message.setAuthentication("qmwu2000@gmail.com", "Imfrankie");
		// message.setDebug(true);
		//
		// message.send();
		// } catch (EmailException e) {
		// e.printStackTrace();
		// }

		return null;
	}
}
