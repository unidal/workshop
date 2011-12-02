package com.site.game.sanguo.thread.handler;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Model;
import com.site.game.sanguo.model.Question;
import com.site.game.sanguo.model.Status;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.game.sanguo.thread.handler.question.Answerer;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class QuestionHandler implements ThreadHandler, LogEnabled {
   private List<Answerer> m_answerers;

   private Request m_request;

   private Logger m_logger;

   private MessageFormat m_format = new MessageFormat("{0}您的回答正确 {1}<{2}");

   private boolean answerQuestion(ThreadContext ctx, Question question, int result) throws ThreadException {
      Session session = ctx.getSession();

      ctx.getSession().setProperty("village-id", ctx.getMainVillageId());
      session.setProperty("question-result", String.valueOf(result));
      ThreadHelper.setRandom(ctx);

      try {
         String response = ThreadHelper.executeRequest(session, m_request, true);
         Object[] parts = m_format.parse(response);
         String message = String.valueOf(parts[1]);

         m_logger.info("Question answered: choice " + result + " for " + question);
         m_logger.info(message);
         return true;
      } catch (ParseException e) {
         // ignore
      } catch (ThreadException e) {
         // ignore it
      } catch (Exception e) {
         throw new ThreadException("Error when answering question: " + question, e);
      }

      return false;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      Model model = ctx.getModel();
      Question question = model.getQuestion();

      if (question != null && !model.getUnansweredQuestions().containsKey(question.getId())) {
         String subject = question.getContent().trim();
         String[] choices = { question.getItem1(), question.getItem2(), question.getItem3(), question.getItem4() };
         boolean hasAnswerer = false;
         int result = 0;

         for (Answerer answerer : m_answerers) {
            if (answerer.canAnswer(subject)) {
               hasAnswerer = true;
               result = answerer.answer(subject, choices);
               break;
            }
         }

         boolean isAnswered = false;

         if (!hasAnswerer) {
            m_logger.warn("No Answerer configured for " + question);
         } else if (result == 0) {
            m_logger.warn(question + " is not answered.");
         } else {
            isAnswered = answerQuestion(ctx, question, result);
         }

         updateModel(ctx, question, isAnswered);
      }
   }

   private void updateModel(ThreadContext ctx, Question question, boolean isAnswered) {
      Model model = ctx.getModel();
      Farm mainFarm = model.getFarms().get(0);
      Status status = new Status();
      Calendar cal = Calendar.getInstance();

      if (!isAnswered) {
         model.getUnansweredQuestions().put(question.getId(), question);
      } else {
         status.setDirty(true);
      }

      // set to next day
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);

      status.setName("question");
      status.setNextSchedule(cal.getTime());
      mainFarm.getHandlerStatuses().put(status.getName(), status);
   }
}
