package org.unidal.ezsell.biz;

import static com.site.lookup.util.StringUtils.isEmpty;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.common.LastUrlManager;
import org.unidal.ezsell.dal.ItemPreference;
import org.unidal.ezsell.dal.ItemPreferenceDao;
import org.unidal.ezsell.dal.ItemPreferenceEntity;
import org.unidal.ezsell.dal.Transaction;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.dal.TransactionEntity;
import org.unidal.ezsell.transaction.CustomsDeclaration;
import org.unidal.ezsell.view.JspViewer;
import org.unidal.ezsell.view.ReportAction;
import org.unidal.ezsell.view.ReportViewer;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.lookup.util.StringUtils;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;

public class CustomsDeclarationPage implements PageHandler<EbayContext> {
   @Inject
   private ReportViewer m_reporter;

   @Inject
   private JspViewer m_jspViewer;

   @Inject
   private LastUrlManager m_lastUrlManager;

   @Inject
   private TransactionDao m_trxDao;

   @Inject
   private ItemPreferenceDao m_itemPreferenceDao;

   private ItemPreference getItemByTransactionId(int trxId) {
      try {
         Transaction trx = m_trxDao.findByPK(trxId, TransactionEntity.READSET_FULL);
         ItemPreference item = m_itemPreferenceDao.findByPK(trx.getItemId(), ItemPreferenceEntity.READSET_FULL);

         return item;
      } catch (DalException e) {
         // ignore it
      }

      return null;
   }

   public void handleInbound(EbayContext ctx) {
      CustomsDeclarationPayload payload = (CustomsDeclarationPayload) ctx.getPayload();
      int id = payload.getId();

      if (payload.getLastUrl() == null) {
         payload.setLastUrl(m_lastUrlManager.getLastUrl(ctx));
      }

      if (payload.isSubmit()) {
         if (!ctx.hasErrors()) {
            if (isEmpty(payload.getItemTitle())) {
               ctx.addError(new ErrorObject("title.required", null));
            }

            if (payload.getItemQuantity() <= 0) {
               ctx.addError(new ErrorObject("quantity.lessThanZero", null));
            }

            if (isEmpty(payload.getToName())) {
               ctx.addError(new ErrorObject("name.required", null));
            }

            if (isEmpty(payload.getToAddress())) {
               ctx.addError(new ErrorObject("address.required", null));
            }
         }

         if (!ctx.hasErrors() && id > 0) {
            try {
               ItemPreference item = getItemByTransactionId(id);

               if (item != null) {
                  item.setKeyItemId(item.getItemId());
                  item.setCdTitle(payload.getItemTitle());
                  item.setCdWeight(payload.getWeight());
                  item.setCdValue(payload.getItemValue());
                  item.setCdOrigin(payload.getOrigin());

                  m_itemPreferenceDao.updateByPK(item, ItemPreferenceEntity.UPDATESET_FULL);
               }
            } catch (DalException e) {
               ctx.addError(new ErrorObject("dal.item.failure", e));
            }
         }
      }
   }

   public void handleOutbound(EbayContext ctx) throws ServletException, IOException {
      EbayModel model = new EbayModel(ctx);
      CustomsDeclarationPayload payload = (CustomsDeclarationPayload) ctx.getPayload();
      CustomsDeclaration cd = new CustomsDeclaration();

      model.setCustomsDeclaration(cd);

      cd.setToName(payload.getToName());
      cd.setToAddress(payload.getToAddress());
      cd.setTrackingNo(payload.getTrackingNo());
      cd.setItemTitle(payload.getItemTitle());
      cd.setItemQuantity(payload.getItemQuantity());
      cd.setWeight(payload.getWeight());
      cd.setItemValue(payload.getItemValue());
      cd.setOrigin(payload.getOrigin());

      if (payload.isSubmit() && !ctx.hasErrors()) {
         model.setReportAction(ReportAction.CUSTOMS_DECLARATION_REPORT);
         model.setReportId(String.valueOf(payload.getId()));
         m_reporter.view(ctx, model);
      } else {
         if (cd.getItemQuantity() <= 0) {
            cd.setItemQuantity(1);
         }

         if (StringUtils.isEmpty(payload.getWeight())) {
            ItemPreference item = getItemByTransactionId(payload.getId());

            if (item != null) {
               cd.setItemTitle(item.getCdTitle());
               cd.setItemValue(item.getCdValue());
               cd.setWeight(item.getCdWeight());
               cd.setOrigin(item.getCdOrigin());
            }
         }

         model.setPage(EbayPage.CUSTOMS_DECLARATION);
         m_jspViewer.view(ctx, model);
      }
   }
}
