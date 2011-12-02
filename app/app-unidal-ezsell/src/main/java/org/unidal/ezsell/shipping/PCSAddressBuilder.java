package org.unidal.ezsell.shipping;

import org.unidal.ezsell.dal.ShippingAddress;

public class PCSAddressBuilder implements AddressBuilder {
   public Address buildShippingAddress(ShippingAddress sa) {
      Address address = new Address();
      StringBuilder sb = new StringBuilder(1024);
      String CRLF = "\r\n";

      sb.append(sa.getStreet()).append(CRLF);

      if (sa.getStreet2() != null) {
         sb.append(sa.getStreet2()).append(CRLF);
      }

      sb.append(sa.getPostalCode()).append(' ');
      sb.append(sa.getCity());

      if (sa.getState() != null) {
         sb.append(' ').append(sa.getState());
      }

      sb.append(CRLF);
      sb.append(sa.getCountryName());

      if (sa.getPhone() != null) {
         sb.append(" Tel: ").append(sa.getPhone());
      }

      address.setAddress(sb.toString());
      address.setName(sa.getName());

      return address;
   }
}
