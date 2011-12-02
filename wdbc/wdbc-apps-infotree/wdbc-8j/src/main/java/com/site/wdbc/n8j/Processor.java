package com.site.wdbc.n8j;

import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.StringUtils;

import com.site.wdbc.http.Session;
import com.site.wdbc.infotree.InfoTreeConnector;
import com.site.wdbc.infotree.InfoTreeMessage;

public class Processor implements com.site.wdbc.http.Processor, Initializable {
   private Configuration m_configuration;

   private InfoTreeConnector m_connector;

   private String action;

   private String username;

   private String password;

   private String category;

   private String province;

   private String city;

   private boolean DEBUG = true;

   public void initialize() throws InitializationException {
      username = m_configuration.getUserName();
      password = m_configuration.getPassword();
      action = m_configuration.getAction();
      city = m_configuration.getCityName();
      province = m_configuration.getProvinceName();
      category = m_configuration.getCategoryInfoTree();
   }

   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();
      String type = prop.get("list:type");
      String title = prop.get("list:title");
      String zone = prop.get("details:district");
      String address = prop.get("list:address");
      String desc = prop.get("description:text");
      String mobilephone = prop.get("details:mobilephone");
      String telephone = prop.get("list:phone");
      String qq = prop.get("details:qq");
      String msn = prop.get("details:msn");
      String email = prop.get("details:email");
      String contactInfo = prop.get("details:contact");
      String picture = prop.get("details:picture-link");

      if (desc != null) {
         desc = StringUtils.replace(desc, "\r\n>", "\r\n");
      }

      InfoTreeMessage msg = new InfoTreeMessage();

      msg.setUsername(username);
      msg.setPassword(password);
      msg.setTitle(title);
      msg.setContent(address + "\r\n\r\n" + (desc == null ? "" : desc) + "\r\n\r\n" + contactInfo);
      msg.setType(type);
      msg.setCategory(category);
      msg.setProvince(province);
      msg.setCity(city);
      msg.setZone(zone);
      msg.setExpireDays("28");
      msg.setMobile(mobilephone);
      msg.setTelephone(telephone);
      msg.setQq(qq);
      msg.setMsn(msn);
      msg.setEmail(email);
      msg.setPictureUrl1(picture);
      msg.setNotifyEmail(email != null ? email : msn);
      msg.setSourceUrl(session.getLastUrl().toExternalForm());

      try {
         m_connector.publish(action, msg);
      } catch (Exception e) {
         e.printStackTrace();
      }

      if (DEBUG) {
         System.out.println(msg.toString().replace("\r\n", "\\r\\n"));
      }
   }
}
