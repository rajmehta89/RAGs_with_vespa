# Deploy to Docker Hub: raj153
# Usage: .\deploy-to-dockerhub.ps1

Write-Host "Deploying to Docker Hub: raj153" -ForegroundColor Cyan
Write-Host ""

# Login
Write-Host "Logging into Docker Hub..." -ForegroundColor Yellow
docker login
if ($LASTEXITCODE -ne 0) { exit 1 }
Write-Host "✓ Logged in" -ForegroundColor Green

# Build
Write-Host "Building image..." -ForegroundColor Yellow
docker build -t raj153/vespa-hybrid-search:latest .
if ($LASTEXITCODE -ne 0) { exit 1 }
Write-Host "✓ Built" -ForegroundColor Green

# Push
Write-Host "Pushing to Docker Hub..." -ForegroundColor Yellow
docker push raj153/vespa-hybrid-search:latest
if ($LASTEXITCODE -ne 0) { exit 1 }
Write-Host "✓ Pushed" -ForegroundColor Green

Write-Host ""
Write-Host "Done! View at: https://hub.docker.com/repositories/raj153" -ForegroundColor Green

