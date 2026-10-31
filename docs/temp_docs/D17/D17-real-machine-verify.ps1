# D17 真机验证脚本（mock 模式，端口 8080）
# 覆盖：健康检查、同会话多轮、历史查询、归属隔离、非法入参
$ErrorActionPreference = 'Continue'
$base = 'http://127.0.0.1:8080/api/v1/ai'
$header = 'X-Dev-User'

function Post-Chat($owner, $body) {
    $headers = @{ $header = $owner }
    Invoke-RestMethod -Uri "$base/chat" -Method Post -ContentType 'application/json; charset=utf-8' `
        -Headers $headers -Body ($body | ConvertTo-Json -Depth 5) -SkipHttpErrorCheck
}

function Get-Path($owner, $path) {
    $headers = @{ $header = $owner }
    Invoke-RestMethod -Uri "$base$path" -Method Get -Headers $headers -SkipHttpErrorCheck
}

Write-Output '===== 1. 健康检查 ====='
$health = Invoke-RestMethod -Uri "$base/health" -Method Get -SkipHttpErrorCheck
$health | ConvertTo-Json -Depth 5

Write-Output '===== 2. 同会话三轮多轮对话（owner=ut_smoke） ====='
$r1 = Post-Chat 'ut_smoke' @{ message = '第一轮问题：设备无法启动怎么办' }
$r1 | ConvertTo-Json -Depth 6
$sid = $r1.data.sessionId
Write-Output "SESSION_ID=$sid"

$r2 = Post-Chat 'ut_smoke' @{ message = '第二轮问题：那指示灯不亮呢'; sessionId = $sid }
$r2 | ConvertTo-Json -Depth 6
$r3 = Post-Chat 'ut_smoke' @{ message = '第三轮问题：重启后还需要检查什么'; sessionId = $sid }
$r3 | ConvertTo-Json -Depth 6

Write-Output '===== 3. 历史查询接口 ====='
$sessions = Get-Path 'ut_smoke' '/sessions?page=1&size=5'
$sessions | ConvertTo-Json -Depth 8
$messages = Get-Path 'ut_smoke' "/sessions/$sid/messages?page=1&size=20"
$messages | ConvertTo-Json -Depth 8

Write-Output '===== 4. 归属隔离：ut_smoke_b 读取 ut_smoke 会话应被拒绝 ====='
$cross = Get-Path 'ut_smoke_b' "/sessions/$sid/messages"
$cross | ConvertTo-Json -Depth 5

Write-Output '===== 5. 非法入参：空白消息应被拒绝 ====='
$bad = Post-Chat 'ut_smoke' @{ message = '   ' }
$bad | ConvertTo-Json -Depth 5

Write-Output '===== 6. 非法开发身份头应被拒绝 ====='
try {
    Invoke-RestMethod -Uri "$base/chat" -Method Post -ContentType 'application/json; charset=utf-8' `
        -Headers @{ $header = 'bad/owner!' } -Body (@{ message = '测试非法身份' } | ConvertTo-Json) `
        -SkipHttpErrorCheck
} catch {
    Write-Output "UNEXPECTED_THROW: $($_.Exception.Message)"
}
