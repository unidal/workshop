package com.site.wdbc.ebay.arch;

import java.net.URL;

import org.junit.Test;

import com.site.wdbc.http.Session;
import com.site.wdbc.http.dsl.AbstractConfigurator;
import com.site.wdbc.http.dsl.WdbcTestCase;

public class Crawler extends WdbcTestCase {
   public Crawler() {
      super(new Configurator());
   }

   public static void main(String[] args) {
      main(new Crawler());
   }

   @Override
   public void setUp() throws Exception {
      super.setUp();

      Session session = lookup(Session.class);
      Configuration configuration = lookup(Configuration.class);

      session.pushProperties();
      session.setProperty("username", configuration.getUserName());
      session.setProperty("password", configuration.getPassword());
      session.setLastUrl(new URL("http://devtraining.corp.ebay.com/"));
   }

   @Test
   public void testFindQuery() throws Exception {
      executeFindQuery("section");
   }

   @Test
   public void testSelectQuery() throws Exception {
      executeSelectQuery("section");
   }

   static class Configurator extends AbstractConfigurator {
      @Override
      public void configure() {
         query("home") //
               .from("http://devtraining.corp.ebay.com/");

         query("login") //
               .from("http://devtraining.corp.ebay.com/login/index.php") //
               .usePost() //
               .withParams( //
                     param("inputs").add( //
                           param("input", "${username}").attr("name", "username"), //
                           param("input", "${password}").attr("name", "password"), //
                           param("input", "1").attr("name", "testcookies"), //
                           null) //
               );

         query("course") //
               .from("http://devtraining.corp.ebay.com/") //
               .withSamplePage("pages/course.html") //
               .forFields( //
                     field("title", //
                           "html.body.div[5].div.div[2].table.tr.td[2].div.div.table[*].tr[*].td[2].a", //
                           "Creating a Service Consumer with the SOA Framework 2.5", //
                           "eBay Architecture Strategies Tutorial"), //
                     field("link@href", //
                           "html.body.div[5].div.div[2].table.tr.td[2].div.div.table[*].tr[*].td[2].a.@href", //
                           "view.php?id=15", //
                           "view.php?id=14"), //
                     null) //
               .filterBy( //
                     filter(CourseFilter.class) //
                           .req(Session.class));

         query("section") //
               .from("${course:link}") //
               .withSamplePage("pages/section.html") //
               .forFields( //
                     field("title", //
                           "html.body.div[5].div.div[3].table.tr[*].td[2].a.*text", //
                           "Factors that Affect the ebay.com Site Design", //
                           "Introduction to Partitioning"), //
                     field("link@href", //
                           "html.body.div[5].div.div[3].table.tr[*].td[2].a.@href", //
                           "view.php?id=247", //
                           "view.php?id=259"), //
                     field("title2", //
                           "html.body.div[5].div.div[3].div.table.tr.td[2].div.table.tr[*].td[2].ul.li.a.*text", //
                           "Installing EDE", //
                           "Megajars Overview"), //
                     field("link2@href", //
                           "html.body.div[5].div.div[3].div.table.tr.td[2].div.table.tr[*].td[2].ul.li.a.@href", //
                           "view.php?id=179", //
                           "view.php?id=180"), //
                     null) //
               .filterBy( //
                     filter(SectionFilter.class) //
                           .req(Session.class));

         query("details") //
               .from("${section:link}") //
               .withSamplePage("pages/details.html") //
               .forFields( //
                     field("title", //
                           "html.head.title", //
                           "Reflecting Changes on the Web site Without Delay"), //
                     field("content", //
                           "html.body.div[5].div.div[3].div.*", //
                           "Generally, when the load or traffic is low"), //
                     null);

         flow() //
               .with(handler() //
                     .executeQuery("home", "login") //
                     .forEach("course", 1, handler() //
                           .forEach("section", handler() //
                                 .forEach("details", handler() //
                                       .processBy(processor("details", DetailsProcessor.class) //
                                             .req(ResultHandler.class, ImageHandler.class))) //
                           ) //
                           .processBy(processor("course", CourseProcessor.class) //
                                 .req(ResultHandler.class) //
                           ) //
                     ) //
               );
      }
   }
}
