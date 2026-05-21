$frontend = Join-Path $PSScriptRoot "frontend"
Set-Location $frontend
Write-Host "Iniciando frontend en $frontend ..." -ForegroundColor Green
if (-not (Test-Path "node_modules")) {
    npm install
}
npm start
