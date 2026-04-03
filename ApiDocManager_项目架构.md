# ApiDocManager 项目架构

## 基本信息
- **项目名称：** ApiDocManager（API接口文档管理工具）
- **数据库名称：** api_doc_manager
- **后端项目名：** apidoc-backend
- **前端项目名：** apidoc-frontend

## 技术栈
- 后端：Spring Boot 3.x + MyBatis-Plus + MySQL 8.0
- 前端：Vue 3 + Element Plus + Axios + Vue Router
- 构建：Maven（后端）+ Vite（前端）

## 数据库设计（3张表）

```
project（项目表）
├── id              主键
├── name            项目名称（唯一）
├── description     项目描述
├── base_url        基础URL
├── status          状态：1启用 0禁用
├── created_at      创建时间
└── updated_at      更新时间

api_group（接口分组表）
├── id              主键
├── project_id      所属项目ID
├── name            分组名称
├── sort_order      排序号
└── created_at      创建时间

api_endpoint（接口表）
├── id              主键
├── project_id      所属项目ID
├── group_id        所属分组ID
├── method          请求方法 GET/POST/PUT/DELETE
├── path            接口路径
├── summary         接口摘要
├── description     详细描述
├── request_headers 请求头（JSON）
├── request_params  请求参数（JSON）
├── request_body    请求体示例（JSON）
├── response_body   响应体示例（JSON）
├── status          状态：1已完成 2开发中 3已废弃
├── created_at      创建时间
└── updated_at      更新时间
```

## 后端目录结构

```
apidoc-backend/
├── pom.xml
└── src/main/java/com/apidoc/
    ├── ApiDocApplication.java              # 启动类
    ├── common/
    │   ├── result/
    │   │   └── Result.java                 # 统一响应封装
    │   └── exception/
    │       ├── BusinessException.java      # 业务异常
    │       └── GlobalExceptionHandler.java # 全局异常处理
    ├── config/
    │   ├── CorsConfig.java                 # 跨域配置
    │   └── MyBatisPlusConfig.java          # MP分页插件配置
    ├── entity/
    │   ├── Project.java
    │   ├── ApiGroup.java
    │   └── ApiEndpoint.java
    ├── mapper/
    │   ├── ProjectMapper.java
    │   ├── ApiGroupMapper.java
    │   └── ApiEndpointMapper.java
    ├── service/
    │   ├── ProjectService.java
    │   ├── ApiGroupService.java
    │   ├── ApiEndpointService.java
    │   └── impl/
    │       ├── ProjectServiceImpl.java
    │       ├── ApiGroupServiceImpl.java
    │       └── ApiEndpointServiceImpl.java
    └── controller/
        ├── ProjectController.java          # /api/project/*
        ├── ApiGroupController.java         # /api/group/*
        └── ApiEndpointController.java      # /api/endpoint/*
```

## 前端目录结构

```
apidoc-frontend/
├── package.json
├── vite.config.js
├── index.html
└── src/
    ├── main.js
    ├── App.vue
    ├── api/
    │   ├── axios.js                        # axios实例配置
    │   └── index.js                        # API接口定义
    ├── router/
    │   └── index.js                        # 路由配置
    ├── views/
    │   ├── ProjectList.vue                 # 项目列表页
    │   ├── ApiList.vue                     # 接口列表页（某项目下）
    │   └── ApiDetail.vue                   # 接口详情页
    └── components/
        ├── ProjectDialog.vue               # 项目新增/编辑弹窗
        ├── GroupDialog.vue                 # 分组新增/编辑弹窗
        ├── EndpointDialog.vue              # 接口新增/编辑弹窗
        └── MethodTag.vue                   # 请求方法标签组件（GET绿/POST蓝/PUT橙/DELETE红）
```

## 后端API设计

### 项目管理
| 方法   | 路径                  | 说明         |
|--------|----------------------|-------------|
| GET    | /api/project/list    | 项目列表     |
| GET    | /api/project/{id}    | 项目详情     |
| POST   | /api/project         | 新增项目     |
| PUT    | /api/project/{id}    | 修改项目     |
| DELETE | /api/project/{id}    | 删除项目     |

### 接口分组管理
| 方法   | 路径                          | 说明             |
|--------|------------------------------|-----------------|
| GET    | /api/group/list/{projectId}  | 某项目的分组列表  |
| POST   | /api/group                   | 新增分组         |
| PUT    | /api/group/{id}              | 修改分组         |
| DELETE | /api/group/{id}              | 删除分组         |

### 接口管理
| 方法   | 路径                              | 说明                 |
|--------|----------------------------------|---------------------|
| GET    | /api/endpoint/page               | 分页查询接口         |
| GET    | /api/endpoint/list/{projectId}   | 某项目的全部接口      |
| GET    | /api/endpoint/{id}               | 接口详情             |
| POST   | /api/endpoint                    | 新增接口             |
| PUT    | /api/endpoint/{id}               | 修改接口             |
| DELETE | /api/endpoint/{id}               | 删除接口             |

## 前端页面流程

```
项目列表页（ProjectList.vue）
  ├── 展示所有项目卡片/列表
  ├── 新增项目（弹窗）
  ├── 编辑项目（弹窗）
  ├── 删除项目
  └── 点击项目 → 进入接口列表页

接口列表页（ApiList.vue）
  ├── 左侧：分组树（可增删改分组）
  ├── 右侧：当前分组下的接口列表
  ├── 新增接口（弹窗）
  ├── 编辑接口（弹窗）
  ├── 删除接口
  └── 点击接口 → 进入接口详情页

接口详情页（ApiDetail.vue）
  ├── 展示接口基本信息（方法、路径、摘要）
  ├── 展示请求参数表格
  ├── 展示请求体JSON（带语法高亮）
  └── 展示响应体JSON（带语法高亮）
```

## 端口配置
- 后端：8081
- 前端：5173（Vite默认）
- 数据库：3306
