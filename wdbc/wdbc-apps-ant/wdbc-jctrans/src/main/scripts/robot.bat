@echo off
REM JDK version 1.5 is required. However, it does not work on JDK 1.6.
REM Following system properties are supported by this robot:
REM    -DdataDir             Where will the data be stored? Default value is "target".
REM    -DmaxPages            How many pages of data to be fetched. Default value is "2".

java -jar wdbc-jctrans.jar
