package com.site.kernel.logging.cal;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Stack;

import com.site.kernel.logging.Log;

public final class CalFactory {
   public static final String VERSION = "CalFactory 1.1, made by Frankie Wu, 2005-08-17";

   private static final Log LOG = Log.getLog(CalFactory.class);

   public static CalTransaction createCalTransaction(String type, String name) {
      if (isCalEnabled()) {
         return new CalTransactionImpl(type, name);
      } else {
         return new NullCalTransaction(type, name);
      }
   }

   public static CalEvent createCalEvent(String type, String name) {
      if (isCalEnabled()) {
         return new CalEventImpl(type, name);
      } else {
         return new NullCalEvent(type, name);
      }
   }

   public static CalEvent createCalEvent(String type, String name, String status, String data) {
      if (isCalEnabled()) {
         return new CalEventImpl(type, name, status, data);
      } else {
         return new NullCalEvent(type, name, status, data);
      }
   }

   public static CalHeartbeat createCalHeartbeat(String type, String name) {
      if (isCalEnabled()) {
         return new CalHeartbeatImpl(type, name);
      } else {
         return new NullCalHeartbeat(type, name);
      }
   }

   public static CalHeartbeat createCalHeartbeat(String type, String name, String data) {
      if (isCalEnabled()) {
         return new CalHeartbeatImpl(type, name, data);
      } else {
         return new NullCalHeartbeat(type, name, data);
      }
   }

   public static CalTransaction getCalTransaction(String type, String name) {
      if (isCalEnabled()) {
         return new CalTransactionImpl(type, name);
      } else {
         return new NullCalTransaction(type, name);
      }
   }

   public static CalTransaction getCalTransaction() {
      if (isCalEnabled()) {
         return CalTransactionImpl.getCurrent();
      } else {
         return NullCalTransaction.getCurrent();
      }
   }

   public static CalTransaction getCalTransaction(String type) {
      if (isCalEnabled()) {
         return CalTransactionImpl.getCurrent(type);
      } else {
         return NullCalTransaction.getCurrent(type);
      }
   }

   private static boolean isCalEnabled() {
      return false;
      // TODO
      // return LOG.getCalAppender() != null;
   }

   private static abstract class CalBase {
      public String m_name;

      public String m_type;

      public String m_status;

      public boolean m_completed;

      public StringBuffer m_data;

      public static final char TAG_ATOMIC_TRANSACTION = 'A';

      public static final char TAG_TRANSACTION_START = 't';

      public static final char TAG_TRANSACTION_END = 'T';

      public static final char TAG_EVENT = 'E';

      public static final char TAG_HEARTBEAT = 'H';

      private static final int[] supportedTags = new int[] { TAG_ATOMIC_TRANSACTION, TAG_TRANSACTION_START, TAG_TRANSACTION_END, TAG_EVENT, TAG_HEARTBEAT };

      private static final MessageFormat[] defaultFormats = new MessageFormat[] { new MessageFormat("A\t{0,date,HH:mm:ss.SSS}\t{1}\t{2}\t{3}\t{5,number}\t{4}"),
            new MessageFormat("t\t{0,date,HH:mm:ss.SSS}\t{1}\t{2}"), new MessageFormat("T\t{0,date,HH:mm:ss.SSS}\t{1}\t{2}\t{3}\t{5,number}\t{4}"),
            new MessageFormat("E\t{0,date,HH:mm:ss.SSS}\t{1}\t{2}\t{3}\t{4}"), new MessageFormat("H\t{0,date,HH:mm:ss.SSS}\t{1}\t{2}\t{3}\t{4}") };

      protected CalBase(String type, String name) {
         m_type = type;
         m_name = name;
         m_data = new StringBuffer(64);

         if (type == null || type.length() == 0)
            throw new IllegalArgumentException("TYPE can not be empty");
         else if (name == null || name.length() == 0)
            throw new IllegalArgumentException("NAME can not be empty");

         setStatus((String) null);
      }

      public final void addData(String name, Object[] values) {
         StringBuffer sb = m_data;
         int len = (values == null ? 0 : values.length);

         synchronized (sb) {
            if (sb.length() > 0)
               sb.append('&');

            sb.append(name).append("={");

            for (int i = 0; i < len; i++) {
               if (i > 0)
                  sb.append(',');

               if (values[i] == null)
                  sb.append("null");
               else
                  sb.append(values[i].toString());
            }

            sb.append('}');
         }
      }

      public final void addData(String name, String value) {
         if (name != null && name.length() > 0) {
            if (m_data.length() > 0)
               m_data.append('&');

            m_data.append(name).append('=').append(value == null ? "" : value);
         } else
            throw new IllegalArgumentException("NAME can not be empty");
      }

