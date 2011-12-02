package com.site.kernel.dal.db;

import java.util.Map;

/**
 * @author qwu
 */
public interface TableProvider {
   /**
    * get logical table name, usually, it's constant
    * 
    * It'll be used when register this table provider
    */
   String getLogicalTableName();

   /**
    * get logical data source name by hints
    * 
    * This method will be called before getPhysicalTableName(hints)
    * So fail-over logical can be implemented at this method
    * 
    * new hint can be added for the next call to getPhysicalTableName(hints)
    */
   String getLogicalDataSource(Map hints);

   /**
    * get physical table name by hints
    * 
    * Normally, several common implementations could be here
    * 
    * 1) Mod-X pattern, by hint keyId % X
    * 2) Read/Write pattern, by hint "QUERY_TYPE"
    * 3) Segment pattner, by hint keyId range
    * 4) Logical by any hints
    */
   String getPhysicalTableName(Map hints);
}
