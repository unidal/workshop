package com.site.lookup;

import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.manager.ComponentManagerManager;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;

public abstract class ContainerHolder implements Contextualizable {
   private PlexusContainer m_container;

   public void contextualize(Context context) throws ContextException {
      m_container = (PlexusContainer) context.get("plexus");
   }

   protected <T> boolean hasComponent(Class<T> role) {
      return hasComponent(role, null);
   }

   protected <T> boolean hasComponent(Class<T> role, Object roleHint) {
      return m_container.hasComponent(role.getName(), roleHint == null ? "default" : roleHint.toString());
   }

   protected ComponentDescriptor getComponentDescriptor(Object component) {
      if (m_container instanceof DefaultPlexusContainer) {
         try {
            DefaultPlexusContainer container = (DefaultPlexusContainer) m_container;
            ComponentManagerManager cmm = container.getComponentManagerManager();

            return cmm.findComponentManagerByComponentInstance(component).getComponentDescriptor();
         } catch (Exception e) {
            // ignore it
         }
      }

      throw new RuntimeException("Component(" + component + ") is not found.");
   }

   protected <T> T lookup(Class<T> role) throws LookupException {
      return lookup(role, null);
   }

   @SuppressWarnings("unchecked")
   protected <T> T lookup(Class<T> role, String roleHint) throws LookupException {
      try {
         return (T) m_container.lookup(role, roleHint == null ? "default" : roleHint.toString());
      } catch (ComponentLookupException e) {
         throw new LookupException("Component(" + role.getName() + ") lookup failure. details: " + e.getMessage(), e);
      }
   }

   protected void release(Object component) throws LookupException {
      if (component != null) {
         try {
            m_container.release(component);
         } catch (ComponentLifecycleException e) {
            throw new LookupException("Can't release component: " + component, e);
         }
      }
   }
}
