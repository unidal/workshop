package com.site.game.sanguo.thread.handler.fighting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.game.sanguo.Configuration;
import com.site.game.sanguo.model.Colonia;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.wdbc.WdbcFetcher;
import com.site.wdbc.WdbcResult;

public class RangedColoniaManager implements ColoniaManager, LogEnabled {
   private Configuration m_configuration;

   private WdbcFetcher m_wdbcFetcher;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private void filterColonias(ThreadContext ctx, List<Colonia> colonias) {
      Set<Colonia> excludes = new HashSet<Colonia>();
      int x = ctx.getGame().getVillage().getX();
      int y = ctx.getGame().getVillage().getY();
      int diameter = m_configuration.getFightDiameter();
      int maxPopulation = m_configuration.getFightMaxPopulation();
      List<String> emperors = Arrays.asList(m_configuration.getFightExcludeEmperors().split(","));

      for (Colonia colonia : colonias) {
         int xx = colonia.getX();
         int yy = colonia.getY();

         if (colonia.getAlliance() != null && colonia.getAlliance().length() > 0) {
            excludes.add(colonia);
         } else if (isNotSingleVillage(colonias, colonia)) {
            excludes.add(colonia);
         } else if ((xx - x) * (xx - x) + (yy - y) * (yy - y) > diameter * diameter) {
            excludes.add(colonia);
         } else if (colonia.getPopulation() >= maxPopulation) {
            excludes.add(colonia);
         } else if (emperors.contains(colonia.getEmperor())) {
            excludes.add(colonia);
         }
      }

      for (Colonia colonia : excludes) {
         colonias.remove(colonia);
      }
   }

   public List<Colonia> getColonias(ThreadContext ctx) {
      List<Colonia> colonias = new ArrayList<Colonia>(1024);
      int diameter = m_configuration.getFightDiameter();
      int segments = (diameter + 6) / 7;
      int x = ctx.getGame().getVillage().getX();
      int y = ctx.getGame().getVillage().getY();

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

                  colonia.setEmperor((String) result.getCell(row, "emperor"));
                  colonia.setAlliance((String) result.getCell(row, "alliance"));
                  colonia.setPopulation(Integer.parseInt((String) result.getCell(row, "population")));
                  colonia.setX(xx);
                  colonia.setY(yy);

                  colonias.add(colonia);
               }
            }
         }
      } catch (Exception e) {
         m_logger.warn("Error when getting colonia list.", e);
      }

      filterColonias(ctx, colonias);

      m_logger.info(colonias.size() + " colonias found in the diameter of " + diameter);
      return colonias;
   }

   private boolean isNotSingleVillage(List<Colonia> colonias, Colonia colonia) {
      String emperor = colonia.getEmperor();
      int count = 0;

      for (Colonia e : colonias) {
         if (e.getEmperor().equals(emperor)) {
            count++;

            if (count > 1) {
               return true;
            }
         }
      }

      return false;
   }
}
