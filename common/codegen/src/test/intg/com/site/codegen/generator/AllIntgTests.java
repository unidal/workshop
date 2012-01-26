package com.site.codegen.generator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.site.codegen.generator.cat.JsonTest;
import com.site.codegen.generator.cat.SchemaTest;
import com.site.codegen.generator.cat.XmlTest;

@RunWith(Suite.class)
@SuiteClasses({

XmlTest.class,

JsonTest.class,

SchemaTest.class,

})
public class AllIntgTests {

}
