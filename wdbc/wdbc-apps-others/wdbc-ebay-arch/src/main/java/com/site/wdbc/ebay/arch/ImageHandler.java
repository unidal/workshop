package com.site.wdbc.ebay.arch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpException;
import org.codehaus.plexus.util.IOUtil;
import org.jdom.Attribute;
import org.jdom.Element;

import com.site.lookup.ContainerHolder;
import com.site.lookup.annotation.Inject;
import com.site.wdbc.http.Response;
import com.site.wdbc.http.Session;

public class ImageHandler extends ContainerHolder {
   @Inject
   private Session m_session;

   @Inject
   private Configuration m_configuration;

   public void handleElement(Element element) {
      if (element.getName().equalsIgnoreCase("img")) {
         Attribute src = element.getAttribute("src");
         String remoteUrl = src.getValue();

         if (remoteUrl != null && !remoteUrl.startsWith("javascript:")) {
            try {
               String localUrl = saveToLocalFile(remoteUrl);

               src.setValue(localUrl);
            } catch (Exception e) {
               System.err.println(String.format("Error when accessing URL(%s), IGNORED. Exception: %s", remoteUrl, e));
            }
         }
      }
   }

   private String saveToLocalFile(String remoteUrl) throws IOException, IllegalStateException, HttpException {
      Response response = m_session.createRequest(remoteUrl).execute(m_session);
      InputStream source = response.getContent();
      String localFile = "images/" + new File(remoteUrl).getName();
      File file = new File(m_configuration.getOutputDir(), localFile);

      file.getParentFile().mkdirs();
      FileOutputStream target = new FileOutputStream(file);

      IOUtil.copy(source, target);
      source.close();
      target.close();

      return localFile;
   }
}