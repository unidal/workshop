package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.xml.sax.InputSource;

import com.site.dal.xml.XmlAdapter;
import com.site.dal.xml.XmlException;
import com.site.game.sanguo.Configuration;
import com.site.game.sanguo.api.Game;
import com.site.game.sanguo.api.Html;
import com.site.game.sanguo.api.Resource;
import com.site.game.sanguo.api.Village;
import com.site.game.sanguo.model.Build;
import com.site.game.sanguo.model.Colonia;
import com.site.game.sanguo.model.Court;
import com.site.game.sanguo.model.Equipment;
import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Field;
import com.site.game.sanguo.model.Gift;
import com.site.game.sanguo.model.Model;
import com.site.game.sanguo.model.Price;
import com.site.game.sanguo.model.Question;
import com.site.game.sanguo.model.Soldier;
import com.site.game.sanguo.model.StateTask;
import com.site.game.sanguo.model.Status;
import com.site.game.sanguo.model.Stock;
import com.site.game.sanguo.model.Task;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.game.sanguo.thread.handler.fighting.ColoniaManager;
import com.site.game.sanguo.thread.wdbc.WdbcFetcher;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Request;

public class RefreshHandler implements ThreadHandler, LogEnabled {
   private Logger m_logger;

   private Configuration m_configuration;

   private XmlAdapter m_xmlAdapter;

   private WdbcFetcher m_wdbcFetcher;

   private Request m_resourcesStatusRequest;

   private long m_timeDifference = Long.MIN_VALUE;

   private ColoniaManager m_coloniaManager;

   private String convertEquipmentTaskTypeName(String typeName) {
      if (typeName == null) {
         return null;
      } else if (typeName.equals("武力")) {
         return "单挑";
      } else if (typeName.equals("统御")) {
         return "围剿";
      } else if (typeName.equals("政治")) {
         return "说服";
      } else if (typeName.equals("智力")) {
         return "招纳";
      }

      return null;
   }

   private String convertFieldTypeCode(String typeName) {
      if ("农田".equals(typeName)) {
         return "crop";
      } else if ("林场".equals(typeName)) {
         return "lumber";
      } else if ("采石场".equals(typeName)) {
         return "clay";
      } else if ("矿山".equals(typeName)) {
         return "iron";
      }

      return null;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private List<Build> getBuildList(ThreadContext ctx) {
      List<Build> buildings = new ArrayList<Build>();

      try {
         WdbcResult result = m_wdbcFetcher.getBuildingList(ctx);
         int len = result.getRowSize();

         for (int i = 0; i < len; i++) {
            Build building = new Build();
            String level = ((String) result.getCell(i, "level"));

            building.setResourceId(Integer.parseInt((String) result.getCell(i, "id")));
            building.setTypeName((String) result.getCell(i, "type"));

            if (level != null && level.length() > 0) {
               building.setLevel(Integer.parseInt(level));
            }

            buildings.add(building);
         }
      } catch (Exception e) {
         m_logger.warn("Error when getting building list.", e);
      }

      return buildings;
   }

   private List<Colonia> getColoniaList(ThreadContext ctx) {
      List<Colonia> colonias = new ArrayList<Colonia>(30);
      int diameter = m_configuration.getFightDiameter();
      int segments = (diameter + 6) / 7;
      int x = ctx.getGame().getVillage().getX();
      int y = ctx.getGame().getVillage().getY();
      Set<String> done = new HashSet<String>();
      Set<String> excludes = new HashSet<String>();

      try {
         for (int i = 0; i < segments; i++) {
            for (int j = 0; j < segments; j++) {
               int offsetx = (i - segments / 2) * 7 + 3;
               int offsety = (j - segments / 2) * 7 + 3;
               WdbcResult result = m_wdbcFetcher.getVillageList(ctx, x + offsetx, y + offsety);
               int len = result.getRowSize();

               for (int row = 0; row < len; row++) {
                  Colonia colonia = new Colonia();
                  int xx = Integer.parseInt((String) result.getCell(row, "x"));
                  int yy = Integer.parseInt((String) result.getCell(row, "y"));

                  colonia.setX(xx);
                  colonia.setY(yy);
                  colonia.setVillageName((String) result.getCell(row, "village"));
                  colonia.setTribe((String) result.getCell(row, "tribe"));
                  colonia.setAlliance((String) result.getCell(row, "alliance"));
                  colonia.setEmperor((String) result.getCell(row, "emperor"));
                  colonia.setPopulation(Integer.parseInt((String) result.getCell(row, "population")));
                  colonia.setRank((String) result.getCell(row, "rank"));

                  String key = xx + ":" + yy;

                  if (!done.contains(key) && colonia.getAlliance().length() == 0
                        && (xx - x) * (xx - x) + (yy - y) * (yy - y) <= diameter * diameter) {
                     if (colonia.getPopulation() > 220) {
                        excludes.add(colonia.getEmperor());
                     } else if (!excludes.contains(colonia.getEmperor())) {
                        colonias.add(colonia);
                        done.add(key);
                     }
                  }
               }
            }
         }
      } catch (Exception e) {
         m_logger.warn("Error when getting colonia list.", e);
      }

      Collections.shuffle(colonias);
      m_logger.info(colonias.size() + " colonias found in the diameter of " + diameter);
      return colonias;
   }

   private Court getCourt(ThreadContext ctx) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Court court = new Court();

      try {
         WdbcResult result = m_wdbcFetcher.getCourtList(ctx);
         String date = (String) result.getCell(0, "date");
         Boolean hasButton = (Boolean) result.getCell(0, "hasButton");

         court.setNextSchedule(dateFormat.parse(date));
         court.setEligible(hasButton.booleanValue());
      } catch (Exception e) {
         m_logger.warn("Error when fetching court list.", e);
      }

      return court;
   }

