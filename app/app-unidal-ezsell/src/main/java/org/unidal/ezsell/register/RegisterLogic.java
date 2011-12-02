package org.unidal.ezsell.register;

import java.util.Date;

import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.SellerEntity;
import org.unidal.ezsell.dal.Operator;
import org.unidal.ezsell.dal.OperatorDao;
import org.unidal.ezsell.dal.OperatorEntity;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;

public class RegisterLogic {
   @Inject
   private OperatorDao m_operatorDao;

   @Inject
   private SellerDao m_sellerDao;

   @Inject
   private EbayAccountLink m_accountLink;

   public Operator addOperator(String email, String password) throws DalException, OperatorAlreadyExistedException {
      try {
         m_operatorDao.findByEmail(email, OperatorEntity.READSET_FULL);
         
         // user exists
         try {
            Operator user = m_operatorDao.authenticate(email, password, OperatorEntity.READSET_FULL);
            OperatorStatus status = OperatorStatus.getByValue(user.getStatus(), null);
            
            switch (status) {
            case CREATED:
            case EMAIL_CONFIRMED:
               return user;
            case EBAY_SELLER_LINKED:
            case SUSPENDED:
               throw new OperatorAlreadyExistedException("User(" + email + ") is already existed.");
            }
         } catch (DalException e) {
            throw new OperatorAlreadyExistedException("User(" + email + ") is already existed.");
         }
      } catch (DalException e) {
         // user not exist, continue
      }

      Operator user = new Operator();

      user.setEmail(email);
      user.setPassword(password);
      user.setLastLoginDate(new Date());
      user.setStatus(OperatorStatus.CREATED.getValue());

      m_operatorDao.insert(user);
      return user;
   }

   public String encodeToken(Operator user) {
      if (user != null) {
         return "U" + user.getOperatorId();
      }

      return null;
   }

   public Seller addSeller(int userId, String ebayAccount, String ebayAuthToken) throws DalException {
      Seller seller = new Seller();
      Operator user = m_operatorDao.findByPK(userId, OperatorEntity.READSET_FULL);

      seller.setOperatorId(userId);
      seller.setEbayAccount(ebayAccount);
      seller.setEbayAppId(m_accountLink.getAppId());
      seller.setEbayAuthToken(ebayAuthToken);

      m_sellerDao.insert(seller);

      user.setKeyOperatorId(userId);
      user.setEbaySellerId(seller.getSellerId());
      user.setStatus(OperatorStatus.EBAY_SELLER_LINKED.getValue());
      m_operatorDao.updateByPK(user, OperatorEntity.UPDATESET_FULL);

      return seller;
   }

   public Operator changeOperatorStatus(int userId, OperatorStatus toStatus) throws DalException {
      Operator user = m_operatorDao.findByPK(userId, OperatorEntity.READSET_FULL);

      if (user.getStatus() != toStatus.getValue()) {
         user.setStatus(toStatus.getValue());
         user.setKeyOperatorId(userId);

         m_operatorDao.updateByPK(user, OperatorEntity.UPDATESET_FULL);
      }

      return user;
   }

   public Operator decodeToken(String token) {
      Operator user = new Operator();

      if (token != null && token.length() > 0) {
         int userId = Integer.parseInt(token.substring("U".length()));

         user.setOperatorId(userId);
      } else {
         user.setOperatorId(-1);
      }

      return user;
   }

   public Seller bindSeller(int userId, String sellerAccount) {
      try {
         Seller seller = m_sellerDao.findByEbayAccount(sellerAccount, SellerEntity.READSET_FULL);
         Operator user = m_operatorDao.findByPK(userId, OperatorEntity.READSET_FULL);

         user.setKeyOperatorId(user.getOperatorId());
         user.setEbaySellerId(seller.getSellerId());
         user.setStatus(OperatorStatus.EBAY_SELLER_LINKED.getValue());
         m_operatorDao.updateByPK(user, OperatorEntity.UPDATESET_FULL);

         return seller;
      } catch (DalException e) {
         // ignore it
      }

      return null;
   }
}
