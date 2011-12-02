package org.unidal.ezsell.biz;

import org.unidal.ezsell.EbayPayload;

import com.site.web.mvc.payload.annotation.FieldMeta;

public class CustomsDeclarationPayload extends EbayPayload {
   @FieldMeta("id")
   private int m_id;

   @FieldMeta("submit")
   private boolean m_submit;

   @FieldMeta("toName")
   private String m_toName;

   @FieldMeta("toAddress")
   private String m_toAddress;

   @FieldMeta("trackingNo")
   private String m_trackingNo;

   @FieldMeta("itemTitle")
   private String m_itemTitle;

   @FieldMeta("itemQuantity")
   private int m_itemQuantity;

   @FieldMeta("weight")
   private String m_weight;

   @FieldMeta("itemValue")
   private String m_itemValue;
   
   @FieldMeta("origin")
   private String m_origin;

   public int getId() {
      return m_id;
   }

   public int getItemQuantity() {
      return m_itemQuantity;
   }

   public String getItemTitle() {
      return m_itemTitle;
   }

   public String getItemValue() {
      return m_itemValue;
   }

   public String getOrigin() {
      return m_origin;
   }

   public String getToAddress() {
      return m_toAddress;
   }

   public String getToName() {
      return m_toName;
   }

   public String getTrackingNo() {
      return m_trackingNo;
   }

   public String getWeight() {
      return m_weight;
   }

   public boolean isSubmit() {
      return m_submit;
   }

   public void setId(int id) {
      m_id = id;
   }

   public void setItemQuantity(int itemQuantity) {
      m_itemQuantity = itemQuantity;
   }

   public void setItemTitle(String itemTitle) {
      m_itemTitle = itemTitle;
   }

   public void setItemValue(String itemValue) {
      m_itemValue = itemValue;
   }

   public void setOrigin(String origin) {
      m_origin = origin;
   }

   public void setSubmit(boolean submit) {
      m_submit = submit;
   }

   public void setToAddress(String toAddress) {
      m_toAddress = toAddress;
   }

   public void setToName(String toName) {
      m_toName = toName;
   }

   public void setTrackingNo(String trackingNo) {
      m_trackingNo = trackingNo;
   }

   public void setWeight(String weight) {
      m_weight = weight;
   }
}
