# D17 重启后持久化验证脚本
$base = 'http://127.0.0.1:8080/api/v1/ai'
$header = 'X-Dev-User'
$sid = 'ce38e1e8999a4d31910b2b3926d50926'

Write-Output '===== 1. 重启后查询重启前会话的消息（应仍为 6 条 COMPLETED） ====='
$messages = Invoke-RestMethod -Uri "$base/sessions/$sid/messages?page=1&size=20" `
    -Method Get -Headers @{ $header = 'ut_smoke' } -SkipHttpErrorCheck
$messages | ConvertTo-Json -Depth 8

Write-Output '===== 2. 重启后在旧会话上发起第四轮（mock 应看到历史消息条数=6） ====='
$r4 = Invoke-RestMethod -Uri "$base/chat" -Method Post -ContentType 'application/json; charset=utf-8' `
    -Headers @{ $header = 'ut_smoke' } `
    -Body (@{ message = '第四轮问题：重启后还能记得之前的上下文吗'; sessionId = $sid } | ConvertTo-Json) `
    -SkipHttpErrorCheck
$r4 | ConvertTo-Json -Depth 6

Write-Output '===== 3. 再次查询，序号应连续到 8 ====='
$after = Invoke-RestMethod -Uri "$base/sessions/$sid/messages?page=1&size=20" `
    -Method Get -Headers @{ $header = 'ut_smoke' } -SkipHttpErrorCheck
$after.data.records | ForEach-Object { "seq=$($_.sequenceNo) role=$($_.role) status=$($_.status)" }
