<template>
  <div class="api-detail">
    <el-button @click="goBack" style="margin-bottom: 10px;">返回</el-button>

    <el-card v-loading="loading">
      <!-- 顶部信息 -->
      <div class="api-header">
        <div class="api-title">
          <MethodTag :method="endpoint.method" />
          <span class="api-path">{{ endpoint.path }}</span>
          <el-tag :type="getStatusType(endpoint.status)">{{ getStatusLabel(endpoint.status) }}</el-tag>
        </div>
      </div>

      <!-- 摘要和描述 -->
      <el-divider />
      <div class="api-section">
        <h4>摘要</h4>
        <p>{{ endpoint.summary || '无' }}</p>
      </div>

      <div class="api-section" v-if="endpoint.description">
        <h4>描述</h4>
        <p>{{ endpoint.description }}</p>
      </div>

      <!-- 请求参数 -->
      <div class="api-section" v-if="requestParams.length > 0">
        <h4>请求参数</h4>
        <el-table :data="requestParams" border stripe>
          <el-table-column prop="name" label="参数名" />
          <el-table-column prop="type" label="类型" width="100" />
          <el-table-column prop="required" label="必填" width="80">
            <template #default="{ row }">
              {{ row.required ? '是' : '否' }}
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述" />
          <el-table-column prop="example" label="示例值">
            <template #default="{ row }">
              <code>{{ row.example || '-' }}</code>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 请求体 -->
      <div class="api-section" v-if="endpoint.requestBody">
        <h4>请求体</h4>
        <pre class="json-block">{{ formatJson(endpoint.requestBody) }}</pre>
      </div>

      <!-- 响应体 -->
      <div class="api-section" v-if="endpoint.responseBody">
        <h4>响应体</h4>
        <pre class="json-block">{{ formatJson(endpoint.responseBody) }}</pre>
      </div>
    </el-card>

    <!-- 在线调试区域 -->
    <el-card class="debug-card">
      <template #header>
        <span>在线调试</span>
      </template>

      <!-- URL和方法 -->
      <div class="debug-url-row">
        <MethodTag :method="endpoint.method" />
        <el-input
          v-model="debugUrl"
          placeholder="请输入请求URL"
          class="url-input"
        />
      </div>

      <!-- 请求头编辑 -->
      <div class="debug-section">
        <div class="section-header">
          <span>请求头</span>
          <el-button type="primary" link size="small" @click="addHeader">添加</el-button>
        </div>
        <el-table :data="debugHeaders" border size="small">
          <el-table-column label="Key">
            <template #default="{ row }">
              <el-input v-model="row.key" placeholder="Key" />
            </template>
          </el-table-column>
          <el-table-column label="Value">
            <template #default="{ row }">
              <el-input v-model="row.value" placeholder="Value" />
            </template>
          </el-table-column>
          <el-table-column width="60">
            <template #default="{ $index }">
              <el-button type="danger" link size="small" @click="removeHeader($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 请求参数编辑 -->
      <div class="debug-section">
        <div class="section-header">
          <span>请求参数</span>
          <el-button type="primary" link size="small" @click="addParam">添加</el-button>
        </div>
        <el-table :data="debugParams" border size="small">
          <el-table-column label="Key">
            <template #default="{ row }">
              <el-input v-model="row.key" placeholder="Key" />
            </template>
          </el-table-column>
          <el-table-column label="Value">
            <template #default="{ row }">
              <el-input v-model="row.value" placeholder="Value" />
            </template>
          </el-table-column>
          <el-table-column width="60">
            <template #default="{ $index }">
              <el-button type="danger" link size="small" @click="removeParam($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 请求体编辑 -->
      <div class="debug-section" v-if="showRequestBody">
        <div class="section-header">
          <span>请求体</span>
        </div>
        <el-input
          v-model="debugBody"
          type="textarea"
          :rows="6"
          placeholder="请输入请求体JSON"
        />
      </div>

      <!-- 发送按钮 -->
      <div class="debug-section">
        <el-button
          type="primary"
          :loading="sending"
          @click="sendRequest"
        >
          {{ sending ? '请求中...' : '发送请求' }}
        </el-button>
      </div>

      <!-- 响应结果 -->
      <div class="debug-section">
        <div class="section-header">
          <span>响应结果</span>
        </div>
        <div v-if="!hasResponse" class="response-empty">
          点击发送请求查看响应结果
        </div>
        <div v-else class="response-content">
          <div class="response-status">
            <el-tag :type="getResponseStatusType(debugResponse.statusCode)">
              {{ debugResponse.statusCode || 'Error' }}
            </el-tag>
            <span class="duration">{{ debugResponse.duration }}ms</span>
          </div>

          <div class="response-headers" v-if="debugResponse.headers">
            <h5>响应头</h5>
            <div class="kv-list">
              <div v-for="(value, key) in debugResponse.headers" :key="key" class="kv-item">
                <span class="kv-key">{{ key }}:</span>
                <span class="kv-value">{{ value }}</span>
              </div>
            </div>
          </div>

          <div class="response-body" v-if="debugResponse.body">
            <h5>响应体</h5>
            <pre class="response-body-content">{{ formatResponseBody(debugResponse.body) }}</pre>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { apiEndpointApi, debugApi, projectApi } from '../api'
