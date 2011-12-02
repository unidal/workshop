package org.unidal.ezsell.biz;

import static com.site.lookup.util.StringUtils.isEmpty;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.dal.Operator;
import org.unidal.ezsell.dal.OperatorDao;
import org.unidal.ezsell.dal.OperatorEntity;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.SellerEbayTeam;
import org.unidal.ezsell.dal.SellerEbayTeamDao;
import org.unidal.ezsell.dal.SellerEntity;
import org.unidal.ezsell.view.JspViewer;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;

public class ProfilePage implements PageHandler<EbayContext> {
   @Inject
   private JspViewer m_jspViewer;

   @Inject
   private OperatorDao m_operatorDao;

   @Inject
   private SellerDao m_sellerDao;

   @Inject
   private SellerEbayTeamDao m_sellerEbayTeamDao;

   public void handleInbound(EbayContext ctx) {
      ProfilePayload payload = (ProfilePayload) ctx.getPayload();

      if (payload.isChangePassword()) {
         String password1 = payload.getPassword1();
         String password2 = payload.getPassword2();

         if (password1 != null && password1.length() > 0 && password1.equals(password2)) {
            try {
               int operatorId = ctx.getLoginUserId();
               Operator operator = m_operatorDao.findByPK(operatorId, OperatorEntity.READSET_FULL);

               operator.setKeyOperatorId(operatorId);
               operator.setPassword(password1);

               m_operatorDao.updateByPK(operator, OperatorEntity.UPDATESET_FULL);

               ctx.addError(new ErrorObject("user.password.updated", null));
            } catch (DalException e) {
               ctx.addError(new ErrorObject("dal.user.error", e));
            }
         } else {
            ctx.addError(new ErrorObject("user.password.mismatched", null));
         }
      } else if (payload.isUpdateProfile()) {
         if (!isEmpty(payload.getName()) && !isEmpty(payload.getAddress())) {
            Seller seller = ctx.getSeller();

            seller.setKeySellerId(seller.getSellerId());
            seller.setName(payload.getName());
            seller.setAddress(payload.getAddress());

            try {
               m_sellerDao.updateByPK(seller, SellerEntity.UPDATESET_FULL);
            } catch (DalException e) {
               ctx.addError(new ErrorObject("dal.seller.error", e));
            }

            // is owner & team at ebay
            if (seller.getOperatorId() == ctx.getLoginUserId() && seller.getTeamAtEbay() > 0) {
               SellerEbayTeam team = new SellerEbayTeam();

               team.setSellerId(seller.getSellerId());
               team.setTeamName(payload.getTeamName());
               team.setTeamLeader(payload.getTeamLeader());
               team.setTeamLeaderPhone(payload.getTeamLeaderPhone());
               team.setTeamLeaderCube(payload.getTeamLeaderCube());

               try {
                  m_sellerEbayTeamDao.insert(team);
               } catch (DalException e) {
                  ctx.addError(new ErrorObject("dal.seller_ebay_team.error", e));
               }
            }
         }
      }
   }

   public void handleOutbound(EbayContext ctx) throws ServletException, IOException {
      EbayModel model = new EbayModel(ctx);

      model.setPage(EbayPage.PROFILE);
      m_jspViewer.view(ctx, model);
   }
}
