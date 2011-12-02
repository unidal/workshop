package com.site.email;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.xml.sax.InputSource;

import com.site.dal.xml.XmlAdapter;
import com.site.dal.xml.XmlException;
import com.site.dal.xml.registry.XmlRegistry;
import com.site.email.model.EmailModel;
import com.site.email.model.EmailsModel;
import com.site.lookup.annotation.Inject;

public class EmailManager implements Initializable {
   @Inject
   private XmlRegistry m_registry;

   @Inject
   private XmlAdapter m_adapter;

   private Map<String, EmailModel> m_map = new HashMap<String, EmailModel>();

   public void initialize() throws InitializationException {
      m_registry.register(EmailsModel.class);
   }

   public void loadModel(InputSource source) throws XmlException {
      EmailsModel emails = m_adapter.unmarshal(source);

      m_map.putAll(emails.getEmailMap());
   }

   public EmailMessage getEmailMessage(String name) {
      EmailModel model = m_map.get(name);
      EmailMessage message = new EmailMessage(name, "text".equalsIgnoreCase(model.getFormat()));

      // TODO
      return message;
   }
}
