package com.site.codegen.generator.model.cat;

import java.io.File;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.site.codegen.generator.model.ModelGenerateContext;
import com.site.codegen.generator.model.ModelGenerateTestSupport;

@RunWith(JUnit4.class)
public class CatGeneratorTest extends ModelGenerateTestSupport {
   @Override
   protected File getProjectBaseDir() {
      return new File("target/generated-model-cat");
   }

   @Override
   protected boolean isDebug() {
      return false;
   }

   @Override
   protected boolean isVerbose() {
      return true;
   }

   @Test
   public void testGenerateConfig() throws Exception {
      generate("config-manifest.xml");
   }

   @Test
   public void testGenerateFailureReport() throws Exception {
      generate(new ModelGenerateContext(getProjectBaseDir(), getResourceFile("failure-report-manifest.xml"),
            isVerbose(), isDebug()) {
         @Override
         protected void configure(Map<String, String> properties) {
            // generate XSD file with java code
            // for integration purpose in AllIntgTests
            properties.put("src-main-resources", "src/main/java");
         }
      });
   }

   @Test
   public void testGenerateIpReport() throws Exception {
      generate("ip-manifest.xml");
   }

   @Test
   public void testGenerateTransactionReport() throws Exception {
      generate("transaction-report-manifest.xml");
   }
}
