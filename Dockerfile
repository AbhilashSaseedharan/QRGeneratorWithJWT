# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/mySpringApp-0.0.1-SNAPSHOT.jar app.jar

# Install Redis and supervisor
RUN apt-get update && \
    apt-get install -y redis-server supervisor && \
    rm -rf /var/lib/apt/lists/*

COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Expose port 8080
EXPOSE 8080

# Start both Redis and Spring Boot app
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
