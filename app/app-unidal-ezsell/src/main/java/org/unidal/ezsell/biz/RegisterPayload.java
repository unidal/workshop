package org.unidal.ezsell.biz;

import org.unidal.ezsell.EbayPayload;
import org.unidal.ezsell.register.RegisterStep;

import com.site.web.mvc.payload.annotation.FieldMeta;

public class RegisterPayload extends EbayPayload {
   @FieldMeta("email")
   private String m_email;

   @FieldMeta("password")
   private String m_password;

   @FieldMeta("step")
   private RegisterStep m_step;

   @FieldMeta("submit")
   private boolean m_submit;

   @FieldMeta("token")
   private String m_token;

   @FieldMeta("userId")
   private int m_userId;

   @FieldMeta("ebayAccount")
   private String m_ebayAccount;

   @FieldMeta("ebayAccountLinkUrl")
   private String m_ebayAccountLinkUrl;

   @FieldMeta("ebayAuthToken")
   private String m_ebayAuthToken;

   public String getEbayAccount() {
      return m_ebayAccount;
   }

   public String getEbayAccountLinkUrl() {
      return m_ebayAccountLinkUrl;
   }

   public String getEbayAuthToken() {
      return m_ebayAuthToken;
   }

   public String getEmail() {
      return m_email;
   }

   public String getPassword() {
      return m_password;
   }

   public RegisterStep getStep() {
      return m_step;
   }

   public String getToken() {
      return m_token;
   }

   public int getUserId() {
      return m_userId;
   }

   public boolean isSubmit() {
      return m_submit;
   }

   public void setEbayAccount(String ebayAccount) {
      m_ebayAccount = ebayAccount;
   }

   public void setEbayAccountLinkUrl(String ebayAccountLinkUrl) {
      m_ebayAccountLinkUrl = ebayAccountLinkUrl;
   }

   public void setEbayAuthToken(String ebayAuthToken) {
      m_ebayAuthToken = ebayAuthToken;
   }

   public void setEmail(String email) {
      m_email = email;
   }

   public void setNextStep() {
      int id = m_step.getId();

      m_step = RegisterStep.getById(id + 1, null);
   }

   public void setPassword(String password) {
      m_password = password;
   }

   void setStep(RegisterStep step) {
      m_step = step;
   }

   public void setStep(int step) {
      m_step = RegisterStep.getById(step, RegisterStep.USER_FORM);
   }

   public void setSubmit(boolean submit) {
      m_submit = submit;
   }

   public void setToken(String token) {
      m_token = token;
   }

   public void setUserId(int userId) {
      m_userId = userId;
   }
}
