## 1. 后端基础骨架

- [ ] 1.1 创建 Spring Boot 项目骨架，配置 pom.xml（spring-boot-starter-web、mybatis-plus-boot-starter、mysql-connector、lombok）
- [ ] 1.2 配置 application.yml（数据源连接 api_doc_manager、端口8081、MP配置）
- [ ] 1.3 创建启动类 ApiDocApplication.java
- [ ] 1.4 创建 Result.java 统一响应封装（code、msg、data）
- [ ] 1.5 创建 BusinessException.java 和 GlobalExceptionHandler.java
- [ ] 1.6 创建 CorsConfig.java 跨域配置（允许5173端口）
- [ ] 1.7 创建 MyBatisPlusConfig.java 分页插件配置

## 2. 后端实体和Mapper

- [ ] 2.1 创建 Project 实体类（@TableName("project")，字段映射）
- [ ] 2.2 创建 ApiGroup 实体类（@TableName("api_group")，字段映射）
- [ ] 2.3 创建 ApiEndpoint 实体类（@TableName("api_endpoint")，字段映射）
- [ ] 2.4 创建 ProjectMapper、ApiGroupMapper、ApiEndpointMapper

## 3. 后端Service - 项目管理

- [ ] 3.1 创建 ProjectService 接口（list、getById、create、update、delete）
- [ ] 3.2 创建 ProjectServiceImpl 实现
- [ ] 3.3 create 时校验项目名称唯一，重复抛出 BusinessException
- [ ] 3.4 delete 时级联删除该项目下所有分组和接口

## 4. 后端Service - 分组管理

- [ ] 4.1 创建 ApiGroupService 接口（listByProjectId、create、update、delete）
- [ ] 4.2 创建 ApiGroupServiceImpl 实现
- [ ] 4.3 delete 时级联删除该分组下所有接口

## 5. 后端Service - 接口管理

- [ ] 5.1 创建 ApiEndpointService 接口（page、listByProjectId、listByGroupId、getById、create、update、delete）
- [ ] 5.2 创建 ApiEndpointServiceImpl 实现
- [ ] 5.3 create 时校验同项目下 method + path 唯一，重复抛出 BusinessException

## 6. 后端Controller - 项目管理

