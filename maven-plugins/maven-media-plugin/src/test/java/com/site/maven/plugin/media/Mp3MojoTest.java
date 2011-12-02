package com.site.maven.plugin.media;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.site.maven.plugin.common.Injector;

import junit.framework.TestCase;

public class Mp3MojoTest extends TestCase {
	public void testDefault() throws MojoExecutionException, MojoFailureException {
		Mp3Mojo mojo = new Mp3Mojo();

		Injector.setField(mojo, "rootDirs", "d:/medias/mp3");
		
		// mojo.execute();
	}
}
