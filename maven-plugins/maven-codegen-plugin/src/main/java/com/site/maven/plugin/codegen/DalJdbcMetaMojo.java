package com.site.maven.plugin.codegen;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.site.codegen.meta.TableMeta;

/**
 * DAL Metadata generator for JDBC
 * 
 * @goal dal-jdbc-meta
 * @author Frankie Wu
 */
public class DalJdbcMetaMojo extends AbstractMojo {
	/**
	 * Table meta component
	 * 
	 * @component
	 * @required
	 * @readonly
	 */
	protected TableMeta m_meta;

	/**
	 * Current project base directory
	 * 
	 * @parameter expression="${basedir}"
	 * @required
	 * @readonly
	 */
	protected File baseDir;

	/**
	 * @parameter expression="${jdbc.driver}"
	 * @required
	 */
	protected String driver;

	/**
	 * @parameter expression="${jdbc.url}"
	 * @required
	 */
	protected String url;

	/**
	 * @parameter expression="${jdbc.user}"
	 * @required
	 */
	protected String user;

	/**
	 * @parameter expression="${jdbc.password}"
	 * @required
	 */
	protected String password;

	/**
	 * @parameter expression="${jdbc.connectionProperties}"
	 */
	protected String connectionProperties;

	/**
	 * @parameter
	 */
	protected List<String> includes;

	/**
	 * @parameter
	 */
	protected List<String> excludes;

	/**
	 * @parameter expression="${outputFile}"
	 *            default-value="src/main/resources/META-INF/dal/jdbc/codegen.xml"
	 * @required
	 */
	protected String outputFile;

	@SuppressWarnings("unchecked")
	private void resolveAliasConfliction(Element entities) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<Element> children = entities.getChildren("entity");

		for (Element entity : children) {
			String alias = entity.getAttributeValue("alias");
			Integer count = map.get(alias);

			if (count == null) {
				map.put(alias, 1);
			} else {
				count++;
				map.put(alias, count);
				entity.setAttribute("alias", alias + (count));
			}
		}
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		Connection conn = setupConnection();

		try {
			DatabaseMetaData meta = conn.getMetaData();
			List<String> tables = getTables(meta);

			Element entities = new Element("entities");

			for (String table : tables) {
				Element entity = m_meta.getMeta(meta, table);

				entities.addContent(entity);
			}

			resolveAliasConfliction(entities);

			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			File file;

			if (outputFile.startsWith("/") || outputFile.indexOf(':') > 0) {
				file = new File(outputFile);
			} else {
				file = new File(baseDir, outputFile);
			}

			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			outputter.output(entities, new FileWriter(file));
			getLog().info("File " + file.getCanonicalPath() + " generated.");
		} catch (Exception e) {
			throw new MojoExecutionException("Error when generating DAL meta: "
					+ e, e);
		}
	}

	private Connection setupConnection() throws MojoExecutionException {
		Properties info = new Properties();

		info.put("user", user);
		info.put("password", password);

		if (connectionProperties != null) {
			String[] pairs = connectionProperties.split(Pattern.quote("&"));

			for (String pair : pairs) {
				int pos = pair.indexOf('=');

				if (pos > 0) {
					info.put(pair.substring(0, pos), pair.substring(pos + 1));
				} else {
					getLog().warn("invalid property: " + pair + " ignored.");
				}
			}
		}

		try {
			Class.forName(driver);

			return DriverManager.getConnection(url, info);
		} catch (Exception e) {
			throw new MojoExecutionException("Can't get connection: " + e, e);
		}
	}

	private List<String> getTables(DatabaseMetaData meta) throws SQLException {
		List<String> tables = new ArrayList<String>();

		if (includes == null) {
			includes = new ArrayList<String>();

			ResultSet rs = meta.getTables(null, null, "%",
					new String[] { "TABLE" });

			while (rs.next()) {
				String table = rs.getString("TABLE_NAME");

				tables.add(table);
			}

			rs.close();
		} else {
			for (String include : includes) {
				ResultSet rs = meta.getTables(null, null, include,
						new String[] { "TABLE" });

				while (rs.next()) {
					String table = rs.getString("TABLE_NAME");

					if (!tables.contains(table)) {
						tables.add(table);
					}
				}

				rs.close();
			}
		}

		if (excludes != null) {
			for (String exclude : excludes) {
				int index = tables.indexOf(exclude);

				if (index >= 0) {
					tables.remove(index);
				}
			}
		}

		Collections.sort(tables);

		return tables;
	}
}
