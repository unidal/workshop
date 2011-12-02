package com.site.game.sanguo.thread.handler.fighting;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.game.sanguo.Configuration;
import com.site.game.sanguo.model.Colonia;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.wdbc.WdbcFetcher;
import com.site.wdbc.WdbcResult;

public class AddressedColoniaManager implements ColoniaManager, LogEnabled {
   private Configuration m_configuration;

   private WdbcFetcher m_wdbcFetcher;

   private Logger m_logger;

   public List<Colonia> getColonias(ThreadContext ctx) {
      String villages = m_configuration.getFightVillages();
      String[] parts = villages.split(",");
      List<Colonia> colonias = new ArrayList<Colonia>(parts.length);

      for (String part : parts) {
         if (part.trim().length() > 0) {
            int pos = part.indexOf(':');

            if (pos < 0) {
               pos = part.indexOf('|');
            }

            if (pos > 0) {
               try {
                  Colonia colonia = new Colonia();
                  int x = Integer.parseInt(part.substring(0, pos).trim());
                  int y = Integer.parseInt(part.substring(pos + 1).trim());
                  WdbcResult result = m_wdbcFetcher.getVillageDetail(ctx, x, y);
                  int row = 0;

                  if (result.getRowSize() == 0) {
                     m_logger.warn("No village found at " + x + ":" + y);
                     continue;
                  }

                  colonia.setX(x);
                  colonia.setY(y);
                  colonia.setVillageName((String) result.getCell(row, "village"));
                  colonia.setTribe((String) result.getCell(row, "tribe"));
                  colonia.setAlliance((String) result.getCell(row, "alliance"));
                  colonia.setEmperor((String) result.getCell(row, "emperor"));
                  colonia.setPopulation(Integer.parseInt((String) result.getCell(row, "population")));
                  colonia.setRank((String) result.getCell(row, "rank"));

                  colonias.add(colonia);
               } catch (Exception e) {
                  // ignore it
                  m_logger.warn("Error when getting info of village(" + part + ")", e);
               }
            }
         }
      }

      m_logger.info(colonias.size() + " colonias found by address.");

      return colonias;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