   private List<Equipment> getEquipmentList(ThreadContext ctx) {
      List<Equipment> equipments = new ArrayList<Equipment>();

      try {
         WdbcResult result = m_wdbcFetcher.getStoreItems(ctx);
         int len = result.getRowSize();

         for (int i = 0; i < len; i++) {
            Equipment equipment = new Equipment();

            equipment.setId(Integer.parseInt((String) result.getCell(i, "id")));
            equipment.setName((String) result.getCell(i, "name"));
            equipment.setPoints(Integer.parseInt((String) result.getCell(i, "points")));
            equipment.setTypeName((String) result.getCell(i, "type"));
            equipment.setTaskTypeName(convertEquipmentTaskTypeName(equipment.getTypeName()));
            equipment.setCount(Integer.parseInt((String) result.getCell(i, "count")));

            equipments.add(equipment);
         }
      } catch (Exception e) {
         m_logger.warn("Error when getting equipment list.", e);
      }

      return equipments;
   }

   private List<StateTask> getStateTaskList(ThreadContext ctx) {
      List<StateTask> stateTasks = new ArrayList<StateTask>();

      try {
         WdbcResult result = m_wdbcFetcher.getStateList(ctx);
         int len = result.getRowSize();

         for (int i = 0; i < len; i++) {
            StateTask stateTask = new StateTask();

            stateTask.setId(Integer.parseInt((String) result.getCell(i, "id")));
            stateTask.setStateName((String) result.getCell(i, "state"));
            stateTask.setPoints(Integer.parseInt((String) result.getCell(i, "points")));
            stateTask.setTypeName((String) result.getCell(i, "type"));
            stateTask.setGeneral((String) result.getCell(i, "general"));
            stateTask.setStatus((String) result.getCell(i, "status"));

            stateTasks.add(stateTask);
         }
      } catch (Exception e) {
         m_logger.warn("Error when getting state task list.", e);
      }

      return stateTasks;
   }

   private List<Farm> getFarms(ThreadContext ctx) {
      Model model = ctx.getModel();

      if (model.getFarms().isEmpty()) {
         for (Village e : ctx.getGame().getVillageList()) {
            Farm farm = new Farm();

            farm.setVillageId(e.getId());
            farm.setVillageName(e.getName());

            if (e.getName().equals(ctx.getGame().getVillage().getName())) {
               farm.setMain(true);
            }

            model.getFarms().add(farm);
         }
      }

      return model.getFarms();
   }

