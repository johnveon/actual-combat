SET CLASSPATH=%~dp0..\build\classes;%~dp0..\..\lib\*;..\..\target\*;%CLASSPATH%

set mainclass=com.ctg.itrdc.NJedisTest2
echo on
java -cp "%CLASSPATH%" %mainclass% %*