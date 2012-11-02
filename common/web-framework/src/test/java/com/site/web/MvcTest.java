package com.site.web;

import org.junit.Test;

import com.site.test.junit.HttpTestCase;
import com.site.test.server.EmbeddedServer;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.test.book.BookModule;

public class MvcTest extends HttpTestCase {
   @Override
   protected void configure(EmbeddedServer server) {
      server.addServlet(new MVC().setContainer(getContainer()), "mvc-servlet", "/*");
   }

   @Override
   protected int getPort() {
      return super.getPort()+1;
   }

   @Test
   public void testElse() throws Exception {
      checkRequest("/book/else", "==>doElse==>no payload==>transition==>showList[]");

      checkRequest("/book/else?id=1", "==>doElse==>no payload==>transition==>error:No method annotated by @"
            + OutboundActionMeta.class.getSimpleName() + "(unknown) found in " + BookModule.class);
   }

   @Test
   public void testList() throws Exception {
      checkRequest("/book/list", "==>doList==>transition==>showList[]");
      checkRequest("/book/list/1", "==>doList==>transition==>showList[]");

      checkRequest("/book/list?id=1", "==>doList==>error:Error occured during handling transition(default)");
   }

   @Test
   public void testAdd() throws Exception {
      checkRequest("/book/add?id=1&name=first", "==>signin==>permission==>doAdd==>transition==>showList[1(first)]");
      checkRequest("/book/add?id=2&name=second",
            "==>signin==>permission==>doAdd==>transition==>showList[1(first), 2(second)]");

      checkRequest("/book/add?id=0&name=zero", "==>signin==>permission==>doAdd==>transition==>showAdd");
      checkRequest("/book/add?id=3&name=",
            "==>signin==>permission==>error:Error occured during handling inbound action(add)!");
   }
}
