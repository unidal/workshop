#JDK version 1.5 is required. However, it does not work on JDK 1.6.
#Following system properties are supported by this robot:
#   -Dcategory             Category id to be fetched, ie. "2400"
#   -Dcategory.infotree    Corresponding category id of InfoTree to store data, ie. "126"
#   -DmaxDays              How many days of data to be fetched. Default value is "3".
#   -DmaxPages             How many pages of data to be crawled. Default value is "2".
#   -Dcity                 City code in the URL, ie. "sh", "bj". Default value is "0200"
#   -Dusername             Which username in www.xiaoxishu.com will be used for listing the message
#   -Dpassword             Which password for the username will be used for listing the message
#   -Daction               Which URL will message post to. Default value is: "http://www.xiaoxishu.com/XSpiderRobot"
#
#For example:
#   java -Dcategory=2400 -Dcity=0200 -Dcategory.infotree=126 -DmaxDdays=5 -jar wdbc-51job.jar

java -Dcity=0200 -Dcategory=2400 -Dcategory.infotree=126 -jar wdbc-51job.jar
