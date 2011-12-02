package org.unidal.ezsell.view;

import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.EbayPage;

import com.site.lookup.util.ByteArrayUtils;
import com.site.web.mvc.Action;
import com.site.web.mvc.view.BaseFileViewer;

public class FileViewer extends BaseFileViewer<EbayPage, Action, EbayContext, EbayModel> {
	@Override
	protected String getFileName(EbayContext ctx, EbayModel model) {
		FileAction action = model.getFileAction();

		return action.getFileName(model.getFileId());
	}

	@Override
	protected String getJspFilePath(EbayContext ctx, EbayModel model) {
		FileAction action = model.getFileAction();

		return action.getPath();
	}

	@Override
	protected String getMimeType(EbayContext ctx, EbayModel model) {
		FileAction action = model.getFileAction();

		return action.getMimeType();
	}

	@Override
	protected byte[] process(EbayContext ctx, EbayModel model, byte[] content) {
		return ByteArrayUtils.trim(content);
	}
}
