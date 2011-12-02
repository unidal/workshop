@echo off
rem JDK version 1.5 is required. However, it does not work on JDK 1.6.
rem Following system properties are supported by this robot:
rem    -Dcategory             Category id to be fetched, ie. "lady"
rem    -Dcategory.infotree    Corresponding category id of InfoTree to store data, ie. "349"
rem    -DmaxDays              How many days of data to be fetched. Default value is "3".
rem    -DmaxPages             How many pages of data to be crawled. Default value is "2".
rem    -Dcity                 City code in the URL, ie. "sh", "bj". Default value is "sh"
rem    -Dusername             Which username in www.xiaoxishu.com will be used for listing the message
rem    -Dpassword             Which password for the username will be used for listing the message
rem    -Daction               Which URL will message post to. Default value is: "http://www.xiaoxishu.com/XSpiderRobot"
rem 
rem For example:
rem   java -Dcategory=lady -Dcity=sh -Dcategory.infotree=349 -DmaxDdays=5 -jar wdbc-eachnet.jar

java -Dcity=sh -Dcategory=lady -Dcategory.infotree=349 -Dlink=http://search2.eachnet.com/search/-3-103007-25|298-------------------------basicsearch.html -jar wdbc-eachnet.jar
