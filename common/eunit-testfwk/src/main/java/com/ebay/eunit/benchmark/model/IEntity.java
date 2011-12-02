package com.ebay.eunit.benchmark.model;

public interface IEntity<T> {
   public void accept(IVisitor visitor);

   public abstract void mergeAttributes(T other);

}
