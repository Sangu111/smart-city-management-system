# PowerShell Deployment Script for Smart City Management System
Write-Host "🚀 Smart City Management System - Docker Deployment" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

# Check if Docker is installed
try {
    docker --version | Out-Null
    Write-Host "✅ Docker is installed" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker is not installed. Please install Docker Desktop first." -ForegroundColor Red
    exit 1
}

# Check if Docker Compose is available
try {
    docker-compose --version | Out-Null
    Write-Host "✅ Docker Compose is available" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker Compose is not available. Please install Docker Compose." -ForegroundColor Red
    exit 1
}

Write-Host "📋 Prerequisites check passed" -ForegroundColor Green

# Stop existing containers
Write-Host "🛑 Stopping existing containers..." -ForegroundColor Yellow
docker-compose down 2>$null

# Build and start containers
Write-Host "🔨 Building and starting containers..." -ForegroundColor Yellow
docker-compose up --build -d

# Wait for services to start
Write-Host "⏳ Waiting for services to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Check container status
$containers = docker-compose ps --services --filter "status=running"
if ($containers) {
    Write-Host "✅ Containers are running successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "🌐 Application URLs:" -ForegroundColor Cyan
    Write-Host "   - Main App: http://localhost:8080" -ForegroundColor White
    Write-Host "   - Login: http://localhost:8080/login.jsp" -ForegroundColor White
    Write-Host "   - Register: http://localhost:8080/register.jsp" -ForegroundColor White
    Write-Host ""
    Write-Host "🔑 Test Credentials:" -ForegroundColor Cyan
    Write-Host "   - Admin: admin / admin123" -ForegroundColor White
    Write-Host "   - Citizen: citizen1 / test123" -ForegroundColor White
    Write-Host ""
    Write-Host "📊 To view logs: docker-compose logs -f" -ForegroundColor Yellow
    Write-Host "🛑 To stop: docker-compose down" -ForegroundColor Yellow
} else {
    Write-Host "❌ Failed to start containers. Check logs with: docker-compose logs" -ForegroundColor Red
    exit 1
}