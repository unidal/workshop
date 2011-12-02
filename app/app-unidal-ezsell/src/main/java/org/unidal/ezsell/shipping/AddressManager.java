package org.unidal.ezsell.shipping;

import org.unidal.ezsell.dal.ShippingAddress;
import org.unidal.ezsell.dal.ShippingAddressDao;
import org.unidal.ezsell.dal.ShippingAddressEntity;
import org.unidal.ezsell.dal.Transaction;

import com.site.dal.jdbc.DalException;
import com.site.lookup.ContainerHolder;
import com.site.lookup.annotation.Inject;

public class AddressManager extends ContainerHolder {
   @Inject
   private ShippingAddressDao m_saDao;

   public Address getShippingAddress(Transaction trx) throws DalException {
      ShippingAddress sa = m_saDao.findByPK(trx.getPaymentId(), ShippingAddressEntity.READSET_FULL);

      if (hasComponent(AddressBuilder.class, sa.getCountryName())) {
         AddressBuilder builder = lookup(AddressBuilder.class, sa.getCountryName());

         return builder.buildShippingAddress(sa);
      } else {
         AddressBuilder builder = lookup(AddressBuilder.class, "default");

         return builder.buildShippingAddress(sa);
      }
   }
}
