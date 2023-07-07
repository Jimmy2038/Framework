@echo off
set "FRAME_DIR=D:\projetFramework\"
set "FRONT_DIR=D:\projetFramework\Front"
set "TESTFRONT_DIR=D:\projetFramework\testFront"
set "TOMCAT=C:\Program Files\Apache Software Foundation\apache-tomcat-8.5.87\webapps"

cd "%TESTFRONT_DIR%\web\WEB-INF"

mkdir lib 

cd "%FRONT_DIR%\src\java\etu2038\framework"

javac -d . *.java

cd "%FRONT_DIR%\src\java\etu2038\framework"

jar -cvf %FRAME_DIR%Front.jar .

cd %FRAME_DIR%

copy Front.jar "%TESTFRONT_DIR%\web\WEB-INF\lib"

cd "%TESTFRONT_DIR%\src\java"

javac -cp "%TESTFRONT_DIR%\web\WEB-INF\lib\Front.jar" -d "%TESTFRONT_DIR%\web\WEB-INF\classes" *.java 

cd "%TESTFRONT_DIR%\web"

jar -cfv Front.war .
del "%TOMCAT%\"Front.war
copy Front.war "%TOMCAT%"