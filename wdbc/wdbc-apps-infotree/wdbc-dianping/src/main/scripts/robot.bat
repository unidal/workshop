@echo off
REM JDK version 1.5 is required. However, it does not work on JDK 1.6.
REM Following system properties are supported by this robot:
REM    -Dcategory            Category id to be fetched from. Default value is "c1".
REM    -Dcategory.infotree   Which category id of InfoTree.com to store data to. Default value is "349".
REM    -Dcity                City code in the URL. Default value is "1".
REM    -DmaxDays             How many days of data to be fetched. Default value is "3".
REM    -DmaxPages            How many pages of data to be fetched. Default value is "2".
REM    -Daction              Which URL will message post to. Default value is "http://www.xiaoxishu.com/XSpiderRobot".
REM    -Dpassword            Which password will be used to list message. Default value is "a123456".
REM    -Dusername            Which username will be used to list message. Default value is "perry111701".

java -Dcategory=c1 -Dcategory.infotree=349 -Dcity=1 -jar wdbc-dianping.jar
