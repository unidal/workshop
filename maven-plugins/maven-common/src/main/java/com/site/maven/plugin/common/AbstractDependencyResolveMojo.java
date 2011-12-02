package com.site.maven.plugin.common;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.ResolutionNode;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ExcludesArtifactFilter;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

public abstract class AbstractDependencyResolveMojo extends AbstractMojo {
   /**
    * Current project
    * 
    * @parameter expression="${project}"
    * @required
    * @readonly
    */
   protected MavenProject project;

   /**
    * Local maven repository.
    * 
    * @parameter expression="${localRepository}"
    * @required
    * @readonly
    */
   protected ArtifactRepository localRepository;

   /**
    * Artifact factory, needed to download source jars for inclusion in
    * classpath.
    * 
    * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
    * @required
    * @readonly
    */
   protected ArtifactFactory artifactFactory;

   /**
    * Artifact collector, needed to resolve dependencies.
    * 
    * @component role="org.apache.maven.artifact.resolver.ArtifactCollector"
    * @required
    * @readonly
    */
   protected ArtifactCollector artifactCollector;

   /**
    * @component role="org.apache.maven.artifact.metadata.ArtifactMetadataSource"
    *            hint="maven"
    */
   protected ArtifactMetadataSource artifactMetadataSource;

   /**
    * If the executed project is a reactor project, this will contains the full
    * list of projects in the reactor.
    * 
    * @parameter expression="${reactorProjects}"
    * @required
    * @readonly
    */
   protected List<MavenProject> reactorProjects;

   /**
    * Artifact resolver, needed to download source jars for inclusion in
    * classpath.
    * 
    * @component role="org.apache.maven.artifact.resolver.ArtifactResolver"
    * @required
    * @readonly
    */
   protected ArtifactResolver artifactResolver;

   @SuppressWarnings("unchecked")
   protected Map<String, Artifact> createManagedVersionMap(List<Dependency> dependencies, Set<Artifact> result)
         throws MojoExecutionException {
      Map<String, Artifact> map = new TreeMap<String, Artifact>();

      try {
         for (Dependency d : dependencies) {
            VersionRange versionRange = VersionRange.createFromVersionSpec(d.getVersion());
            String type = d.getType();
            String scope = d.getScope();
            String classifier = d.getClassifier();
            boolean isOptional = d.isOptional();
            List<Exclusion> exclusions = d.getExclusions();

            if (type == null) {
               type = "jar";
            }

            if (scope == null) {
               scope = Artifact.SCOPE_COMPILE;
            }

            Artifact artifact = artifactFactory.createDependencyArtifact(d.getGroupId(), d.getArtifactId(), versionRange, type,
                  classifier, scope, null, isOptional);
            List<String> list = new ArrayList<String>();

            for (Exclusion e : exclusions) {
               list.add(e.getGroupId() + ":" + e.getArtifactId());
            }

            if (Artifact.SCOPE_SYSTEM.equals(scope)) {
               artifact.setFile(new File(d.getSystemPath()));
            }

            artifact.setDependencyFilter(new ExcludesArtifactFilter(list));

            result.add(artifact);
            map.put(d.getManagementKey(), artifact);
         }
      } catch (InvalidVersionSpecificationException e) {
         throw new MojoExecutionException("Unable to parser version: " + e, e);
      }

      return map;
   }

   protected MavenProject findReactorProject(String groupId, String artifactId, String version) {
      if (reactorProjects != null) {
         for (MavenProject p : reactorProjects) {
            if (p.getGroupId().equals(groupId) && p.getArtifactId().equals(artifactId) && p.getVersion().equals(version)) {
               return p;
            }
         }
      }

      return null;
   }

   @SuppressWarnings("unchecked")
   protected ClassLoader getClassLoader(ArtifactFilter filter) throws MojoExecutionException {
      List<Artifact> artifacts = new ArrayList<Artifact>();

      artifacts.add(project.getArtifact());
      resolveDependencies(project.getDependencies(), artifacts, filter);

      return makeClassLoader(artifacts);
   }

   protected ClassLoader getCompileClassLoader() throws MojoExecutionException {
      return getClassLoader(DependencyFilter.NO_TEST);
   }

   protected ClassLoader getTestClassLoader() throws MojoExecutionException {
      return getClassLoader(null);
   }

   protected boolean isReactorProject(Artifact artifact) {
      return findReactorProject(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion()) != null;
   }

   protected ClassLoader makeClassLoader(List<Artifact> artifacts) throws MojoExecutionException {
      List<URL> urls = new ArrayList<URL>(artifacts.size());

      try {
         for (Artifact artifact : artifacts) {
            if (artifact != null) {
               File file = artifact.getFile();

               if (file != null) {
                  urls.add(file.toURI().toURL());
               } else {
                  getLog().warn(String.format("Can't resolve artifact(%s)!", artifact));
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return new URLClassLoader(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
   }

   protected void resolveDependencies(Dependency dependency, List<Artifact> result, ArtifactFilter filter)
         throws MojoExecutionException {
      resolveDependencies(Arrays.asList(dependency), result, filter);
   }

   @SuppressWarnings("unchecked")
   protected void resolveDependencies(List<Dependency> dependencies, List<Artifact> result, ArtifactFilter filter)
         throws MojoExecutionException {
      List<Dependency> externals = new ArrayList<Dependency>();

      for (Dependency d : dependencies) {
         MavenProject mavenProject = findReactorProject(d.getGroupId(), d.getArtifactId(), d.getVersion());

         if (mavenProject != null) {
            Artifact artifact = mavenProject.getArtifact();
            List<Artifact> attachedArtifacts = mavenProject.getAttachedArtifacts();

            if (artifact.getFile() == null) {
               artifact.setFile(new File(mavenProject.getBuild().getOutputDirectory()));
            }

            if (filter == null || filter.include(artifact) && !result.contains(artifact)) {
               result.add(artifact);
            }

            for (Artifact a : attachedArtifacts) {
               if (filter == null || filter.include(a) && !result.contains(a)) {
                  result.add(a);
               }
            }

            resolveDependencies(mavenProject.getDependencies(), result, filter);
         } else {
            externals.add(d);
         }
      }

      Set<Artifact> artifacts = new HashSet<Artifact>();
      Map<String, Artifact> managedVersionMap = createManagedVersionMap(externals, artifacts);

      try {
         ArtifactResolutionResult artifactResolutionResult = artifactCollector.collect(artifacts, project.getArtifact(),
               managedVersionMap, localRepository, project.getRemoteArtifactRepositories(), artifactMetadataSource, null,
               new ArrayList<Object>());
         Set<ResolutionNode> nodes = artifactResolutionResult.getArtifactResolutionNodes();

         for (ResolutionNode node : nodes) {
            Artifact artifact = node.getArtifact();

            if (artifact.getFile() == null) {
               artifactResolver.resolve(artifact, node.getRemoteRepositories(), localRepository);
            }

            if (filter == null || filter.include(artifact) && !result.contains(artifact)) {
               result.add(artifact);
            }
         }
      } catch (ArtifactResolutionException e) {
         throw new MojoExecutionException("Unable to resolve artifact: " + e, e);
      } catch (ArtifactNotFoundException e) {
         throw new MojoExecutionException("Artifact not found: " + e, e);
      }
   }

   protected enum DependencyFilter implements ArtifactFilter {
      NO_TEST {
         @Override
         public boolean include(Artifact artifact) {
            String scope = artifact.getScope();

            // exclude test scope
            return !"test".equals(scope);
         }
      };
   }
}
