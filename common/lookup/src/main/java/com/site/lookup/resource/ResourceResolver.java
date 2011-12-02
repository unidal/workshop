package com.site.lookup.resource;

import java.io.IOException;
import java.net.URL;

public interface ResourceResolver {
   public URL resolve(String relativePath) throws IOException;
}
