package org.unidal.ezsell.register;

import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.dal.Operator;

public class RegisterModel extends EbayModel {
   private Operator m_user;

   private String m_token;

   public RegisterModel(EbayContext actionContext) {
      super(actionContext);
   }

   public String getToken() {
      return m_token;
   }

   public Operator getUser() {
      return m_user;
   }

   public void setToken(String token) {
      m_token = token;
   }

   public void setUser(Operator user) {
      m_user = user;
   }
}
