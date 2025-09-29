# Multi-stage Dockerfile for Java EE Web Application
FROM maven:3.9.4-openjdk-17-slim AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the WAR file
RUN mvn clean package -DskipTests

# Production stage
FROM tomcat:9.0.80-jdk17-openjdk-slim

# Remove default ROOT application
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy WAR file as ROOT.war for root context
COPY --from=builder /app/target/smart-city.war /usr/local/tomcat/webapps/ROOT.war

# Expose port (Render uses $PORT environment variable)
EXPOSE 8080

# Start Tomcat with port configuration for Render
CMD ["sh", "-c", "exec catalina.sh run -Dserver.port=${PORT:-8080}"]