   private List<Field> getFieldList(ThreadContext ctx) {
      List<Field> fields = new ArrayList<Field>();

      try {
         WdbcResult result = m_wdbcFetcher.getResourceList(ctx);
         int len = result.getRowSize();

         for (int i = 0; i < len; i++) {
            Field field = new Field();
            String level = ((String) result.getCell(i, "level"));

            field.setResourceId(Integer.parseInt((String) result.getCell(i, "id")));
            field.setTypeName((String) result.getCell(i, "type"));
            field.setTypeCode(convertFieldTypeCode(field.getTypeName()));

            if (level != null && level.length() > 0) {
               field.setLevel(Integer.parseInt(level));
            }

            fields.add(field);
         }
      } catch (Exception e) {
         m_logger.warn("Error when getting field list.", e);
      }

      return fields;
   }

   private Gift getGift(ThreadContext ctx) {
      Gift gift = new Gift();

      Html[] htmls = ctx.getGame().getHtmls();

      for (Html html : htmls) {
         if (html.getId().equals("mygift")) {
            gift.setEligible(html.getText().contains("新手礼包"));
            break;
         }
      }

      return gift;
   }

   private Question getQuestion(ThreadContext ctx) {
      com.site.game.sanguo.api.Question q = ctx.getGame().getQuestion();

      if (q != null && !q.isAnswered()) {
         Question question = new Question();

         question.setId(q.getId());
         question.setContent(q.getContent());
         question.setItem1(q.getItem1());
         question.setItem2(q.getItem2());
         question.setItem3(q.getItem3());
         question.setItem4(q.getItem4());

         return question;
      }

      return null;
   }

   private List<Task> getQueuedTasks(ThreadContext ctx) {
      List<Task> tasks = new ArrayList<Task>();

      if (ctx.getGame().getTasks() != null) {
         if (ctx.getGame().getTasks().getTaskList() != null) {
            for (com.site.game.sanguo.api.Task e : ctx.getGame().getTasks().getTaskList()) {
               Task task = new Task();

               task.setId(e.getQueueid());
               task.setTypeId(e.getType());
               task.setIntro(e.getIntro());
               task.setBeginTime(e.getBegintime());
               task.setEndTime(e.getEndtime());
               tasks.add(task);
            }
         }
      }

      return tasks;
   }

   private Game getResourcesStatus(ThreadContext ctx) throws HttpException, IOException, ThreadException, XmlException {
      ThreadHelper.setRandom(ctx);

      String content = ThreadHelper.executeRequest(ctx.getSession(), m_resourcesStatusRequest, false);

      try {
         return m_xmlAdapter.unmarshal(new InputSource(new StringReader(content)));
      } catch (XmlException e) {
         m_logger.info("Content is: \r\n" + content);
         throw e;
      }
   }

   private List<Soldier> getSoldierList(ThreadContext ctx) {
      List<Soldier> soldiers = new ArrayList<Soldier>();

      if (ctx.getGame().getSoldiers().getSoldierList() != null) {
         for (com.site.game.sanguo.api.Soldier e : ctx.getGame().getSoldiers().getSoldierList()) {
            Soldier soldier = new Soldier();

            soldier.setVillageId(e.getVid());
            soldier.setTribe(e.getTribe());
            soldier.setTypeId(e.getType());
            soldier.setCount(e.getCount());

            soldiers.add(soldier);
         }
      }

      return soldiers;
   }

   private boolean isFirstTime() {
      return m_timeDifference == Long.MIN_VALUE;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      if (isFirstTime() || needRefresh(ctx)) {
         try {
            if (isFirstTime()) {
               Game game = getResourcesStatus(ctx);

               m_timeDifference = System.currentTimeMillis() - game.getTime().getTime();

               ctx.setTimeDifference(m_timeDifference);
               ctx.setModel(new Model());
               ctx.setGame(game);
            }

            syncModel(ctx);
         } catch (Exception e) {
            m_logger.warn("Error when handling Refresh.", e);
         }
      }
   }

