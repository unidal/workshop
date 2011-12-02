package com.site.kernel.logging;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RendererSupport;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.site.kernel.SystemPathFinder;
import com.site.kernel.common.BaseXmlHandler;
import com.site.kernel.logging.Log4jConfigurator.Config.Appender;
import com.site.kernel.util.ClazzLoader;

class Log4jConfigurator implements Configurator {
	public static final String VERSION = "Log4jConfigurator 1.0, made by Frankie Wu, 2004-05-10";

	private static final String LOG4J = "log4j.xml";

	private static Config s_config;

	public void doConfigure(URL url, LoggerRepository repository) {
		try {
			s_config = new Config(url.getFile());

			s_config.setLoggerRepository(repository);
			s_config.loadXml();
		} catch (SAXParseException saxpe) {
			System.err.println("XML ERROR:");
			System.err.println("OCCURED: AT (LINE:" + saxpe.getLineNumber() + ",COLUMN:" + saxpe.getColumnNumber()
					+ ")");
			System.err.println("MESSAGE: " + saxpe.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	static void init() {
		File configDir = SystemPathFinder.getAppConfig();
		File xmlFile = new File(configDir, LOG4J);

		try {
			s_config = new Config(xmlFile.getPath());

			s_config.setLoggerRepository(LogManager.getLoggerRepository());
			s_config.loadXml();
		} catch (SAXParseException saxpe) {
			System.err.println("XML ERROR:");
			System.err.println("OCCURED: AT (LINE:" + saxpe.getLineNumber() + ",COLUMN:" + saxpe.getColumnNumber()
					+ ")");
			System.err.println("MESSAGE: " + saxpe.getMessage());

			throw new ExceptionInInitializerError(saxpe);
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				LogManager.shutdown();

				Log4jConfigurator.closeCalAppender();
			}
		});
	}

	static void closeCalAppender() {
		org.apache.log4j.Appender a = getCalAppender();

		if (a != null) {
			a.close();
		}
	}

	static org.apache.log4j.Appender getCalAppender() {
		Appender appender = (Appender) s_config.getConfiguration().getAppender("CAL");

		if (appender != null) {
			return appender.getAppender();
		} else {
			return null;
		}
	}

	public static final class Config {
		private String m_configXml;

		private Throwable m_initException;

		public LoggerRepository m_repository;

		public Properties m_props;

		private Configuration m_configuration;

		public Config(String configXml) {
			m_configXml = configXml;

			// Sub-Classes
			m_configuration = null;
		}

		public void addConfiguration(Configuration configuration) {
			m_configuration = configuration;
		}

		public void destroy() {
			// Call destory of configuration
			if (m_configuration != null)
				m_configuration.destroy();

			m_configuration = null;
		}

		public Configuration getConfiguration() {
			return m_configuration;
		}

		public Throwable getInitException() {
			return m_initException;
		}

		public final static boolean getAttributeBoolean(Attributes attrs, String name, String defaultValue) {
			String value = (attrs == null ? null : attrs.getValue(name));

			if (value == null)
				value = defaultValue;

			if (value != null
					&& (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("yes") || value
							.equalsIgnoreCase("1"))) {
				return true;
			}

			return false;
		}

		public final static String getAttributeString(Attributes attrs, String name) {
			return attrs.getValue(name);
		}

		public void init(Attributes attrs) {
		}

		public final static int inList(String data, String[] list) {
			if (data == null)
				return 0;

			for (int i = 0; i < list.length; i++) {
				if (data.equals(list[i]))
					return i + 1;
			}

			return 0;
		}

		public final static boolean isEmpty(String data) {
			return data == null || data.length() == 0;
		}

		public void loadXml() throws SAXException, IOException {
			XmlLoader loader = new XmlLoader();
			loader.parse(this);

			this.setReady(this);
		}

		public void setReady(Config config) {
			m_props = new Properties();

			// Call setReady of configuration
			if (m_configuration != null)
				m_configuration.setReady(this);
		}

		public void setLoggerRepository(LoggerRepository repository) {
			m_repository = repository;
		}

		public void setParameters(Object instance, Vector<Param> params) {
			PropertySetter propSetter = new PropertySetter(instance);

			for (int i = 0; i < params.size(); i++) {
				Param param = params.elementAt(i);
				String value = this.subst(param.value);

				propSetter.setProperty(param.name, value);
			}

			propSetter.activate();
		}

		public String subst(String value) {
			try {
				if (value != null)
					return OptionConverter.substVars(value, m_props);
			} catch (IllegalArgumentException iae) {
				LogLog.warn("Could not perform variable substitution.", iae);
			}

			return value;
		}

		public static final class Appender {
			public String name;

			public String clazz;

			public org.apache.log4j.Appender appender;

			private Vector<Param> params; // ELEMENT Param

			private ErrorHandler errorHandler;

			private Vector<Layout> layouts; // ELEMENT Layout

			private Vector<Filter> filters; // ELEMENT Filter

			private Vector<AppenderRef> appenderRefs; // ELEMENT AppenderRef

			public Appender(Attributes attrs) {
				// Attributes
				this.name = Config.getAttributeString(attrs, "name");
				this.clazz = Config.getAttributeString(attrs, "class");

				// Sub-Classes
				this.appenderRefs = new Vector<AppenderRef>();
				this.errorHandler = null;
				this.filters = new Vector<Filter>();
				this.layouts = new Vector<Layout>();
				this.params = new Vector<Param>();
			}

			public void addAppenderRef(AppenderRef appenderRef) {
				this.appenderRefs.addElement(appenderRef);
			}

			public void addErrorHandler(ErrorHandler errorHandler) {
				this.errorHandler = errorHandler;
			}

			public void addFilter(Filter filter) {
				this.filters.addElement(filter);
			}

			public void addLayout(Layout layout) {
				this.layouts.addElement(layout);
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				// Call destory of errorHandler
				if (errorHandler != null)
					errorHandler.destroy();

				// Call destory of layouts
				for (int i = 0; i < this.layouts.size(); i++) {
					Layout layout = this.layouts.elementAt(i);

					layout.destroy();
				}

				// Call destory of filters
				for (int i = 0; i < this.filters.size(); i++) {
					Filter filter = this.filters.elementAt(i);

					filter.destroy();
				}

				// Call destory of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.destroy();
				}

				this.params = null;
				this.errorHandler = null;
				this.layouts = null;
				this.filters = null;
				this.appenderRefs = null;
			}

			public AppenderRef getAppenderRef(int index) {
				if (index < 0 || index >= this.appenderRefs.size())
					return null;
				else
					return this.appenderRefs.elementAt(index);
			}

			public ErrorHandler getErrorHandler() {
				return this.errorHandler;
			}

			public Filter getFilter(int index) {
				if (index < 0 || index >= this.filters.size())
					return null;
				else
					return this.filters.elementAt(index);
			}

			public Layout getLayout(int index) {
				if (index < 0 || index >= this.layouts.size())
					return null;
				else
					return this.layouts.elementAt(index);
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public boolean isValid() {
				if (Config.isEmpty(this.name) || this.clazz == null || !Pattern.matches("\\w+(\\.\\w+)*", this.clazz))
					return false;
				else
					return true;
			}

			public void setReady(Configuration configuration) {
				String name = s_config.subst(this.name);
				String className = s_config.subst(this.clazz);

				try {
					Object instance = ClazzLoader.loadClass(className).newInstance();

					this.appender = (org.apache.log4j.Appender) instance;

					this.appender.setName(this.name);
					s_config.setParameters(this.appender, this.params);
				} catch (Throwable t) {
					LogLog.error("Could not create an Appender[" + name + "]. Reported error follows.", t);
					return;
				}

				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				// Call setReady of errorHandler
				if (errorHandler != null)
					errorHandler.setReady(this);

				// Call setReady of layouts
				for (int i = 0; i < this.layouts.size(); i++) {
					Layout layout = this.layouts.elementAt(i);

					layout.setReady(this);
				}

				// Call setReady of filters
				for (int i = 0; i < this.filters.size(); i++) {
					Filter filter = this.filters.elementAt(i);

					filter.setReady(this);
				}

				// Call setReady of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.setReady(this);
				}
			}

			public String toString() {
				return "Appender[" + "name=" + this.name + ",class=" + this.clazz + "]";
			}

			public org.apache.log4j.Appender getAppender() {
				return this.appender;
			}
		}

		public static final class AppenderRef {
			public String ref;

			public Appender appender;

			private boolean isReady; // To avoiding recurring call setReady

			public AppenderRef(Attributes attrs) {
				// Attributes
				this.ref = Config.getAttributeString(attrs, "ref");
			}

			public void destroy() {
			}

			public boolean isValid() {
				if (Config.isEmpty(this.ref))
					return false;
				else
					return true;
			}

			public void setReady(Object parent) {
				if (this.isReady)
					return;

				this.isReady = true;

				String name = s_config.subst(this.ref);

				this.appender = s_config.m_configuration.getAppender(name);

				if (this.appender == null)
					LogLog.debug("Appender named [" + name + "] not found.");
				else {
					org.apache.log4j.Appender currentAppender = this.appender.getAppender();

					if (parent instanceof Category) {
						Category category = (Category) parent;

						if (category.cat.getAppender(name) == null) {
							LogLog.debug("Adding appender named [" + name + "] to category [" + category.cat.getName()
									+ "].");
							category.cat.addAppender(currentAppender);
						}
					} else if (parent instanceof Logger) {
						Logger logger = (Logger) parent;

						if (logger.cat.getAppender(name) == null) {
							LogLog.debug("Adding appender named [" + name + "] to category [" + logger.cat.getName()
									+ "].");
							logger.cat.addAppender(currentAppender);
						}
					} else if (parent instanceof Root) {
						Root root = (Root) parent;

						if (root.cat.getAppender(name) == null) {
							LogLog.debug("Adding appender named [" + name + "] to category [" + root.cat.getName()
									+ "].");
							root.cat.addAppender(currentAppender);
						}
					} else if (parent instanceof Appender) {
						org.apache.log4j.Appender parentAppender = ((Appender) parent).getAppender();

						if (parentAppender instanceof AppenderAttachable) {
							AppenderAttachable aa = (AppenderAttachable) parentAppender;

							if (aa.getAppender(name) == null) {
								LogLog.debug("Attaching appender named [" + name + "] to appender named ["
										+ parentAppender.getName() + "].");
								aa.addAppender(currentAppender);
							}
						} else
							LogLog.error("Requesting attachment of appender named [" + name + "] to appender named ["
									+ parentAppender.getName()
									+ "] which does not implement org.apache.log4j.spi.AppenderAttachable.");
					} else if (parent instanceof ErrorHandler) {
						ErrorHandler errorHandler = (ErrorHandler) parent;

						LogLog.debug("Setting backup appender named [" + name + "] to ErrorHandler.");
						errorHandler.eh.setBackupAppender(currentAppender);
					}
				}
			}

			public String toString() {
				return "AppenderRef[" + "ref=" + this.ref + "]";
			}
		}

		public static final class Category {
			public String name;

			public String clazz;

			public boolean additivity;

			public org.apache.log4j.Logger cat;

			private Vector<Param> params; // ELEMENT Param

			private Priority priority;

			private Level level;

			private Vector<AppenderRef> appenderRefs; // ELEMENT AppenderRef

			public Category(Attributes attrs) {
				// Attributes
				this.name = Config.getAttributeString(attrs, "name");
				this.clazz = Config.getAttributeString(attrs, "class");
				this.additivity = Config.getAttributeBoolean(attrs, "additivity", "true");

				// Sub-Classes
				this.appenderRefs = new Vector<AppenderRef>();
				this.level = null;
				this.params = new Vector<Param>();
				this.priority = null;
			}

			public void addAppenderRef(AppenderRef appenderRef) {
				this.appenderRefs.addElement(appenderRef);
			}

			public void addLevel(Level level) {
				this.level = level;
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void addPriority(Priority priority) {
				this.priority = priority;
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				// Call destory of priority
				if (priority != null)
					priority.destroy();

				// Call destory of level
				if (level != null)
					level.destroy();

				// Call destory of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.destroy();
				}

				this.params = null;
				this.priority = null;
				this.level = null;
				this.appenderRefs = null;
			}

			public AppenderRef getAppenderRef(int index) {
				if (index < 0 || index >= this.appenderRefs.size())
					return null;
				else
					return this.appenderRefs.elementAt(index);
			}

			public Level getLevel() {
				return this.level;
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public Priority getPriority() {
				return this.priority;
			}

			public boolean isValid() {
				if (Config.isEmpty(this.name) || this.clazz == null || !Pattern.matches("\\w+(\\.\\w+)*", this.clazz))
					return false;
				else
					return true;
			}

			public void setReady(Configuration configuration) {
				String name = s_config.subst(this.name);

				try {
					String className = s_config.subst(this.clazz);
					Class cls = ClazzLoader.loadClass(className);
					Method method = cls.getMethod("getLogger", new Class[] { String.class });

					LogLog.debug("Desired logger sub-class: [" + className + ']');
					this.cat = (org.apache.log4j.Logger) method.invoke(null, new Object[] { name });
				} catch (Throwable t) {
					LogLog.error("Could not retrieve category [" + name + "]. Reported error follows.", t);
					return;
				}

				// Setting up a category needs to be an atomic operation, in
				// order
				// to protect potential log operations while category
				// configuration is in progress.
				synchronized (cat) {
					LogLog.debug("Setting [" + name + "] additivity to [" + additivity + "].");
					this.cat.setAdditivity(additivity);

					// Remove all existing appenders from cat. They will be
					// reconstructed if need be.
					this.cat.removeAllAppenders();
					s_config.setParameters(this.cat, this.params);
				}

				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				// Call setReady of priority
				if (priority != null)
					priority.setReady(this);

				// Call setReady of level
				if (level != null)
					level.setReady(this);

				// Call setReady of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.setReady(this);
				}
			}

			public String toString() {
				return "Category[" + "name=" + this.name + ",class=" + this.clazz + ",additivity=" + this.additivity
						+ "]";
			}
		}

		public static final class CategoryFactory {
			public String clazz;

			private Vector<Param> params; // ELEMENT Param

			public CategoryFactory(Attributes attrs) {
				// Attributes
				this.clazz = Config.getAttributeString(attrs, "class");

				// Sub-Classes
				this.params = new Vector<Param>();
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				this.params = null;
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public boolean isValid() {
				if (this.clazz == null || !Pattern.matches("\\w+(\\.\\w+)*", this.clazz))
					return false;
				else
					return true;
			}

			public void setReady(Configuration configuration) {
				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				Object catFactory = OptionConverter.instantiateByClassName(s_config.subst(this.clazz),
						LoggerFactory.class, null);

				s_config.setParameters(catFactory, this.params);
			}

			public String toString() {
				return "CategoryFactory[" + "class=" + this.clazz + "]";
			}
		}

		public static final class Configuration {
			public boolean debug;

			public String threshold;

			private Vector<Param> params; // ELEMENT Param

			private Vector<Renderer> renderers; // ELEMENT Renderer

			private Hashtable<String, Appender> appenders; // KEY name => ELEMENT Appender

			private Hashtable<String, Category> categories; // KEY name => ELEMENT Category

			private Vector<Logger> loggers; // ELEMENT Logger

			private CategoryFactory categoryFactory;

			private Root root;

			public Configuration(Attributes attrs) {
				// Attributes
				this.debug = Config.getAttributeBoolean(attrs, "debug", "false");
				this.threshold = Config.getAttributeString(attrs, "threshold");

				// Sub-Classes
				this.appenders = new Hashtable<String, Appender>();
				this.categories = new Hashtable<String, Category>();
				this.categoryFactory = null;
				this.loggers = new Vector<Logger>();
				this.params = new Vector<Param>();
				this.renderers = new Vector<Renderer>();
				this.root = null;
			}

			public void addAppender(Appender appender) {
				this.appenders.put(appender.name, appender);
			}

			public void addCategory(Category category) {
				this.categories.put(category.name, category);
			}

			public void addCategoryFactory(CategoryFactory categoryFactory) {
				this.categoryFactory = categoryFactory;
			}

			public void addLogger(Logger logger) {
				this.loggers.addElement(logger);
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void addRenderer(Renderer renderer) {
				this.renderers.addElement(renderer);
			}

			public void addRoot(Root root) {
				this.root = root;
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				// Call destory of renderers
				for (int i = 0; i < this.renderers.size(); i++) {
					Renderer renderer = this.renderers.elementAt(i);

					renderer.destroy();
				}

				// Call destory of appenders
				Enumeration<Appender> appenderEnum = this.appenders.elements();
				while (appenderEnum.hasMoreElements()) {
					Appender appender = appenderEnum.nextElement();

					appender.destroy();
				}

				// Call destory of categories
				Enumeration<Category> categoryEnum = this.categories.elements();
				while (categoryEnum.hasMoreElements()) {
					Category category = categoryEnum.nextElement();

					category.destroy();
				}

				// Call destory of loggers
				for (int i = 0; i < this.loggers.size(); i++) {
					Logger logger = this.loggers.elementAt(i);

					logger.destroy();
				}

				// Call destory of categoryFactory
				if (categoryFactory != null)
					categoryFactory.destroy();

				// Call destory of root
				if (root != null)
					root.destroy();

				this.params = null;
				this.renderers = null;
				this.appenders = null;
				this.categories = null;
				this.loggers = null;
				this.categoryFactory = null;
				this.root = null;
			}

			public Appender getAppender(String name) {
				return this.appenders.get(name);
			}

			public Category getCategory(String name) {
				return this.categories.get(name);
			}

			public CategoryFactory getCategoryFactory() {
				return this.categoryFactory;
			}

			public Logger getLogger(int index) {
				if (index < 0 || index >= this.loggers.size())
					return null;
				else
					return this.loggers.elementAt(index);
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public Renderer getRenderer(int index) {
				if (index < 0 || index >= this.renderers.size())
					return null;
				else
					return this.renderers.elementAt(index);
			}

			public Root getRoot() {
				return this.root;
			}

			public boolean isValid() {
				if (this.threshold != null
						&& Config.inList(this.threshold, new String[] { "all", "debug", "info", "warn", "error",
								"fatal", "off" }) <= 0)
					return false;
				else
					return true;
			}

			public void setReady(Config config) {
				if (this.debug)
					LogLog.setInternalDebugging(true);

				if (!Config.isEmpty(this.threshold))
					config.m_repository.setThreshold(this.threshold);

				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					config.m_props.put(param.name, param.value);
				}

				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);
					String value = param.value;

					if (value.indexOf('$') >= 0)
						config.m_props.put(param.name, config.subst(value));
				}

				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				// Call setReady of renderers
				for (int i = 0; i < this.renderers.size(); i++) {
					Renderer renderer = this.renderers.elementAt(i);

					renderer.setReady(this);
				}

				// Call setReady of appenders
				Enumeration<Appender> appenderEnum = this.appenders.elements();
				while (appenderEnum.hasMoreElements()) {
					Appender appender = appenderEnum.nextElement();

					appender.setReady(this);
				}

				// Call setReady of categories
				Enumeration<Category> categoryEnum = this.categories.elements();
				while (categoryEnum.hasMoreElements()) {
					Category category = categoryEnum.nextElement();

					category.setReady(this);
				}

				// Call setReady of loggers
				for (int i = 0; i < this.loggers.size(); i++) {
					Logger logger = this.loggers.elementAt(i);

					logger.setReady(this);
				}

				// Call setReady of categoryFactory
				if (categoryFactory != null)
					categoryFactory.setReady(this);

				// Call setReady of root
				if (root != null)
					root.setReady(this);
			}

			public String toString() {
				return "Configuration[" + "debug=" + this.debug + ",threshold=" + this.threshold + "]";
			}
		}

		public static final class ErrorHandler {
			public String clazz;

			public org.apache.log4j.spi.ErrorHandler eh;

			private Vector<Param> params; // ELEMENT Param

			private RootRef rootRef;

			private Vector<LoggerRef> loggerRefs; // ELEMENT LoggerRef

			private Vector<AppenderRef> appenderRefs; // ELEMENT AppenderRef

			public ErrorHandler(Attributes attrs) {
				// Attributes
				this.clazz = Config.getAttributeString(attrs, "class");

				// Sub-Classes
				this.appenderRefs = new Vector<AppenderRef>();
				this.loggerRefs = new Vector<LoggerRef>();
				this.params = new Vector<Param>();
				this.rootRef = null;
			}

			public void addAppenderRef(AppenderRef appenderRef) {
				this.appenderRefs.addElement(appenderRef);
			}

			public void addLoggerRef(LoggerRef loggerRef) {
				this.loggerRefs.addElement(loggerRef);
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void addRootRef(RootRef rootRef) {
				this.rootRef = rootRef;
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				// Call destory of rootRef
				if (rootRef != null)
					rootRef.destroy();

				// Call destory of loggerRefs
				for (int i = 0; i < this.loggerRefs.size(); i++) {
					LoggerRef loggerRef = this.loggerRefs.elementAt(i);

					loggerRef.destroy();
				}

				// Call destory of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.destroy();
				}

				this.params = null;
				this.rootRef = null;
				this.loggerRefs = null;
				this.appenderRefs = null;
			}

			public AppenderRef getAppenderRef(int index) {
				if (index < 0 || index >= this.appenderRefs.size())
					return null;
				else
					return this.appenderRefs.elementAt(index);
			}

			public LoggerRef getLoggerRef(int index) {
				if (index < 0 || index >= this.loggerRefs.size())
					return null;
				else
					return this.loggerRefs.elementAt(index);
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public RootRef getRootRef() {
				return this.rootRef;
			}

			public boolean isValid() {
				if (this.clazz == null || !Pattern.matches("\\w+(\\.\\w+)*", this.clazz))
					return false;
				else
					return true;
			}

			public void setReady(Appender appender) {
				String className = s_config.subst(this.clazz);

				this.eh = (org.apache.log4j.spi.ErrorHandler) OptionConverter.instantiateByClassName(className,
						org.apache.log4j.spi.ErrorHandler.class, null);

				if (this.eh != null) {
					this.eh.setAppender(appender.getAppender());

					if (this.rootRef != null)
						this.eh.setLogger(s_config.m_repository.getRootLogger());

					for (int i = 0; i < this.loggerRefs.size(); i++) {
						LoggerRef loggerRef = this.loggerRefs.elementAt(i);
						String name = s_config.subst(loggerRef.ref);

						this.eh.setLogger(s_config.m_repository.getLogger(name));
					}

					s_config.setParameters(eh, this.params);
				}

				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				// Call setReady of rootRef
				if (rootRef != null)
					rootRef.setReady(this);

				// Call setReady of loggerRefs
				for (int i = 0; i < this.loggerRefs.size(); i++) {
					LoggerRef loggerRef = this.loggerRefs.elementAt(i);

					loggerRef.setReady(this);
				}

				// Call setReady of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.setReady(this);
				}
			}

			public String toString() {
				return "ErrorHandler[" + "class=" + this.clazz + "]";
			}
		}

		public static final class Filter {
			public String clazz;

			public org.apache.log4j.spi.Filter filter;

			private Vector<Param> params; // ELEMENT Param

			public Filter(Attributes attrs) {
				// Attributes
				this.clazz = Config.getAttributeString(attrs, "class");

				// Sub-Classes
				this.params = new Vector<Param>();
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				this.params = null;
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public boolean isValid() {
				if (this.clazz == null || !Pattern.matches("\\w+(\\.\\w+)*", this.clazz))
					return false;
				else
					return true;
			}

			public void setReady(Appender appender) {
				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				String className = s_config.subst(this.clazz);

				this.filter = (org.apache.log4j.spi.Filter) OptionConverter.instantiateByClassName(className,
						Filter.class, null);

				if (filter != null) {
					s_config.setParameters(filter, this.params);

					LogLog.debug("Adding filter of type [" + className + "] to appender named ["
							+ appender.getAppender().getName() + "].");
					appender.getAppender().addFilter(this.filter);
				}
			}

			public String toString() {
				return "Filter[" + "class=" + this.clazz + "]";
			}
		}

		public static final class Layout {
			public String clazz;

			public org.apache.log4j.Layout layout;

			private Vector<Param> params; // ELEMENT Param

			public Layout(Attributes attrs) {
				// Attributes
				this.clazz = Config.getAttributeString(attrs, "class");

				// Sub-Classes
				this.params = new Vector<Param>();
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				this.params = null;
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public boolean isValid() {
				if (this.clazz == null || !Pattern.matches("\\w+(\\.\\w+)*", this.clazz))
					return false;
				else
					return true;
			}

			public void setReady(Appender appender) {
				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				String className = s_config.subst(this.clazz);

				try {
					Object instance = ClazzLoader.loadClass(className).newInstance();

					s_config.setParameters(instance, this.params);
					this.layout = (org.apache.log4j.Layout) instance;

					LogLog.debug("Setting layout of type [" + className + "] for appender ["
							+ appender.getAppender().getName() + "].");
					appender.getAppender().setLayout(this.layout);
				} catch (Throwable t) {
					LogLog.error("Could not create the Layout. Reported error follows.", t);
				}
			}

			public String toString() {
				return "Layout[" + "class=" + this.clazz + "]";
			}
		}

		public static class Level {
			public String m_clazz;

			public String m_value;

			public org.apache.log4j.Level m_level;

			private Vector<Param> m_params; // ELEMENT Param

			private boolean m_isReady; // To avoiding recurring call setReady

			public Level(Attributes attrs) {
				// Attributes
				m_clazz = Config.getAttributeString(attrs, "class");
				m_value = Config.getAttributeString(attrs, "value");

				// Sub-Classes
				m_params = new Vector<Param>();
			}

			public void addParam(Param param) {
				m_params.addElement(param);
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < m_params.size(); i++) {
					Param param = m_params.elementAt(i);

					param.destroy();
				}

				m_params = null;
			}

			public Param getParam(int index) {
				if (index < 0 || index >= m_params.size())
					return null;
				else
					return m_params.elementAt(index);
			}

			public boolean isValid() {
				return true;
			}

			public void setReady(Object parent) {
				if (m_isReady)
					return;

				m_isReady = true;

				// Call setReady of params
				for (int i = 0; i < m_params.size(); i++) {
					Param param = m_params.elementAt(i);

					param.setReady(this);
				}

				String value = s_config.subst(m_value);

				if (!Config.isEmpty(m_clazz)) {
					try {
						String className = s_config.subst(m_clazz);
						Class cls = ClazzLoader.loadClass(className);
						Method method = cls.getMethod("toLevel", new Class[] { String.class });

						LogLog.debug("Desired Level sub-class: [" + className + ']');
						m_level = (org.apache.log4j.Level) method.invoke(null, new Object[] { value });
					} catch (Exception e) {
						LogLog.error("Could not create level [" + value + "]. Reported error follows.", e);
						return;
					}
				} else if (!Config.isEmpty(value) && !value.equalsIgnoreCase("inherited"))
					m_level = OptionConverter.toLevel(value, org.apache.log4j.Level.DEBUG);

				if (parent instanceof Root) {
					Root root = (Root) parent;

					if (m_level != null) {
						LogLog.debug("root level set to " + m_level);
						root.cat.setLevel(m_level);
					} else
						LogLog.error("Root level cannot be inherited. Ignoring directive.");
				} else if (parent instanceof Category) {
					Category category = (Category) parent;

					LogLog.debug(category.cat.getName() + " level set to " + m_level);
					category.cat.setLevel(m_level);
				} else if (parent instanceof Logger) {
					Logger logger = (Logger) parent;

					LogLog.debug(logger.cat.getName() + " level set to " + m_level);
					logger.cat.setLevel(m_level);
				}
			}

			public String toString() {
				return "Level[" + "class=" + m_clazz + ",value=" + m_value + "]";
			}
		}

		public static final class Logger {
			public String name;

			public boolean additivity;

			public org.apache.log4j.Logger cat;

			private Priority priority;

			private Level level;

			private Vector<AppenderRef> appenderRefs; // ELEMENT AppenderRef

			public Logger(Attributes attrs) {
				// Attributes
				this.name = Config.getAttributeString(attrs, "name");
				this.additivity = Config.getAttributeBoolean(attrs, "additivity", "true");

				// Sub-Classes
				this.appenderRefs = new Vector<AppenderRef>();
				this.level = null;
				this.priority = null;
			}

			public void addAppenderRef(AppenderRef appenderRef) {
				this.appenderRefs.addElement(appenderRef);
			}

			public void addLevel(Level level) {
				this.level = level;
			}

			public void addPriority(Priority priority) {
				this.priority = priority;
			}

			public void destroy() {
				// Call destory of priority
				if (priority != null)
					priority.destroy();

				// Call destory of level
				if (level != null)
					level.destroy();

				// Call destory of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.destroy();
				}

				this.priority = null;
				this.level = null;
				this.appenderRefs = null;
			}

			public AppenderRef getAppenderRef(int index) {
				if (index < 0 || index >= this.appenderRefs.size())
					return null;
				else
					return this.appenderRefs.elementAt(index);
			}

			public Level getLevel() {
				return this.level;
			}

			public Priority getPriority() {
				return this.priority;
			}

			public boolean isValid() {
				if (Config.isEmpty(this.name))
					return false;
				else
					return true;
			}

			public void setReady(Configuration configuration) {
				String name = s_config.subst(this.name);

				this.cat = s_config.m_repository.getLogger(name);

				// Setting up a category needs to be an atomic operation, in
				// order
				// to protect potential log operations while category
				// configuration is in progress.
				synchronized (cat) {
					LogLog.debug("Setting [" + name + "] additivity to [" + additivity + "].");
					this.cat.setAdditivity(additivity);

					// Remove all existing appenders from cat. They will be
					// reconstructed if need be.
					this.cat.removeAllAppenders();
				}

				// Call setReady of priority
				if (priority != null)
					priority.setReady(this);

				// Call setReady of level
				if (level != null)
					level.setReady(this);

				// Call setReady of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.setReady(this);
				}
			}

			public String toString() {
				return "Logger[" + "name=" + this.name + ",additivity=" + this.additivity + "]";
			}
		}

