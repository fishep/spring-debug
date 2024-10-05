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
# useSSL=true

# method 2
docker cp mysql:/var/lib/mysql/ca.pem ./
docker cp mysql:/var/lib/mysql/server-cert.pem ./


keytool -importcert -alias MySQLCACert -file ca.pem -keystore truststore -storepass changeit
keytool -list -keystore truststore

keytool -import -trustcacerts -v -alias MysqlCA -file ca.pem -keystore $env:java_home\lib\security\cacerts\mysql.ks -storepass changeit
keytool -list -v -keystore $env:java_home\lib\security\cacerts\mysql.ks -storepass changeit
-Djavax.net.ssl.trustStore=$env:java_home\lib\security\cacerts\mysql.ks -Djavax.net.ssl.trustStorePassword=changeit
-Djavax.net.ssl.trustStore=E:\java\jdk\home\lib\security\cacerts\mysql.ks -Djavax.net.ssl.trustStorePassword=changeit

"jdbc:mysql://mysql.dev:3306/demo?verifyServerCertificate=true&useSSL=true&requireSSL=true"

#keytool -import -trustcacerts -v -alias Mysql -file "C:\ProgramData\MySQL\MySQL Server 8.0\Data\ca.crt" -keystore "C:\Program Files\Java\jdk1.8.0_192\jre\lib\security\cacerts"
keytool -import -trustcacerts -v -alias MysqlCA -file ./server-cert.pem -keystore $env:java_home\lib\security\cacerts\mysql.ks -storepass changeit

keytool -import -trustcacerts -v -alias MysqlCA -noprompt -file ./server-cert.pem -keystore $JAVA_HOME\lib\security\cacerts -storepass changeit

keytool -list -v -keystore $env:java_home\lib\security\cacerts\mysql.ks -storepass changeit
keytool -list -v -keystore .keystore -storepass changeit




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



//生成一个 CA 私钥
openssl genrsa 2048 > cert/ca-key.pem
//使用私钥生成一个新的数字证书,执行这个命令时, 会需要填写一些问题, 随便填写就可以了
openssl req -sha1 -new -x509 -nodes -days 3650 -key ./cert/ca-key.pem > cert/ca-cert.pem
//创建服务器端RSA 私钥和数字证书,这个命令会生成一个新的私钥(server-key.pem), 同时会使用这个新私钥来生成一个证书请求文件(server-req.pem).
//这个命令同样需要回答几个问题, 随便填写即可. 不过需要注意的是, A challenge password 这一项需要为空
openssl req -sha1 -newkey rsa:2048 -days 3650 -nodes -keyout cert/server-key.pem > cert/server-req.pem
//将生成的私钥转换为 RSA 私钥文件格式
openssl rsa -in cert/server-key.pem -out cert/server-key.pem
//使用原先生成的 CA 证书来生成一个服务器端的数字证书
openssl x509 -sha1 -req -in cert/server-req.pem -days 3650 -CA cert/ca-cert.pem -CAkey cert/ca-key.pem -set_serial 01 > cert/server-cert.pem
//创建客户端的 RSA 私钥和数字证书
openssl req -sha1 -newkey rsa:2048 -days 3650 -nodes -keyout cert/client-key.pem > cert/client-req.pem
//将生成的私钥转换为 RSA 私钥文件格式
openssl rsa -in cert/client-key.pem -out cert/client-key.pem
//为客户端创建一个数字证书
openssl x509 -sha1 -req -in cert/client-req.pem -days 3650 -CA cert/ca-cert.pem -CAkey cert/ca-key.pem -set_serial 01 > cert/client-cert.pem


keytool -importcert -alias MySQLCACert -file ca-cert.pem -keystore truststore -storepass 密码
keytool -list -keystore mysql.ks


