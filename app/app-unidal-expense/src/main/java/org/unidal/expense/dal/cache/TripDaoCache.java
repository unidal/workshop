package org.unidal.expense.dal.cache;

import org.unidal.expense.dal.Trip;
import org.unidal.expense.dal.TripDao;

import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.lookup.annotation.Inject;

public class TripDaoCache extends TripDao {
   @Inject
   private CacheManager<Integer, Trip> m_cacheManager;

   @Override
   public int deleteByPK(Trip proto) throws DalException {
      m_cacheManager.evictFromCache(proto.getKeyId());

      return super.deleteByPK(proto);
   }

   @Override
   public Trip findByPK(int keyUserId, Readset<Trip> readset) throws DalException {
      Trip user = m_cacheManager.getFromCache(keyUserId, readset);

      if (user == null) {
         user = super.findByPK(keyUserId, readset);
         m_cacheManager.putToCache(keyUserId, readset, user);
      }

      return user;
   }

   @Override
   public int updateByPK(Trip proto, Updateset<Trip> updateset) throws DalException {
      m_cacheManager.evictFromCache(proto.getKeyId());

      return super.updateByPK(proto, updateset);
   }
}
