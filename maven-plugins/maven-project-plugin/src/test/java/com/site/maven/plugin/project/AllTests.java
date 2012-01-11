package com.site.maven.plugin.project;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

CreateTemplateMojoTest.class,

BuildPlanMojoTest.class,

MigrateMojoTest.class

})
public class AllTests {

}
