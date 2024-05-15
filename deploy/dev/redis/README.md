# redis

```shell

#跟随重定向
redis-cli -c 

for i in {0..5}; do ping -c 1 redis-$i; done | grep PING | sed -r "s/PING redis-[0-9] \((.*)\): 56 data bytes/\1/"  | xargs -I {} echo {}:6379, | xargs

```