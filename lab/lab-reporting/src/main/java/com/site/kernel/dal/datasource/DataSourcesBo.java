package com.site.kernel.dal.datasource;

import java.util.List;

public class DataSourcesBo extends DataSourcesDo {

	static {
		init();
	}

	public void addDataSourceBo(DataSourceBo dataSource) {
		addDataSourceDo(dataSource);
	}

	public List getDataSourceBos() {
		return getDataSourceDos();
	}

	public void initDataSources() {
		List bos = getDataSourceBos();

		for (int i = 0; i < bos.size(); i++) {
			DataSourceBo bo = (DataSourceBo) bos.get(i);

			if (bo.isEnabled()) {
				bo.initDataSource();
			}
		}
	}

}
