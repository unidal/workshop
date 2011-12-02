package com.site.test.browser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.CommandLineUtils.StringStreamConsumer;
import org.codehaus.plexus.util.cli.shell.CmdShell;

import com.site.test.env.Platform;

public abstract class AbstractBrowser implements Browser, LogEnabled {
   private Logger m_logger;

   public void display(String html) {
      display(html, "utf-8");
   }

   public void display(String html, String charset) {
      URL url = saveToTemporaryFile(html, false, charset);

      display(url);
   }

   public abstract String[] getCommandLine(String url);

   public void display(URL url) {
      if (!isAvailable()) {
         throw new RuntimeException(getId() + " is unavailable.");
      }

      if (Platform.isWindows()) {
         try {
            String[] commandLine = getCommandLine(url.toExternalForm());
            CmdShell shell = new CmdShell();
            Commandline cmdLine = new Commandline(shell);
            StringStreamConsumer consumer = new StringStreamConsumer();

            cmdLine.addArguments(commandLine);

            CommandLineUtils.executeCommandLine(cmdLine, consumer, consumer);

            String output = consumer.getOutput();

            if (output != null && output.length() > 0) {
               m_logger.info(output);
            }
         } catch (Exception e) {
            throw new RuntimeException("Error when display page(" + url.toExternalForm() + ")", e);
         }
      } else {
         throw new UnsupportedOperationException("Platform( " + System.getProperty("os.name") + ") not supported");
      }
   }

   private URL saveToTemporaryFile(String html, boolean deleteOnExit, String charset) {
      try {
         File tempFile = File.createTempFile("test", ".html");

         if (deleteOnExit) {
            tempFile.deleteOnExit();
         }

         OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(tempFile), charset);

         out.write(html);
         out.close();

         return tempFile.getCanonicalFile().toURI().toURL();
      } catch (Exception e) {
         throw new RuntimeException("Error when writing to temporary file: " + e.getMessage(), e);
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   protected Logger getLogger() {
      return m_logger;
   }
}
