# Imagem oficial do OpenJDK 17 da Red Hat
FROM registry.access.redhat.com/ubi8/openjdk-17:latest

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o jar do aplicativo para dentro do container
COPY demo-app/build-artifacts/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Exponhe a porta 8080
EXPOSE 8080

# Define o comando de inicialização do container
ENTRYPOINT ["java", "-jar", "/app/demo.jar"]
