package com.ebay.eunit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.ebay.eunit.annotation.RunGroups;
import com.ebay.eunit.benchmark.testfwk.BenchmarkTests;
import com.ebay.eunit.handler.GroupsHandlerTest;
import com.ebay.eunit.handler.InterceptHandlerTest;
import com.ebay.eunit.handler.RunIgnoreHandlerTest;
import com.ebay.eunit.invocation.MethodInvokerTest;
import com.ebay.eunit.testfwk.EunitRuntimeConfigTest;
import com.ebay.eunit.testfwk.HandlerTest;
import com.ebay.eunit.testfwk.InjectionTest;
import com.ebay.eunit.testfwk.JUnitTest;
import com.ebay.eunit.testfwk.junit.EunitExceptionValveTest;

@RunWith(EunitSuiteRunner.class)
@RunGroups(exclude = "benchmark")
@SuiteClasses({

/* .benchmark.testfwk */
BenchmarkTests.class,

/* .handler */
InterceptHandlerTest.class,

GroupsHandlerTest.class,

RunIgnoreHandlerTest.class,

/* .invocation */
MethodInvokerTest.class,

/* .testfwk */
EunitRuntimeConfigTest.class,

JUnitTest.class,

HandlerTest.class,

InjectionTest.class,

/* .testfwk.junit */
EunitExceptionValveTest.class

})
public class AllTests {

}
