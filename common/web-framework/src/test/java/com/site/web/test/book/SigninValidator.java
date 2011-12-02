package com.site.web.test.book;

import com.site.web.mvc.Validator;

public class SigninValidator implements Validator<BookContext> {
   public void validate(BookContext ctx) throws Exception {
      ctx.write("==>signin");
   }
}
