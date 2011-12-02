package org.unidal.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.unidal.xml.view.XmlViewer;

import com.site.lookup.annotation.Inject;
import com.site.web.mvc.AbstractModule;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.annotation.ErrorActionMeta;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.ModuleMeta;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.mvc.annotation.PayloadMeta;
import com.site.web.mvc.annotation.TransitionMeta;

@ModuleMeta(name = "xml", defaultInboundAction = "source", defaultTransition = "default", defaultErrorAction = "default")
public class XmlModule extends AbstractModule {
   @Inject
   private XmlCodeGenerator m_generator;

   @Inject
   private XmlViewer m_viewer;

   private void copyModelFromPayload(XmlModel model, XmlPayload payload) {
      model.setContent(payload.getContent());

      String[] namespaces = payload.getNamespaces();
      String[] packages = payload.getPackages();

      if (namespaces != null && packages != null) {
         int len = namespaces.length;

         for (int i = 0; i < len; i++) {
            model.addNamespace(namespaces[i]);
            model.addPackage(packages[i]);
         }
      }
   }

   @PayloadMeta(XmlPayload.class)
   @InboundActionMeta(name = "download")
   public void doDownload(XmlContext<?> ctx) {
      if (!ctx.hasErrors()) {
         XmlPayload payload = (XmlPayload) ctx.getPayload();

         if (payload.isDownload()) {
            String content = payload.getContent();
            String[] namespaces = payload.getNamespaces();
            String[] packages = payload.getPackages();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(16384);

            try {
               m_generator.generateXml(baos, content, namespaces, packages);

               if (baos.size() > 0) {
                  generateDownloadZip(ctx, baos);
                  ctx.stopProcess();
               }
            } catch (Exception e) {
               ctx.addError(new ErrorObject("Can't generate XML due to " + e, e));
            }
         }
      }
   }

   private void generateDownloadZip(XmlContext<?> ctx, ByteArrayOutputStream baos) throws IOException {
      HttpServletResponse response = ctx.getHttpServletResponse();
      ServletOutputStream out = response.getOutputStream();

      response.addHeader("Content-Disposition", "attachment; filename=\"source.zip\";");
      response.setContentType("application/zip");
      response.setContentLength(baos.size());

      out.write(baos.toByteArray());
      out.close();
   }

   @PayloadMeta(XmlPayload.class)
   @InboundActionMeta(name = "mapping")
   public void doMapping(XmlContext<?> ctx) {
      if (!ctx.hasErrors()) {
         XmlPayload payload = (XmlPayload) ctx.getPayload();

         if (payload.isNext()) {
            String[] namespaces = payload.getNamespaces();
            String[] packages = payload.getPackages();

            if (namespaces != null && packages != null) {
               int len = packages.length;

               for (int i = 0; i < len; i++) {
                  if (packages[i] == null) {
                     ctx.addError(new ErrorObject("package." + i + ".required", null));
                  }
               }
            }
         }
      }
   }

   @PayloadMeta(XmlPayload.class)
   @InboundActionMeta(name = "source")
   public void doSource(XmlContext<?> ctx) {
      if (!ctx.hasErrors()) {
         XmlPayload payload = (XmlPayload) ctx.getPayload();

         if (payload.isNext()) {
            String content = payload.getContent();

            if (content != null && content.length() > 0) {
               ctx.addNamespace("");
               // TODO get namespaces from xml parser
            } else {
               ctx.addError(new ErrorObject("content.required", null));
            }
         }
      }
   }

   @TransitionMeta(name = "default")
   public void handleTransition(XmlContext<?> ctx) {
      XmlPayload payload = (XmlPayload) ctx.getPayload();

      switch (payload.getPage()) {
      case SOURCE:
         if (!ctx.hasErrors()) {
            if (payload.isNext()) {
               ctx.setOutboundPage(XmlPage.MAPPING.getName());
            }
         }
         break;
      case MAPPING:
         if (!ctx.hasErrors()) {
            if (payload.isPrevious()) {
               ctx.setOutboundPage(XmlPage.SOURCE.getName());
            } else if (payload.isNext()) {
               ctx.setOutboundPage(XmlPage.DOWNLOAD.getName());
            }
         }
         break;
      case DOWNLOAD:
         if (!ctx.hasErrors()) {
            if (payload.isPrevious()) {
               ctx.setOutboundPage(XmlPage.MAPPING.getName());
            }
         }

         break;
      }
   }

   @ErrorActionMeta(name = "default")
   public void onError(XmlContext<?> ctx) {
      ctx.getException().printStackTrace();
   }

   private void reviseModel(XmlModel model, XmlContext<?> ctx) {
      List<String> namespaces = model.getNamespaces();
      List<String> packages = model.getPackages();

      for (String namespace : ctx.getNamespaces()) {
         if (!namespaces.contains(namespace)) {
            namespaces.add(namespace);
            packages.add("");
         }
      }
   }

   @OutboundActionMeta(name = "download")
   public void showDownload(XmlContext<?> ctx) throws ServletException, IOException {
      XmlModel model = new XmlModel(ctx);
      XmlPayload payload = (XmlPayload) ctx.getPayload();

      model.setPage(XmlPage.DOWNLOAD);
      copyModelFromPayload(model, payload);
      reviseModel(model, ctx);

      m_viewer.view(ctx, model);
   }

   @OutboundActionMeta(name = "mapping")
   public void showMapping(XmlContext<?> ctx) throws ServletException, IOException {
      XmlModel model = new XmlModel(ctx);
      XmlPayload payload = (XmlPayload) ctx.getPayload();

      model.setPage(XmlPage.MAPPING);
      copyModelFromPayload(model, payload);
      reviseModel(model, ctx);

      m_viewer.view(ctx, model);
   }

   @OutboundActionMeta(name = "source")
   public void showSource(XmlContext<?> ctx) throws IOException, ServletException {
      XmlModel model = new XmlModel(ctx);
      XmlPayload payload = (XmlPayload) ctx.getPayload();

      model.setPage(XmlPage.SOURCE);
      copyModelFromPayload(model, payload);
      reviseModel(model, ctx);

      m_viewer.view(ctx, model);
   }
}
