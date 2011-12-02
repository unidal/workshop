package org.unidal.ezsell.shipping;

import org.unidal.ezsell.dal.ShippingAddress;

public interface AddressBuilder {
   public Address buildShippingAddress(ShippingAddress sa);
}
