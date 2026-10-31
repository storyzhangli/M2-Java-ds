# D17 流式接口真机冒烟（SSE）
$base = 'http://127.0.0.1:8080/api/v1/ai'
$resp = Invoke-WebRequest -Uri "$base/chat/stream" -Method Post -ContentType 'application/json; charset=utf-8' `
    -Headers @{ 'X-Dev-User' = 'ut_smoke' } `
    -Body (@{ message = '流式问题：演示 SSE 事件序列' } | ConvertTo-Json) -SkipHttpErrorCheck
Write-Output "STATUS=$($resp.StatusCode) CONTENT_TYPE=$($resp.Headers['Content-Type'])"
Write-Output '----- RAW BODY -----'
Write-Output $resp.Content
