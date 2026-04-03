## ADDED Requirements

### Requirement: 调试区域展示

系统 SHALL 在接口详情页展示"在线调试"区域。

#### Scenario: 进入接口详情页展示调试区域

- **GIVEN** 用户进入某个接口的详情页
- **WHEN** 页面加载完成
- **THEN** 在接口基本信息下方展示"在线调试"区域
- **AND** 请求URL自动拼接为：项目基础URL + 接口路径
- **AND** 请求方法自动填充为该接口定义的方法

---

### Requirement: 请求URL编辑

系统 SHALL 允许用户编辑请求URL。

#### Scenario: 自动拼接URL

- **GIVEN** 项目基础URL为 http://localhost:8080
- **AND** 接口路径为 /api/users/{id}
- **WHEN** 调试区域加载
- **THEN** 请求URL输入框展示 http://localhost:8080/api/users/{id}

#### Scenario: 手动修改URL

- **GIVEN** 调试区域已展示请求URL
- **WHEN** 用户手动修改URL内容
- **THEN** 系统使用用户修改后的URL发送请求

#### Scenario: 路径参数替换

- **GIVEN** 请求URL包含路径参数 {id}
- **WHEN** 用户将 {id} 替换为 1
- **THEN** 实际请求URL变为 http://localhost:8080/api/users/1

---

### Requirement: 请求头编辑

系统 SHALL 允许用户以Key-Value形式编辑请求头。

#### Scenario: 默认请求头

- **GIVEN** 调试区域加载
- **WHEN** 用户查看请求头区域
- **THEN** 默认存在一行 Content-Type: application/json

#### Scenario: 添加请求头

- **GIVEN** 用户在请求头区域
- **WHEN** 用户点击"添加"按钮
- **THEN** 新增一行空的Key-Value输入行

#### Scenario: 删除请求头

- **GIVEN** 请求头区域有多行
- **WHEN** 用户点击某行的删除按钮
- **THEN** 该行被移除

---

### Requirement: 请求参数编辑

系统 SHALL 允许用户以Key-Value形式编辑Query参数。

#### Scenario: 自动填充已定义的参数

- **GIVEN** 接口定义中有请求参数（request_params字段）
- **WHEN** 调试区域加载
- **THEN** 系统从接口定义中解析参数名和描述
- **AND** 自动填充到参数列表中，值为空待用户填写

#### Scenario: 手动添加参数

- **GIVEN** 用户在请求参数区域
- **WHEN** 用户点击"添加"按钮
- **THEN** 新增一行空的Key-Value输入行

#### Scenario: 参数拼接到URL

- **GIVEN** 用户填写了参数 page=1 和 size=10
- **WHEN** 用户发送请求
- **THEN** 实际请求URL拼接为 ...?page=1&size=10

---

### Requirement: 请求体编辑

系统 SHALL 在POST和PUT方法时展示请求体编辑区域。

#### Scenario: POST/PUT方法展示请求体

- **GIVEN** 接口方法为 POST 或 PUT
- **WHEN** 调试区域加载
- **THEN** 展示请求体JSON编辑区域

#### Scenario: GET/DELETE方法隐藏请求体

- **GIVEN** 接口方法为 GET 或 DELETE
- **WHEN** 调试区域加载
- **THEN** 不展示请求体编辑区域

#### Scenario: 自动填充请求体示例

- **GIVEN** 接口定义中有请求体示例（request_body字段）
- **AND** 接口方法为 POST 或 PUT
- **WHEN** 调试区域加载
- **THEN** 请求体编辑区域自动填充接口定义中的示例JSON

#### Scenario: 用户编辑请求体

- **GIVEN** 请求体编辑区域已展示
- **WHEN** 用户修改JSON内容
- **THEN** 系统使用用户修改后的JSON作为请求体

---

### Requirement: 发送请求

系统 SHALL 通过后端代理转发HTTP请求到目标服务器。

#### Scenario: 发送请求成功

- **GIVEN** 用户已填写请求URL和必要参数
- **WHEN** 用户点击"发送请求"按钮
- **THEN** 按钮变为加载状态，展示"请求中..."
- **AND** 系统将请求信息发送到后端代理接口
- **AND** 后端代理转发请求到目标URL
- **AND** 请求完成后按钮恢复正常状态

#### Scenario: 请求URL为空

- **GIVEN** 请求URL输入框为空
- **WHEN** 用户点击"发送请求"
- **THEN** 系统提示"请输入请求URL"
- **AND** 不发送请求

#### Scenario: 请求超时

- **GIVEN** 目标服务器响应超过10秒
- **WHEN** 等待超时
- **THEN** 系统展示错误提示"请求超时"
- **AND** 按钮恢复正常状态

#### Scenario: 目标服务器不可达

- **GIVEN** 目标URL的服务器未启动或地址错误
- **WHEN** 用户发送请求
- **THEN** 系统展示错误提示"无法连接到目标服务器"

---

### Requirement: 响应结果展示

系统 SHALL 在发送请求后展示完整的响应信息。

#### Scenario: 展示响应状态码

- **GIVEN** 请求已完成
- **WHEN** 响应返回
- **THEN** 展示HTTP状态码
- **AND** 2xx 状态码显示绿色
- **AND** 4xx 状态码显示橙色
- **AND** 5xx 状态码显示红色

#### Scenario: 展示响应耗时

- **GIVEN** 请求已完成
- **WHEN** 响应返回
- **THEN** 展示请求耗时（单位毫秒，如 "128ms"）

#### Scenario: 展示响应头

- **GIVEN** 请求已完成
- **WHEN** 响应返回
- **THEN** 以Key-Value列表形式展示响应头

#### Scenario: 展示响应体

- **GIVEN** 请求已完成
- **WHEN** 响应返回
- **THEN** 展示响应体内容
- **AND** 如果是JSON格式则格式化展示（缩进美化）
- **AND** 如果不是JSON则原样展示

#### Scenario: 无响应时展示空状态

- **GIVEN** 用户刚进入调试区域还未发送请求
- **WHEN** 查看响应区域
- **THEN** 展示提示"点击发送请求查看响应结果"
