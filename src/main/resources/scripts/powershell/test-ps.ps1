# test-ps.ps1

$osName = if ($IsWindows) {
    (Get-CimInstance -ClassName Win32_OperatingSystem).Caption
} else {
    uname -a
}

$info = [PSCustomObject]@{
    username    = $env:USER ?? $env:USERNAME
    hostname    = $env:COMPUTERNAME ?? (hostname)
    os          = $osName
    date        = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    pwshVersion = $PSVersionTable.PSVersion.ToString()
}

# Éviter tout bruit et forcer le bon encodage
$OutputEncoding = [Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$info | ConvertTo-Json -Depth 3 | Out-String