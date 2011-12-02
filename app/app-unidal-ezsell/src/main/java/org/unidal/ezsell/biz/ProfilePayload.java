package org.unidal.ezsell.biz;

import org.unidal.ezsell.EbayPayload;

import com.site.web.mvc.payload.annotation.FieldMeta;

public class ProfilePayload extends EbayPayload {
   @FieldMeta("password1")
   private String m_password1;

   @FieldMeta("password2")
   private String m_password2;

   @FieldMeta("name")
   private String m_name;

   @FieldMeta("address")
   private String m_address;

   @FieldMeta("teamName")
   private String m_teamName;

   @FieldMeta("teamLeader")
   private String m_teamLeader;

   @FieldMeta("teamLeaderPhone")
   private String m_teamLeaderPhone;

   @FieldMeta("teamLeaderCube")
   private String m_teamLeaderCube;

   @FieldMeta("cp")
   private boolean m_changePassword;

   @FieldMeta("up")
   private boolean m_updateProfile;

   public String getAddress() {
      return m_address;
   }

   public String getName() {
      return m_name;
   }

   public String getPassword1() {
      return m_password1;
   }

   public String getPassword2() {
      return m_password2;
   }

   public String getTeamLeader() {
      return m_teamLeader;
   }

   public String getTeamLeaderCube() {
      return m_teamLeaderCube;
   }

   public String getTeamLeaderPhone() {
      return m_teamLeaderPhone;
   }

   public String getTeamName() {
      return m_teamName;
   }

   public boolean isChangePassword() {
      return m_changePassword;
   }

   public boolean isUpdateProfile() {
      return m_updateProfile;
   }

   public void setAddress(String address) {
      m_address = address;
   }

   public void setChangePassword(boolean changePassword) {
      m_changePassword = changePassword;
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setPassword1(String password1) {
      m_password1 = password1;
   }

   public void setPassword2(String password2) {
      m_password2 = password2;
   }

   public void setTeamLeader(String teamLeader) {
      m_teamLeader = teamLeader;
   }

   public void setTeamLeaderCube(String teamLeaderCube) {
      m_teamLeaderCube = teamLeaderCube;
   }

   public void setTeamLeaderPhone(String teamLeaderPhone) {
      m_teamLeaderPhone = teamLeaderPhone;
   }

   public void setTeamName(String teamName) {
      m_teamName = teamName;
   }

   public void setUpdateProfile(boolean updateProfile) {
      m_updateProfile = updateProfile;
   }
}
