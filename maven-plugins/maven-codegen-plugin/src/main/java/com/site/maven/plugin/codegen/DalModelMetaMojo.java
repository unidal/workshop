package com.site.maven.plugin.codegen;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jdom.Document;
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
	 * Current project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject m_project;

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
	 * @parameter expression="${outputDir}"
	 *            default-value="${basedir}/src/main/resources/META-INF/dal/model"
	 * @required
	 */
	protected String outputDir;

	/**
	 * @parameter expression="${packageName}"
	 */
	protected String packageName;

	private String detectPackageName() {
		if (packageName != null) {
			return packageName;
		}

		String groupId = m_project.getGroupId();
		String artifactId = m_project.getArtifactId();

		return (groupId + "." + artifactId + ".model").replace('-', '.');
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			File inFile = getFile(inputFile);
			Document codegen = m_meta.getCodegen(new FileReader(inFile));
			File outDir = getFile(outputDir);
			File outFile = new File(outDir, "codegen.xml");

			if (!outDir.exists()) {
				outDir.mkdirs();
			}

			saveFile(codegen, outFile);

			File modelFile = new File(outDir, "model.xml");

			if (!modelFile.exists()) {
				Document model = m_meta.getModel(detectPackageName());

				saveFile(model, modelFile);
			}

			File manifestFile = new File(outDir, "manifest.xml");

			if (!manifestFile.exists()) {
				Document manifest = m_meta.getManifest(outFile.getName(), modelFile.getName());

				saveFile(manifest, manifestFile);
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Error when generating model meta: " + e, e);
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

	private void saveFile(Document codegen, File file) throws IOException {
		Format format = Format.getPrettyFormat();
		XMLOutputter outputter = new XMLOutputter(format);
		FileWriter writer = new FileWriter(file);

		try {
			outputter.output(codegen, writer);
			getLog().info("File " + file.getCanonicalPath() + " generated.");
		} finally {
			writer.close();
		}
	}
}
