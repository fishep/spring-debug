```shell
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.math=ALL-UNNAMED
--add-opens java.base/sun.net.util=ALL-UNNAMED

JVM_OPTS= 
--add-exports=java.naming/com.sun.jndi.ldap=ALL-UNNAMED 
--add-opens=java.base/java.lang=ALL-UNNAMED 
--add-opens=java.base/java.lang.invoke=ALL-UNNAMED 
--add-opens=java.base/java.io=ALL-UNNAMED 
--add-opens=java.base/java.security=ALL-UNNAMED 
--add-opens=java.base/java.util=ALL-UNNAMED 
--add-opens=java.management/javax.management=ALL-UNNAMED 
--add-opens=java.naming/javax.naming=ALL-UNNAMED
```

```shell

# windows set java8 environment, via environment variable or absolute path
set JAVA_HOME=%JAVA8_HOME%
set JAVA_HOME=E:\java\jdk\corretto-1.8.0_342
set PATH=%JAVA_HOME%\bin;%PATH%

# linux or mac set java8 environment, via environment variable or absolute path
export JAVA_HOME=$JAVA8_HOME
export JAVA_HOME=/e/java/jdk/corretto-1.8.0_342
export PATH="$JAVA_HOME/bin:$PATH"

```