      public final void addData(String nameValuePairs) {
         int len = (nameValuePairs == null ? 0 : nameValuePairs.length());

         if (len > 0) {
            if (nameValuePairs.charAt(len - 1) == '&') // remove last
               // '&'
               // if
               // has
               len--;

            if (len > 0) {
               if (m_data.length() > 0)
                  m_data.append('&');

               m_data.append(nameValuePairs.substring(0, len));
            }
         }
      }

      protected final void addData(StringBuffer nameValuePairs) {
         m_data.append(nameValuePairs);
      }

      public abstract void complete();

      public final void complete(String status) {
         setStatus(status);

         complete();
      }

      private static MessageFormat findMessageFormat(char tag) {
         final int len = supportedTags.length;

         for (int i = 0; i < len; i++) {
            if (supportedTags[i] == tag)
               return defaultFormats[i];
         }

         throw new IllegalArgumentException("Tag(" + tag + ") is not defined");
      }

      public String getData() {
         return m_data.toString();
      }

      public String getName() {
         return m_name;
      }

      public String getStatus() {
         return m_status;
      }

      public String getType() {
         return m_type;
      }

      protected void logMessage(char tag, Object[] columns) {
//         MessageFormat format = findMessageFormat(tag);
//         String line = format.format(columns);

         // TODO
         // Appender appender = LOG.getCalAppender();
         //
         // if (appender != null) {
         // appender.doAppend(new LoggingEvent(Log.FQCN, LOG.getLogger(),
         // Level.OFF, line, null));
         // }
      }

      public abstract boolean isCompleted();

      public final void setStatus(String status) {
         if (status != null)
            m_status = status;
         else
            m_status = "0";
      }

      public final void setStatus(Throwable t) {
         if (t != null) {
            String name = t.getClass().getName();
            int pos = name.lastIndexOf('.');

            if (pos < 0)
               m_status = name;
            else
               m_status = name.substring(pos + 1);
         }
      }
   }

   private static final class CalEventImpl extends CalBase implements CalEvent {
      public String toString() {
         return "CalEventImpl[" + super.toString() + "]";
      }

      public static final String[] SUPPORTED_TYPES = { "Info", "Warn", "Error", "CAL" };

      public CalEventImpl(String type, String name) {
         this(type, name, null, null);
      }

      public CalEventImpl(String type, String name, String status, String data) {
         super(type, name);

         checkValidation(type);
         setStatus(status);
         addData(data);
      }

      private void checkValidation(String type) {
         for (int i = 0; i < SUPPORTED_TYPES.length; i++) {
            if (SUPPORTED_TYPES[i].equals(type))
               return;
         }

         throw new IllegalArgumentException("only TYPES(Info,Warn,Error,CAL) are supported by CalEvent ");
      }

      public void complete() {
         if (!m_completed) {
            final Object[] columns = { new Date(), m_type, m_name, m_status, m_data.toString() };

            logMessage(TAG_EVENT, columns);
            m_completed = true;
         } else
            CalEventImpl.sendImmediate("CAL", "BadInstrument", "EventAlreadyCompleted", "type=" + m_type + "&name=" + m_name);
      }

      public boolean isCompleted() {
         return m_completed;
      }

      public static void sendImmediate(String type, String name, String status, String data) {
         CalEventImpl ce = new CalEventImpl(type, name);

         ce.setStatus(status);
         ce.addData(data);
         ce.complete();
      }
   }

   private static final class CalHeartbeatImpl extends CalBase implements CalHeartbeat {
      public String toString() {
         return "CalHeartbeatImpl[" + super.toString() + "]";
      }

      public CalHeartbeatImpl(String type, String name) {
         this(type, name, null);
      }

      public CalHeartbeatImpl(String type, String name, String data) {
         super(type, name);

         addData(data);
      }

      public void complete() {
         if (!m_completed) {
            final Object[] columns = { new Date(), m_type, m_name, m_status, m_data.toString() };

            logMessage(TAG_HEARTBEAT, columns);
            m_completed = true;
         } else
            CalEventImpl.sendImmediate("CAL", "BadInstrument", "HeartbeatAlreadyCompleted", "type=" + m_type + "&name=" + m_name);
      }

      public boolean isCompleted() {
         return m_completed;
      }

      public void reset() {
         setStatus((String) null);
         m_data.setLength(0);
         m_completed = false;
      }

