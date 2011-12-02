package com.site.maven.plugin.codegen.dbconfig;

public interface IEntity<T> {
   public void accept(IVisitor visitor);

   public void mergeAttributes(T other);

}
