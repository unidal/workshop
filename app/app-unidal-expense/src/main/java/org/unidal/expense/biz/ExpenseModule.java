package org.unidal.expense.biz;

import com.site.web.mvc.AbstractModule;
import com.site.web.mvc.annotation.ModuleMeta;
import com.site.web.mvc.annotation.ModulePagesMeta;

@ModuleMeta(name = "e", defaultInboundAction = "home", defaultTransition = "default", defaultErrorAction = "default")
@ModulePagesMeta({

org.unidal.expense.biz.home.Handler.class,

org.unidal.expense.biz.trip.Handler.class,

org.unidal.expense.biz.member.Handler.class,

org.unidal.expense.biz.member.signin.Handler.class,

})
public class ExpenseModule extends AbstractModule {

}
