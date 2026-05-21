$origen = Join-Path $PSScriptRoot "data\datos-ejemplo.json"
$destino = Join-Path $PSScriptRoot "data\recetas.json"

if (-not (Test-Path $origen)) {
    Write-Host "No se encuentra $origen" -ForegroundColor Red
    exit 1
}

Copy-Item $origen $destino -Force
Write-Host "Datos de ejemplo copiados a data\recetas.json" -ForegroundColor Green
Write-Host "Reinicia el backend para ver los cambios." -ForegroundColor Yellow
