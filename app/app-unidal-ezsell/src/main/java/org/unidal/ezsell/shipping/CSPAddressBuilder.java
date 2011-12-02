package org.unidal.ezsell.shipping;

import org.unidal.ezsell.dal.ShippingAddress;

public class CSPAddressBuilder implements AddressBuilder {
   public Address buildShippingAddress(ShippingAddress sa) {
      Address address = new Address();
      StringBuilder sb = new StringBuilder(1024);
      String CRLF = "\r\n";

      sb.append(sa.getStreet()).append(CRLF);

      if (sa.getStreet2() != null) {
         sb.append(sa.getStreet2()).append(CRLF);
      }

      sb.append(sa.getCity()).append(' ');

      if (sa.getState() != null) {
         sb.append(sa.getState()).append(' ');
      }

      sb.append(sa.getPostalCode()).append(CRLF);
      sb.append(sa.getCountryName());

      if (sa.getPhone() != null) {
         sb.append(" Tel: ").append(sa.getPhone());
      }

      address.setAddress(sb.toString());
      address.setName(sa.getName());

      return address;
   }
}
