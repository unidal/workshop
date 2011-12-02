package org.unidal.ezsell.transaction;

public class CustomsDeclaration {
   private String m_toName;

   private String m_toAddress;

   private String m_trackingNo;

   private String m_itemTitle;

   private int m_itemQuantity;

   private String m_weight;

   private String m_itemValue;

   private String m_origin;

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
