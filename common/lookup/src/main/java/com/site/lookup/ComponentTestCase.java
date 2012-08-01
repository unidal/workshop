package com.site.lookup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.MutablePlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.DefaultContext;
import org.junit.After;
import org.junit.Before;

//@RunWith(BlockJUnit4ClassRunner.class)
public abstract class ComponentTestCase extends TestCase {
   private MutablePlexusContainer m_container;

   private Map<Object, Object> m_context;

   private String m_basedir;

   protected void customizeContext(Context context) throws Exception {
   }

   protected String getBasedir() {
      if (m_basedir != null) {
         return m_basedir;
      }

      m_basedir = System.getProperty("basedir");
      if (m_basedir == null) {
         m_basedir = new File("").getAbsolutePath();
      }

      return m_basedir;
   }

   protected PlexusContainer getContainer() {
      return m_container;
   }

   protected String getCustomConfigurationName() {
      return null;
   }

   protected String getDefaultConfigurationName() throws Exception {
      return getClass().getName().replace('.', '/') + ".xml";
   }

   // ----------------------------------------------------------------------
   // Helper methods for sub classes
   // ----------------------------------------------------------------------
   protected String getResource(String path) {
      return getClass().getResource(path).getFile();
   }

   protected File getResourceFile(String path) {
      return new File(getClass().getResource(path).getFile());
   }

   protected File getTestFile(String path) {
      return new File(getBasedir(), path);
   }

   protected File getTestFile(String basedir, String path) {
      File basedirFile = new File(basedir);

      if (!basedirFile.isAbsolute()) {
         basedirFile = getTestFile(basedir);
      }

      return new File(basedirFile, path);
   }

   protected String getTestPath(String path) {
      return getTestFile(path).getAbsolutePath();
   }

   protected String getTestPath(String basedir, String path) {
      return getTestFile(basedir, path).getAbsolutePath();
   }

   // ----------------------------------------------------------------------
   // Container access
   // ----------------------------------------------------------------------
   protected <T> T lookup(Class<T> role) throws Exception {
      return (T) getContainer().lookup(role);
   }

   protected <T> T lookup(Class<T> role, Object roleHint) throws Exception {
      return (T) getContainer().lookup(role, roleHint == null ? null : roleHint.toString());
   }

   protected <T> void release(T component) throws Exception {
      getContainer().release(component);
   }

   @Before
   @Override
   public void setUp() throws Exception {
      m_basedir = getBasedir();

      // ----------------------------------------------------------------------------
      // Context Setup
      // ----------------------------------------------------------------------------

      m_context = new HashMap<Object, Object>();

      m_context.put("basedir", getBasedir());

      customizeContext(new DefaultContext(m_context));

      boolean hasPlexusHome = m_context.containsKey("plexus.home");

      if (!hasPlexusHome) {
         File f = getTestFile("target/plexus-home");

         if (!f.isDirectory()) {
            f.mkdir();
         }

         m_context.put("plexus.home", f.getAbsolutePath());
      }

      // ----------------------------------------------------------------------------
      // Configuration
      // ----------------------------------------------------------------------------

      String config = getCustomConfigurationName();

      ContainerConfiguration containerConfiguration = new DefaultContainerConfiguration().setName("test").setContext(
            m_context);

      if (config != null) {
         containerConfiguration.setContainerConfiguration(config);
      } else {
         String resource = getDefaultConfigurationName();

         containerConfiguration.setContainerConfiguration(resource);
      }

      m_container = (MutablePlexusContainer) ContainerLoader.getDefaultContainer(containerConfiguration);
   }

   @After
   @Override
   public void tearDown() throws Exception {
      if (m_container != null) {
         m_container.dispose();

         m_container = null;
      }
   }
}