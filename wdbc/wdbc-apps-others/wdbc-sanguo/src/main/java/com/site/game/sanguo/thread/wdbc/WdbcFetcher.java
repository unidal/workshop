package com.site.game.sanguo.thread.wdbc;

import java.io.IOException;

import org.apache.http.HttpException;

import com.site.dal.xml.XmlException;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcResult;

public interface WdbcFetcher {

   public WdbcResult getBuildingDetail(ThreadContext ctx, int id) throws HttpException, IOException, XmlException,
         WdbcException;

   public WdbcResult getBuildingList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException;

   public WdbcResult getGeneralItems(ThreadContext ctx, int id) throws HttpException, IOException, XmlException,
         WdbcException;

   public WdbcResult getGeneralList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException;

   public WdbcResult getResourceDetail(ThreadContext ctx, int id) throws HttpException, IOException, XmlException,
         WdbcException;

   public WdbcResult getResourceList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException;

   public WdbcResult getStateList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException;

   public WdbcResult getStoreItems(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException;

   public WdbcResult getVillageList(ThreadContext ctx, int x, int y) throws HttpException, IOException, XmlException,
         WdbcException;

   public WdbcResult getCourtList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException;

   public WdbcResult getGeneralDetail(ThreadContext ctx, int id) throws HttpException, IOException, XmlException,
         WdbcException;

   public WdbcResult getVillageDetail(ThreadContext ctx, int villageId) throws HttpException, IOException,
         XmlException, WdbcException;

   public WdbcResult getVillageDetail(ThreadContext ctx, int x, int y) throws HttpException, IOException, XmlException,
         WdbcException;
}
