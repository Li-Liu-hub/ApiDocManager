## Why

团队开发中接口文档管理混乱，文档散落在飞书、Notion、聊天记录里，
前后端协作时经常找不到最新版本的接口定义，导致联调效率低、接口理解有偏差。
需要一个轻量的接口文档管理工具，让团队在一个地方统一维护API文档。

## What Changes

- 新增项目管理功能（创建、编辑、删除项目）
- 新增接口分组管理功能（在项目下按模块分组）
- 新增接口定义管理功能（定义请求方法、路径、参数、请求体、响应体）
- 新增接口详情展示（请求参数表格、JSON语法高亮）
- 前端三个页面：项目列表页、接口列表页、接口详情页

## Capabilities

### New Capabilities

- project-management: 项目的增删改查
- api-group-management: 接口分组的增删改查
- api-endpoint-management: 接口定义的增删改查和详情展示

## Impact

- Backend: 新增3个Controller、3个Service、3个Mapper、3个Entity、通用响应封装、全局异常处理、跨域配置
- Frontend: 新增3个页面、4个组件、API调用模块、路由配置
- Database: 3张表（project、api_group、api_endpoint）


