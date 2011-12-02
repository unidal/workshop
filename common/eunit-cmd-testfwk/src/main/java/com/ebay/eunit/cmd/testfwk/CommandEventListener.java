package com.ebay.eunit.cmd.testfwk;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import com.ebay.eunit.cmd.annotation.GET;
import com.ebay.eunit.cmd.annotation.POST;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.RootEntity;
import com.ebay.eunit.testfwk.ClassContext.ModelContext;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.event.Event;
import com.ebay.eunit.testfwk.spi.event.IEventListener;

public enum CommandEventListener implements IEventListener {
   INSTANCE;

   @SuppressWarnings("unchecked")
   @Override
   public void onEvent(IClassContext context, Event event) {
      AnnotatedElement source = event.getSource();
      ModelContext<RootEntity> ctx = (ModelContext<RootEntity>) (ModelContext<?>) context.forModel();

      switch (event.getType()) {
      case BEFORE_CLASS:
         RootEntity root = new RootEntity();

         ctx.push(root);
         ctx.setModel(root);
         break;
      case BEFORE_METHOD:
         Method method = (Method) source;

         if (method.isAnnotationPresent(GET.class) || method.isAnnotationPresent(POST.class)) {
            CommandEntity cmd = new CommandEntity(method.getName());
            RootEntity r = ctx.peek();

            cmd.setMethod(method);
            r.addCommand(cmd);
            ctx.push(cmd);
         } else {
            ctx.push(null);
         }

         break;
      case AFTER_METHOD:
         ctx.pop();
         break;
      case AFTER_CLASS:
         ctx.pop();
         break;
      }
   }
}
