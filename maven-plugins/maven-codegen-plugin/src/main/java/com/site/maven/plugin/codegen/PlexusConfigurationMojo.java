package com.site.maven.plugin.codegen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.site.helper.Reflects;

/**
 * Code generator for Plexus dependency injection descriptor
 * 
 * @goal plexus
 * @author Frankie Wu
 */
public class PlexusConfigurationMojo extends AbstractMojo {
	/**
	 * Current project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject m_project;

	/**
	 * Configurator class name.
	 * 
	 * @parameter expression="${configurator}"
	 * @required
	 */
	protected String configurator;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			
			Class<?> configuratorClass = Reflects.forClass().getClass(configurator);

			if (configuratorClass == null) {
				throw new MojoExecutionException("Configurator class is not found: " + configurator);
			}

			Reflects.forMethod().invokeStaticMethod(configuratorClass, "main");
		} catch (MojoExecutionException e) {
			throw e;
		} catch (Exception e) {
			throw new MojoExecutionException("Code generating failed.", e);
		}
	}
}
