# JDK version 1.5 is required. However, it does not work on JDK 1.6.
# Following system properties are supported by this robot:
#    -Dserver              Server are you connecting to. Default value is "x17.sanguo.xiaonei.com".
#    -Dlogin.url           URL to login xiaonei.com. Default value is "http://login.xiaonei.com/Login.do".
#    -Dlogin.email         Email to login xiaonei.com. Default value is "".
#    -Dlogin.password      Password to login xiaonei.com. Default value is "".
#    -Dfight.diameter      Fight diameter. Default value is "0".
#    -Dfight.minCount      Min fight count. Default value is "30".
#    -Dfight.maxCount      Max fight count. Default value is "30".
#    -Dfight.maxPopulation Max fight population. Default value is "50".
#    -Dfight.villages      Fight villages list in format of x1:y1,x2:y2 Default value is "".
#    -Dfight.noEmperors    No fight to emperors in format of emperor1,emperor2,... Default value is "".
#    -Dbuilding.plan       Customized building plan. i.e. village1=>build1:level1,build2:level2,... one line for each village Default value is "".

java -jar sanguo.jar
