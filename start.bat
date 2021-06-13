@echo off

set ADDRESS=localhost
set QUERY_PORT=69
set TCP_PORT=70
set UDP_PORT=71

java -jar build/libs/gmserver-1.0.1-SNAPSHOT-shaded.jar

pause