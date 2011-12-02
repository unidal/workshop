package com.site.web.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.site.web.http.HttpServletResponseWrapper;
import com.site.web.mvc.Action;
import com.site.web.mvc.Page;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ViewModel;

public abstract class BaseFileViewer<P extends Page, A extends Action, S extends ActionContext<?>, T extends ViewModel<P, A, S>>
		implements Viewer<P, A, S, T> {
	/**
	 * return file name to be shown in browser download dialog.
	 * <p>
	 * 
	 * Note: This method will be ignore if the {@link BasePdfViewer.process()}
	 * return null.
	 * 
	 * @return file name to be shown in browser download dialog.
	 */
	protected abstract String getFileName(S ctx, T model);

	/**
	 * return JSP file path (relative to warRoot) to forward and get the result
	 * for further process.
	 * <p>
	 * 
	 * Note: This method will be ignore if the {@link BasePdfViewer.process()}
	 * return null.
	 * 
	 * @return JSP file path (relative to warRoot) to forward.
	 */
	protected abstract String getJspFilePath(S ctx, T model);

	/**
	 * return mime-type for file to be downloaded.
	 * <p>
	 * 
	 * Note: This method will be ignore if the {@link BasePdfViewer.process()}
	 * return null.
	 * 
	 * @return mime-type. for example, text/html, text/xml, application/pdf etc.
	 */
	protected abstract String getMimeType(S ctx, T model);

	/**
	 * @return processed content, null returned will stop further flush
	 */
	protected abstract byte[] process(S ctx, T model, byte[] content);

	public void view(S ctx, T model) throws ServletException, IOException {
		HttpServletRequest req = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		HttpServletResponseWrapper res = new HttpServletResponseWrapper(response, true);

		req.setAttribute("payload", ctx.getPayload());
		req.setAttribute("model", model);
		req.getRequestDispatcher(getJspFilePath(ctx, model)).forward(req, res);

		byte[] result = process(ctx, model, res.getByteArray());

		if (result != null) {
			ServletOutputStream sos = response.getOutputStream();
			String fileName = getFileName(ctx, model);

			response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
			response.setContentType(getMimeType(ctx, model));
			response.setContentLength(result.length);
			sos.write(result);
			sos.close();
		} else {
			// ignore if result is null
		}
	}
}
