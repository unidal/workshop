#JDK version 1.5 is required. However, it does not work on JDK 1.6.
#Following system properties are supported by this robot:
#   -Dcategory             Category id to be fetched, ie. "lady"
#   -Dcategory.infotree    Corresponding category id of InfoTree to store data, ie. "349"
#   -DmaxDays              How many days of data to be fetched. Default value is "3".
#   -DmaxPages             How many pages of data to be crawled. Default value is "2".
#   -Dcity                 City code in the URL, ie. "sh", "bj". Default value is "sh"
#   -Dusername             Which username in www.xiaoxishu.com will be used for listing the message
#   -Dpassword             Which password for the username will be used for listing the message
#   -Daction               Which URL will message post to. Default value is: "http://www.xiaoxishu.com/XSpiderRobot"
#
#For example:
#   java -Dcategory=lady -Dcity=sh -Dcategory.infotree=349 -DmaxDdays=5 -jar wdbc-taobao.jar

java -Dcity=sh -Dcategory=lady -Dcategory.infotree=349 -Dlink=http://list.taobao.com/browse/16/t-0----------------------g,zhh3viy----------3-------40-list-coefp-0-all-16.htm -jar wdbc-taobao.jar
