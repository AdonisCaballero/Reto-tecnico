Set-Location $PSScriptRoot
Write-Host "Iniciando frontend..." -ForegroundColor Green
if (-not (Test-Path "node_modules")) {
    npm install
}
npm start
