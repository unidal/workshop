package com.site.lookup;

import java.util.List;

import org.codehaus.plexus.PlexusContainer;
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

	protected <T> T lookup(Class<T> role) throws LookupException {
		return lookup(role, null);
	}

	protected <T> T lookup(Class<T> role, String roleHint) throws LookupException {
		try {
			return (T) m_container.lookup(role, roleHint == null ? "default" : roleHint.toString());
		} catch (ComponentLookupException e) {
			throw new LookupException("Component(" + role.getName() + ":"
			      + (roleHint == null ? "default" : roleHint.toString()) + ") lookup failure. details: " + e.getMessage(),
			      e);
		}
	}

	@SuppressWarnings("unchecked")
   protected <T> List<T> lookupList(Class<T> role) throws LookupException {
		try {
			return (List<T>) m_container.lookupList(role.getName());
		} catch (ComponentLookupException e) {
			throw new LookupException("Component list(" + role.getName() + ") lookup failure. details: " + e.getMessage(),
			      e);
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
