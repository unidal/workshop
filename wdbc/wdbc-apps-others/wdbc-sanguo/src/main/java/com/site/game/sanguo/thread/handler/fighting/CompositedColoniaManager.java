package com.site.game.sanguo.thread.handler.fighting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.site.game.sanguo.model.Colonia;
import com.site.game.sanguo.thread.ThreadContext;

public class CompositedColoniaManager implements ColoniaManager {
   private List<ColoniaManager> m_managers;

   public List<Colonia> getColonias(ThreadContext ctx) {
      List<Colonia> colonias = new ArrayList<Colonia>(32);
      Set<String> added = new HashSet<String>();

      for (ColoniaManager manager : m_managers) {
         List<Colonia> list = manager.getColonias(ctx);

         for (Colonia colonia : list) {
            String key = colonia.getX() + ":" + colonia.getY();

            if (!added.contains(key)) {
               colonias.add(colonia);
               added.add(key);
            }
         }
      }

      return colonias;
   }
}
