@echo off
rem JDK version 1.5 is required. However, it does not work on JDK 1.6.
rem Following system properties are supported by this robot:
rem    -Dcategory.sosoko      sosoko.com category id to fetch, ie. "001_001"
rem    -Dcategory.infotree    Infotree category id to store, ie. "277"
rem    -Ddays                 How many latest days of data to be fetched, default value is "3".
rem    -Dusername             Which username in www.xiaoxishu.com will be used for listing the message
rem    -Dpassword             Which password for the username will be used for listing the message
rem    -Daction               Which URL will message post to. Default value is: "http://www.xiaoxishu.com/XSpiderRobot"
rem    -Dcity                 City code in the URL, ie. "sh", "bj". Default value is "sh".
rem
rem For example:
rem    java -Dcategory.sosoko=001_001 -Dcity=sh -Dcategory.infotree=277 -Ddays=5 -jar wdbc-sosoko.jar

java -Dcategory.sosoko=001_001 -Dcategory.infotree=277 -jar wdbc-sosoko.jar