		public static final class LoggerRef {
			public String ref;

			public LoggerRef(Attributes attrs) {
				// Attributes
				this.ref = Config.getAttributeString(attrs, "ref");
			}

			public void destroy() {
			}

			public boolean isValid() {
				if (Config.isEmpty(this.ref))
					return false;
				else
					return true;
			}

			public void setReady(ErrorHandler errorHandler) {
			}

			public String toString() {
				return "LoggerRef[" + "ref=" + this.ref + "]";
			}
		}

		public static final class Param {
			public String name;

			public String value;

			private boolean isReady; // To avoiding recurring call setReady

			public Param(Attributes attrs) {
				// Attributes
				this.name = Config.getAttributeString(attrs, "name");
				this.value = Config.getAttributeString(attrs, "value");
			}

			public void destroy() {
			}

			public boolean isValid() {
				if (Config.isEmpty(this.name))
					return false;
				else
					return true;
			}

			public void setReady(Object parent) {
				if (this.isReady)
					return;

				this.isReady = true;
			}

			public String toString() {
				return "Param[" + "name=" + this.name + ",value=" + this.value + "]";
			}
		}

		public static final class Priority extends Level {
			public String clazz;

			public String value;

			private Vector<Param> params; // ELEMENT Param

			private boolean isReady; // To avoiding recurring call setReady

