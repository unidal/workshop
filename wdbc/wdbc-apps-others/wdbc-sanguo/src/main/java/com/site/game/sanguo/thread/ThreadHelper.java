package com.site.game.sanguo.thread;

import java.io.IOException;

import org.apache.http.HttpException;
import org.codehaus.plexus.util.IOUtil;

import com.site.game.sanguo.api.Game;
import com.site.game.sanguo.api.Html;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Response;
import com.site.wdbc.http.Session;

public class ThreadHelper {
   private static long s_random = (long) (Math.random() * 1000000L);

   public static String executeRequest(Session session, Request request, boolean isAction) throws HttpException,
         IOException, ThreadException {
      Response response = request.execute(session);

      String content = IOUtil.toString(response.getContent(), response.getCharset());

      if (isAction) {
         if (content != null && content.contains("资源不足")) {
            throw new ThreadException("资源不足");
         }
      }

      return content;
   }

   public static void setRandom(ThreadContext ctx) {
      Session session = ctx.getSession();

      session.setProperty("random", s_random++);
   }

   public static String getHtml(Game game, String id) {
      StringBuilder sb = new StringBuilder(2048);

      for (Html html : game.getHtmls()) {
         if (html.getId().equals(id)) {
            sb.append(html.getText());
         }
      }

      return sb.toString();
   }
}
