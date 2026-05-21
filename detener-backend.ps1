# Detiene procesos que usan el puerto 8080 (backend Spring Boot)
$conexiones = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
if (-not $conexiones) {
    Write-Host "No hay ningun proceso escuchando en el puerto 8080." -ForegroundColor Yellow
    exit 0
}

$pids = $conexiones.OwningProcess | Select-Object -Unique
foreach ($pid in $pids) {
    $proc = Get-Process -Id $pid -ErrorAction SilentlyContinue
    if ($proc) {
        Write-Host "Deteniendo $($proc.ProcessName) (PID $pid)..." -ForegroundColor Green
        Stop-Process -Id $pid -Force
    }
}

Write-Host "Puerto 8080 liberado. Ya puedes ejecutar: mvn spring-boot:run" -ForegroundColor Green
