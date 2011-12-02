package org.unidal.ezsell.cache;

import org.unidal.ezsell.dal.Operator;
import org.unidal.ezsell.dal.OperatorDao;

import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.lookup.annotation.Inject;

public class OperatorDaoCache extends OperatorDao {
   @Inject
   private CacheManager<Integer, Operator> m_cacheManager;

   @Override
   public int deleteByPK(Operator proto) throws DalException {
      m_cacheManager.evictFromCache(proto.getKeyOperatorId());

      return super.deleteByPK(proto);
   }

   @Override
   public Operator findByPK(int keyUserId, Readset<Operator> readset) throws DalException {
      Operator user = m_cacheManager.getFromCache(keyUserId, readset);

      if (user == null) {
         user = super.findByPK(keyUserId, readset);
         m_cacheManager.putToCache(keyUserId, readset, user);
      }

      return user;
   }

   @Override
   public int updateByPK(Operator proto, Updateset<Operator> updateset) throws DalException {
      m_cacheManager.evictFromCache(proto.getKeyOperatorId());

      return super.updateByPK(proto, updateset);
   }
}
