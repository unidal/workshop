package com.ebay.eunit.mock.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class ServletOutputStreamMock extends ServletOutputStream {
   private final OutputStream m_outputStream;

   public ServletOutputStreamMock(OutputStream outputStream) {
      m_outputStream = outputStream;
   }

   public void write(int b) throws IOException {
      m_outputStream.write(b);
   }
}
