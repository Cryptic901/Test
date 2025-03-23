# Используем OpenJDK 17
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем JAR-файл (исправленный путь!)
COPY target/TestApp-0.0.1-SNAPSHOT.jar app.jar

# Проверяем, что файл реально скопировался
RUN ls -lh app.jar && test -s app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]