package com.site.wdbc.kijiji;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.StringOutputStream;

import com.site.wdbc.http.Session;
import com.site.wdbc.http.impl.HttpResponseSource;

public class Processor implements com.site.wdbc.http.Processor, LogEnabled, Initializable {
   private Configuration m_configuration;

   private String action;

   private String username;

   private String password;

   private String category;

   private String province;

   private String city;

   private boolean DEBUG = true;

   private Logger m_logger;
   
   private String[] m_names = {
         "user_name",
         "user_password",
         "postMsgTitle",
         "postMsgContent",
         "postMsgType",
         "postMsgCat",
         "postMsgProvince",
         "postMsgCity",
         "postMsgZone",
         "postMsgExpireDays",
         "postMsgExpireDate",
         "postMsgMobile",
         "postMsgTel",
         "postMsgQq",
         "postMsgMsn",
         "postMsgEmail",
         "postMsgUrl",
         "postMsgWangwang",
         "postMsgPicUrl1",
         "notifyEmail",
         "sourceUrl",
         };
   
   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();
      String title = prop.get("list:title");
      String categoryName = prop.get("details:categoryName");
      String categoryId = prop.get("details:categoryId");
      String districtName = prop.get("details:districtName");
      String districtId = prop.get("details:districtId");
      String date = prop.get("details:date");
      String body = prop.get("details:body");
      String picture = prop.get("picture:link");
      String mobilephone = prop.get("contact:mobilephone");
      String telephone = prop.get("contact:telephone");
      String qq = prop.get("contact:qq");
      String msn = prop.get("contact:msn");
      String email = prop.get("contact:email");
      String url = prop.get("contact:url");
      String contact = prop.get("contact:contact");
      String contactInfo = prop.get("contact:text");
      String type = "";

      if (districtName == null || districtName.length() == 0) {
         districtName = "上海";
         districtId = "21";
      }

      if (body != null && body.startsWith("供求关系: 招聘")) {
         type = "1"; // iwant
      } else if (body != null && body.startsWith("供求关系: 求职")) {
         type = "2"; // ihave
      } else {
         type = "3"; // unknown
      }
      
      String[] values = {
           username,
           password,
           title,
           body + "\r\n\r\n" + contactInfo,
           type,
           category,
           province,
           city,
           districtName,
           "28",
           null,
           mobilephone,
           telephone,
           qq,
           msn,
           email,
           url,
           null,
           picture,
           (email != null ? email : msn),
           session.getLastUrl().toExternalForm(),
      };

      // reset it since it's optional
      prop.put("picture:link", "");
      prop.put("contact:mobilephone", "");
      prop.put("contact:telephone", "");
      prop.put("contact:qq", "");
      prop.put("contact:msn", "");
      prop.put("contact:contact", "");
      prop.put("contact:text", "");

      try {
         postToSite(values);
      } catch (Exception e) {
         e.printStackTrace();
      }

      if (DEBUG) {
         Map<String, String> record = new HashMap<String, String>();

         record.put("title", title);
         record.put("categoryName", categoryName);
         record.put("categoryId", categoryId);
         record.put("districtName", districtName);
         record.put("districtId", districtId);
         record.put("date", date);
         record.put("type", type);
         record.put("picture", picture);
         record.put("mobilephone", mobilephone);
         record.put("telephone", telephone);
         record.put("qq", qq);
         record.put("contact", contact);
         record.put("contact-info", contactInfo);
         record.put("body", body.replace("\r\n", "\\r\\n"));

         System.out.println(record);
      }
   }

   private NameValuePair[] getNamepairs(String[] names, String[] values) {
      int len = names.length;
      List<NameValuePair> pairs = new ArrayList<NameValuePair>(len);

      for (int i = 0; i < len; i++) {
         String name = names[i];
         String value = values[i];

         if (value != null && value.length() > 0) {
            pairs.add(new BasicNameValuePair(name, value));
         }
      }
      
      return pairs.toArray(new NameValuePair[0]);
   }

   public void initialize() throws InitializationException {
      username = m_configuration.getUserName();
      password = m_configuration.getPassword();
      action = m_configuration.getAction();
      city = m_configuration.getCityName();
      province = m_configuration.getProvinceName();
      category = m_configuration.getCategoryInfoTree();
   }

   private void postToSite(String[] values) throws URISyntaxException, HttpException, IOException, InterruptedException {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpPost post = new HttpPost(action);
      HttpEntity entity = new UrlEncodedFormEntity(getNamepairs(m_names, values));
      
      post.setEntity(entity);

      StringOutputStream sos =new StringOutputStream();
      entity.writeTo(sos);
      
      m_logger.info("Post data to " + action);
      m_logger.info("Parameters: " + sos.toString());
      
      HttpResponse response = client.execute(post);

      if (DEBUG) {
         System.out.println(new HttpResponseSource(response).getContent());
      }
      
      m_logger.info("Response status line: " + response.getStatusLine());
   }
}
