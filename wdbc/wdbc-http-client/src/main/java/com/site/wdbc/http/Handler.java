package com.site.wdbc.http;

import java.util.List;

import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcQuery;

public interface Handler {
   public void addRequest(Request request);

   public void execute(Session session) throws WdbcException;

   public void setChildren(List<Handler> children);

   public void setHandleCurrentPage(boolean handleCurrentPage);

   public void setInterval(long seconds);

   public void setProcessor(Processor processor);

   public void setQuery(WdbcQuery query);
}
