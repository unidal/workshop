package com.site.web.test.book;

import java.io.IOException;

import com.site.web.mvc.Validator;

public class PermissionValidator implements Validator<BookContext> {
   public void validate(BookContext ctx) throws IOException {
      ctx.write("==>permission");
   }
}
