package com.site.game.sanguo;

import java.util.ArrayList;
import java.util.List;

import com.site.dal.xml.XmlAdapter;
import com.site.dal.xml.registry.XmlRegistry;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.engine.DefaultThreadEngine;
import com.site.game.sanguo.thread.engine.ThreadEngine;
import com.site.game.sanguo.thread.handler.BuildingHandler;
import com.site.game.sanguo.thread.handler.CourtHandler;
import com.site.game.sanguo.thread.handler.FightHandler;
import com.site.game.sanguo.thread.handler.GeneralHandler;
import com.site.game.sanguo.thread.handler.GiftHandler;
import com.site.game.sanguo.thread.handler.LandingPageHandler;
import com.site.game.sanguo.thread.handler.QuestionHandler;
import com.site.game.sanguo.thread.handler.RefreshHandler;
import com.site.game.sanguo.thread.handler.ResourceHandler;
import com.site.game.sanguo.thread.handler.SigninHandler;
import com.site.game.sanguo.thread.handler.TraderHandler;
import com.site.game.sanguo.thread.handler.building.BuildingPlan;
import com.site.game.sanguo.thread.handler.building.CustomizedBuildingPlan;
import com.site.game.sanguo.thread.handler.building.DefaultBuildingPlan;
import com.site.game.sanguo.thread.handler.building.PrimaryBuildingPlan;
import com.site.game.sanguo.thread.handler.building.SecondaryBuildingPlan;
import com.site.game.sanguo.thread.handler.fighting.AddressedColoniaManager;
import com.site.game.sanguo.thread.handler.fighting.ColoniaManager;
import com.site.game.sanguo.thread.handler.fighting.CompositedColoniaManager;
import com.site.game.sanguo.thread.handler.fighting.RangedColoniaManager;
import com.site.game.sanguo.thread.handler.general.TaskEvaluator;
import com.site.game.sanguo.thread.handler.question.Answerer;
import com.site.game.sanguo.thread.handler.question.ChapterAnswerer;
import com.site.game.sanguo.thread.handler.question.IdiomAnswerer;
import com.site.game.sanguo.thread.handler.question.KillAnswerer;
import com.site.game.sanguo.thread.handler.question.MiscAnswerer;
import com.site.game.sanguo.thread.handler.question.NameAnswerer;
import com.site.game.sanguo.thread.handler.question.WordAnswerer;
import com.site.game.sanguo.thread.handler.trader.DefaultTraderPlan;
import com.site.game.sanguo.thread.handler.trader.TraderCalculator;
import com.site.game.sanguo.thread.handler.trader.TraderPlan;
import com.site.game.sanguo.thread.wdbc.DefaultWdbcFetcher;
import com.site.game.sanguo.thread.wdbc.WdbcFetcher;
import com.site.game.sanguo.thread.wdbc.filter.BuildingDetailFilter;
import com.site.game.sanguo.thread.wdbc.filter.BuildingListFilter;
import com.site.game.sanguo.thread.wdbc.filter.CourtListFilter;
import com.site.game.sanguo.thread.wdbc.filter.GeneralDetailFilter;
import com.site.game.sanguo.thread.wdbc.filter.GeneralItemsFilter;
import com.site.game.sanguo.thread.wdbc.filter.GeneralListFilter;
import com.site.game.sanguo.thread.wdbc.filter.MapDetailFilter;
import com.site.game.sanguo.thread.wdbc.filter.MapStatusFilter;
import com.site.game.sanguo.thread.wdbc.filter.ResourceDetailFilter;
import com.site.game.sanguo.thread.wdbc.filter.ResourceListFilter;
import com.site.game.sanguo.thread.wdbc.filter.StateListFilter;
import com.site.game.sanguo.thread.wdbc.filter.StoreItemsFilter;
import com.site.game.sanguo.thread.wdbc.filter.VillageDetailFilter;
import com.site.lookup.configuration.Component;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.configuration.AbstractWdbcComponentsConfigurator;
import com.site.wdbc.http.impl.DefaultSession;
import com.site.wdbc.http.impl.FormRequest;

