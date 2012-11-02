package com.site.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.site.web.jsp.function.FormatTest;
import com.site.web.lifecycle.ActionResolverTest;
import com.site.web.mvc.model.AnnotationMatrixTest;
import com.site.web.mvc.model.ModuleManagerTest;
import com.site.web.mvc.payload.PayloadProviderTest;

@RunWith(Suite.class)
@SuiteClasses({

MvcTest.class,

FormatTest.class,

ActionResolverTest.class,

AnnotationMatrixTest.class,

ModuleManagerTest.class,

PayloadProviderTest.class

})
public class AllTests {

}
