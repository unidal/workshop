package com.site.bes.payload;

import static com.site.kernel.dal.ValueType.DATE;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;

import java.util.Date;

import com.site.bes.EventPayload;
import com.site.kernel.dal.DataObjectField;

// separator :=:
public class MessagePayload extends EventPayload {
   public static final DataObjectField AREA = new DataObjectField("area", STRING);

   public static final DataObjectField CATEGORY_PATH = new DataObjectField("category-path", STRING);

   public static final DataObjectField BODY = new DataObjectField("body", STRING);

   public static final DataObjectField EMAIL = new DataObjectField("email", STRING);

   public static final DataObjectField EXPIRY = new DataObjectField("expiry", DATE);

   public static final DataObjectField ID = new DataObjectField("id", INT);

   public static final DataObjectField MSN = new DataObjectField("msn", STRING);

   public static final DataObjectField PHONE = new DataObjectField("phone", STRING);

   public static final DataObjectField QQ = new DataObjectField("qq", STRING);

   public static final DataObjectField TITLE = new DataObjectField("title", STRING);

   public static final DataObjectField TYPE = new DataObjectField("type", INT);

   public static final DataObjectField USER_ID = new DataObjectField("user-id", INT);

   public static final DataObjectField USER_NAME = new DataObjectField("user-name", STRING);

   private String m_area;

   private String m_categoryPath;

   private String m_body;

   private String m_email;

   private Date m_expiry;

   private int m_id;

   private String m_msn;

   private String m_phone;

   private String m_qq;

   private String m_title;

   private int m_type;

   private int m_userId;

   private String m_userName;

   static {
      initialize();
   }

   public String getArea() {
      return m_area;
   }

   public String getCategoryPath() {
      return m_categoryPath;
   }

   public String getBody() {
      return m_body;
   }

   public String getEmail() {
      return m_email;
   }

   public Date getExpiry() {
      return m_expiry;
   }

   public int getId() {
      return m_id;
   }

   public String getMsn() {
      return m_msn;
   }

   public String getPhone() {
      return m_phone;
   }

   public String getQq() {
      return m_qq;
   }

   public String getTitle() {
      return m_title;
   }

   public int getType() {
      return m_type;
   }

   public int getUserId() {
      return m_userId;
   }

   public String getUserName() {
      return m_userName;
   }

   public void setArea(String area) {
      m_area = area;
      setFieldUsed(AREA, true);
   }

   public void setCategoryPath(String categoryPath) {
      m_categoryPath = categoryPath;
      setFieldUsed(CATEGORY_PATH, true);
   }

   public void setBody(String body) {
      m_body = body;
      setFieldUsed(BODY, true);
   }

   public void setEmail(String email) {
      m_email = email;
      setFieldUsed(EMAIL, true);
   }

   public void setExpiry(Date expiry) {
      m_expiry = expiry;
      setFieldUsed(EXPIRY, true);
   }

   public void setId(int id) {
      m_id = id;
      setFieldUsed(ID, true);
   }

   public void setMsn(String msn) {
      m_msn = msn;
      setFieldUsed(MSN, true);
   }

   public void setPhone(String phone) {
      m_phone = phone;
      setFieldUsed(PHONE, true);
   }

   public void setQq(String qq) {
      m_qq = qq;
      setFieldUsed(QQ, true);
   }

   public void setTitle(String title) {
      m_title = title;
      setFieldUsed(TITLE, true);
   }

   public void setType(int type) {
      m_type = type;
      setFieldUsed(TYPE, true);
   }

   public void setUserId(int userId) {
      m_userId = userId;
      setFieldUsed(USER_ID, true);
   }

   public void setUserName(String userName) {
      m_userName = userName;
      setFieldUsed(USER_NAME, true);
   }
}
