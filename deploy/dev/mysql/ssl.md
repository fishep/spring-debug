### ssl

```mysql
show global variables like '%ssl%';
show variables like 'datadir';

```

```shell
#Mon Sep 09 14:23:07 CST 2024 WARN: Establishing SSL connection without server's identity verification is not recommended. 
# According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. 
# For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. 
# You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.

# method 1
# verifyServerCertificate=false
# useSSL=false

# method 2
docker cp mysql:/var/lib/mysql/server-cert.pem ./

#keytool -import -trustcacerts -v -alias Mysql -file "C:\ProgramData\MySQL\MySQL Server 8.0\Data\ca.crt" -keystore "C:\Program Files\Java\jdk1.8.0_192\jre\lib\security\cacerts"
keytool -import -trustcacerts -v -alias MysqlCA -file ./server-cert.pem -keystore $env:java_home\lib\security\cacerts\mysql.ks -storepass changeit

keytool -import -trustcacerts -v -alias MysqlCA -noprompt -file ./server-cert.pem -keystore $JAVA_HOME\lib\security\cacerts -storepass changeit

keytool -list -v -keystore $env:java_home\lib\security\cacerts\mysql.ks -storepass changeit
keytool -list -v -keystore .keystore -storepass changeit

 -Djavax.net.ssl.trustStore=$env:java_home\lib\security\cacerts\mysql.ks -Djavax.net.ssl.trustStorePassword=changeit


#1: ObjectId: 2.5.29.19 Criticality=true
BasicConstraints:[
  CA:false
  PathLen: undefined
]



```

```shell
keytool -list -keystore $env:java_home\lib\security\cacerts -storepass changeit
keytool -import -alias mysqlca -file mysql-ca.crt -keystore $env:java_home\lib\security\cacerts


keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks
keytool -list -v -keystore jwt.jks
keytool -alias jwt -exportcert -keystore jwt.jks -file jwt.cer

keytool -import -trustcacerts -alias mysqlcajks -file mysql-ca.crt -keystore $env:java_home\lib\security\cacerts\mysql-ca.jks -storepass changeit
keytool -v -list -keystore $env:java_home\lib\security\cacerts\mysql-ca.jks -storepass changeit
```
