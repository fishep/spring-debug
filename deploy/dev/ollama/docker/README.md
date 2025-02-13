# 基于docker部署ollama服务

### ollama
```shell

#docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
#docker run -d --gpus=all -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
#docker run -d -p 3000:8080 --gpus=all -v ollama:/root/.ollama -v open-webui:/app/backend/data 
#--name open-webui --restart always ghcr.io/open-webui/open-webui:ollama
#docker run -d -p 3000:8080 --add-host=host.docker.internal:host-gateway -v open-webui:/app/backend/data 
#--name open-webui --restart always ghcr.io/open-webui/open-webui:main

cd ollama058
docker compose -p ai up -d

docker exec -it ollama bash
#默认是7b
ollama run deepseek-r1

#http://localhost:11434/

```

### open-webui
```shell
#docker run -d -p 3000:8080 
#--add-host=host.docker.internal:host-gateway 
#-v open-webui:/app/backend/data 
#--name open-webui --restart always ghcr.io/open-webui/open-webui:main

cd open-webui
docker compose -p ai up -d

# 

```

### dify
```shell
#https://github.com/langgenius/dify.git

cd dify/docker
cp .env.example .env
docker compose -p ai up -d

#安装
#http://localhost/install 

#访问
#http://localhost

```