			public Priority(Attributes attrs) {
				super(attrs);

				// Attributes
				this.clazz = Config.getAttributeString(attrs, "class");
				this.value = Config.getAttributeString(attrs, "value");

				// Sub-Classes
				this.params = new Vector<Param>();
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				this.params = null;
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public boolean isValid() {
				return true;
			}

			public void setReady(Object parent) {
				if (this.isReady)
					return;

				this.isReady = true;

				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				super.setReady(parent);
			}

			public String toString() {
				return "Priority[" + "class=" + this.clazz + ",value=" + this.value + ',' + super.toString() + "]";
			}
		}

		public static final class Renderer {
			public String renderedClass;

			public String renderingClass;

			public Renderer(Attributes attrs) {
				// Attributes
				this.renderedClass = Config.getAttributeString(attrs, "renderedClass");
				this.renderingClass = Config.getAttributeString(attrs, "renderingClass");
			}

			public void destroy() {
			}

			public boolean isValid() {
				if (this.renderedClass == null || !Pattern.matches("\\w+(\\.\\w+)*", this.renderedClass)
						|| this.renderingClass == null || !Pattern.matches("\\w+(\\.\\w+)*", this.renderingClass))
					return false;
				else
					return true;
			}

			public void setReady(Configuration configuration) {

				if (s_config.m_repository instanceof RendererSupport) {
					String renderedClass = s_config.subst(this.renderedClass);
					String renderingClass = s_config.subst(this.renderingClass);

					RendererMap.addRenderer((RendererSupport) s_config.m_repository, renderedClass, renderingClass);
				}
			}

