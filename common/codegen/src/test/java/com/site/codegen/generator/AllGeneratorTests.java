package com.site.codegen.generator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.site.codegen.generator.cat.CatGeneratorTest;
import com.site.codegen.generator.jdbc.JdbcGeneratorTest;
import com.site.codegen.generator.plugin.PluginGeneratorTest;
import com.site.codegen.generator.test.TestGeneratorTest;
import com.site.codegen.generator.webres.WebresGeneratorTest;
import com.site.codegen.generator.xml.XmlGeneratorTest;

@RunWith(Suite.class)
@SuiteClasses({

CatGeneratorTest.class,

JdbcGeneratorTest.class,

PluginGeneratorTest.class,

TestGeneratorTest.class,

WebresGeneratorTest.class,

XmlGeneratorTest.class

})
public class AllGeneratorTests {

}
