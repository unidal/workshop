package com.site.dal.jdbc.entity;

public interface EntityInfoManager {

	public void register(Class<?> entityClass);

	public EntityInfo getEntityInfo(Class<?> entityClass);

	public EntityInfo getEntityInfo(String logicalName);

}