			public String toString() {
				return "Renderer[" + "renderedClass=" + this.renderedClass + ",renderingClass=" + this.renderingClass
						+ "]";
			}
		}

		public static final class Root {
			public org.apache.log4j.Logger cat;

			private Vector<Param> params; // ELEMENT Param

			private Priority priority;

			private Level level;

			private Vector<AppenderRef> appenderRefs; // ELEMENT AppenderRef

			public Root(Attributes attrs) {
				// Sub-Classes
				this.appenderRefs = new Vector<AppenderRef>();
				this.level = null;
				this.params = new Vector<Param>();
				this.priority = null;
			}

			public void addAppenderRef(AppenderRef appenderRef) {
				this.appenderRefs.addElement(appenderRef);
			}

			public void addLevel(Level level) {
				this.level = level;
			}

			public void addParam(Param param) {
				this.params.addElement(param);
			}

			public void addPriority(Priority priority) {
				this.priority = priority;
			}

			public void destroy() {
				// Call destory of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.destroy();
				}

				// Call destory of priority
				if (priority != null)
					priority.destroy();

				// Call destory of level
				if (level != null)
					level.destroy();

				// Call destory of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.destroy();
				}

				this.params = null;
				this.priority = null;
				this.level = null;
				this.appenderRefs = null;
			}

