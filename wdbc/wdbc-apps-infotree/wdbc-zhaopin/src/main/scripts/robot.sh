# JDK version 1.5 is required. However, it does not work on JDK 1.6.
# Following system properties are supported by this robot:
#    -Durl                 Search URL for the job list Default value is "http://search.zhaopin.com/jobs/request.asp?SchJobType=&subJobtype=&industry=%E8%AE%A1%E7%AE%97%E6%9C%BA%E8%BD%AF%E4%BB%B6&PublishDate=&WorkingExp=&EduLevel=&CompanyType=&CompanySize=&sortby=&SearchModel=0&vip_type=&JobLocation=%E4%B8%8A%E6%B5%B7&KeyWord=&DYWE=1225798083213.401123.1225803852.1225808731.3".
#    -Dcategory            Category id to be fetched from. Default value is "0".
#    -Dcategory.infotree   Which category id of InfoTree.com to store data to. Default value is "349".
#    -Dcity                City code in the URL. Default value is "shanghai".
#    -DmaxDays             How many days of data to be fetched. Default value is "3".
#    -DmaxPages            How many pages of data to be fetched. Default value is "2".
#    -Daction              Which URL will message post to. Default value is "http://www.xiaoxishu.com/XSpiderRobot".
#    -Dpassword            Which password will be used to list message. Default value is "a123456".
#    -Dusername            Which username will be used to list message. Default value is "perry111701".

java -Durl=http://search.zhaopin.com/jobs/request.asp?SchJobType=&subJobtype=&industry=%E8%AE%A1%E7%AE%97%E6%9C%BA%E8%BD%AF%E4%BB%B6&PublishDate=&WorkingExp=&EduLevel=&CompanyType=&CompanySize=&sortby=&SearchModel=0&vip_type=&JobLocation=%E4%B8%8A%E6%B5%B7&KeyWord=&DYWE=1225798083213.401123.1225803852.1225808731.3 -Dcategory=0 -Dcategory.infotree=349 -Dcity=shanghai -jar wdbc-baixing.jar
