package com.site.bes.engine.config;
import static com.site.kernel.dal.Cardinality.ONE_TO_MANY;
import static com.site.kernel.dal.ValueType.TIME;
import static com.site.kernel.dal.model.NodeType.ELEMENT;
import static com.site.kernel.dal.model.NodeType.MODEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class ConsumersDo extends XmlModel {
   public static final XmlModelField RECOVERY_CHECK_INTERVAL = new XmlModelField("recovery-check-interval", ELEMENT, TIME, "1m");

   public static final XmlModelField CONSUMER = new XmlModelField("consumer", MODEL, ONE_TO_MANY, ConsumerDo.class);

   private long m_recoveryCheckInterval;

   private List m_consumers = new ArrayList(3);

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public ConsumersDo() {
      super(null, "consumers");
   }

   protected void addConsumerDo(ConsumerDo consumer) {
      super.addChild(CONSUMER, consumer);
   }
   
   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
      for (int i = 0; i != m_consumers.size(); i++) {
         ConsumerDo consumer = (ConsumerDo) m_consumers.get(i);

         consumer.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      if (m_consumers.size() == 0) {
         throw new ValidationException("At least one consumer should be defined under " + this);
      }
   
      for (int i = 0; i != m_consumers.size(); i++) {
         ConsumerDo consumer = (ConsumerDo) m_consumers.get(i);

         consumer.validate(parents);
      }
   }

   protected List getConsumerDos() {
      return m_consumers;
   }
   
   public long getRecoveryCheckInterval() {
      return m_recoveryCheckInterval;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(RECOVERY_CHECK_INTERVAL, attrs);
   }

   public void setRecoveryCheckInterval(long recoveryCheckInterval) {
      m_recoveryCheckInterval = recoveryCheckInterval;
      setFieldUsed(RECOVERY_CHECK_INTERVAL, true);
   }
   
}