			public AppenderRef getAppenderRef(int index) {
				if (index < 0 || index >= this.appenderRefs.size())
					return null;
				else
					return this.appenderRefs.elementAt(index);
			}

			public Level getLevel() {
				return this.level;
			}

			public Param getParam(int index) {
				if (index < 0 || index >= this.params.size())
					return null;
				else
					return this.params.elementAt(index);
			}

			public Priority getPriority() {
				return this.priority;
			}

			public boolean isValid() {
				return true;
			}

			public void setReady(Configuration configuration) {
				this.cat = s_config.m_repository.getRootLogger();

				// Setting up a category needs to be an atomic operation, in
				// order to protect potential log operations while category
				// configuration is in progress.
				synchronized (cat) {
					// Remove all existing appenders from cat. They will be
					// reconstructed if need be.
					this.cat.removeAllAppenders();
				}

				// Call setReady of params
				for (int i = 0; i < this.params.size(); i++) {
					Param param = this.params.elementAt(i);

					param.setReady(this);
				}

				// Call setReady of priority
				if (priority != null)
					priority.setReady(this);

				// Call setReady of level
				if (level != null)
					level.setReady(this);

				// Call setReady of appenderRefs
				for (int i = 0; i < this.appenderRefs.size(); i++) {
					AppenderRef appenderRef = this.appenderRefs.elementAt(i);

					appenderRef.setReady(this);
				}
			}

