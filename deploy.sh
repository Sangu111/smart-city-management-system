#!/bin/bash
# Deployment script for Smart City Management System

echo "🚀 Smart City Management System - Docker Deployment"
echo "=================================================="

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo "📋 Checking prerequisites..."
if ! command_exists docker; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

if ! command_exists docker-compose; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "✅ Prerequisites check passed"

# Build and run with Docker Compose
echo "🔨 Building and starting containers..."
docker-compose down 2>/dev/null
docker-compose up --build -d

echo "⏳ Waiting for services to start..."
sleep 30

# Check if containers are running
if docker-compose ps | grep -q "Up"; then
    echo "✅ Containers are running successfully!"
    echo ""
    echo "🌐 Application URLs:"
    echo "   - Main App: http://localhost:8080"
    echo "   - Login: http://localhost:8080/login.jsp"
    echo "   - Register: http://localhost:8080/register.jsp"
    echo ""
    echo "🔑 Test Credentials:"
    echo "   - Admin: admin / admin123"
    echo "   - Citizen: citizen1 / test123"
    echo ""
    echo "📊 To view logs: docker-compose logs -f"
    echo "🛑 To stop: docker-compose down"
else
    echo "❌ Failed to start containers. Check logs with: docker-compose logs"
    exit 1
fi