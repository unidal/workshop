package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.dal.xml.XmlException;
import com.site.game.sanguo.model.Equipment;
import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Soldier;
import com.site.game.sanguo.model.StateTask;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.game.sanguo.thread.handler.general.General;
import com.site.game.sanguo.thread.handler.general.TaskEvaluator;
import com.site.game.sanguo.thread.wdbc.WdbcFetcher;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class GeneralHandler implements ThreadHandler, LogEnabled {
   private WdbcFetcher m_wdbcFetcher;

   private TaskEvaluator m_taskEvaluator;

   private Request m_startStateRequest;

   private Request m_changeItemRequest;

   private Request m_unladeItemRequest;

   private Logger m_logger;

   private int m_count;

   private void assignTask(ThreadContext ctx, General general, StateTask task) throws HttpException, IOException {
      Session session = ctx.getSession();

      session.setProperty("general-id", general.getId());
      session.setProperty("task-id", task.getId());
      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_startStateRequest, true);

         task.setStatus("已接受");

         for (Soldier soldier : ctx.getFarm().getSoldiers()) {
            if (soldier.getTypeId() == 100 && soldier.getId() == general.getId()) {
               soldier.setCount(0);
               break;
            }
         }

         m_logger.info(general.getName() + " is assigned to task(" + task.getPoints() + ": " + task.getTypeName()
               + " - " + task.getGeneral() + ")");
      } catch (ThreadException e) {
         // ignore it
      }
   }

   private boolean canProcess(ThreadContext ctx, Farm farm) {
      int count = 0;

      for (Soldier soldier : farm.getSoldiers()) {
         if (soldier.getTypeId() == 100) {
            count++;
         }
      }

      return count > 0;
   }

   private void changeEquipment(ThreadContext ctx, General general, Equipment equipment) throws HttpException,
         IOException {
      Session session = ctx.getSession();

      session.setProperty("general-id", general.getId());
      session.setProperty("item-id", equipment.getId());
      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_changeItemRequest, true);

         equipment.setCount(equipment.getCount() - 1);
         m_logger.info("Change equipment to " + equipment.getName() + "(" + equipment.getTypeName() + " + "
               + equipment.getPoints() + ") for " + general.getName());
      } catch (ThreadException e) {
         // ignore it
      }
   }

   private Equipment chooseEquipment(ThreadContext ctx, General general, StateTask task) {
      Equipment selected = null;

      for (Equipment equipment : ctx.getModel().getEquipments()) {
         if (equipment.getCount() > 0 && task.getTypeName().equals(equipment.getTaskTypeName())) {
            if (selected == null || equipment.getPoints() > selected.getPoints()) {
               selected = equipment;
            }
         }
      }

      return selected;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private void executeTask(ThreadContext ctx, General general) throws HttpException, IOException, ThreadException,
         XmlException, WdbcException {
      for (StateTask task : ctx.getFarm().getStateTasks()) {
         if (task.getStatus().equals("未接受") && m_taskEvaluator.evaluate(general, task)) {
            Equipment oldEquipment = getEquipment(ctx, general);

            if (oldEquipment != null) {
               unladeEquipment(ctx, general, oldEquipment);
            }

            Equipment newEquipment = chooseEquipment(ctx, general, task);

            if (newEquipment != null) {
               changeEquipment(ctx, general, newEquipment);
            }

            assignTask(ctx, general, task);
            break;
         }
      }
   }

   private Equipment getEquipment(ThreadContext ctx, General general) throws HttpException, IOException, XmlException,
         WdbcException {
      WdbcResult result = m_wdbcFetcher.getGeneralDetail(ctx, general.getId());
      int len = result.getRowSize();

      if (len > 0) {
         try {
            int id = Integer.parseInt((String) result.getCell(0, "baowu"));

            for (Equipment equipment : ctx.getModel().getEquipments()) {
               if (equipment.getId() == id) {
                  return equipment;
               }
            }
         } catch (NumberFormatException e) {
            // ignore it
         }
      }

      return null;
   }

   private List<General> fetchGeneralList(ThreadContext ctx) throws HttpException, IOException, XmlException,
         WdbcException {
      WdbcResult result = m_wdbcFetcher.getGeneralList(ctx);
      int len = result.getRowSize();
      List<General> generals = new ArrayList<General>(len);

      for (int i = 0; i < len; i++) {
         General general = new General();

         general.setId(Integer.parseInt((String) result.getCell(i, "id")));
         general.setName((String) result.getCell(i, "name"));
         general.setVillage((String) result.getCell(i, "village"));
         general.setType((String) result.getCell(i, "type"));
         general.setLevel(Integer.parseInt((String) result.getCell(i, "level")));
         general.setTili(Integer.parseInt((String) result.getCell(i, "tili")));
         general.setWuli(Integer.parseInt((String) result.getCell(i, "wuli")));
         general.setTongyuli(Integer.parseInt((String) result.getCell(i, "tongyuli")));
         general.setZhili(Integer.parseInt((String) result.getCell(i, "zhili")));
         general.setZhenzhili(Integer.parseInt((String) result.getCell(i, "zhenzhili")));
         general.setStatus((String) result.getCell(i, "status"));
         generals.add(general);
      }

      return generals;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      if (canProcess(ctx)) {
         for (Farm farm : ctx.getModel().getFarms()) {
            ctx.setFarm(farm);
            ctx.getSession().setProperty("village-id", farm.getVillageId());

            if (canProcess(ctx, farm)) {
               try {
                  List<General> generals = fetchGeneralList(ctx);

                  for (General general : generals) {
                     executeTask(ctx, general);
                  }
               } catch (Exception e) {
                  m_logger.warn("Error when handling Task.", e);
               }
            }
         }
      }
   }

   private boolean canProcess(ThreadContext ctx) {
      m_count++;

      if (m_count == 5) {
         m_count = 0;
         return true;
      } else {
         return false;
      }
   }

   private void unladeEquipment(ThreadContext ctx, General general, Equipment equipment) throws HttpException,
         IOException {
      Session session = ctx.getSession();

      session.setProperty("general-id", general.getId());
      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_unladeItemRequest, true);

         m_logger.info("Unlade equipment from " + general.getName());
         equipment.setCount(equipment.getCount() + 1);
      } catch (ThreadException e) {
         // ignore it
      }
   }
}