      public static void sendImmediate(String type, String name, String status, String data) {
         CalHeartbeatImpl ce = new CalHeartbeatImpl(type, name);

         ce.setStatus(status);
         ce.addData(data);
         ce.complete();
      }
   }

   private static final class CalTransactionImpl extends CalBase implements CalTransaction {
      public long m_startTime;

      public boolean m_startSent;

      private static ThreadLocal<Stack<CalTransaction>> threadLocal = new ThreadLocal<Stack<CalTransaction>>() {
         protected synchronized Stack<CalTransaction> initialValue() {
            return new Stack<CalTransaction>();
         }
      };

      public CalTransactionImpl(String type, String name) {
         super(type, name);

         pushTransaction();
         m_startTime = System.currentTimeMillis();
      }

      public void complete() {
         if (!m_completed) {
            // pop current thread out
            final Stack tls = (Stack) threadLocal.get();

            if (tls.isEmpty())
               CalEventImpl.sendImmediate("CAL", "BadInstrument", "MismatchTransactionEnd", "type=" + m_type + "&name=" + m_name);
            else {
               CalTransaction ct = (CalTransaction) (tls.peek());

               while (ct != this) {
                  CalEventImpl.sendImmediate("CAL", "BadInstrument", "NoTransactionEnd", "type=" + ct.getType() + "&name=" + ct.getName() + "&depth=" + tls.size());
                  ct.complete(); // complete the Transaction first

                  ct = (CalTransaction) tls.peek();
               }

               if (ct == this) // Pop out the current Transaction
                  tls.pop();
            }

            final long endTime = System.currentTimeMillis();
            final Object[] columns = { new Date(m_startSent ? endTime : m_startTime), m_type, m_name, m_status, m_data.toString(), new Long(endTime - m_startTime) };

            if (m_startSent)
               logMessage(TAG_TRANSACTION_END, columns);
            else
               logMessage(TAG_ATOMIC_TRANSACTION, columns);

            m_startSent = true;
            m_completed = true;
         } else
            CalEventImpl.sendImmediate("CAL", "BadInstrument", "TransactionAlreadyCompleted", "type=" + m_type + "&name=" + m_name);
      }

      /**
       * get the current transaction for type
       * 
       * @param type
       *           transaction type
       * @return the current transaction only if the type matches, return null
       *         if there is no current transaction or the type doesn't match
       *         the current transaction's type
       */
      public static CalTransaction getCurrent(String type) {
         final Stack tls = (Stack) threadLocal.get();
         final int len = tls.size();

         for (int i = len - 1; i >= 0; i--) {
            final CalTransaction ct = (CalTransaction) tls.get(i);

            if (ct.getType().equals(type))
               return ct;
         }

         return null;
      }

      /**
       * get the current transaction
       * 
       * @return the current transaction only if the type matches, return null
       *         if there is no current transaction
       */
      public static CalTransaction getCurrent() {
         final Stack tls = (Stack) threadLocal.get();

         if (!tls.isEmpty())
            return (CalTransaction) tls.peek();
         else
            return null;
      }

      /**
       * get the parent transaction
       * 
       * @return parent transaction if there is one, null otherwise
       */
      public CalTransaction getParent() {
         if (!m_completed) {
            final Stack tls = (Stack) threadLocal.get();
            final int len = tls.size();

            if (len > 1)
               return (CalTransaction) tls.get(len - 2);
         }

         return null;
      }

      /**
       * check if the transaction is completed
       * 
       * @return true if completed, false otherwise
       */
      public boolean isCompleted() {
         return m_completed;
      }

      /**
       * push current transaction into thread local stack
       */
      private void pushTransaction() {
         final Stack<CalTransaction> tls = (Stack<CalTransaction>) threadLocal.get();

         if (!tls.isEmpty()) {
            final CalTransactionImpl parent = (CalTransactionImpl) tls.peek();

            parent.writeBeginning();
         }

         tls.push(this);
      }

      private void writeBeginning() {
         if (!m_startSent) {
            final Object[] columns = { new Date(m_startTime), m_type, m_name };

            logMessage(TAG_TRANSACTION_START, columns);
            m_startSent = true;
         }
      }

      public String toString() {
         return "CalTransactionImpl[" + super.toString() + "]";
      }
   }

   private static abstract class NullCalBase {
      public String m_name;

      public String m_type;

      public String m_status;

      public boolean m_completed;

      public StringBuffer m_data;

      protected NullCalBase(String type, String name) {
         m_type = type;
         m_name = name;
         m_data = new StringBuffer();
         m_status = "0";
      }

      public void addData(String name, Object[] values) {
      }

      public void addData(String name, String value) {
         // Just do nothing here
      }

