```shell
gradle clean build -i -x test --no-daemon -Dprofile=dev -b build.gradle processResources 
gradle clean build -i -x test --no-daemon -Dprofile=dev -b build.gradle 


./gradlew clean
./gradlew :back-gateway:clean
./gradlew :back-gateway:build -i -x test --no-daemon -Dprofile=dev -b build.gradle 
./gradlew :back-gateway:build -i -x test --no-daemon -Dprofile=dev -b build.gradle processResources

./gradlew :back-service:clean
./gradlew :back-service:build -i -x test --no-daemon -Dprofile=dev -b build.gradle processResources

./gradlew :microservice:sso-server:clean
./gradlew :microservice:sso-server:build -i -x test --no-daemon -Dprofile=dev -b build.gradle processResources

```