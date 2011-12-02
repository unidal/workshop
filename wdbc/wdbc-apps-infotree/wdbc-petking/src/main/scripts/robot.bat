@echo off
rem JDK version 1.5 is required. However, it does not work on JDK 1.6.
rem Following system properties are supported by this robot:
rem    -Dcategory.petking     petking.cn category id to fetch, ie. "2"
rem    -Dcategory.infotree    Infotree category id to store, ie. "256"
rem    -Ddays                 How many latest days of data to be fetched, default value is "3".
rem    -Dusername             Which username in www.xiaoxishu.com will be used for listing the message
rem    -Dpassword             Which password for the username will be used for listing the message
rem    -Daction               Which URL will message post to. Default value is: "http://www.xiaoxishu.com/XSpiderRobot"
rem
rem For example:
rem    java -Dcategory.petking=2 -Dcity=sh -Dcategory.infotree=256 -Ddays=5 -jar wdbc-petking.jar

java -Dcategory.petking=2 -Dcategory.infotree=256 -jar wdbc-petking.jar
