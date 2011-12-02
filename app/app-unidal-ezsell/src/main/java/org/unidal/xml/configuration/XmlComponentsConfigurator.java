package org.unidal.xml.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.xml.XmlCodeGenerator;
import org.unidal.xml.XmlModule;
import org.unidal.xml.view.XmlJspViewer;
import org.unidal.xml.view.XmlViewer;

import com.site.codegen.generator.Generator;
import com.site.codegen.manifest.ManifestCreator;
import com.site.codegen.meta.XmlMetaHelper;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class XmlComponentsConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all
            .add(C(XmlCodeGenerator.class).req(ManifestCreator.class, XmlMetaHelper.class).req(Generator.class,
                  "dal-xml"));
      all.add(C(XmlViewer.class, XmlJspViewer.class));
      all.add(C(XmlModule.class).req(XmlCodeGenerator.class, XmlViewer.class));

      return all;
   }
}
