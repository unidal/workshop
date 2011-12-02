package com.site.maven.plugin.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.site.media.mp3.MP3File;
import com.site.media.mp3.TagContent;

/**
 * Package all source files in the project for backup purpose.
 * 
 * @goal package
 */
public class Mp3Mojo extends AbstractMojo {
   /**
    * Root directories for MP3 files
    * 
    * @parameter expression="${rootDirs}" default-value="d:/medias/mp3"
    * @required
    */
   private String rootDirs;

   /**
    * Files to be excluded
    * 
    * @parameter
    */
   private String[] excludes;

   private void addAttribute(Element element, String name, Object value) {
      String trimmedText = null;

      if (value instanceof TagContent) {
         TagContent content = (TagContent) value;

         trimmedText = content.getTextContent().trim();

         try {
            if (isWithoutUnicodeCharacter(trimmedText)) {
               trimmedText = new String(trimmedText.getBytes("iso-8859-1"), "gbk");
            }
         } catch (UnsupportedEncodingException e) {
            // ignore it
         }
      } else if (value != null) {
         trimmedText = value.toString().trim();
      }

      if (trimmedText != null && trimmedText.length() > 0) {
         if (name.equals("artist") && !isWithoutUnicodeCharacter(trimmedText)) {
            trimmedText = trimNonUnicodeCharacter(trimmedText);
         }

         element.setAttribute(name, trimmedText);
      }
   }

   private String trimNonUnicodeCharacter(String text) {
      int len = text.length();
      int i, j;

      for (i = 0; i < len; i++) {
         char ch = text.charAt(i);

         if (ch > 255) {
            break;
         }
      }

      for (j = len - 1; j > i; j--) {
         char ch = text.charAt(j);

         if (ch > 255) {
            break;
         }
      }

      return text.substring(i, j + 1);
   }

   private void addAttribute(Element element, String name, String content) {
      element.setAttribute(name, content.trim());
   }

   private void addAttributes(Element element, MP3File mp3, String... names) {
      for (String name : names) {
         try {
            Object value = getProperty(mp3, name);

            addAttribute(element, name, value);
         } catch (Exception e) {
            // ignore it
         }
      }
   }

   private void buildDom(Element root, String[] includes, String[] excludes) throws Exception {
      String[] dirs = rootDirs.split(",");

      for (String dir : dirs) {
         DirectoryScanner scanner = new DirectoryScanner();
         File baseDir = new File(dir);

         scanner.setBasedir(dir);
         scanner.setCaseSensitive(false);
         scanner.setExcludes(excludes);
         scanner.setIncludes(includes);
         scanner.scan();

         String[] files = scanner.getIncludedFiles();
         for (String file : files) {
            String path = new File(baseDir, file).getAbsolutePath();
            Element element = new Element("mp3");

            root.addContent(element);
            element.setAttribute("path", path);

            try {
               MP3File mp3 = new MP3File(path);

               addAttributes(element, mp3, "title", "album", "artist", "band", "length", "genre", "year");

               if (mp3.getBitrate() > 0) {
                  addAttribute(element, "bitrate", mp3.getBitrate() + "kbps");
               }

               addAttribute(element, "lengthInTime", toTime(mp3.getLength()));
            } catch (Exception e) {
               // ignore it
               e.printStackTrace();
            }
         }
      }
   }

   public void execute() throws MojoExecutionException, MojoFailureException {
      try {
         String[] includes = { "**/*.mp3" };
         XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setIndent("   "));
         OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("target/mp3.xml"), "utf-8");
         Element root = new Element("root");
         buildDom(root, includes, excludes);

         outputter.output(new Document().setRootElement(root), writer);
         writer.close();

         getLog().info("File mp3.xml generated.");
      } catch (Exception e) {
         throw new MojoExecutionException("Error when executing mojo: " + e, e);
      }
   }

   private Object getProperty(Object instance, String name) throws Exception {
      String getter = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
      Method method = instance.getClass().getMethod(getter);

      return method.invoke(instance);
   }

   private boolean isWithoutUnicodeCharacter(String text) {
      int len = text.length();
      boolean hasUnicode = false;

      for (int i = 0; i < len; i++) {
         char ch = text.charAt(i);

         if (ch > 256) { // unicode character
            hasUnicode = true;
         }
      }

      return !hasUnicode;
   }

   private String toTime(long time) {
      StringBuilder sb = new StringBuilder();
      int hour = (int) (time / 3600);
      int minute = (int) (time / 60);
      int second = (int) (time % 60);

      if (hour <= 9) {
         sb.append('0');
      }

      sb.append(hour).append(':');

      if (minute <= 9) {
         sb.append('0');
      }

      sb.append(minute).append(':');

      if (second <= 9) {
         sb.append('0');
      }

      sb.append(second);

      return sb.toString();
   }
}