- [ ] 6.1 创建 ProjectController（/api/project/*）
- [ ] 6.2 实现 GET /api/project/list 查询所有项目
- [ ] 6.3 实现 GET /api/project/{id} 查询项目详情
- [ ] 6.4 实现 POST /api/project 新增项目
- [ ] 6.5 实现 PUT /api/project/{id} 修改项目
- [ ] 6.6 实现 DELETE /api/project/{id} 删除项目（级联删除）

## 7. 后端Controller - 分组管理

- [ ] 7.1 创建 ApiGroupController（/api/group/*）
- [ ] 7.2 实现 GET /api/group/list/{projectId} 查询某项目的分组列表
- [ ] 7.3 实现 POST /api/group 新增分组
- [ ] 7.4 实现 PUT /api/group/{id} 修改分组
- [ ] 7.5 实现 DELETE /api/group/{id} 删除分组（级联删除接口）

## 8. 后端Controller - 接口管理

- [ ] 8.1 创建 ApiEndpointController（/api/endpoint/*）
- [ ] 8.2 实现 GET /api/endpoint/page 分页查询接口（支持按projectId、groupId、method筛选）
- [ ] 8.3 实现 GET /api/endpoint/list/{projectId} 查询某项目全部接口
- [ ] 8.4 实现 GET /api/endpoint/{id} 查询接口详情
- [ ] 8.5 实现 POST /api/endpoint 新增接口
- [ ] 8.6 实现 PUT /api/endpoint/{id} 修改接口
- [ ] 8.7 实现 DELETE /api/endpoint/{id} 删除接口

## 9. 后端验证

- [ ] 9.1 启动后端，访问接口确认数据库连接正常
- [ ] 9.2 用Postman或curl测试 GET /api/project/list 返回测试数据
- [ ] 9.3 测试新增项目名称重复时返回错误提示
- [ ] 9.4 测试删除项目后分组和接口同步被删除

## 10. 前端基础骨架

- [ ] 10.1 创建 Vue 3 + Vite 项目，安装 element-plus、axios、vue-router
- [ ] 10.2 配置 main.js（引入 Element Plus）
- [ ] 10.3 创建 api/axios.js（baseURL 指向 http://localhost:8081，响应拦截器处理错误提示）
- [ ] 10.4 创建 api/index.js（定义 projectApi、groupApi、endpointApi）
- [ ] 10.5 创建 router/index.js（三个路由：/projects、/projects/:id/apis、/apis/:id）

## 11. 前端 - 项目列表页

- [ ] 11.1 创建 ProjectList.vue 基础骨架（el-card 包裹）
- [ ] 11.2 实现项目列表展示（el-table，列：项目名称、描述、基础URL、状态、创建时间、操作）
- [ ] 11.3 实现"新增项目"按钮，点击弹出 ProjectDialog
- [ ] 11.4 创建 ProjectDialog.vue（新增/编辑共用弹窗，表单：名称必填、描述、基础URL）
- [ ] 11.5 实现新增提交：调用 projectApi.create，成功后关闭弹窗刷新列表
- [ ] 11.6 实现编辑：点击编辑按钮回填数据，提交调用 projectApi.update
- [ ] 11.7 实现删除：确认弹窗后调用 projectApi.delete
- [ ] 11.8 实现点击项目名称跳转到接口列表页（router.push）
- [ ] 11.9 实现无数据时展示空状态

## 12. 前端 - 接口列表页

- [ ] 12.1 创建 ApiList.vue 基础骨架（左右布局：左侧分组树 + 右侧接口列表）
- [ ] 12.2 左侧实现分组树展示（调用 groupApi.listByProjectId）
- [ ] 12.3 左侧实现"新增分组"按钮，弹出 GroupDialog
- [ ] 12.4 创建 GroupDialog.vue（分组名称输入弹窗）
- [ ] 12.5 左侧实现分组编辑和删除（删除带确认弹窗）
- [ ] 12.6 实现点击分组切换右侧接口列表（调用 endpointApi 按 groupId 筛选）
- [ ] 12.7 右侧实现接口列表表格（列：请求方法标签、路径、摘要、状态标签、操作）
- [ ] 12.8 创建 MethodTag.vue 组件（GET绿/POST蓝/PUT橙/DELETE红）
- [ ] 12.9 右侧实现"新增接口"按钮，弹出 EndpointDialog
- [ ] 12.10 创建 EndpointDialog.vue（表单：方法下拉、路径必填、摘要必填、描述、请求参数JSON、请求体JSON、响应体JSON、状态下拉）
- [ ] 12.11 实现接口编辑和删除
- [ ] 12.12 实现点击接口名称跳转到接口详情页
- [ ] 12.13 实现"返回项目列表"按钮

## 13. 前端 - 接口详情页

- [ ] 13.1 创建 ApiDetail.vue 基础骨架
- [ ] 13.2 顶部展示：MethodTag + 接口路径 + 状态标签
- [ ] 13.3 展示接口摘要和详细描述
- [ ] 13.4 展示请求参数表格（从JSON解析为表格：参数名、类型、是否必填、描述、示例值）
- [ ] 13.5 展示请求体JSON（pre标签 + JSON.stringify格式化 + 简单语法高亮样式）
- [ ] 13.6 展示响应体JSON（同上）
- [ ] 13.7 实现"返回"按钮回到接口列表页
- [ ] 13.8 接口状态标签样式（已完成绿/开发中蓝/已废弃灰）

## 14. 前端 - App.vue和全局样式

- [ ] 14.1 App.vue 配置 router-view
- [ ] 14.2 全局样式：body边距、字体、背景色

## 15. 联调验证（对照 delta spec）

- [ ] 15.1 验证项目列表加载正常，无项目时展示空状态
- [ ] 15.2 验证新增项目成功、名称重复报错、名称为空校验不通过
- [ ] 15.3 验证编辑项目回填数据并提交成功
- [ ] 15.4 验证删除项目弹出确认，确认后级联删除
- [ ] 15.5 验证点击项目进入接口列表页，左侧分组树展示正确
- [ ] 15.6 验证新增/编辑/删除分组正常
- [ ] 15.7 验证点击分组切换右侧接口列表
- [ ] 15.8 验证新增接口成功、方法+路径重复报错
- [ ] 15.9 验证编辑接口回填数据并提交成功
- [ ] 15.10 验证删除接口弹出确认后删除成功
- [ ] 15.11 验证点击接口进入详情页，展示完整信息
- [ ] 15.12 验证请求方法标签颜色：GET绿/POST蓝/PUT橙/DELETE红
- [ ] 15.13 验证接口状态标签颜色：已完成绿/开发中蓝/已废弃灰
- [ ] 15.14 验证JSON格式化展示正常
- [ ] 15.15 验证详情页"返回"按钮回到接口列表页
