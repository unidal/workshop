@echo off
xcopy /M /F src\main\webapp\default.css target\t\
xcopy /M /F src\main\webapp\jsp\* target\t\jsp\
xcopy /M /F src\main\webapp\WEB-INF\tags\* target\t\WEB-INF\tags\
