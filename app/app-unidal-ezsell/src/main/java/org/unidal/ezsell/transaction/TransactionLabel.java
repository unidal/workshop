package org.unidal.ezsell.transaction;

import static com.site.lookup.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.dal.Label;
import org.unidal.ezsell.dal.LabelDao;
import org.unidal.ezsell.dal.LabelEntity;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.dal.TransactionEntity;

import com.site.app.util.Lists;
import com.site.app.util.Lists.Factor;
import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.lookup.util.StringUtils;
import com.site.web.mvc.ErrorObject;

public class TransactionLabel implements LogEnabled {
   @Inject
   private TransactionDao m_trxDao;

   @Inject
   private LabelDao m_labelDao;

   private Logger m_logger;

   private void addDistinctLabels(Set<String> labels, List<Label> list) {
      for (Label label : list) {
         if (!labels.contains(label.getLabel())) {
            labels.add(label.getLabel());
         }
      }
   }

   public void apply(EbayContext ctx, String labels, Integer[] ids, Mode mode) {
      try {
         List<Transaction> transactions = m_trxDao.findAllByIds(ids, TransactionEntity.READSET_FULL);

         for (Transaction trx : transactions) {
            apply(ctx, trx, labels, mode);
         }
      } catch (DalException e) {
         e.printStackTrace();
         ctx.addError(new ErrorObject("dal.transaction.error", e));
      }
   }

   public void apply(EbayContext ctx, Transaction trx, String labels, Mode mode) {
      String id = String.valueOf(trx.getId());

      try {
         List<Label> insert = new ArrayList<Label>();
         List<Label> update = new ArrayList<Label>();
         List<Label> delete = new ArrayList<Label>();

         List<Label> newLabels = createLabels(labels, id);
         List<Label> oldLabels = m_labelDao.findAllByTypeAndId(Type.SELLER_TRANSACTIONS.getId(), id,
               LabelEntity.READSET_FULL);

         switch (mode) {
         case ADD:
            insert.addAll(newLabels);
            break;
         case REMOVE:
            delete.addAll(newLabels);
            break;
         case REPLACE:
            Lists.segregate(newLabels, oldLabels, insert, update, delete, new Factor<Label>() {
               public Object getId(Label label) {
                  return label.getLabel();
               }

               public Label merge(Label newItem, Label oldItem) {
                  return oldItem;
               }
            });
            break;
         }

         if (!insert.isEmpty()) {
            m_labelDao.insert(insert.toArray(new Label[0]));
         }

         if (!delete.isEmpty()) {
            setLabelsKey(delete);
            m_labelDao.deleteByPK(delete.toArray(new Label[0]));
         }

         trx.setKeyId(trx.getId());
         trx.setLabels(getTransactionLabels(oldLabels, newLabels, mode));
         m_trxDao.updateByPK(trx, TransactionEntity.UPDATESET_FULL);
      } catch (DalException e) {
         ctx.addError(new ErrorObject("dal.label.error", e));
      }
   }

   private List<Label> createLabels(String labels, String id) {
      String[] parts = labels.split(" ");
      List<Label> list = new ArrayList<Label>();
      Set<String> done = new HashSet<String>();

      for (String part : parts) {
         if (part.length() == 0) {
            continue;
         }

         Label label = new Label();

         label.setLabel(part);
         label.setType(Type.SELLER_TRANSACTIONS.getId());
         label.setId(id);
         label.setKeyLabel(part);
         label.setKeyType(Type.SELLER_TRANSACTIONS.getId());
         label.setKeyId(id);

         if (!done.contains(part)) {
            done.add(part);
            list.add(label);
         }
      }

      return list;
   }

   public Integer[] getTransactionIdsByLabels(String labels, boolean nullIfEmpty) throws DalException {
      if (isEmpty(labels)) {
         return null;
      }

      Set<Integer> ids = new HashSet<Integer>();

      if (labels != null) {
         String[] parts = labels.split(" ");
         List<Label> matched = m_labelDao.findAllByTypeAndLabels(Type.SELLER_TRANSACTIONS.getId(), parts,
               LabelEntity.READSET_ID);

         for (Label e : matched) {
            ids.add(Integer.parseInt(e.getId()));
         }
      }

      if (nullIfEmpty && ids.isEmpty()) {
         return null;
      } else {
         return ids.toArray(new Integer[0]);
      }
   }

   private String getTransactionLabels(List<Label> oldLabels, List<Label> newLabels, Mode mode) {
      Set<String> labels = new LinkedHashSet<String>();

      switch (mode) {
      case ADD:
         addDistinctLabels(labels, oldLabels);
         addDistinctLabels(labels, newLabels);
         break;
      case REMOVE:
         addDistinctLabels(labels, oldLabels);
         removeDistinctLabels(labels, newLabels);
         break;
      case REPLACE:
         addDistinctLabels(labels, newLabels);
         break;
      }

      return StringUtils.join(labels, " ");
   }

   public void reindexLabels(List<Transaction> transactions) throws DalException {
      for (Transaction transaction : transactions) {
         reindexLabels(transaction);
      }
   }

   private void reindexLabels(Transaction transaction) throws DalException {
      String id = String.valueOf(transaction.getId());

      m_logger.info("Re-index labels for transaction: " + id);

      if (transaction.getLabels() == null) {
         Label label = new Label();

         label.setId(id);
         m_labelDao.deleteById(label);
         return;
      }

      List<Label> newLabels = createLabels(transaction.getLabels(), id);
      List<Label> oldLabels = m_labelDao.findAllByTypeAndId(Type.SELLER_TRANSACTIONS.getId(), id,
            LabelEntity.READSET_FULL);
      List<Label> insert = new ArrayList<Label>();
      List<Label> update = new ArrayList<Label>();
      List<Label> delete = new ArrayList<Label>();

      Lists.segregate(newLabels, oldLabels, insert, update, delete, new Factor<Label>() {
         public Object getId(Label label) {
            return label.getLabel();
         }

         public Label merge(Label newItem, Label oldItem) {
            return oldItem;
         }
      });

      if (!insert.isEmpty()) {
         m_labelDao.insert(insert.toArray(new Label[0]));
      }

      if (!delete.isEmpty()) {
         setLabelsKey(delete);
         m_labelDao.deleteByPK(delete.toArray(new Label[0]));
      }
   }

   private void removeDistinctLabels(Set<String> labels, List<Label> list) {
      for (Label label : list) {
         if (labels.contains(label.getLabel())) {
            labels.remove(label.getLabel());
         }
      }
   }

   private void setLabelsKey(List<Label> labels) {
      for (Label label : labels) {
         label.setKeyLabel(label.getLabel());
         label.setKeyType(label.getType());
         label.setKeyId(label.getId());
      }
   }

   public static enum Mode {
      ADD, REMOVE, REPLACE;
   }

   public static enum Type {
      SELLER_TRANSACTIONS(1);

      private int m_id;

      private Type(int id) {
         m_id = id;
      }

      public int getId() {
         return m_id;
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
