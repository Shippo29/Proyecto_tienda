<#
stop-all.ps1

Propósito:
- Detener procesos iniciados por run-all.ps1 (lee .run_all_processes.json)
- Ejecutar `docker compose -f docker-compose.kafka.yml down` para parar Kafka

Uso:
  Ejecutar desde PowerShell:
    .\stop-all.ps1

Notas:
- Ejecutar como administrador si falta permiso para matar procesos o parar Docker.
#>

# --- Config
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $ScriptDir

$pidFile = Join-Path $ScriptDir '.run_all_processes.json'
if (Test-Path $pidFile) {
    Write-Host "Leyendo procesos desde $pidFile"
    try {
        $json = Get-Content $pidFile -Raw | ConvertFrom-Json
        foreach ($item in $json) {
            try {
                Stop-Process -Id $item.pid -Force -ErrorAction SilentlyContinue
                Write-Host "[OK] Detenido $($item.name) PID $($item.pid)" -ForegroundColor Green
            } catch {
                Write-Host "[WARN] No se pudo detener PID $($item.pid) - puede haber finalizado ya" -ForegroundColor Yellow
            }
        }
        Remove-Item $pidFile -Force -ErrorAction SilentlyContinue
    } catch {
        Write-Host "[ERROR] Falló al leer o parsear $pidFile" -ForegroundColor Red
    }
} else {
    Write-Host "Archivo de procesos no encontrado: $pidFile" -ForegroundColor Yellow
}

# Detener docker compose (Kafka)
$composeFile = Join-Path $ScriptDir 'docker-compose.kafka.yml'
if (Test-Path $composeFile) {
    Write-Host "Parando Docker Compose (Kafka) usando: docker compose -f $composeFile down"
    docker compose -f $composeFile down
    Write-Host "[OK] Docker compose detenido" -ForegroundColor Green
} else {
    Write-Host "docker-compose.kafka.yml no encontrado en $composeFile" -ForegroundColor Yellow
}

Write-Host "stop-all.ps1 finalizado."