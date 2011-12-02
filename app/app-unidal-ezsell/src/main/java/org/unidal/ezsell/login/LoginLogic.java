package org.unidal.ezsell.login;

import java.util.Date;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.SellerEntity;
import org.unidal.ezsell.dal.Operator;
import org.unidal.ezsell.dal.OperatorDao;
import org.unidal.ezsell.dal.OperatorEntity;
import org.unidal.ezsell.register.OperatorStatus;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;

public class LoginLogic implements LogEnabled {
   @Inject
   private OperatorDao m_operatorDao;

   @Inject
   private SellerDao m_sellerDao;

   private Logger m_logger;

   private void checkUserStatus(String email, int status) throws UserSuspendedException, UserNotReadyException {
      if (status == OperatorStatus.SUSPENDED.getValue()) {
         m_logger.info("Login failure, seller account is suspended: " + email);
         throw new UserSuspendedException("User(" + email + ") is suspended.");
      } else if (status != OperatorStatus.EBAY_SELLER_LINKED.getValue()) {
         m_logger.info("Login failure, seller account is not linked: " + email);
         throw new UserNotReadyException("User(" + email + ") is not ready, its current status is: " + status);
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void login(TokenManager tokenManager, String email, String password) throws DalException,
         UserSuspendedException, UserNotReadyException {
      Operator operator = null;

      try {
         operator = m_operatorDao.authenticate(email, password, OperatorEntity.READSET_FULL);

         checkUserStatus(email, operator.getStatus());

         int userId = operator.getOperatorId();
         String newToken = tokenManager.buildToken(userId);

         tokenManager.setToken(newToken);
         operator.setLastLoginDate(new Date());
         operator.setLoginFailures(0);
      } catch (DalException e) {
         operator = m_operatorDao.findByEmail(email, OperatorEntity.READSET_FULL);

         operator.setLoginFailures(operator.getLoginFailures() + 1);
      } finally {
         if (operator != null) {
            operator.setKeyOperatorId(operator.getOperatorId());

            m_operatorDao.updateByPK(operator, OperatorEntity.UPDATESET_FULL);
         }
      }
   }

   public Seller getSeller(int userId) throws DalException {
      Operator user = m_operatorDao.findByPK(userId, OperatorEntity.READSET_FULL);

      if (user.getStatus() == OperatorStatus.EBAY_SELLER_LINKED.getValue()) {
         Seller seller = m_sellerDao.findByPK(user.getEbaySellerId(), SellerEntity.READSET_FULL);

         return seller;
      } else {
         throw new DalException("User(" + user.getEmail() + ") is inactive.");
      }
   }
}
