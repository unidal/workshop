package com.site.wdbc.jctrans.carsinfo;

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
               .config(E("uriPattern").value("http://land.jctrans.com/Carsinfo/index-0-0-------{0}.html"))
               .req(Configuration.class));

      all.addAll(RQF("list", ListFilter.class)
               .rconfig(E("action").value("${page-links:link}"))
               .qconfig(E("paths").add(
                        P("id", "-"),
                        P("date", "form.div[2].div.div[6-].ul.li[4].p.span"),
                        P("link", "form.div[2].div.div[6-].ul.li[4].p.a.@href"),
                        P("title", "form.div[2].div.div[6-].ul.li[4].p.a"),
                        P("location", "form.div[2].div.div[6-].ul.li[5]"),
                        P("range", "form.div[2].div.div[6-].ul.li[6]"),
                        P("price", "form.div[2].div.div[6-].ul.li[7]"),
                        null))
               .freq(Session.class)
               .getComponents());

      all.addAll(RQF("details", DetailsFilter.class)
               .rconfig(E("action").value("${list:link}"))
               .qconfig(E("paths").add(
                        P("company", "div[*].div[3].div.table.tr[2].td[2]"),
                        P("title", "div[*].div[3].div.table.tr[3].td[2]"),
                        P("date", "div[*].div[3].div.table.tr[4].td[2]"),
                        P("type", "div[*].div[3].div.table.tr[5].td[2]"),
                        P("service", "div[*].div[3].div.table.tr[6].td[2]"),
                        P("vechiel", "div[*].div[3].div.table.tr[7].td[2]"),
                        P("length", "div[*].div[3].div.table.tr[8].td[2]"),
                        P("weight", "div[*].div[3].div.table.tr[9].td[2]"),
                        P("location", "div[*].div[3].div.table.tr[10].td[2]"),
                        P("range", "div[*].div[3].div.table.tr[11].td[2]"),
                        P("price", "div[*].div[3].div.table.tr[13].td[2]"),
                        P("payment", "div[*].div[3].div.table.tr[14].td[2]"),
                        P("expiry", "div[*].div[3].div.table.tr[15].td[2]"),
                        P("notes", "div[*].div[3].div.table.tr[16].td[2].*text"),
                        null))
               .getComponents());
      
      return all;
   }

   @Override
   protected File getConfigurationFile() {
      return new File("src/main/resources/META-INF/plexus/components-carsinfo.xml");
   }
}
