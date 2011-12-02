package org.unidal.ezsell.cache;

import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.SellerDao;

import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.lookup.annotation.Inject;

public class SellerDaoCache extends SellerDao {
   @Inject
   private CacheManager<Integer, Seller> m_cacheManager;

   @Override
   public int deleteByPK(Seller proto) throws DalException {
      m_cacheManager.evictFromCache(proto.getKeySellerId());

      return super.deleteByPK(proto);
   }

   @Override
   public Seller findByPK(int keySellerId, Readset<Seller> readset) throws DalException {
      Seller seller = m_cacheManager.getFromCache(keySellerId, readset);

      if (seller == null) {
         seller = super.findByPK(keySellerId, readset);
         m_cacheManager.putToCache(keySellerId, readset, seller);
      }

      return seller;
   }

   @Override
   public int updateByPK(Seller proto, Updateset<Seller> updateset) throws DalException {
      m_cacheManager.evictFromCache(proto.getKeySellerId());

      return super.updateByPK(proto, updateset);
   }
}
