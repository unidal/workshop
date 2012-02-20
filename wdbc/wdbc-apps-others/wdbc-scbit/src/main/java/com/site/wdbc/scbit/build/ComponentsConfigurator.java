package com.site.wdbc.scbit.build;

import java.util.ArrayList;
import java.util.List;

import com.site.lookup.configuration.Component;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.configuration.AbstractWdbcComponentsConfigurator;
import com.site.wdbc.scbit.Configuration;
import com.site.wdbc.scbit.PageLinksQuery;
import com.site.wdbc.scbit.Processor;
import com.site.wdbc.scbit.SummaryFilter;

public class ComponentsConfigurator extends AbstractWdbcComponentsConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(S().config(E("headers").add(E("header", "name", "User-Agent") //
            .value("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; .NET CLR 1.1.4322)"))));

      all.add(C(Configuration.class) //
            .config(E("config").value("config.xml")));

      all.add(P(Processor.class));

      all.add(Q("page-links", PageLinksQuery.class) //
            .config(E("uriPattern").value("http://www.oxfordjournals.org/nar/database/summary/{0}")) //
            .req(Configuration.class));

      all.addAll(RQF("summary", SummaryFilter.class) //
            .rconfig(E("action").value("${page-links:link}")) //
            .qconfig(E("paths").add( //
                  P("title", "html.body.div.div[5].div.div.h1"), //
                  P("category", "html.body.div.div[5].div.div.div[*].a"), //
                  P("category-link", "html.body.div.div[5].div.div.div[*].a.@href"), //
                  P("hint", "html.body.div.div[5].div.div.h3"), //
                  P("description", "html.body.div.div[5].div.div.div[*].*text"), //
                  P("link", "html.body.div.div[5].div.div.div[*].*"), //
                  null)) //
            .freq(Session.class).getComponents());

      all.add(F().config(E("handler", "query", "page-links", "interval", "1") //
            .add(E("page", "name", "summary"), //
                  E("handler", "query", "summary") //
                        .add(E("processor", "name", "default"), //
                              null))));

      return all;
   }
}
