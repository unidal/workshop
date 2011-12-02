package org.unidal.ezsell.biz;

import java.io.IOException;

import javax.servlet.ServletException;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.common.LastUrlManager;
import org.unidal.ezsell.dal.Shipping;
import org.unidal.ezsell.dal.ShippingDao;
import org.unidal.ezsell.dal.ShippingEntity;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.dal.TransactionEntity;
import org.unidal.ezsell.transaction.TransactionLabel;
import org.unidal.ezsell.transaction.TransactionProcessor;
import org.unidal.ezsell.transaction.TransactionLabel.Mode;
import org.unidal.ezsell.view.JspViewer;
import org.unidal.ezsell.view.ReportAction;
import org.unidal.ezsell.view.ReportViewer;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;

public class SellerTransactionPage implements PageHandler<EbayContext>, LogEnabled {
   @Inject
   private ReportViewer m_reporter;

   @Inject
   private JspViewer m_jspViewer;

   @Inject
   private TransactionDao m_trxDao;

   @Inject
   private ShippingDao m_shippingDao;

   @Inject
   private TransactionLabel m_label;

   @Inject
   private TransactionProcessor m_trxProcessor;

   @Inject
   private LastUrlManager m_lastUrlManager;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void handleInbound(EbayContext ctx) {
      SellerTransactionPayload payload = (SellerTransactionPayload) ctx.getPayload();

      if (payload.getLastUrl() == null) {
         payload.setLastUrl(m_lastUrlManager.getLastUrl(ctx));
      }

      if (!ctx.hasErrors()) {
         Transaction trx = null;

         try {
            trx = m_trxDao.findByPK(payload.getId(), TransactionEntity.READSET_FULL);

            ctx.setTransaction(trx);
         } catch (DalException e) {
            // show the seller transactions page if no transaction found
            showSellerTransactionsPage(ctx);
         }

         if (trx != null) {
            if (payload.isMarkAsShipped()) {
               boolean changed = !isSame(trx.getShippingTrackingId(), payload.getTrackingNumber());

               if (changed) {
                  updateTrackingNumber(ctx, trx);
               }
            } else if (payload.isUpdateLabels()) {
               boolean changed = !isSame(trx.getLabels(), payload.getLabels());

               if (changed) {
                  m_label.apply(ctx, trx, payload.getLabels(), Mode.REPLACE);
               }
            } else if (payload.isPrintCustomsDeclaration()) {
               showCustomsDeclaration(ctx, trx);
            } else {
               m_lastUrlManager.setLastUrl(ctx);
            }
         }
      }
   }

   private double getShippingFee(String trackingNumber) {
      try {
         Shipping shipping = m_shippingDao.findByPK(trackingNumber, ShippingEntity.READSET_FULL);

         return shipping.getPriceAfterDiscount();
      } catch (DalException e) {
         // ignore it
      }

      return 0;
   }

   public void handleOutbound(EbayContext ctx) throws ServletException, IOException {
      SellerTransactionPayload payload = (SellerTransactionPayload) ctx.getPayload();
      EbayModel model = new EbayModel(ctx);
      Transaction transaction = ctx.getTransaction();

      model.setTransaction(transaction);

      if (payload.isPrintShippingLabel()) {
         model.setReportAction(ReportAction.SHIPPING_LEVEL_REPORT);
         model.setReportId(String.valueOf(payload.getId()));
         m_reporter.view(ctx, model);
      } else {
         if (payload.getTrackingNumber() == null) {
            payload.setTrackingNumber(transaction.getShippingTrackingId());
         }

         if (payload.getLabels() == null) {
            payload.setLabels(transaction.getLabels());
         }

         model.setPage(EbayPage.SELLER_TRANSACTION);
         m_jspViewer.view(ctx, model);
      }
   }

   private boolean isSame(String s1, String s2) {
      if (s1 == null && s2 == null) {
         return true;
      } else if (s1 != null && s2 != null) {
         return s1.equals(s2);
      }

      return false;
   }

   private void showCustomsDeclaration(EbayContext ctx, Transaction trx) {
      SellerTransactionPayload oldPayload = (SellerTransactionPayload) ctx.getPayload();
      CustomsDeclarationPayload payload = new CustomsDeclarationPayload();

      payload.setNextPage(EbayPage.CUSTOMS_DECLARATION);
      payload.setId(trx.getId());
      payload.setLastUrl(oldPayload.getLastUrl());
      payload.setToName(trx.getBuyerName());
      payload.setToAddress(trx.getShippingAddress());
      payload.setTrackingNo(trx.getShippingTrackingId());
      payload.setItemQuantity(trx.getQuantityPurchased());
      payload.setItemTitle(trx.getItemTitle());
      payload.setItemValue("$" + (int) Math.ceil(trx.getAmountPaid()));
      payload.setOrigin("China");

      ctx.setPayload(payload);
      ctx.setOutboundPage("customs_declaration");
   }

   private void showSellerTransactionsPage(EbayContext ctx) {
      ctx.setOutboundPage("seller_transactions");
   }

   private void updateTrackingNumber(EbayContext ctx, Transaction trx) {
      SellerTransactionPayload payload = (SellerTransactionPayload) ctx.getPayload();

      try {
         trx.setShippingTrackingId(payload.getTrackingNumber());
         trx.setShippingCarrier("WanSe"); // TODO
         trx.setShippingFee(getShippingFee(payload.getTrackingNumber()));

         if (m_trxProcessor.markAsShipped(ctx.getSeller(), trx)) {
            trx.setKeyId(payload.getId());

            m_trxDao.updateByPK(trx, TransactionEntity.UPDATESET_FULL);
         }
      } catch (Exception e) {
         m_logger.warn("Error when updating tracking number for transaction(" + payload.getId() + ")", e);
         ctx.addError(new ErrorObject("dal.transaction.update", e));
      }
   }
}
