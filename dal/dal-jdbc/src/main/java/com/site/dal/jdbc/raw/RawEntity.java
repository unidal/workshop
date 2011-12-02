package com.site.dal.jdbc.raw;

import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.dal.jdbc.annotation.Entity;

@Entity(logicalName = "raw", alias = "raw")
public class RawEntity {
   public static final Readset<RawDataObject> READSET_FULL = new Readset<RawDataObject>(new DataField[0]);

   public static final Updateset<RawDataObject> UPDATESET_FULL = new Updateset<RawDataObject>(new DataField[0]);
}