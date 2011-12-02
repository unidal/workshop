package com.ebay.eunit.web.testfwk;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebay.eunit.mock.http.HttpServerMocks.IHandler;

class StaticPageHandler implements IHandler {
   private StaticPageBaseCmdDef m_cmdDef;

   public StaticPageHandler(String commandName, Class<?> clazz) {
      m_cmdDef = new StaticPageBaseCmdDef(commandName, clazz);
   }

   @SuppressWarnings("unchecked")
   @Override
   public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      Class<? extends StaticDarwinCommand> cmdClass = m_cmdDef.getControllerCommandClass();

      try {
         StaticDarwinCommand cmd = cmdClass.newInstance();
         CmdCtx ctx = new CmdCtx();
         String name = m_cmdDef.getCommandName();

         ctx.setCmd(cmd);
         ctx.setCmdDefinition(m_cmdDef);
         ctx.setWebRequestHelper(new WebRequestHelper(request, name));
         ctx.setHttpResponseHelper(new HttpResponseHelper(response, request, name));

         EbayCommandResponse cmdResponse = (EbayCommandResponse) cmd.processStaticRequest(ctx);

         ctx.setCmdResponse(cmdResponse);
         new StaticPageResponseHandler().handleResponse(ctx);
      } catch (Exception e) {
         e.printStackTrace();
         throw new ServletException("Error when invoking command: " + cmdClass.getName(), e);
      }
   }
}