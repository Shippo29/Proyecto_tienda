<#
run-all.ps1

Propósito:
- Levanta Kafka usando docker-compose.kafka.yml
- Comprueba puertos necesarios
- Abre terminales separados para inventario, pedidos, envios, api-gateway y frontend
- Ejecuta los comandos necesarios (mvnw / npm start / npm run dev)
- Guarda los PIDs en .run_all_processes.json para detenerlos con stop-all.ps1

Uso:
  Ejecutar desde PowerShell (recomendado como administrador si es necesario):
    .\run-all.ps1

Notas:
- Este script asume Windows PowerShell (powershell.exe) disponible.
- Usa el wrapper Maven (mvnw.cmd) si existe; si no, usa `mvn`.
- Si `node_modules` no existe en proyectos Node, ejecuta `npm install` automáticamente.
- El script crea `.run_all_processes.json` en la raíz para trackear PIDs.
#>

# --- Configuración inicial
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $ScriptDir

# Puertos a comprobar
$portsToCheck = @{ 'API Gateway' = 8080; 'Inventario' = 8081; 'Pedidos' = 8082; 'Envios' = 8083; 'Frontend' = 5173 }

function Is-Port-InUse {
    param([int]$port)
    # Intentar Test-NetConnection cuando esté disponible
    try {
        $res = Test-NetConnection -ComputerName '127.0.0.1' -Port $port -WarningAction SilentlyContinue
        if ($null -ne $res) { return $res.TcpTestSucceeded }
    } catch { }
    # Fallback: netstat grep (simple heurística)
    try {
        $pattern = ":$port\s"
        $found = netstat -ano | Select-String $pattern
        return $found -ne $null
    } catch { return $false }
}

Write-Host "== Comprobando puertos requeridos =="
foreach ($name in $portsToCheck.Keys) {
    $port = $portsToCheck[$name]
    if (Is-Port-InUse $port) {
        Write-Host "[WARN] Puerto $port ($name) parece estar en uso." -ForegroundColor Yellow
    } else {
        Write-Host "[OK] Puerto $port ($name) libre." -ForegroundColor Green
    }
}

# Levantar Kafka
Write-Host "`n== Iniciando Kafka (docker compose) =="
$composeFile = Join-Path $ScriptDir 'docker-compose.kafka.yml'
if (-not (Test-Path $composeFile)) {
    Write-Host "[ERROR] docker-compose.kafka.yml no encontrado en $composeFile" -ForegroundColor Red
    exit 1
}

Write-Host "Ejecutando: docker compose -f $composeFile up -d"
docker compose -f $composeFile up -d

# Esperar a que Kafka escuche en 9092
$maxWaitSeconds = 60
$elapsed = 0
while (-not (Is-Port-InUse 9092) -and $elapsed -lt $maxWaitSeconds) {
    Start-Sleep -Seconds 2
    $elapsed += 2
    Write-Host "Esperando a Kafka en 9092... ${elapsed}s"
}
if (Is-Port-InUse 9092) {
    Write-Host "[OK] Kafka iniciado" -ForegroundColor Green
} else {
    Write-Host "[WARN] Kafka no respondió en $maxWaitSeconds s. Continúo de todos modos." -ForegroundColor Yellow
}

# Función para abrir nueva ventana de PowerShell y ejecutar un comando de larga ejecución
function Start-NewTerminalProcess {
    param(
        [string]$name,
        [string]$workdir,
        [string]$cmdLine
    )
    Write-Host "`nIniciando $name en nueva ventana PowerShell..."
    $command = "Set-Location -LiteralPath '$workdir'; $cmdLine"
    $proc = Start-Process -FilePath 'powershell' -ArgumentList '-NoExit','-Command',$command -WorkingDirectory $workdir -PassThru
    Start-Sleep -Seconds 1
    Write-Host "[OK] $name iniciado (PID $($proc.Id))"
    return $proc.Id
}

# Rutas de los servicios (relativas a la raíz del repo)
$inventarioPath = Join-Path $ScriptDir 'inventario-service'
$pedidosPath   = Join-Path $ScriptDir 'pedidos-service'
$enviosPath    = Join-Path $ScriptDir 'envios-service'
$gatewayPath   = Join-Path $ScriptDir 'api-gateway'
$frontendPath  = Join-Path $ScriptDir 'frontend\frontend'

# Lista para almacenar procesos
$processes = @()

# Helper para elegir comando mvn (wrapper o instalado)
function Get-MvnCommand($dir) {
    if (Test-Path (Join-Path $dir 'mvnw.cmd')) { return "& '.\\mvnw.cmd' -DskipTests spring-boot:run" }
    return "mvn -DskipTests spring-boot:run"
}

# Iniciar microservicios en nuevas terminales
if (Test-Path $inventarioPath) {
    $pidInv = Start-NewTerminalProcess 'inventario-service' $inventarioPath (Get-MvnCommand $inventarioPath)
    $processes += @{ name='inventario-service'; pid=$pidInv }
} else { Write-Host "[WARN] inventario-service no encontrado en $inventarioPath" -ForegroundColor Yellow }

if (Test-Path $pedidosPath) {
    $pidPedidos = Start-NewTerminalProcess 'pedidos-service' $pedidosPath (Get-MvnCommand $pedidosPath)
    $processes += @{ name='pedidos-service'; pid=$pidPedidos }
} else { Write-Host "[WARN] pedidos-service no encontrado en $pedidosPath" -ForegroundColor Yellow }

if (Test-Path $enviosPath) {
    $pidEnvios = Start-NewTerminalProcess 'envios-service' $enviosPath (Get-MvnCommand $enviosPath)
    $processes += @{ name='envios-service'; pid=$pidEnvios }
} else { Write-Host "[WARN] envios-service no encontrado en $enviosPath" -ForegroundColor Yellow }

# api-gateway
Start-Process -FilePath 'cmd.exe' -ArgumentList '/c npm install' -WorkingDirectory "ruta\al\api-gateway"
Start-Process -FilePath 'cmd.exe' -ArgumentList '/c npm install' -WorkingDirectory "ruta\al\frontend\frontend"

# Guardar PIDs en archivo para stop script
$pidFile = Join-Path $ScriptDir '.run_all_processes.json'
$processes | ConvertTo-Json | Out-File -FilePath $pidFile -Encoding utf8

Write-Host "`n== Resumen =="
foreach ($p in $processes) {
    Write-Host "[OK] $($p.name) PID $($p.pid)"
}

Write-Host "`nrun-all.ps1 finalizado. Para detener todo ejecute: .\stop-all.ps1"
