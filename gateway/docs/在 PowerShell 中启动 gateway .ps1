Write-Output "在 PowerShell 中启动 huava gateway "

npx kill-port 8888
Start-Sleep -Seconds 3

Set-Location D:\git\huava-cloud-example\gateway\target
chcp 65001
$host.UI.RawUI.WindowTitle = "gateway"
& "C:\Program Files\Java\graalvm-jdk-21\bin\java.exe" -jar '-Dfile.encoding=UTF-8' gateway-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --server.port=8888
