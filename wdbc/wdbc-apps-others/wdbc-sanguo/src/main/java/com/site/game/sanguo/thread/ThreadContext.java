package com.site.game.sanguo.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.game.sanguo.api.Game;
import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Model;
import com.site.game.sanguo.thread.handler.building.Building;
import com.site.wdbc.http.Session;

public class ThreadContext {
   private Session m_session;

   private boolean m_signedIn;

   private String m_mainVillageId;

   private boolean m_forceRefresh;

   private List<Building> m_buildings;

   private Map<Class<?>, Object> m_customDataMap = new HashMap<Class<?>, Object>();

   // --
   private Game m_game;

   private Model m_model;

   private Farm m_farm;

   private long m_timeDifference;

   public long getTimeDifference() {
      return m_timeDifference;
   }

   public void setTimeDifference(long timeDifference) {
      m_timeDifference = timeDifference;
   }

   public Model getModel() {
      return m_model;
   }

   public void setModel(Model model) {
      m_model = model;
   }

   public Farm getFarm() {
      return m_farm;
   }

   public void setFarm(Farm farm) {
      m_farm = farm;
   }

   public List<Building> getBuildings() {
      return m_buildings;
   }

   @SuppressWarnings("unchecked")
   public <T> T getData(Class<?> clazz) {
      return (T) m_customDataMap.get(clazz);
   }

   public Game getGame() {
      return m_game;
   }

   public String getMainVillageId() {
      return m_mainVillageId;
   }

   public Session getSession() {
      return m_session;
   }

   public boolean isForceRefresh() {
      return m_forceRefresh;
   }

   public boolean isSignedIn() {
      return m_signedIn;
   }

   public void setBuildings(List<Building> buildings) {
      m_buildings = buildings;
   }

   public <T> void setData(Class<?> clazz, T data) {
      m_customDataMap.put(clazz, data);
   }

   public void setForceRefresh(boolean forceRefresh) {
      m_forceRefresh = forceRefresh;
   }

   public void setGame(Game game) {
      m_game = game;
   }

   public void setMainVillageId(String mainVillageId) {
      m_mainVillageId = mainVillageId;
   }

   public void setSession(Session session) {
      m_session = session;
   }

   public void setSignedIn(boolean signedIn) {
      m_signedIn = signedIn;
   }
}
