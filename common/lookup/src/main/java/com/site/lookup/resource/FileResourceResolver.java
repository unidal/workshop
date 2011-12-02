package com.site.lookup.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.site.lookup.annotation.Inject;

public class FileResourceResolver implements ResourceResolver {
   @Inject
   private File m_baseDir;

   public FileResourceResolver() {
   }

   public FileResourceResolver(File baseDir) {
      m_baseDir = baseDir;
   }

   public URL resolve(String relativePath) throws IOException {
      return new File(m_baseDir, relativePath).toURI().toURL();
   }
}
