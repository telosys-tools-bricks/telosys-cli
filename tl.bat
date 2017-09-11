@echo off
rem  echo bat file name : %0
rem  echo bat file dir  : %~dp0
rem  echo arg1 : %1
rem  echo arg2 : %2
java -cp %~dp0/telosys-cli-3.0.0-004.jar org.telosys.tools.launcher.ApplicationLauncher %1 %2