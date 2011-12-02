@echo off
REM JDK version 1.5 is required(http://java.sun.com/javase/downloads/index_jdk5.jsp). However, it may not work on JDK 1.6.
REM Following system properties are supported by this robot:
REM    -Dserver              Server are you connecting to. Default value is "x17.sanguo.xiaonei.com".
REM    -Dlogin.email         Email to login xiaonei.com. Default value is "".
REM    -Dlogin.password      Password to login xiaonei.com. Default value is "".
REM    -Dfight.diameter      Fight diameter. Default value is "0".
REM    -Dfight.minCount      Min fight count. Default value is "30".
REM    -Dfight.maxCount      Max fight count. Default value is "30".
REM    -Dfight.maxPopulation Max fight population. Default value is "50".
REM    -Dfight.villages      Fight villages list in format of x1:y1,x2:y2 Default value is "".
REM    -Dfight.noEmperors    No fight to emperors in format of emperor1,emperor2,... Default value is "".

java -Dlogin.email=<account@email.com> -Dlogin.password=<password> -jar sanguo.jar 
