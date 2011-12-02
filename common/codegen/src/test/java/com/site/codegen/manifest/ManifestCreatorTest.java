package com.site.codegen.manifest;

import org.junit.Test;

import com.site.lookup.ComponentTestCase;

public class ManifestCreatorTest extends ComponentTestCase {
   @Test
   public void testCreate() throws Exception {
      ManifestCreator creator = lookup(ManifestCreator.class);
      
      assertNotNull(creator.create("123", "456"));
   }
}
