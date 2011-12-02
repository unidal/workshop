@echo off
rem JDK version 1.5 is required. However, it does not work on JDK 1.6.
rem Following system properties are supported by this robot:
rem    -Dcategory.kijiji      KIJIJI category id to fetch, ie. 2104020
rem    -Dcategory.infotree    Infotree category id to store, ie. 349
rem    -Ddays                 How many latest days of data to be fetched, default value is "3".
rem    -Dtype                 What type of data. available types: active, good. default value is "good"
rem    -Dusername             Which username in www.xiaoxishu.com will be used for listing the message
rem    -Dpassword             Which password for the username will be used for listing the message
rem    -Daction               Which URL will message post to. Default value is: "http://www.xiaoxishu.com/XSpiderRobot"
rem
rem For example:
rem    java -Dcategory.kijiji=2104020 -Dcategory.infotree=349 -Ddays=5 -jar wdbc-kijiji.jar
rem
java -Dcategory.kijiji=2104020 -Dcategory.infotree=349 -jar wdbc-kijiji.jar
