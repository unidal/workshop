package com.site.maven.plugin.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.TypeArtifactFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.site.maven.plugin.common.AbstractDependencyResolveMojo;

/**
 * Create the compile classpath and put it to the maven properties for other
 * plugins(such as java2wsdl) usage.
 * 
 * @goal create-classpath
 */
public class CreateClasspathMojo extends AbstractDependencyResolveMojo {
	/**
	 * Property name to which the created classpath will be stored
	 * 
	 * @parameter expression="${propertyName}"
	 *            default-value="site.compile.classpath"
	 */
	private String propertyName;

	/**
	 * No Jar library is included
	 * 
	 * @parameter default-value="false"
	 */
	boolean noJar;

	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (project.getPackaging().equals("pom")) {
			getLog().info("Ignored for POM project");

			return;
		}

		List<Artifact> artifacts = new ArrayList<Artifact>();
		ArtifactFilter filter = new TypeArtifactFilter("jar") {
			@Override
			public boolean include(Artifact artifact) {
				if (noJar && artifact.getFile().isFile()) {
					return false;
				}

				return super.include(artifact);
			}
		};

		resolveDependencies(project.getDependencies(), artifacts, filter);

		StringBuilder sb = new StringBuilder();

		sb.append(project.getBuild().getOutputDirectory());

		for (Artifact artifact : artifacts) {
			sb.append(File.pathSeparatorChar);
			sb.append(artifact.getFile().getAbsolutePath());
		}

		if (sb.length() > 0) {
			String cp = sb.toString();
			Properties properties = project.getProperties();

			properties.put(propertyName, cp);
			getLog().info("Set property " + propertyName);
			getLog().debug("Set property: " + propertyName + "=" + cp);
		}
	}
}
