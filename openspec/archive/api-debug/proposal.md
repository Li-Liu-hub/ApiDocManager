## Why

当前系统只能查看接口文档，无法验证接口是否可用。
开发人员定义完接口后需要切换到Postman或curl去测试，
操作繁琐且请求参数需要重新填写，无法复用已定义的接口信息。
需要在接口详情页直接发送HTTP请求并查看响应结果，
实现"看文档→测接口"的一站式体验。

## What Changes

- 接口详情页新增"在线调试"区域
- 支持编辑请求URL（基础URL + 接口路径拼接，路径参数可替换）
- 支持编辑请求头（Key-Value形式）
- 支持编辑请求参数（Query Params，Key-Value形式）
- 支持编辑请求体（JSON编辑区域，仅POST/PUT时展示）
- 支持发送请求并展示响应结果（状态码、响应头、响应体、耗时）
- 后端新增请求代理接口（前端不能直接跨域调目标服务器，需要后端转发）

## Capabilities

### New Capabilities

- api-debug: 在线调试请求发送、请求代理转发、响应结果展示

### Modified Capabilities

- api-detail: 接口详情页增加"在线调试"Tab或区域

## Impact

- Backend: 新增 DebugController 和 DebugService，引入 RestTemplate 或 OkHttp 做请求转发
- Frontend: 修改 ApiDetail.vue，新增调试区域组件
- Database: 无新增表