import MethodTag from '../components/MethodTag.vue'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const endpoint = ref({})

// 调试相关数据
const debugUrl = ref('')
const debugHeaders = ref([
  { key: 'Content-Type', value: 'application/json' }
])
const debugParams = ref([])
const debugBody = ref('')
const sending = ref(false)
const hasResponse = ref(false)
const debugResponse = ref({})

const requestParams = computed(() => {
  if (!endpoint.value.requestParams) return []
  try {
    const params = JSON.parse(endpoint.value.requestParams)
    if (Array.isArray(params)) {
      return params
    }
    return []
  } catch {
    return []
  }
})

const showRequestBody = computed(() => {
  return endpoint.value.method === 'POST' || endpoint.value.method === 'PUT'
})

const loadEndpoint = async () => {
  const id = parseInt(route.params.id)
  loading.value = true
  try {
    const res = await apiEndpointApi.getById(id)
    endpoint.value = res.data || {}
    initDebugData()
  } catch (error) {
    ElMessage.error('加载接口详情失败')
    console.error('Failed to load endpoint:', error)
  } finally {
    loading.value = false
  }
}

const initDebugData = () => {
  // 自动拼接URL
  if (endpoint.value.projectId) {
    // 获取项目信息来获取base_url
    loadProjectBaseUrl()
  }

  // 初始化请求参数
  debugParams.value = requestParams.value.map(p => ({
    key: p.name || '',
    value: p.example || ''
  }))

  // 初始化请求体
  if (showRequestBody.value && endpoint.value.requestBody) {
    debugBody.value = formatJson(endpoint.value.requestBody)
  }
}

const loadProjectBaseUrl = async () => {
  try {
    const res = await projectApi.getById(endpoint.value.projectId)
    const project = res.data
    if (project && project.baseUrl && endpoint.value.path) {
      // 拼接 baseUrl 和 path
      let baseUrl = project.baseUrl.replace(/\/$/, '') // 去除末尾斜杠
      let path = endpoint.value.path
      if (!path.startsWith('/')) {
        path = '/' + path
      }
      debugUrl.value = baseUrl + path
    } else if (endpoint.value.path) {
      debugUrl.value = endpoint.value.path
    }
  } catch (error) {
    console.error('Failed to load project baseUrl:', error)
    if (endpoint.value.path) {
      debugUrl.value = endpoint.value.path
    }
  }
}

const addHeader = () => {
  debugHeaders.value.push({ key: '', value: '' })
}

const removeHeader = (index) => {
  debugHeaders.value.splice(index, 1)
}

