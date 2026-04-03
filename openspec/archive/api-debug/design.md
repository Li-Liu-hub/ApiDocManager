## Context

当前系统已有接口详情页（ApiDetail.vue），展示接口基本信息、请求参数表格、
请求体和响应体JSON。项目表中有base_url字段存储目标服务器地址。
后端运行在8081端口，前端运行在5173端口。
前端无法直接跨域请求目标服务器，必须通过后端代理转发。

## Goals / Non-Goals

Goals:

- 在接口详情页增加在线调试功能
- 支持编辑URL、请求头、Query参数、请求体
- 通过后端代理转发请求，返回完整响应信息
- 展示状态码、响应头、响应体、耗时

Non-Goals:

- 本次不做请求历史记录保存
- 本次不做环境变量管理（如开发环境、测试环境切换）
- 本次不做请求集合和批量测试
- 本次不做WebSocket和SSE调试
- 本次不做文件上传调试
- 本次不做认证Token的自动管理

## Decisions

### Decision: 后端代理使用RestTemplate而不是OkHttp

原因：Spring Boot自带RestTemplate，无需引入额外依赖，
对于简单的HTTP转发场景足够用
备选方案：引入OkHttp
不采用原因：增加依赖，对于当前场景没有明显优势

### Decision: 代理接口接收JSON对象而不是透传原始请求

原因：前端需要把URL、方法、请求头、参数、请求体打包成一个对象发给后端，
后端解析后构造真实请求。这样后端能完整控制请求构造过程
备选方案：前端直接拼装完整HTTP请求通过后端透传
不采用原因：后端无法做超时控制和错误处理

### Decision: 超时时间固定10秒而不是用户可配置

原因：简化实现，10秒对于API调试场景够用
备选方案：提供超时时间输入框
不采用原因：增加界面复杂度，当前阶段不必要

### Decision: 调试区域直接嵌入详情页而不是独立页面

原因：调试和查看文档是一体的操作，用户看完文档直接测试，
不需要跳转到另一个页面
备选方案：新建独立的调试页面
不采用原因：来回跳转体验差，参数无法自动填充

### Decision: 请求头和Query参数用Key-Value动态行而不是JSON编辑

原因：Key-Value形式更直观，用户不需要手写JSON格式，
添加删除操作也更方便
备选方案：统一用JSON textarea编辑
不采用原因：对非JSON内容（如请求头）不友好，容易写错格式

### Decision: 响应体JSON格式化在前端做而不是后端做

原因：后端只负责原样返回响应内容，格式化是展示层的事情，
前端用JSON.stringify(JSON.parse(body), null, 2)即可
备选方案：后端格式化后返回
不采用原因：后端无法确定响应是否是JSON，强行格式化可能报错

## Risks / Trade-offs

- RestTemplate在Spring Boot 3中标记为维护模式，
  官方推荐WebClient → 当前场景简单同步请求足够，
  不需要WebClient的异步能力
- 代理接口没有鉴权，任何人可以通过它发送任意请求 →
  内部工具可接受，后续加登录功能后再限制
- 不支持HTTPS证书校验跳过 → 如果目标服务器用自签名证书会报错，
  后续按需添加
- 大响应体可能导致前端渲染卡顿 → 响应体超过1MB时截断展示

## API Design

### 代理请求接口

POST /api/debug/send

请求体：
{
  "method": "GET",
  "url": "http://localhost:8080/api/users/1",
  "headers": {
    "Content-Type": "application/json",
    "Authorization": "Bearer xxx"
  },
  "params": {
    "page": "1",
    "size": "10"
  },
  "body": "{\"name\":\"test\"}"
}

响应体：
{
  "code": 200,
  "msg": "success",
  "data": {
    "statusCode": 200,
    "headers": {
      "Content-Type": "application/json",
      "X-Request-Id": "abc123"
    },
    "body": "{\"id\":1,\"name\":\"张三\"}",
    "duration": 128
  }
}

## Files to Create / Modify

### 后端新增

- dto/debug/DebugRequestDTO.java（请求入参）
- dto/debug/DebugResponseDTO.java（响应出参）
- service/DebugService.java（接口）
- service/impl/DebugServiceImpl.java（代理转发实现）
- controller/DebugController.java（POST /api/debug/send）

### 前端修改

- api/index.js（新增debugApi）
- views/ApiDetail.vue（新增调试区域）
