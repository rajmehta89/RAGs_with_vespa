# Check Docker Installation and Fix Issues

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Docker Installation Checker" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if docker command exists
Write-Host "Checking Docker installation..." -ForegroundColor Yellow
$dockerCmd = Get-Command docker -ErrorAction SilentlyContinue

if (-not $dockerCmd) {
    Write-Host "❌ Docker command not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Possible solutions:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1. Docker Desktop is not installed" -ForegroundColor White
    Write-Host "   → Download from: https://www.docker.com/products/docker-desktop" -ForegroundColor Cyan
    Write-Host "   → Install it and restart your computer" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "2. Docker Desktop is not running" -ForegroundColor White
    Write-Host "   → Open Docker Desktop from Start menu" -ForegroundColor Cyan
    Write-Host "   → Wait for it to fully start (whale icon stops animating)" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "3. PowerShell needs to be restarted" -ForegroundColor White
    Write-Host "   → Close this PowerShell window" -ForegroundColor Cyan
    Write-Host "   → Open a new PowerShell window" -ForegroundColor Cyan
    Write-Host "   → Try again" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "4. Docker is not in PATH" -ForegroundColor White
    Write-Host "   → Restart your computer after installing Docker" -ForegroundColor Cyan
    Write-Host "   → Or add Docker to PATH manually" -ForegroundColor Cyan
    Write-Host ""
    
    # Check if Docker Desktop is installed but not in PATH
    $dockerPaths = @(
        "$env:ProgramFiles\Docker\Docker\resources\bin\docker.exe",
        "${env:ProgramFiles(x86)}\Docker\Docker\resources\bin\docker.exe",
        "$env:LOCALAPPDATA\Docker\resources\bin\docker.exe"
    )
    
    $foundDocker = $false
    foreach ($path in $dockerPaths) {
        if (Test-Path $path) {
            Write-Host "✓ Found Docker at: $path" -ForegroundColor Green
            Write-Host "  Docker is installed but not in PATH!" -ForegroundColor Yellow
            Write-Host "  Solution: Restart PowerShell or add to PATH" -ForegroundColor Yellow
            $foundDocker = $true
            break
        }
    }
    
    if (-not $foundDocker) {
        Write-Host "Docker Desktop doesn't appear to be installed." -ForegroundColor Yellow
        Write-Host "Please install Docker Desktop first." -ForegroundColor Yellow
    }
    
    exit 1
}

Write-Host "✓ Docker command found" -ForegroundColor Green
Write-Host ""

# Check if Docker daemon is running
Write-Host "Checking if Docker is running..." -ForegroundColor Yellow
docker ps | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Docker is not running!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please:" -ForegroundColor Yellow
    Write-Host "1. Open Docker Desktop from Start menu" -ForegroundColor White
    Write-Host "2. Wait for it to fully start" -ForegroundColor White
    Write-Host "3. Run this script again" -ForegroundColor White
    Write-Host ""
    exit 1
}

Write-Host "✓ Docker is running!" -ForegroundColor Green
Write-Host ""

# Get Docker version
Write-Host "Docker Version:" -ForegroundColor Yellow
docker --version
Write-Host ""

Write-Host "✅ Docker is ready to use!" -ForegroundColor Green
Write-Host ""
Write-Host "You can now run:" -ForegroundColor Yellow
Write-Host "  docker run -d --name vespa --hostname vespa -p 8080:8080 -p 19071:19071 vespaengine/vespa" -ForegroundColor White
Write-Host ""

