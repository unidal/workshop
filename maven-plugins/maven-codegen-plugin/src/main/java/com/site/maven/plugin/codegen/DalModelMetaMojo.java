package com.site.maven.plugin.codegen;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.site.codegen.meta.ModelMeta;

/**
 * DAL Metadata generator for Model
 * 
 * @goal dal-model-meta
 * @author Frankie Wu
 */
public class DalModelMetaMojo extends AbstractMojo {
	/**
	 * XML meta component
	 * 
	 * @component
	 * @required
	 * @readonly
	 */
	protected ModelMeta m_meta;

	/**
	 * Current project base directory
	 * 
	 * @parameter expression="${basedir}"
	 * @required
	 * @readonly
	 */
	protected File baseDir;

	/**
	 * @parameter expression="${inputFile}"
	 * @required
	 */
	protected String inputFile;

	/**
	 * @parameter expression="${outputFile}"
	 *            default-value="src/main/resources/META-INF/dal/model/codegen.xml"
	 * @required
	 */
	protected String outputFile;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			File inFile = getFile(inputFile);
			Element root = m_meta.getMeta(new FileReader(inFile));
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			File outFile = getFile(outputFile);

			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdirs();
			}

			outputter.output(root, new FileWriter(outFile));
			getLog().info("File " + outFile.getCanonicalPath() + " generated.");
		} catch (Exception e) {
			throw new MojoExecutionException("Error when generating XML meta: "
					+ e, e);
		}
	}

	private File getFile(String path) {
		File file;

		if (path.startsWith("/") || path.indexOf(':') > 0) {
			file = new File(path);
		} else {
			file = new File(baseDir, path);
		}

		return file;
	}
}
