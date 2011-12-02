package com.site.app.tracking.counter;

import java.io.IOException;
import java.io.OutputStream;

public interface ImageGenerator {
   public void generate(OutputStream out, int value) throws IOException;
}
