@echo off
REM JDK version 1.5 is required. However, it does not work on JDK 1.6.
REM Following system properties are supported by this robot:
REM    -DcourseId            Course id. Default value is "14".
REM    -DoutputFile          Output file. Default value is "arch.xml".
REM    -Dpassword            Password. Default value is "#edc2wsx".
REM    -DloginId             Login id. Default value is "qwu".

java -jar wdbc-ebay-arch.jar
