package org.unidal.ezsell.shipping;

public class Address {
   private String m_name;

   private String m_address;

   public String getAddress() {
      return m_address;
   }

   public String getName() {
      return m_name;
   }

   public void setAddress(String address) {
      m_address = address;
   }

   public void setName(String name) {
      m_name = name;
   }
}
