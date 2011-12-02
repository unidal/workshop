package org.unidal.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.expense.dal.MemberDao;
import org.unidal.expense.dal.TripDao;
import org.unidal.expense.dal._INDEX;
import org.unidal.expense.dal.cache.CacheManager;
import org.unidal.expense.dal.cache.MemberDaoCache;
import org.unidal.expense.dal.cache.SimpleCacheManager;
import org.unidal.expense.dal.cache.TripDaoCache;

import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import com.site.lookup.configuration.Component;

final class DatabaseConfigurator extends AbstractJdbcResourceConfigurator {
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(defineJdbcDataSourceConfigurationManagerComponent("datasource.xml"));
		all.add(defineJdbcDataSourceComponent("jdbc-unidal", "${jdbc.driver}", "${jdbc.url.unidal}", "${jdbc.user}",
				"${jdbc.password}", "<![CDATA[${jdbc.connectionProperties}]]>"));

		defineSimpleTableProviderComponents(all, "jdbc-unidal", _INDEX.getEntityClasses());

		all.add(C(CacheManager.class, "simple", SimpleCacheManager.class).is(PER_LOOKUP));
		all.add(C(TripDao.class, TripDaoCache.class).req(QueryEngine.class).req(CacheManager.class, "simple"));
		all.add(C(MemberDao.class, MemberDaoCache.class).req(QueryEngine.class).req(CacheManager.class, "simple"));

		return all;
	}
}