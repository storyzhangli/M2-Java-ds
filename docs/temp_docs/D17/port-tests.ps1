# D17 测试移植脚本：复制 D16 测试树并做包名/标识替换（可重复执行前先清理 D17 测试目录）
$ErrorActionPreference = 'Stop'

$src = 'E:\AI\project\AI-Learning-one\M2-Java\D16\D16_CoT\src\test'
$dst = 'E:\AI\project\AI-Learning-one\M2-Java\D17\D17_DHSA_MTDCA\src\test'

if (Test-Path -LiteralPath $dst) {
    Remove-Item -LiteralPath $dst -Recurse -Force
}
Copy-Item -LiteralPath $src -Destination $dst -Recurse

# 目录包名 d15 -> d17（测试类原包名为 com.zl.d15.springai）
$oldDir = Join-Path $dst 'java\com\zl\d15'
$newDir = Join-Path $dst 'java\com\zl\d17'
Move-Item -LiteralPath $oldDir -Destination $newDir

# 文本替换（仅 .java 文件）
$javaFiles = Get-ChildItem -LiteralPath $dst -Recurse -Filter '*.java'
foreach ($file in $javaFiles) {
    $text = [IO.File]::ReadAllText($file.FullName, [Text.Encoding]::UTF8)
    $text = $text.Replace('com.zl.d16', 'com.zl.d17')
    $text = $text.Replace('com.zl.d15', 'com.zl.d17')
    $text = $text.Replace('D16TestBootstrap', 'D17TestBootstrap')
    $text = $text.Replace('D16_CoT', 'D17_DHSA_MTDCA')
    [IO.File]::WriteAllText($file.FullName, $text, (New-Object Text.UTF8Encoding($false)))
}

Write-Output ("移植完成，Java 文件数=" + $javaFiles.Count)