public class ComponentsConfigurator extends AbstractWdbcComponentsConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }

   private void addBuildingComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "building", BuildingHandler.class)
               .req(WdbcFetcher.class, BuildingPlan.class)
               .req(Request.class, "build.upgrade", "m_upgradeRequest")
               .req(Request.class, "build.construction", "m_constructionRequest")
               );
      
      all.add(C(BuildingPlan.class, DefaultBuildingPlan.class)
               .req(BuildingPlan.class, "primary", "m_primary")
               .req(BuildingPlan.class, "secondary", "m_secondary")
               .req(BuildingPlan.class, "customized", "m_customized")
               );
      all.add(C(BuildingPlan.class, "primary", PrimaryBuildingPlan.class));
      all.add(C(BuildingPlan.class, "secondary", SecondaryBuildingPlan.class));
      all.add(C(BuildingPlan.class, "customized", CustomizedBuildingPlan.class)
               .req(Configuration.class));
      all.add(C(Request.class, "build.upgrade", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=build.upgrade&bid=${building-id}&villageid=${village-id}&rand=${random}]]>")));
      all.add(C(Request.class, "build.construction", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=build.construction&bid=${building-id}&btid=${building-type-id}&villageid=${village-id}&rand=${random}]]>")));
   }
   
   private void addTraderComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "trader", TraderHandler.class)
               .req(TraderPlan.class)
               .req(Request.class, "trade", "m_tradeRequest"));
      all.add(C(TraderPlan.class, DefaultTraderPlan.class)
            .req(TraderCalculator.class));
      all.add(C(TraderCalculator.class));
      
      all.add(C(Request.class, "trade", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=build.act&btid=18&do=submitTrade&villageid=${village-id}&rand=${random}]]>"),
                       E("method").value("POST"),
                       E("inputs").add(
                             E("input", "name", "Trader_Resource_Lumber").value("${lumber}"),
                             E("input", "name", "Trader_Resource_Clay").value("${clay}"),
                             E("input", "name", "Trader_Resource_Iron").value("${iron}"),
                             E("input", "name", "Trader_Resource_Crop").value("${crop}"),
                             E("input", "name", "Trader_Target_X").value("${x}"),
                             E("input", "name", "Trader_Target_Y").value("${y}"),
                             E("input", "name", "trade_num").value("1"),
                             E("input", "name", "village_name").value("${villageName}"),
                             E("input", "name", "con_num"),
                             null)));
   }

   private void addDalXmlComponents(List<Component> all) {
//      all.add(C(XmlRegistryFilter.class, DefaultXmlRegistryFilter.class)
//               .config(E("packages")
//                        .add(E("package").value(Game.class.getPackage().getName()))));
//      
//      all.add(C(XmlRegistry.class, DefaultXmlRegistry.class).req(XmlRegistryFilter.class));
//      all.add(C(XmlAdapter.class, DefaultXmlAdapter.class));
   }
   
   private void addGiftComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "gift", GiftHandler.class)
               .req(Request.class, "mygift"));
      
      all.add(C(Request.class, "mygift", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=index.mygift&villageid=${village-id}&rand=${random}]]>")));
   }
   
   private void addLandingPageComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "landing-page", LandingPageHandler.class)
               .req(Configuration.class, Session.class)
               .req(Request.class, "landing-page"));
      
      all.add(C(Request.class, "landing-page", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/]]>")));
   }
   
   private void addQuestionComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "question", QuestionHandler.class)
               .req(Request.class, "question")
               .req(Answerer.class, new String[] { "name", "chapter", "word", "kill", "idiom", "misc" }, "m_answerers"));

      all.add(C(Answerer.class, "name", NameAnswerer.class));
      all.add(C(Answerer.class, "chapter", ChapterAnswerer.class));
      all.add(C(Answerer.class, "word", WordAnswerer.class));
      all.add(C(Answerer.class, "kill", KillAnswerer.class));
      all.add(C(Answerer.class, "idiom", IdiomAnswerer.class));
      all.add(C(Answerer.class, "misc", MiscAnswerer.class));
      
      all.add(C(Request.class, "question", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=question.answer&rstid=${question-result}&villageid=${village-id}&rand=${random}]]>")));
   }
   
   private void addRefreshComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "refresh", RefreshHandler.class)
               .req(Configuration.class, XmlAdapter.class, WdbcFetcher.class)
               .req(Request.class, "resource_status", "m_resourcesStatusRequest")
               .req(ColoniaManager.class, "composited")
               );
      
      all.add(C(Request.class, "resource_status", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=resources.status&villageid=${village-id}&rand=${random}]]>")));
   }
   
   private void addResourceComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "resource", ResourceHandler.class)
               .req(WdbcFetcher.class)
               .req(Request.class, "detailup", "m_upgradeRequest")
               );
      
      all.add(C(Request.class, "detailup", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=resources.detailup&resourceid=${resource-id}&villageid=${village-id}&rand=${random}]]>")));
   }
   
   private void addSigninComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "signin", SigninHandler.class)
               .req(Configuration.class, Session.class)
               .req(Request.class, "signin"));
      
      all.add(C(Request.class, "signin", FormRequest.class)
               .config(E("action").value("<![CDATA[${login-url}?email=${login-email}&password=${login-password}&origURL=${landing-page-url}&formName=&method=]]>"), 
                        E("method").value("POST")));
   }
   
   private void addGeneralComponents(List<Component> all) {
      all.add(C(TaskEvaluator.class));
      all.add(C(ThreadHandler.class, "general", GeneralHandler.class)
               .req(WdbcFetcher.class, TaskEvaluator.class)
               .req(Request.class, "start_state", "m_startStateRequest")
               .req(Request.class, "change_item", "m_changeItemRequest")
               .req(Request.class, "unlade_item", "m_unladeItemRequest")
               );
      
      all.add(C(Request.class, "start_state", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=emperor.start_state&aid=${task-id}&gid=${general-id}&villageid=${village-id}&rand=${random}]]>")));
      all.add(C(Request.class, "change_item", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=general.changeitem&gid=${general-id}&goodid=${item-id}&villageid=${village-id}&rand=${random}]]>")));
      all.add(C(Request.class, "unlade_item", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=general.unladeitem&gid=${general-id}&type=4&villageid=${village-id}&rand=${random}]]>")));
   }
   
   private void addFightComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "fight", FightHandler.class)
               .req(Configuration.class, WdbcFetcher.class)
               .req(Request.class, "start_war", "m_startWarRequest")
               .req(Request.class, "start_war_confirm", "m_startWarConfirmRequest")
               );
      
      all.add(C(Request.class, "start_war", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=build.act&do=start_war&bid=${building-id}&btid=9&type=1&battlearray=0${soldier-data}&x=${x}&y=${y}&keep=right&villageid=${village-id}&rand=${random}]]>")));
      all.add(C(Request.class, "start_war_confirm", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=build.act&do=start_war&bid=${building-id}&btid=9&start=1&type=1&battlearray=0${soldier-data}&x=${x}&y=${y}&keep=right&villageid=${village-id}&rand=${random}]]>")));
      
      all.add(C(ColoniaManager.class, "ranged", RangedColoniaManager.class)
               .req(Configuration.class, WdbcFetcher.class));
      all.add(C(ColoniaManager.class, "addressed", AddressedColoniaManager.class)
               .req(Configuration.class, WdbcFetcher.class));
      all.add(C(ColoniaManager.class, "composited", CompositedColoniaManager.class)
               .req(ColoniaManager.class, new String[] { "ranged", "addressed" }, "m_managers"));
   }
   
   private void addCourtComponents(List<Component> all) {
      all.add(C(ThreadHandler.class, "court", CourtHandler.class)
               .req(Request.class, "state_court", "m_stateCourtRequest"));
      
      all.add(C(Request.class, "state_court", FormRequest.class)
               .config(E("action").value("<![CDATA[http://${server-name}/index.php?act=emperor.state_court&villageid=${village-id}&rand=${random}]]>")));
   }
   
   private void addWdbcComponents(List<Component> all) {
      all.add(C(WdbcFetcher.class, DefaultWdbcFetcher.class)
               .req(XmlRegistry.class, XmlAdapter.class)
               .req(Request.class, "state_list", "m_stateListRequest")
               .req(Request.class, "general_list", "m_generalListRequest")
               .req(Request.class, "general_items", "m_generalItemsRequest")
               .req(Request.class, "store_items", "m_storeItemsRequest")
               .req(Request.class, "resource_list", "m_resourceListRequest")
               .req(Request.class, "resource_detail", "m_resourceDetailRequest")
               .req(Request.class, "building_list", "m_buildingListRequest")
               .req(Request.class, "building_detail", "m_buildingDetailRequest")
               .req(Request.class, "map_status", "m_mapStatusRequest")
               .req(Request.class, "court_list", "m_courtListRequest")
               .req(Request.class, "general_detail", "m_generalDetailRequest")
               .req(Request.class, "village_detail", "m_villageDetailRequest")
               .req(Request.class, "map_detail", "m_mapDetailRequest")
               );
      
      all.addAll(RQF("state_list", StateListFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=emperor.state_list&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                        P("id", "div.table.tr[2-].td[7].input.@onclick"),
                        P("state", "div.table.tr[2-].td"),
                        P("points", "div.table.tr[2-].td[2]"),
                        P("type", "div.table.tr[2-].td[3]"),
                        P("general", "div.table.tr[2-].td[4]"),
                        P("status", "div.table.tr[2-].td[6]"),
                        null))
                        .getComponents());
      all.addAll(RQF("general_list", GeneralListFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=emperor.general_list&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                        P("id", "div.table.tr[2-].td[2].a.@href"),
                        P("name", "div.table.tr[2-].td[2].a"),
                        P("village", "div.table.tr[2-].td[3].a"),
                        P("type", "div.table.tr[2-].td[4]"),
                        P("level", "div.table.tr[2-].td[5]"),
                        P("tili", "div.table.tr[2-].td[6]"),
                        P("wuli", "div.table.tr[2-].td[7]"),
                        P("tongyuli", "div.table.tr[2-].td[8]"),
                        P("zhili", "div.table.tr[2-].td[9]"),
                        P("zhenzhili", "div.table.tr[2-].td[10]"),
                        P("status", "div.table.tr[2-].td[11]"),
                        null))
                        .getComponents());
      all.addAll(RQF("general_items", GeneralItemsFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=general.items&gid=${general-id}&type=4&villageid=${village-id}&rand=${random}&keep=left]]>"))
               .qconfig(E("paths").add(
                        P("id", "div.ul.li[*].a.@href"),
                        P("name", "div.ul.li[*].span"),
                        null))
                        .getComponents());
      all.addAll(RQF("store_items", StoreItemsFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=store.main&st=3&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                        P("id", "div.ul.li[*].img.@onclick"),
                        P("name", "div.ul.li[*].p.strong"),
                        P("type", "-"),
                        P("points", "-"),
                        P("description", "-"),
                        P("count", "-"),
                        null))
                        .getComponents());
      all.addAll(RQF("resource_list", ResourceListFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=resources.status&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                        P("id", "span[*].@onclick"),
                        P("type", "span[*].@title"),
                        P("level", "span[*]"),
                        null))
                        .getComponents());
      all.addAll(RQF("resource_detail", ResourceDetailFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=resources.detail&rid=${resource-id}&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                        P("lumber", "div.p[3].span"),
                        P("clay", "-"),
                        P("iron", "-"),
                        P("crop", "-"),
                        P("population", "div.p[3].span[2]"),
                        null))
                        .getComponents());
      all.addAll(RQF("building_list", BuildingListFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=build.status&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                        P("id", "area[*].@onclick"),
                        P("type", "area[*].@title"),
                        P("level", "-"),
                        null))
                        .getComponents());
      all.addAll(RQF("building_detail", BuildingDetailFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=build.worksite&bid=${building-id}&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                        P("lumber", "div.p[*].span"),
                        P("clay", "-"),
                        P("iron", "-"),
                        P("crop", "-"),
                        P("population", "div.p[*].span[2]"),
                        null))
                        .getComponents());
      all.addAll(RQF("map_status", MapStatusFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=map.status&uitx=${x}&uity=${y}&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                        P("emperor", "area[*].@onmouseover"),
                        P("population", "-"),
                        P("alliance", "-"),
                        P("x", "-"),
                        P("y", "-"),
                        P("state", "-"),
                        null))
                        .getComponents());
      all.addAll(RQF("court_list", CourtListFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=emperor.court_list&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                     P("hasButton", "div.p.input.@onclick"),
                     P("date", "div.strong.span[*]"),
                     null))
                     .getComponents());
      all.addAll(RQF("general_detail", GeneralDetailFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=general.detail&gid=${general-id}&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                     P("name", "div[2].div.span"),
                     P("level", "div[2].div.ul.li"),
                     P("exp", "div[2].div.ul.li[2]"),
                     P("type", "div[2].div.ul.li[3]"),
                     P("title", "div[2].div.ul.li[4]"),
                     P("village", "div[2].div.ul.li[5]"),
                     P("tili", "div[2].div[2].ul.li.span"),
                     P("wuli", "div[2].div[2].ul.li[2].span"),
                     P("tongyu", "div[2].div[2].ul.li[3].span"),
                     P("zhili", "div[2].div[2].ul.li[4].span"),
                     P("zhenzhi", "div[2].div[2].ul.li[5].span"),
                     P("baowu", "div[2].div[2].ul[3].li.a.img.@src"),
                     P("zuoqi", "div[2].div[2].ul[3].li[2].a.img.@src"),
                     P("juanzhuo", "div[2].div[2].ul[3].li[3].a.img.@src"),
                     null))
                     .getComponents());
      all.addAll(RQF("village_detail", VillageDetailFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=village.detail&vid=${vid}&keep=left&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                     P("village", "div.h2.span[2]"),
                     P("tribe", "div.p"),
                     P("alliance", "div.p.a.strong"),
                     P("emperor", "div.p.a[2].strong"),
                     P("population", "-"),
                     P("rank", "div.p.a[4]"),
                     null))
                     .getComponents());
      all.addAll(RQF("map_detail", MapDetailFilter.class)
               .rconfig(E("action").value("<![CDATA[http://${server-name}/index.php?act=map.detail&uitx=${x}&uity=${y}&keep=right&villageid=${village-id}&rand=${random}]]>"))
               .qconfig(E("paths").add(
                     P("village", "div.h2.span[2]"),
                     P("tribe", "div.p"),
                     P("alliance", "div.p.a.strong"),
                     P("emperor", "div.p.a[2].strong"),
                     P("population", "-"),
                     P("rank", "div.p.a[4]"),
                     null))
                     .getComponents());
   }
   
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(Session.class, DefaultSession.class).config(
               E("headers").add(
                  E("header", "name", "User-Agent")
                     .value("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; .NET CLR 1.1.4322)")
               )));

      all.add(C(Configuration.class).config(E("config").value("config.xml")));
      
      all.add(C(ThreadEngine.class, DefaultThreadEngine.class)
               .config(E("interval").value("120000"))
               .req(ThreadHandler.class, new String[] { 
                  "signin", 
                  "landing-page", 
                  "refresh", 
                  "question", 
//                  "gift", 
                  "resource", 
                  "building", 
//                  "trader", 
//                  "general", 
                  "fight", 
//                  "court",
                  }, "m_handlers"));

      addDalXmlComponents(all);
      addWdbcComponents(all);
      
      addSigninComponents(all);
      addLandingPageComponents(all);
      addRefreshComponents(all);
      addQuestionComponents(all);
      addGiftComponents(all);
      addResourceComponents(all);
      addBuildingComponents(all);
      addGeneralComponents(all);
      addFightComponents(all);
      addCourtComponents(all);
      addTraderComponents(all);
      
      return all;
   }
}
