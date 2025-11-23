@echo off
chcp 65001 > nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
mvn clean compile exec:java -Dexec.mainClass="edu.aston.Main"
pause