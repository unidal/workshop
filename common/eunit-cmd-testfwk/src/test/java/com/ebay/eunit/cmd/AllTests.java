package com.ebay.eunit.cmd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.ebay.eunit.EunitSuiteRunner;
import com.ebay.eunit.cmd.testfwk.CommandTest;
import com.ebay.eunit.cmd.testfwk.ResourceCommandTest;
import com.ebay.eunit.mock.http.HttpClientMocksTest;

@RunWith(EunitSuiteRunner.class)
@SuiteClasses({

/* .cmd.testfwk */
CommandTest.class,

ResourceCommandTest.class,

/* .mock.http */
HttpClientMocksTest.class

})
public class AllTests {

}
