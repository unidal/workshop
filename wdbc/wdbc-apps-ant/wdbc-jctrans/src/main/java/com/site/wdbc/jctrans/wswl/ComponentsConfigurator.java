package com.site.wdbc.jctrans.wswl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.site.lookup.configuration.Component;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.configuration.AbstractWdbcComponentsConfigurator;
import com.site.wdbc.jctrans.Configuration;

public class ComponentsConfigurator extends AbstractWdbcComponentsConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(S().config(
               E("headers").add(
                  E("header", "name", "User-Agent")
                     .value("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; .NET CLR 1.1.4322)")
               )));

      all.add(C(Configuration.class).config(E("config").value("config.xml")));
      
      all.add(P(Processor.class).req(Configuration.class));
      
      all.add(F().config(
                  E("handler", "query", "page-links", "interval", "2").add(
                     E("page", "name", "list"),
                     E("handler", "query", "list", "interval", "2").add(
                        E("page", "name", "details"),
                        E("handler", "query", "details").add(
                           E("processor", "name", "default")
                  )))));

      all.add(Q("page-links", PageLinksQuery.class)
               .config(E("uriPattern").value("http://info.jctrans.com/xueyuan/wswl/wswl/default{0}.shtml"))
               .req(Configuration.class));

      all.addAll(RQF("list", ListFilter.class)
               .rconfig(E("action").value("${page-links:link}"))
               .qconfig(E("paths").add(
                        P("id", "-"),
                        P("link", "div[6].div.div[2].ul.li[*].p.a.@href"),
                        P("title", "div[6].div.div[2].ul.li[*].p.a")
                        ))
               .freq(Session.class)
               .getComponents());

      all.addAll(RQF("details", DetailsFilter.class)
               .rconfig(E("action").value("${list:link}"))
               .qconfig(E("paths").add(
                        P("date", "div[*].div.div.div[2].ul.li.p[2].span"),
                        P("from", "div[*].div.div.div[2].ul.li.p[2].span[2]"),
                        P("body", "div[*].div.div.div[2].ul.li[2].table.tr.td.div.div.p[*]")
                        ))
               .getComponents());
      
      return all;
   }

   @Override
   protected File getConfigurationFile() {
      return new File("src/main/resources/META-INF/plexus/components-wswl.xml");
   }
}
