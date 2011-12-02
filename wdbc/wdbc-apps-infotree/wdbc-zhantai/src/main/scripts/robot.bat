@echo off
rem JDK version 1.5 is required. However, it does not work on JDK 1.6.
rem Following system properties are supported by this robot:
rem    -Dcategory.zhantai     zhantai.com category id to fetch, ie. "31"
rem    -Dcategory.infotree    Infotree category id to store, ie. "212"
rem    -Ddays                 How many latest days of data to be fetched, default value is "3".
rem    -Dusername             Which username in www.xiaoxishu.com will be used for listing the message
rem    -Dpassword             Which password for the username will be used for listing the message
rem    -Daction               Which URL will message post to. Default value is: "http://www.xiaoxishu.com/XSpiderRobot"
rem
rem For example:
rem    java -Dcategory.zhantai=31 -Dcategory.infotree=212 -Ddays=5 -jar wdbc-zhantai.jar

java -Dcategory.zhantai=31 -Dcategory.infotree=212 -jar wdbc-zhantai.jar