   private boolean needRefresh(ThreadContext ctx) {
      Date current = new Date(System.currentTimeMillis() - m_timeDifference);

      for (Farm farm : ctx.getModel().getFarms()) {
         // any schedule is passed
         for (Status status : farm.getHandlerStatuses().values()) {
            if (status.isDirty()) {
               return true;
            } else if (status.getNextSchedule() != null) {
               if (current.after(status.getNextSchedule())) {
                  return true;
               }
            }
         }

         // any task in queue is completed
         for (Task task : farm.getQueuedTasks()) {
            if (current.after(task.getEndTime())) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean shouldForceRefresh() {
      Calendar cal = Calendar.getInstance();

      cal.add(Calendar.MILLISECOND, -(int) m_timeDifference);

      return cal.get(Calendar.MINUTE) == 0;
   }

   private void syncFarm(ThreadContext ctx, Farm farm) throws HttpException, IOException, ThreadException, XmlException {
      ctx.getSession().setProperty("village-id", farm.getVillageId());
      ctx.setFarm(farm);

      ctx.setGame(getResourcesStatus(ctx));
      farm.setX(ctx.getGame().getVillage().getX());
      farm.setY(ctx.getGame().getVillage().getY());

      // stock
      for (Stock e : getStockList(ctx)) {
         farm.getStocks().put(e.getId(), e);
      }

      farm.setStock(getStock(ctx));

      // queued task
      farm.getQueuedTasks().clear();
      farm.getQueuedTasks().addAll(getQueuedTasks(ctx));

      // field
      if (farm.getFields().isEmpty() || shouldForceRefresh()) {
         farm.getFields().clear();
         farm.getFields().addAll(getFieldList(ctx));
      }

      // building
      if (farm.getBuildings().isEmpty() || shouldForceRefresh()) {
         farm.getBuildings().clear();

         for (Build e : getBuildList(ctx)) {
            farm.getBuildings().put(e.getResourceId(), e);
         }
      }

      // soldier
      farm.getSoldiers().clear();
      farm.getSoldiers().addAll(getSoldierList(ctx));

      // colonia
      if ((farm.getColonias().isEmpty() || shouldForceRefresh()) && farm.isMain()) {
         farm.getColonias().clear();

         if (true) { // get colonias from its manager
            farm.getColonias().addAll(m_coloniaManager.getColonias(ctx));
            Collections.shuffle(farm.getColonias());
         } else {
            farm.getColonias().addAll(getColoniaList(ctx));
         }
      }

      // state-task
      if (farm.getStateTasks().isEmpty() || shouldForceRefresh()) {
         farm.getStateTasks().clear();
         farm.getStateTasks().addAll(getStateTaskList(ctx));
      }
   }

   private Price getStock(ThreadContext ctx) {
      Map<String, Stock> stocks = ctx.getFarm().getStocks();
      Price stock = new Price();

      stock.setCrop(stocks.get("crop").getNow());
      stock.setLumber(stocks.get("lumber").getNow());
      stock.setClay(stocks.get("clay").getNow());
      stock.setIron(stocks.get("iron").getNow());
      stock.setPopulation(stocks.get("population").getNow());
      stock.setAvailablePopulation(stocks.get("population").getMax() - stocks.get("population").getNow());

      return stock;
   }

   private List<Stock> getStockList(ThreadContext ctx) {
      List<Stock> stocks = new ArrayList<Stock>();

      for (Resource e : ctx.getGame().getResources()) {
         Stock stock = new Stock();

         stock.setId(e.getId());
         stock.setNow(e.getNow());
         stock.setMax(e.getMax());
         stock.setSpeed(e.getSpeed());
         stock.setIncrease(e.getIncrease());
         stocks.add(stock);
      }

      return stocks;
   }

   private boolean needRefresh(ThreadContext ctx, String handler) {
      if (ctx.getModel().getFarms().isEmpty()) {
         return true;
      }

      Date current = new Date(System.currentTimeMillis() - m_timeDifference);
      Farm mainFarm = ctx.getModel().getFarms().get(0);
      Status status = mainFarm.getHandlerStatuses().get(handler);

      return status == null || status.isDirty() || status.getNextSchedule() != null
            && current.after(status.getNextSchedule());
   }

   private void syncModel(ThreadContext ctx) throws HttpException, IOException, ThreadException, XmlException {
      Model model = ctx.getModel();

      // gift
      model.setGift(getGift(ctx));

      // question
      Question question = getQuestion(ctx);
      if (question != null && !model.getUnansweredQuestions().containsKey(question.getId())) {
         model.setQuestion(question);
      } else {
         model.setQuestion(null);
      }

      // court
      if (needRefresh(ctx, "court")) {
         model.setCourt(getCourt(ctx));
      }

      // equipment
      if (model.getEquipments().isEmpty()) {
         model.getEquipments().addAll(getEquipmentList(ctx));
      }

      // farm
      for (Farm farm : getFarms(ctx)) {
         syncFarm(ctx, farm);
      }

      model.setLastRefresh(ctx.getGame().getTime());
   }
}
