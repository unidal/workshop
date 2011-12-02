@echo off
rem JDK version 1.5 is required. However, it does not work on JDK 1.6.
rem Following system properties are supported by this robot:
rem    -Dcategory             Category id to fetch, ie. "2400"
rem    -Dcategory.infotree    Infotree category id to store, ie. "349"
rem    -Ddays                 How many latest days of data to be fetched, default value is "3".
rem    -Dusername             Which username in www.xiaoxishu.com will be used for listing the message
rem    -Dpassword             Which password for the username will be used for listing the message
rem    -Daction               Which URL will message post to. Default value is: "http://www.xiaoxishu.com/XSpiderRobot"
rem    -Dcity                 City code in the URL, ie. "0100", "0200". Default value is "0200".
rem
rem For example:
rem    java -Dcategory=2400 -Dcity=0200 -Dcategory.infotree=349 -Ddays=5 -jar wdbc-51job.jar

java -Dcategory=2400 -Dcategory.infotree=349 -jar wdbc-51job.jar
