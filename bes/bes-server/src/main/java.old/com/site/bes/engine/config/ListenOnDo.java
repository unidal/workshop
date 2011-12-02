package com.site.bes.engine.config;
import static com.site.kernel.dal.Cardinality.ZERO_TO_MANY;
import static com.site.kernel.dal.model.NodeType.MODEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class ListenOnDo extends XmlModel {
   public static final XmlModelField EVENT = new XmlModelField("event", MODEL, ZERO_TO_MANY, EventDo.class);

   private List m_events = new ArrayList(3);

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public ListenOnDo() {
      super(null, "listen-on");
   }

   protected void addEventDo(EventDo event) {
      super.addChild(EVENT, event);
   }
   
   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
      for (int i = 0; i != m_events.size(); i++) {
         EventDo event = (EventDo) m_events.get(i);

         event.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      for (int i = 0; i != m_events.size(); i++) {
         EventDo event = (EventDo) m_events.get(i);

         event.validate(parents);
      }
   }

   protected List getEventDos() {
      return m_events;
   }
   
   public void loadAttributes(Attributes attrs) {
   }

}
