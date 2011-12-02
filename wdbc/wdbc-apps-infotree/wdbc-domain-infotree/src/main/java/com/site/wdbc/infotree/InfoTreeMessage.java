package com.site.wdbc.infotree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class InfoTreeMessage {
   private static Map<String, Field> s_aliasMap = new LinkedHashMap<String, Field>();

   static {
      initialize();
   }

   private static void initialize() {
      Field[] fields = InfoTreeMessage.class.getDeclaredFields();

      for (Field field : fields) {
         if (Modifier.isStatic(field.getModifiers())) {
            continue;
         }

         Alias alias = field.getAnnotation(Alias.class);
         String name = (alias == null ? field.getName() : alias.value());

         if (name.startsWith("m_")) {
            name = name.substring(2);
         }

         field.setAccessible(true);
         s_aliasMap.put(name, field);
      }
   }

   @Alias("user_name")
   private String m_username;

   @Alias("user_password")
   private String m_password;

   @Alias("postMsgTitle")
   private String m_title;

   @Alias("postMsgContent")
   private String m_content;

   @Alias("postMsgType")
   private String m_type;

   @Alias("postMsgCat")
   private String m_category;

   @Alias("postMsgProvince")
   private String m_province;

   @Alias("postMsgCity")
   private String m_city;

   @Alias("postMsgZone")
   private String m_zone;

   @Alias("postMsgExpireDays")
   private String m_expireDays;

   @Alias("postMsgExpireDate")
   private String m_expireDate;

   @Alias("postMsgMobile")
   private String m_mobile;

   @Alias("postMsgTel")
   private String m_telephone;

   @Alias("postMsgQq")
   private String m_qq;

   @Alias("postMsgMsn")
   private String m_msn;

   @Alias("postMsgEmail")
   private String m_email;

   @Alias("postMsgWangwang")
   private String m_wangwang;

   @Alias("postMsgUrl")
   private String m_url;

   @Alias("postMsgPicUrl1")
   private String m_pictureUrl1;

   @Alias("notifyEmail")
   private String m_notifyEmail;

   @Alias("sourceUrl")
   private String m_sourceUrl;

   public String getCategory() {
      return m_category;
   }

   public String getCity() {
      return m_city;
   }

   public String getContent() {
      return m_content;
   }

   public String getEmail() {
      return m_email;
   }

   public String getExpireDate() {
      return m_expireDate;
   }

   public String getExpireDays() {
      return m_expireDays;
   }

   public String getMobile() {
      return m_mobile;
   }

   public String getMsn() {
      return m_msn;
   }

   public String getNotifyEmail() {
      return m_notifyEmail;
   }

   public String getPassword() {
      return m_password;
   }

   public String getPictureUrl1() {
      return m_pictureUrl1;
   }

   public String getProvince() {
      return m_province;
   }

   public String getQq() {
      return m_qq;
   }

   public String getSourceUrl() {
      return m_sourceUrl;
   }

   public String getTelephone() {
      return m_telephone;
   }

   public String getTitle() {
      return m_title;
   }

   public String getType() {
      return m_type;
   }

   public String getUrl() {
      return m_url;
   }

   public String getUsername() {
      return m_username;
   }

   public String getWangwang() {
      return m_wangwang;
   }

   public String getZone() {
      return m_zone;
   }

   public void setCategory(String category) {
      m_category = category;
   }

   public void setCity(String city) {
      m_city = city;
   }

   public void setContent(String content) {
      m_content = content;
   }

   public void setEmail(String email) {
      m_email = email;
   }

   public void setExpireDate(String expireDate) {
      m_expireDate = expireDate;
   }

   public void setExpireDays(String expireDays) {
      m_expireDays = expireDays;
   }

   public void setMobile(String mobile) {
      m_mobile = mobile;
   }

   public void setMsn(String msn) {
      m_msn = msn;
   }

   public void setNotifyEmail(String notifyEmail) {
      m_notifyEmail = notifyEmail;
   }

   public void setPassword(String password) {
      m_password = password;
   }

   public void setPictureUrl1(String pictureUrl1) {
      m_pictureUrl1 = pictureUrl1;
   }

   public void setProvince(String province) {
      m_province = province;
   }

   public void setQq(String qq) {
      m_qq = qq;
   }

   public void setSourceUrl(String sourceUrl) {
      m_sourceUrl = sourceUrl;
   }

   public void setTelephone(String telephone) {
      m_telephone = telephone;
   }

   public void setTitle(String title) {
      m_title = title;
   }

   public void setType(String type) {
      m_type = type;
   }

   public void setUrl(String url) {
      m_url = url;
   }

   public void setUsername(String username) {
      m_username = username;
   }

   public void setWangwang(String wangwang) {
      m_wangwang = wangwang;
   }

   public void setZone(String zone) {
      m_zone = zone;
   }

   public NameValuePair[] toNameValuePairs() {
      List<NameValuePair> pairs = new ArrayList<NameValuePair>();

      for (Map.Entry<String, Field> entry : s_aliasMap.entrySet()) {
         Object value = null;

         try {
            value = entry.getValue().get(this);
         } catch (Exception e) {
            // ignore it
            e.printStackTrace();
         }

         if (value != null) {
            pairs.add(new BasicNameValuePair(entry.getKey(), value.toString()));
         }
      }

      return pairs.toArray(new NameValuePair[0]);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      boolean first = true;

      sb.append('{');

      for (Map.Entry<String, Field> entry : s_aliasMap.entrySet()) {
         Object value = null;

         try {
            value = entry.getValue().get(this);
         } catch (Exception e) {
            // ignore it
         }

         if (value != null) {
            if (!first) {
               sb.append(", ");
            } else {
               first = false;
            }

            sb.append(entry.getKey()).append('=').append(value.toString());
         }
      }

      sb.append('}');

      return sb.toString();
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target(ElementType.FIELD)
   public static @interface Alias {
      String value();
   }
}