			public String toString() {
				return "Root[" + "<no attribute>" + "]";
			}
		}

		public static final class RootRef {
			public RootRef(Attributes attrs) {
			}

			public void destroy() {
			}

			public boolean isValid() {
				return true;
			}

			public void setReady(ErrorHandler errorHandler) {
			}

			public String toString() {
				return "RootRef[" + "<no attribute>" + "]";
			}
		}

		private static final class XmlLoader extends BaseXmlHandler {
			private Config config;

			private Stack<Object> objs; // ELEMENT object

			private Stack<String> tags; // ELEMENT tag

			public XmlLoader() {
				this.objs = new Stack<Object>();
				this.tags = new Stack<String>();
			}

			public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs)
					throws SAXException {
				String tag = localName;

				try {
					if (tag.equals("appender")) {
						Appender appender = new Appender(attrs);
						String peek = tags.peek();

						if (peek.equals("configuration")) {
							Configuration configuration = (Configuration) objs.peek();

							if (!appender.isValid())
								throw new SAXException("APPENDER(" + appender + ") under CONFIGURATION("
										+ configuration + ") is invalid");

							configuration.addAppender(appender);
						} else
							throw new SAXException("APPENDER(" + appender + ") must follow CONFIGURATION");

						objs.push(appender);
					} else if (tag.equals("appender-ref")) {
						AppenderRef appenderRef = new AppenderRef(attrs);
						String peek = tags.peek();

						if (peek.equals("errorHandler")) {
							ErrorHandler errorHandler = (ErrorHandler) objs.peek();

							if (!appenderRef.isValid())
								throw new SAXException("APPENDER-REF(" + appenderRef + ") under ERRORHANDLER("
										+ errorHandler + ") is invalid");

							errorHandler.addAppenderRef(appenderRef);
						} else if (peek.equals("appender")) {
							Appender appender = (Appender) objs.peek();

							if (!appenderRef.isValid())
								throw new SAXException("APPENDER-REF(" + appenderRef + ") under APPENDER(" + appender
										+ ") is invalid");

							appender.addAppenderRef(appenderRef);
						} else if (peek.equals("category")) {
							Category category = (Category) objs.peek();

							if (!appenderRef.isValid())
								throw new SAXException("APPENDER-REF(" + appenderRef + ") under CATEGORY(" + category
										+ ") is invalid");

							category.addAppenderRef(appenderRef);
						} else if (peek.equals("logger")) {
							Logger logger = (Logger) objs.peek();

							if (!appenderRef.isValid())
								throw new SAXException("APPENDER-REF(" + appenderRef + ") under LOGGER(" + logger
										+ ") is invalid");

							logger.addAppenderRef(appenderRef);
						} else if (peek.equals("root")) {
							Root root = (Root) objs.peek();

							if (!appenderRef.isValid())
								throw new SAXException("APPENDER-REF(" + appenderRef + ") under ROOT(" + root
										+ ") is invalid");

							root.addAppenderRef(appenderRef);
						} else
							throw new SAXException("APPENDER-REF(" + appenderRef
									+ ") must follow ERRORHANDLER or APPENDER or CATEGORY or LOGGER or ROOT");

						objs.push(appenderRef);
					} else if (tag.equals("category")) {
						Category category = new Category(attrs);
						String peek = tags.peek();

						if (peek.equals("configuration")) {
							Configuration configuration = (Configuration) objs.peek();

							if (!category.isValid())
								throw new SAXException("CATEGORY(" + category + ") under CONFIGURATION("
										+ configuration + ") is invalid");

							configuration.addCategory(category);
						} else
							throw new SAXException("CATEGORY(" + category + ") must follow CONFIGURATION");

						objs.push(category);
					} else if (tag.equals("categoryFactory")) {
						CategoryFactory categoryFactory = new CategoryFactory(attrs);
						String peek = tags.peek();

						if (peek.equals("configuration")) {
							Configuration configuration = (Configuration) objs.peek();

							if (!categoryFactory.isValid())
								throw new SAXException("CATEGORYFACTORY(" + categoryFactory + ") under CONFIGURATION("
										+ configuration + ") is invalid");

							configuration.addCategoryFactory(categoryFactory);
						} else
							throw new SAXException("CATEGORYFACTORY(" + categoryFactory + ") must follow CONFIGURATION");

						objs.push(categoryFactory);
					} else if (tag.equals("configuration")) {
						Configuration configuration = new Configuration(attrs);
						String peek = tags.peek();

						if (peek.equals("config")) {
							Config config = (Config) objs.peek();

							if (!configuration.isValid())
								throw new SAXException("CONFIGURATION(" + configuration + ") under CONFIG is invalid");

							config.addConfiguration(configuration);
						} else
							throw new SAXException("CONFIGURATION(" + configuration + ") must follow CONFIG");

						objs.push(configuration);
					} else if (tag.equals("errorHandler")) {
						ErrorHandler errorHandler = new ErrorHandler(attrs);
						String peek = tags.peek();

						if (peek.equals("appender")) {
							Appender appender = (Appender) objs.peek();

							if (!errorHandler.isValid())
								throw new SAXException("ERRORHANDLER(" + errorHandler + ") under APPENDER(" + appender
										+ ") is invalid");

							appender.addErrorHandler(errorHandler);
						} else
							throw new SAXException("ERRORHANDLER(" + errorHandler + ") must follow APPENDER");

						objs.push(errorHandler);
					} else if (tag.equals("filter")) {
						Filter filter = new Filter(attrs);
						String peek = tags.peek();

						if (peek.equals("appender")) {
							Appender appender = (Appender) objs.peek();

							if (!filter.isValid())
								throw new SAXException("FILTER(" + filter + ") under APPENDER(" + appender
										+ ") is invalid");

							appender.addFilter(filter);
						} else
							throw new SAXException("FILTER(" + filter + ") must follow APPENDER");

						objs.push(filter);
					} else if (tag.equals("layout")) {
						Layout layout = new Layout(attrs);
						String peek = tags.peek();

						if (peek.equals("appender")) {
							Appender appender = (Appender) objs.peek();

							if (!layout.isValid())
								throw new SAXException("LAYOUT(" + layout + ") under APPENDER(" + appender
										+ ") is invalid");

							appender.addLayout(layout);
						} else
							throw new SAXException("LAYOUT(" + layout + ") must follow APPENDER");

						objs.push(layout);
					} else if (tag.equals("level")) {
						Level level = new Level(attrs);
						String peek = tags.peek();

						if (peek.equals("category")) {
							Category category = (Category) objs.peek();

							if (!level.isValid())
								throw new SAXException("LEVEL(" + level + ") under CATEGORY(" + category
										+ ") is invalid");

							category.addLevel(level);
						} else if (peek.equals("logger")) {
							Logger logger = (Logger) objs.peek();

							if (!level.isValid())
								throw new SAXException("LEVEL(" + level + ") under LOGGER(" + logger + ") is invalid");

							logger.addLevel(level);
						} else if (peek.equals("root")) {
							Root root = (Root) objs.peek();

							if (!level.isValid())
								throw new SAXException("LEVEL(" + level + ") under ROOT(" + root + ") is invalid");

							root.addLevel(level);
						} else
							throw new SAXException("LEVEL(" + level + ") must follow CATEGORY or LOGGER or ROOT");

						objs.push(level);
					} else if (tag.equals("logger")) {
						Logger logger = new Logger(attrs);
						String peek = tags.peek();

						if (peek.equals("configuration")) {
							Configuration configuration = (Configuration) objs.peek();

							if (!logger.isValid())
								throw new SAXException("LOGGER(" + logger + ") under CONFIGURATION(" + configuration
										+ ") is invalid");

							configuration.addLogger(logger);
						} else
							throw new SAXException("LOGGER(" + logger + ") must follow CONFIGURATION");

						objs.push(logger);
					} else if (tag.equals("logger-ref")) {
						LoggerRef loggerRef = new LoggerRef(attrs);
						String peek = tags.peek();

						if (peek.equals("errorHandler")) {
							ErrorHandler errorHandler = (ErrorHandler) objs.peek();

							if (!loggerRef.isValid())
								throw new SAXException("LOGGER-REF(" + loggerRef + ") under ERRORHANDLER("
										+ errorHandler + ") is invalid");

							errorHandler.addLoggerRef(loggerRef);
						} else
							throw new SAXException("LOGGER-REF(" + loggerRef + ") must follow ERRORHANDLER");

						objs.push(loggerRef);
					} else if (tag.equals("param")) {
						Param param = new Param(attrs);
						String peek = tags.peek();

						if (peek.equals("configuration")) {
							Configuration configuration = (Configuration) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under CONFIGURATION(" + configuration
										+ ") is invalid");

							configuration.addParam(param);
						} else if (peek.equals("appender")) {
							Appender appender = (Appender) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under APPENDER(" + appender
										+ ") is invalid");

							appender.addParam(param);
						} else if (peek.equals("errorHandler")) {
							ErrorHandler errorHandler = (ErrorHandler) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under ERRORHANDLER(" + errorHandler
										+ ") is invalid");

							errorHandler.addParam(param);
						} else if (peek.equals("layout")) {
							Layout layout = (Layout) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under LAYOUT(" + layout + ") is invalid");

							layout.addParam(param);
						} else if (peek.equals("filter")) {
							Filter filter = (Filter) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under FILTER(" + filter + ") is invalid");

							filter.addParam(param);
						} else if (peek.equals("category")) {
							Category category = (Category) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under CATEGORY(" + category
										+ ") is invalid");

							category.addParam(param);
						} else if (peek.equals("priority")) {
							Priority priority = (Priority) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under PRIORITY(" + priority
										+ ") is invalid");

							priority.addParam(param);
						} else if (peek.equals("level")) {
							Level level = (Level) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under LEVEL(" + level + ") is invalid");

							level.addParam(param);
						} else if (peek.equals("categoryFactory")) {
							CategoryFactory categoryFactory = (CategoryFactory) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under CATEGORYFACTORY(" + categoryFactory
										+ ") is invalid");

							categoryFactory.addParam(param);
						} else if (peek.equals("root")) {
							Root root = (Root) objs.peek();

							if (!param.isValid())
								throw new SAXException("PARAM(" + param + ") under ROOT(" + root + ") is invalid");

							root.addParam(param);
						} else
							throw new SAXException(
									"PARAM("
											+ param
											+ ") must follow CONFIGURATION or APPENDER or ERRORHANDLER or LAYOUT or FILTER or CATEGORY or PRIORITY or LEVEL or CATEGORYFACTORY or ROOT");

						objs.push(param);
					} else if (tag.equals("priority")) {
						Priority priority = new Priority(attrs);
						String peek = tags.peek();

						if (peek.equals("category")) {
							Category category = (Category) objs.peek();

							if (!priority.isValid())
								throw new SAXException("PRIORITY(" + priority + ") under CATEGORY(" + category
										+ ") is invalid");

							category.addPriority(priority);
						} else if (peek.equals("logger")) {
							Logger logger = (Logger) objs.peek();

							if (!priority.isValid())
								throw new SAXException("PRIORITY(" + priority + ") under LOGGER(" + logger
										+ ") is invalid");

							logger.addPriority(priority);
						} else if (peek.equals("root")) {
							Root root = (Root) objs.peek();

							if (!priority.isValid())
								throw new SAXException("PRIORITY(" + priority + ") under ROOT(" + root + ") is invalid");

							root.addPriority(priority);
						} else
							throw new SAXException("PRIORITY(" + priority + ") must follow CATEGORY or LOGGER or ROOT");

						objs.push(priority);
					} else if (tag.equals("renderer")) {
						Renderer renderer = new Renderer(attrs);
						String peek = tags.peek();

						if (peek.equals("configuration")) {
							Configuration configuration = (Configuration) objs.peek();

							if (!renderer.isValid())
								throw new SAXException("RENDERER(" + renderer + ") under CONFIGURATION("
										+ configuration + ") is invalid");

							configuration.addRenderer(renderer);
						} else
							throw new SAXException("RENDERER(" + renderer + ") must follow CONFIGURATION");

						objs.push(renderer);
					} else if (tag.equals("root")) {
						Root root = new Root(attrs);
						String peek = tags.peek();

						if (peek.equals("configuration")) {
							Configuration configuration = (Configuration) objs.peek();

							if (!root.isValid())
								throw new SAXException("ROOT(" + root + ") under CONFIGURATION(" + configuration
										+ ") is invalid");

							configuration.addRoot(root);
						} else
							throw new SAXException("ROOT(" + root + ") must follow CONFIGURATION");

						objs.push(root);
					} else if (tag.equals("root-ref")) {
						RootRef rootRef = new RootRef(attrs);
						String peek = tags.peek();

						if (peek.equals("errorHandler")) {
							ErrorHandler errorHandler = (ErrorHandler) objs.peek();

							if (!rootRef.isValid())
								throw new SAXException("ROOT-REF(" + rootRef + ") under ERRORHANDLER(" + errorHandler
										+ ") is invalid");

							errorHandler.addRootRef(rootRef);
						} else
							throw new SAXException("ROOT-REF(" + rootRef + ") must follow ERRORHANDLER");

						objs.push(rootRef);
					} else if (tag.equals("config")) {
						config.init(attrs);
						objs.push(config);
					} else if (tag.equals(""))
						objs.push(tag);
					else
						throw new SAXException("unknown tag(" + tag + ") found");
				} catch (RuntimeException re) {
					SAXException saxe = new SAXException(re);

					saxe.setStackTrace(re.getStackTrace());
					throw saxe;
				}

				tags.push(tag);
			}

			public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
				objs.pop();
				tags.pop();
			}

			public void error(SAXParseException saxpe) throws SAXException {
				config.m_initException = saxpe;
				throw saxpe;
			}

			public void fatalError(SAXParseException saxpe) throws SAXException {
				config.m_initException = saxpe;
				throw saxpe;
			}

			public void parse(Config config) throws SAXException, IOException {
				this.config = config;

				this.config.init(null);
				this.objs.push(this.config);
				this.tags.push("config");

				super.parse(config.m_configXml);
			}

			public void warning(SAXParseException saxpe) throws SAXException {
				config.m_initException = saxpe;
				throw saxpe;
			}
		}
	}
}
