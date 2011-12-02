package org.unidal.ezsell.biz;

import org.unidal.ezsell.EbayPayload;

import com.site.lookup.util.StringUtils;
import com.site.web.mvc.payload.annotation.FieldMeta;

public class SellerTransactionPayload extends EbayPayload {
   @FieldMeta("submit")
   private boolean m_submit;

   @FieldMeta("id")
   private int m_id;

   @FieldMeta("trackingNumber")
   private String m_trackingNumber;

   @FieldMeta("mas")
   private boolean m_markAsShipped;

   @FieldMeta("labels")
   private String m_labels;

   @FieldMeta("ul")
   private boolean m_updateLabels;

   @FieldMeta("pcd")
   private boolean m_printCustomsDeclaration;

   @FieldMeta("psl")
   private boolean m_printShippingLabel;

   public boolean isSubmit() {
      return m_submit;
   }

   public int getId() {
      return m_id;
   }

   public String getTrackingNumber() {
      return StringUtils.trimAll(m_trackingNumber);
   }

   public String getLabels() {
      return m_labels;
   }

   public boolean isUpdateLabels() {
      return m_updateLabels;
   }

   public boolean isPrintCustomsDeclaration() {
      return m_printCustomsDeclaration;
   }

   public boolean isPrintShippingLabel() {
      return m_printShippingLabel;
   }

   public void setSubmit(boolean submit) {
      m_submit = submit;
   }

   public void setId(int id) {
      m_id = id;
   }

   public void setTrackingNumber(String trackingNumber) {
      m_trackingNumber = trackingNumber;
   }

   public void setLabels(String labels) {
      m_labels = labels;
   }

   public boolean isMarkAsShipped() {
      return m_markAsShipped;
   }

   public void setMarkAsShipped(boolean markAsShipped) {
      m_markAsShipped = markAsShipped;
   }

   public void setUpdateLabels(boolean updateLabels) {
      m_updateLabels = updateLabels;
   }

   public void setPrintCustomsDeclaration(boolean printCustomsDeclaration) {
      m_printCustomsDeclaration = printCustomsDeclaration;
   }

   public void setPrintShippingLabel(boolean printShippingLabel) {
      m_printShippingLabel = printShippingLabel;
   }
}
