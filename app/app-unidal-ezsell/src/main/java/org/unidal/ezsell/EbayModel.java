package org.unidal.ezsell;

import java.util.List;

import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.transaction.CustomsDeclaration;
import org.unidal.ezsell.view.EmailId;
import org.unidal.ezsell.view.FileAction;
import org.unidal.ezsell.view.ReportAction;

import com.site.web.mvc.Action;
import com.site.web.mvc.ViewModel;

public class EbayModel extends ViewModel<EbayPage, Action, EbayContext> {
	private Seller m_seller;

	private int m_loginUserId;

	// For seller transactions
	private List<Transaction> m_transactions;

	// For seller transaction detail
	private Transaction m_transaction;

	// For report
	private ReportAction m_reportAction;

	private String m_reportId;

	// For file
	private FileAction m_fileAction;

	private String m_fileId;

	// For email
	private EmailId m_emailId;

	// For customs declaration
	private CustomsDeclaration m_customsDeclaration;

	public EbayModel(EbayContext actionContext) {
		super(actionContext);

		m_seller = actionContext.getSeller();
		m_loginUserId = actionContext.getLoginUserId();
	}

	public CustomsDeclaration getCustomsDeclaration() {
		return m_customsDeclaration;
	}

	@Override
	public Action getDefaultAction() {
		return null;
	}

	public EmailId getEmailId() {
		return m_emailId;
	}

	public FileAction getFileAction() {
		return m_fileAction;
	}

	public String getFileId() {
		return m_fileId;
	}

	public int getLoginUserId() {
		return m_loginUserId;
	}

	public ReportAction getReportAction() {
		return m_reportAction;
	}

	public String getReportId() {
		return m_reportId;
	}

	public Seller getSeller() {
		return m_seller;
	}

	public Transaction getTransaction() {
		return m_transaction;
	}

	public List<Transaction> getTransactions() {
		return m_transactions;
	}

	public void setCustomsDeclaration(CustomsDeclaration customsDeclaration) {
		m_customsDeclaration = customsDeclaration;
	}

	public void setEmailId(EmailId emailId) {
		m_emailId = emailId;
	}

	public void setFileAction(FileAction fileAction) {
		m_fileAction = fileAction;
	}

	public void setFileId(String fileId) {
		m_fileId = fileId;
	}

	public void setReportAction(ReportAction reportAction) {
		m_reportAction = reportAction;
	}

	public void setReportId(String reportId) {
		m_reportId = reportId;
	}

	public void setTransaction(Transaction transaction) {
		m_transaction = transaction;
	}

	public void setTransactions(List<Transaction> transactions) {
		m_transactions = transactions;
	}
}
