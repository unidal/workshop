package com.ebay.eunit.codegen;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.eunit.EunitJUnit4Runner;

@RunWith(EunitJUnit4Runner.class)
@EnableXslCodegen
public class CodegenTest {
   @Test
   @XslCodegen(template = "UseCssTagComponent.xsl", source = "useCssTagComponent.xml")
   public void test() {
   }
}
