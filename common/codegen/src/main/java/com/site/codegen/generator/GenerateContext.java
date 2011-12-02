package com.site.codegen.generator;

import java.io.IOException;
import java.net.URL;

import com.site.codegen.manifest.Manifest;

public interface GenerateContext {
   public URL getManifestXml();

   public URL getManifestXsl();

   public URL getNormalizeXsl();

   public URL getDecorateXsl();

   public URL getTemplateXsl(String relativeFile);
   
   public void openStorage() throws IOException;

   public void addFileToStorage(Manifest manifest, String content) throws IOException;

   public void closeStorage() throws IOException;

   public int getGeneratedFiles();

   public void log(LogLevel logLevel, String message);

   public static enum LogLevel {
      DEBUG, INFO, ERROR;
   }
}
