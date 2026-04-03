## 1. 后端DTO

- [ ] 1.1 创建 DebugRequestDTO（字段：method、url、headers(Map)、params(Map)、body(String)）
- [ ] 1.2 创建 DebugResponseDTO（字段：statusCode、headers(Map)、body(String)、duration(Long)）

## 2. 后端Service

- [ ] 2.1 创建 DebugService 接口（方法：sendRequest(DebugRequestDTO) 返回 DebugResponseDTO）
- [ ] 2.2 创建 DebugServiceImpl 实现
- [ ] 2.3 使用 RestTemplate 构造请求：根据method选择GET/POST/PUT/DELETE
- [ ] 2.4 将 params 拼接到URL的QueryString上
- [ ] 2.5 将 headers 设置到 HttpHeaders 中
- [ ] 2.6 POST/PUT时将body设置为请求体，GET/DELETE时忽略body
- [ ] 2.7 记录请求开始时间和结束时间，计算耗时（毫秒）
- [ ] 2.8 捕获响应：提取statusCode、responseHeaders、responseBody
- [ ] 2.9 异常处理：连接超时返回错误提示"请求超时"
- [ ] 2.10 异常处理：连接拒绝返回错误提示"无法连接到目标服务器"
- [ ] 2.11 配置RestTemplate超时时间为10秒

## 3. 后端Controller

- [ ] 3.1 创建 DebugController（/api/debug）
- [ ] 3.2 实现 POST /api/debug/send 接口
- [ ] 3.3 入参校验：url不能为空，method不能为空

## 4. 后端验证

- [ ] 4.1 启动后端，用Postman测试 POST /api/debug/send
  
      发送一个GET请求到已知可用的地址，确认返回statusCode和body
- [ ] 4.2 测试目标地址不可达时返回错误提示
- [ ] 4.3 测试url为空时返回校验错误

## 5. 前端API

- [ ] 5.1 在 api/index.js 新增 debugApi 对象
  
      包含：send(data) => request.post('/api/debug/send', data)

## 6. 前端调试区域 - 基础结构

- [ ] 6.1 在 ApiDetail.vue 中接口详情信息下方新增"在线调试"区域
  
      用 el-card 包裹，标题为"在线调试"
- [ ] 6.2 顶部展示请求方法标签（不可编辑）+ 请求URL输入框（可编辑）
- [ ] 6.3 URL自动拼接：从接口所属项目的base_url + 接口path拼接填入

## 7. 前端调试区域 - 请求头编辑

- [ ] 7.1 用 el-table 实现Key-Value动态行展示请求头
- [ ] 7.2 默认一行：Content-Type = application/json
- [ ] 7.3 实现"添加"按钮，点击新增一行空的Key-Value
- [ ] 7.4 实现每行的"删除"按钮，点击移除该行

## 8. 前端调试区域 - 请求参数编辑

- [ ] 8.1 用 el-table 实现Key-Value动态行展示Query参数
- [ ] 8.2 如果接口定义有request_params，解析JSON自动填充参数名，值留空
- [ ] 8.3 实现"添加"按钮，新增一行空的Key-Value
- [ ] 8.4 实现每行的"删除"按钮

## 9. 前端调试区域 - 请求体编辑

- [ ] 9.1 判断接口方法：POST/PUT时展示请求体编辑区域，GET/DELETE时隐藏
- [ ] 9.2 用 el-input type="textarea" 展示请求体编辑框，rows至少6行
- [ ] 9.3 如果接口定义有request_body，自动填充格式化后的JSON

## 10. 前端调试区域 - 发送请求

- [ ] 10.1 实现"发送请求"按钮（el-button type="primary"）
- [ ] 10.2 点击后校验URL不为空，为空则提示"请输入请求URL"
- [ ] 10.3 按钮点击后变为loading状态，文字变为"请求中..."
- [ ] 10.4 构造 DebugRequestDTO 对象：
  
      method从接口定义取，url从输入框取，
      headers从Key-Value行转为Map，
      params从Key-Value行转为Map（过滤掉Key为空的行），
      body从textarea取（仅POST/PUT）
- [ ] 10.5 调用 debugApi.send 发送请求
- [ ] 10.6 请求完成后按钮恢复正常状态
- [ ] 10.7 请求失败时展示 ElMessage.error 错误提示

## 11. 前端调试区域 - 响应结果展示

- [ ] 11.1 响应区域默认展示"点击发送请求查看响应结果"灰色提示
- [ ] 11.2 请求完成后展示响应状态码：
  
      用 el-tag 展示，2xx绿色、4xx橙色、5xx红色
- [ ] 11.3 状态码旁边展示请求耗时（如 "128ms"）
- [ ] 11.4 展示响应头：用 el-descriptions 或简单的Key-Value列表
- [ ] 11.5 展示响应体：
  
      尝试 JSON.parse，成功则用 JSON.stringify(parsed, null, 2) 格式化，
      失败则原样展示。放在 pre 标签中
- [ ] 11.6 响应体区域设置 max-height 和 overflow-y: auto，防止内容过长撑爆页面

## 12. 联调验证（对照 delta spec）

- [ ] 12.1 验证进入接口详情页后调试区域展示正常
- [ ] 12.2 验证URL自动拼接正确（base_url + path）
- [ ] 12.3 验证手动修改URL后发送请求使用修改后的URL
- [ ] 12.4 验证路径参数{id}替换为实际值后请求正确
- [ ] 12.5 验证默认请求头Content-Type存在
- [ ] 12.6 验证添加和删除请求头正常
- [ ] 12.7 验证接口定义的参数自动填充到请求参数区域
- [ ] 12.8 验证手动添加和删除请求参数正常
- [ ] 12.9 验证POST/PUT展示请求体，GET/DELETE隐藏请求体
- [ ] 12.10 验证请求体自动填充接口定义中的示例JSON
- [ ] 12.11 验证发送请求成功，按钮loading状态正常
- [ ] 12.12 验证URL为空时提示错误不发送请求
- [ ] 12.13 验证目标服务器不可达时展示错误提示
- [ ] 12.14 验证响应状态码颜色：2xx绿、4xx橙、5xx红
- [ ] 12.15 验证响应耗时展示正确
- [ ] 12.16 验证响应头展示正确
- [ ] 12.17 验证响应体JSON格式化展示正常
- [ ] 12.18 验证非JSON响应体原样展示
- [ ] 12.19 验证未发送请求时展示"点击发送请求查看响应结果"
