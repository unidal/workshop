package org.unidal.ezsell.biz;

import java.io.InputStream;

import org.unidal.ezsell.EbayPayload;

import com.site.web.mvc.payload.annotation.FieldMeta;

public class DataLoadingPayload extends EbayPayload {
   @FieldMeta("submit")
   private boolean m_submit;

   @FieldMeta(value = "paymentFile", file = true)
   private InputStream m_paymentFile;

   @FieldMeta(value = "shippingFile", file = true)
   private InputStream m_shippingFile;

   public InputStream getPaymentFile() {
      return m_paymentFile;
   }

   public InputStream getShippingFile() {
      return m_shippingFile;
   }

   public boolean isSubmit() {
      return m_submit;
   }

   public void setPaymentFile(InputStream paymentFile) {
      m_paymentFile = paymentFile;
   }

   public void setShippingFile(InputStream shippingFile) {
      m_shippingFile = shippingFile;
   }

   public void setSubmit(boolean submit) {
      m_submit = submit;
   }
}
