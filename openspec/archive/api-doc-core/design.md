## Context

这是一个从零开始的新项目，后端使用 Spring Boot 3.x + MyBatis-Plus + MySQL 8.0，
前端使用 Vue 3 + Element Plus + Axios + Vue Router。
数据库已建好3张表（project、api_group、api_endpoint），含测试数据。
后端端口8081，前端端口5173。

## Goals / Non-Goals

Goals:

- 实现项目、分组、接口的完整CRUD
- 前端3个页面 + 4个弹窗组件
- 接口详情页支持JSON语法高亮展示
- 请求方法和接口状态用彩色标签区分
- 前后端跨域配置

Non-Goals:

- 本次不做用户登录和权限控制
- 本次不做接口导入导出（Swagger/OpenAPI格式）
- 本次不做Mock数据生成
- 本次不做接口调试（在线发送请求）
- 本次不做国际化（只做中文）

## Decisions

### Decision: 不做登录系统

原因：这是一个内部工具，核心价值是接口文档管理，
登录系统增加开发量但对核心功能无帮助，后续迭代再加
备选方案：做简单的用户名密码登录
不采用原因：今天要出展示效果，时间不够

### Decision: 接口参数存储为JSON字符串而不是独立子表

原因：请求参数、请求头、请求体、响应体结构各不相同且灵活多变，
用JSON字符串存储最灵活，前端直接解析展示
备选方案：拆成 api_param 子表（id, endpoint_id, name, type, required, description）
不采用原因：增加表数量和关联查询复杂度，对于文档管理场景JSON够用

### Decision: 接口详情用独立页面而不是弹窗

原因：接口详情信息量大（参数表格 + 请求体JSON + 响应体JSON），
弹窗展示空间不够，独立页面体验更好
备选方案：用抽屉（el-drawer）侧滑展示
不采用原因：JSON内容可能很长，抽屉宽度受限

### Decision: JSON高亮使用 pre + 手动格式化而不是引入代码编辑器

原因：只需要展示不需要编辑，用 JSON.stringify 格式化后放在 pre 标签里，
配合简单的CSS样式就够了
备选方案：引入 Monaco Editor 或 CodeMirror
不采用原因：引入重量级编辑器只为了展示JSON，杀鸡用牛刀

### Decision: 前端路由使用 history 模式

原因：URL更美观，/projects、/projects/1/apis、/apis/1 比 /#/projects 好看
备选方案：hash模式
不采用原因：项目不需要兼容老浏览器

### Decision: 删除项目时级联删除分组和接口

原因：孤立的分组和接口没有意义，级联删除保持数据一致性
备选方案：软删除或禁止删除有内容的项目
不采用原因：工具类项目不需要回收站机制，简单直接

## Risks / Trade-offs

- JSON字段存储灵活但失去了数据库层面的字段校验 → 前端做格式校验兜底
- 不做登录意味着任何人都能操作 → 内部工具可接受，后续加登录
- 级联删除不可恢复 → 删除前弹确认弹窗提醒用户
- pre标签展示JSON没有折叠能力 → 对于文档展示场景够用

## Files to Create

### 后端

- ApiDocApplication.java（启动类）
- common/result/Result.java（统一响应）
- common/exception/BusinessException.java（业务异常）
- common/exception/GlobalExceptionHandler.java（全局异常处理）
- config/CorsConfig.java（跨域配置）
- config/MyBatisPlusConfig.java（MP分页插件）
- entity/Project.java, ApiGroup.java, ApiEndpoint.java
- mapper/ProjectMapper.java, ApiGroupMapper.java, ApiEndpointMapper.java
- service/ProjectService.java, ApiGroupService.java, ApiEndpointService.java
- service/impl/ProjectServiceImpl.java, ApiGroupServiceImpl.java, ApiEndpointServiceImpl.java
- controller/ProjectController.java, ApiGroupController.java, ApiEndpointController.java

### 前端

- api/axios.js, api/index.js
- router/index.js
- views/ProjectList.vue, ApiList.vue, ApiDetail.vue
- components/ProjectDialog.vue, GroupDialog.vue, EndpointDialog.vue, MethodTag.vue
- App.vue, main.js

## Migration Plan

无，全新项目从零开始。
