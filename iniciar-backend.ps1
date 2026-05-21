$backend = Join-Path $PSScriptRoot "backend"
Set-Location $backend
Write-Host "Compilando backend..." -ForegroundColor Cyan
mvn -q compile
if ($LASTEXITCODE -ne 0) {
    Write-Host "Error de compilacion." -ForegroundColor Red
    exit 1
}
Write-Host "Iniciando backend en $backend ..." -ForegroundColor Green
mvn spring-boot:run
