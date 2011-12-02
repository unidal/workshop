package org.unidal.ezsell;

import java.util.List;

import org.unidal.ezsell.dal.Operator;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.Transaction;

import com.site.web.mvc.ActionContext;

public class EbayContext extends ActionContext<EbayPayload> {
	private Seller m_seller;

	private int m_loginUserId;

	private Transaction m_transaction;

	private List<Transaction> m_transactions;

	private Operator m_user;

	public int getLoginUserId() {
		return m_loginUserId;
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

	public Operator getUser() {
		return m_user;
	}

	public void setLoginUserId(int loginUserId) {
		m_loginUserId = loginUserId;
	}

	public void setSeller(Seller seller) {
		m_seller = seller;
	}

	public void setTransaction(Transaction transaction) {
		m_transaction = transaction;
	}

	public void setTransactions(List<Transaction> transactions) {
		m_transactions = transactions;
	}

	public void setUser(Operator user) {
		m_user = user;
	}
}
