@echo off
xcopy /M /F src\main\webapp\default.css target\bes\
xcopy /M /F src\main\webapp\jsp\* target\bes\jsp\
xcopy /M /F src\main\webapp\WEB-INF\tags\* target\bes\WEB-INF\tags\
