package org.unidal.ezsell.biz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.IOUtil;
import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.dal.Payment;
import org.unidal.ezsell.dal.PaymentDao;
import org.unidal.ezsell.dal.PaymentEntity;
import org.unidal.ezsell.dal.Shipping;
import org.unidal.ezsell.dal.ShippingDao;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.dal.TransactionEntity;
import org.unidal.ezsell.payment.PayPalPaymentLoader;
import org.unidal.ezsell.shipping.ShippingSummaryLoader;
import org.unidal.ezsell.view.JspViewer;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;

public class DataLoadingPage implements PageHandler<EbayContext>, LogEnabled {
   @Inject
   private JspViewer m_jspViewer;

   @Inject
   private PayPalPaymentLoader m_paymentLoader;

   @Inject
   private ShippingSummaryLoader m_shippingLoader;

   @Inject
   private PaymentDao m_paymentDao;

   @Inject
   private ShippingDao m_shippingDao;

   @Inject
   private TransactionDao m_trxDao;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void handleInbound(EbayContext ctx) {
      DataLoadingPayload payload = (DataLoadingPayload) ctx.getPayload();

      if (payload.isSubmit()) {
         if (payload.getPaymentFile() != null) {
            loadPayments(ctx, payload);
         }

         if (payload.getShippingFile() != null) {
            loadShippings(ctx, payload);
         }
      }
   }

   private void loadShippings(EbayContext ctx, DataLoadingPayload payload) {
      try {
         BufferedReader reader = getBufferedReader(payload.getShippingFile());
         List<Shipping> shippings = m_shippingLoader.getShippings(reader, ctx.getSeller().getSellerId());

         if (shippings.size() > 0) {
            m_shippingDao.insert(shippings.toArray(new Shipping[0]));
         }

         for (Shipping shipping : shippings) {
            updateShipping(shipping);
         }
      } catch (DalException e) {
         m_logger.error("Error when accessing Shipping table.", e);
         ctx.addError(new ErrorObject("dal.shipping.error", e));
      } catch (IOException e) {
         m_logger.error("Error when loading Shipping upload file.", e);
         e.printStackTrace();
         ctx.addError(new ErrorObject("load.shipping.error", e));
      }
   }

   private void updateShipping(Shipping shipping) {
      String trackingId = shipping.getShippingTrackingId();

      try {
         Transaction trx = m_trxDao.findByShippingTrackingId(trackingId, TransactionEntity.READSET_FULL);

         trx.setKeyId(trx.getId());
         trx.setShippingFee(shipping.getPriceAfterDiscount());

         m_trxDao.updateByPK(trx, TransactionEntity.UPDATESET_FULL);
      } catch (DalException e) {
         // ignore it
      }
   }

   private void loadPayments(EbayContext ctx, DataLoadingPayload payload) {
      try {
         BufferedReader reader = getBufferedReader(payload.getPaymentFile());
         List<Payment> payments = m_paymentLoader.getPayments(reader);

         if (payments.size() > 0) {
            for (Payment payment : payments) {
               payment.setSellerId(ctx.getSeller().getSellerId());
            }

            m_paymentDao.insert(payments.toArray(new Payment[0]));
         }

         for (Payment payment : payments) {
            updatePayment(payment);
         }
      } catch (DalException e) {
         m_logger.error("Error when accessing Payment table.", e);
         ctx.addError(new ErrorObject("dal.payment.error", e));
      } catch (IOException e) {
         m_logger.error("Error when loading Payment upload file.", e);
         e.printStackTrace();
         ctx.addError(new ErrorObject("load.payment.error", e));
      }
   }

   private void updatePayment(Payment payment) {
      if (payment.getAmount() > 0) {
         String[] itemIds = "".split(", ");

         for (String itemId : itemIds) {
            updatePaymentFee(payment, itemId);
         }
      } else {
         updateRefundPayment(payment);
      }
   }

   private void updateRefundPayment(Payment payment) {
      String paymentId = payment.getPaymentId();

      System.out.println("refund: " + paymentId);
   }

   private void updatePaymentFee(Payment payment, String itemId) {
      String buyerAccount = payment.getBuyerAccount();
      double amountPaid = payment.getAmount();
      Date transactionDate = payment.getOrderTime();

      try {
         if (itemId.length() > 0) {
            Transaction trx = m_trxDao.findByItemBuyerAccountAndAmountAndDate(Long.valueOf(itemId), buyerAccount,
                  amountPaid, transactionDate, TransactionEntity.READSET_FULL);

            payment.setKeyPaymentId(payment.getPaymentId());
            payment.setSellerId(trx.getSellerId());
            payment.setTransactionId(trx.getId());

            trx.setKeyId(trx.getId());
            trx.setPaymentId(payment.getPaymentId());
            trx.setPaymentFee(payment.getFeeAmount());

            m_paymentDao.updateByPK(payment, PaymentEntity.UPDATESET_FULL);
            m_trxDao.updateByPK(trx, TransactionEntity.UPDATESET_FULL);
         } else { // refund
            Transaction trx = m_trxDao.findByPaymentId(payment.getPaymentId(), TransactionEntity.READSET_FULL);

            trx.setKeyId(trx.getId());
            trx.setAmountPaid(trx.getAmountPaid() + payment.getAmount());
            trx.setPaymentFee(trx.getPaymentFee() + payment.getFeeAmount());

            m_trxDao.updateByPK(trx, TransactionEntity.UPDATESET_FULL);
         }
      } catch (DalException e) {
         // ignore it
         // e.printStackTrace();
      }
   }

   private BufferedReader getBufferedReader(InputStream input) throws IOException {
      byte[] ba = IOUtil.toByteArray(input);
      String charset = guessCharset(ba);

      return new BufferedReader(new StringReader(new String(ba, charset)));
   }

   private String guessCharset(byte[] ba) {
      if (ba.length > 3) {
         int c0 = ba[0] & 0xFF;
         int c1 = ba[1] & 0xFF;
         int c2 = ba[2] & 0xFF;

         if (c0 >= 0xE0 && c1 >= 0x80 && c1 <= 0xBF && c2 >= 0x80 && c2 <= 0xBF) {
            return "utf-8";
         }
      }

      return "gbk";
   }

   public void handleOutbound(EbayContext ctx) throws ServletException, IOException {
      EbayModel model = new EbayModel(ctx);

      model.setPage(EbayPage.DATA_LOADING);
      m_jspViewer.view(ctx, model);
   }
}
