@echo off

mkdir temporary

set "FRAMEWORK_DIR=D:\Framework\framework"
set "TEST_FRAMEWORK_DIR=D:\Framework\test-framework"
set "TEMPORARY_DIR=D:\Framework\temporary"

cd "%TEMPORARY_DIR%"

mkdir WEB_INF

set "WEB_INF_TEMP_DIR=%TEMPORARY_DIR%\WEB_INF"

cd "%WEB_INF_TEMP_DIR%"

mkdir lib classes

cd ..

cd "%FRAMEWORK_DIR%"

javac -d . *.java

jar -cf framework.jar *

cd ..

copy "%FRAMEWORK_DIR%\framework.jar"  "%WEB_INF_TEMP_DIR%\lib\"

set "CLASSES_TEST_FRAMEWORK_DIR=%TEST_FRAMEWORK_DIR%\WEB-INF\classes\etu2038\framework\entity\"

copy "%CLASSES_TEST_FRAMEWORK_DIR%\Emp.class" "%WEB_INF_TEMP_DIR%\classes"

copy "%TEST_FRAMEWORK_DIR%\*.jsp" "%TEMPORARY_DIR%"

cd "%TEMPORARY_DIR%"

set "WAR_TEMP_FILE=%TEMPORARY_DIR%\frameworkTemporary.war"

jar -cvf "%WAR_TEMP_FILE%" .

echo %WAR_TEMP_FILE%

copy "%WAR_TEMP_FILE%" "C:\Program Files\Apache Software Foundation\apache-tomcat-8.5.87\webapps"