const addParam = () => {
  debugParams.value.push({ key: '', value: '' })
}

const removeParam = (index) => {
  debugParams.value.splice(index, 1)
}

const sendRequest = async () => {
  if (!debugUrl.value.trim()) {
    ElMessage.error('请输入请求URL')
    return
  }

  // 转换headers为Map
  const headers = {}
  debugHeaders.value.forEach(item => {
    if (item.key && item.key.trim()) {
      headers[item.key.trim()] = item.value || ''
    }
  })

  // 转换params为Map，过滤空key
  const params = {}
  debugParams.value.forEach(item => {
    if (item.key && item.key.trim()) {
      params[item.key.trim()] = item.value || ''
    }
  })

  sending.value = true
  hasResponse.value = false

  try {
    const res = await debugApi.send({
      method: endpoint.value.method,
      url: debugUrl.value,
      headers,
      params,
      body: showRequestBody.value ? debugBody.value : undefined
    })
    debugResponse.value = res.data || {}
    hasResponse.value = true
  } catch (error) {
    ElMessage.error(error.message || '请求失败')
    console.error('Debug request failed:', error)
  } finally {
    sending.value = false
  }
}

const goBack = () => {
  if (endpoint.value.projectId) {
    router.push(`/projects/${endpoint.value.projectId}/apis`)
  } else {
    router.back()
  }
}

const getStatusType = (status) => {
  const types = { 1: 'success', 2: 'info', 3: 'warning' }
  return types[status] || 'info'
}

const getStatusLabel = (status) => {
  const labels = { 1: '已完成', 2: '开发中', 3: '已废弃' }
  return labels[status] || '未知'
}

const getResponseStatusType = (statusCode) => {
  if (!statusCode) return 'danger'
  if (statusCode >= 200 && statusCode < 300) return 'success'
  if (statusCode >= 400 && statusCode < 500) return 'warning'
  if (statusCode >= 500) return 'danger'
  return 'info'
}

const formatJson = (str) => {
  if (!str) return ''
  try {
    return JSON.stringify(JSON.parse(str), null, 2)
  } catch {
    return str
  }
}

const formatResponseBody = (body) => {
  if (!body) return ''
  try {
    return JSON.stringify(JSON.parse(body), null, 2)
  } catch {
    return body
  }
}

onMounted(() => {
  loadEndpoint()
})
</script>

<style scoped>
.api-detail {
  padding: 20px;
}

.api-header {
  margin-bottom: 20px;
}

.api-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.api-path {
  font-size: 18px;
  font-weight: bold;
}

.api-section {
  margin-bottom: 20px;
}

.api-section h4 {
  margin-bottom: 10px;
  color: #606266;
}

.api-section p {
  color: #303133;
  line-height: 1.6;
}

.json-block {
  background-color: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 15px;
  overflow-x: auto;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.5;
}

.debug-card {
  margin-top: 20px;
}

.debug-url-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.url-input {
  flex: 1;
}

.debug-section {
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.section-header span {
  font-weight: 500;
  color: #606266;
}

.response-empty {
  color: #909399;
  text-align: center;
  padding: 40px 0;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.response-content {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 15px;
}

.response-status {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.duration {
  color: #606266;
  font-size: 14px;
}

.response-headers {
  margin-bottom: 15px;
}

.response-headers h5,
.response-body h5 {
  margin: 0 0 10px 0;
  color: #606266;
  font-size: 14px;
}

.kv-list {
  font-size: 13px;
}

.kv-item {
  padding: 4px 0;
  word-break: break-all;
}

.kv-key {
  color: #409eff;
  font-weight: 500;
}

.kv-value {
  color: #303133;
  margin-left: 5px;
}

.response-body {
  margin-top: 15px;
}

.response-body-content {
  background-color: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 15px;
  margin: 0;
  max-height: 300px;
  overflow-y: auto;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-all;
}
</style>