      public void addData(String nameValuePairs) {
         // Just do nothing here
      }

      public void complete() {
         m_completed = true;
      }

      public final void complete(String status) {
         m_status = status;
         m_completed = true;
      }

      public String getData() {
         return m_data.toString();
      }

      public String getName() {
         return m_name;
      }

      public String getStatus() {
         return m_status;
      }

      public String getType() {
         return m_type;
      }

      public boolean isCompleted() {
         return m_completed;
      }

      public void setStatus(String status) {
         // Just do nothing here
      }

      public void setStatus(Throwable t) {
         // Just do nothing here
      }
   }

   private static final class NullCalEvent extends NullCalBase implements CalEvent {

      protected NullCalEvent(String type, String name) {
         super(type, name);
      }

      protected NullCalEvent(String type, String name, String status, String data) {
         super(type, name);

         setStatus(status);
         addData(data);
      }

      public String toString() {
         return "NullCalEvent[" + super.toString() + "]";
      }
   }

   private static final class NullCalHeartbeat extends NullCalBase implements CalHeartbeat {

      protected NullCalHeartbeat(String type, String name) {
         super(type, name);
      }

      protected NullCalHeartbeat(String type, String name, String data) {
         super(type, name);

         addData(data);
      }

      public String toString() {
         return "NullCalHeartbeat[" + super.toString() + "]";
      }
   }

   private static final class NullCalTransaction extends NullCalBase implements CalTransaction {
      public String toString() {
         return "NullCalTransaction[" + super.toString() + "]";
      }

      private static ThreadLocal<Stack<CalTransaction>> threadLocal = new ThreadLocal<Stack<CalTransaction>>() {
         protected synchronized Stack<CalTransaction> initialValue() {
            return new Stack<CalTransaction>();
         }
      };

      public NullCalTransaction(String type, String name) {
         super(type, name);

         pushTransaction();
      }

      public void complete() {
         if (!isCompleted()) {
            // pop current thread out
            final Stack tls = (Stack) threadLocal.get();

            if (tls.isEmpty())
               CalEventImpl.sendImmediate("CAL", "BadInstrument", "MismatchTransactionEnd", "type=" + m_type + "&name=" + m_name);
            else {
               CalTransaction ct = (CalTransaction) (tls.peek());

               while (ct != this) {
                  CalEventImpl.sendImmediate("CAL", "BadInstrument", "NoTransactionEnd", "type=" + ct.getType() + "&name=" + ct.getName() + "&depth=" + tls.size());
                  ct.complete(); // complete the Transaction first

                  ct = (CalTransaction) tls.peek();
               }

               if (ct == this) // Pop out the current Transaction
                  tls.pop();
            }

            // Just do nothing here
            super.complete();
         } else
            CalEventImpl.sendImmediate("CAL", "BadInstrument", "TransactionAlreadyCompleted", "type=" + m_type + "&name=" + m_name);
      }

      /**
       * get the current transaction for type
       * 
       * @param type
       *           transaction type
       * @return the current transaction only if the type matches, return null
       *         if there is no current transaction or the type doesn't match
       *         the current transaction's type
       */
      public static CalTransaction getCurrent(String type) {
         final Stack tls = (Stack) threadLocal.get();
         final int len = tls.size();

         for (int i = len - 1; i >= 0; i--) {
            final CalTransaction ct = (CalTransaction) tls.get(i);

            if (ct.getType().equals(type))
               return ct;
         }

         return null;
      }

      /**
       * get the current transaction
       * 
       * @return the current transaction only if the type matches, return null
       *         if there is no current transaction
       */
      public static CalTransaction getCurrent() {
         final Stack tls = (Stack) threadLocal.get();

         if (!tls.isEmpty())
            return (CalTransaction) tls.peek();
         else
            return null;
      }

      /**
       * get the parent transaction
       * 
       * @return parent transaction if there is one, null otherwise
       */
      public CalTransaction getParent() {
         if (!isCompleted()) {
            final Stack tls = (Stack) threadLocal.get();
            final int len = tls.size();

            if (len > 1)
               return (CalTransaction) tls.get(len - 2);
         }

         return null;
      }

      /**
       * push current transaction into thread local stack
       */
      private void pushTransaction() {
         final Stack<CalTransaction> tls = (Stack<CalTransaction>) threadLocal.get();

         if (!tls.isEmpty() && (tls.peek() instanceof CalTransactionImpl)) {
            final CalTransactionImpl parent = (CalTransactionImpl) tls.peek();

            parent.writeBeginning();
         }

         tls.push(this);
      }
   }
}
