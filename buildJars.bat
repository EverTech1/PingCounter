::Modify JAVA_HOME according to your JKD location
set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_202"
set PATH=%JAVA_HOME%\bin;%PATH%
call gradlew -b build.gradle reobfCurseforge
call gradlew -b build2.gradle reobfModrinth