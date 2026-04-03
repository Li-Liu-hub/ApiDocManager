<template>
  <div class="api-list">
    <el-button @click="goBack" style="margin-bottom: 10px;">返回项目列表</el-button>

    <el-row :gutter="20">
      <!-- 左侧分组树 -->
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>分组列表</span>
              <el-button type="primary" link size="small" @click="handleAddGroup">新增分组</el-button>
            </div>
          </template>

          <el-tree
            :data="groupList"
            :props="{ label: 'name', children: 'children' }"
            node-key="id"
            @node-click="handleGroupClick"
            default-expand-all
          >
            <template #default="{ node, data }">
              <span class="tree-node">
                <span>{{ node.label }}</span>
                <span class="tree-node-actions">
                  <el-button type="primary" link size="small" @click.stop="handleEditGroup(data)">编辑</el-button>
                  <el-button type="danger" link size="small" @click.stop="handleDeleteGroup(data)">删除</el-button>
                </span>
              </span>
            </template>
          </el-tree>
        </el-card>
      </el-col>

      <!-- 右侧接口列表 -->
      <el-col :span="18">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>接口列表{{ selectedGroup ? `- ${selectedGroup.name}` : '' }}</span>
              <el-button type="primary" @click="handleAddEndpoint" :disabled="!selectedGroup">新增接口</el-button>
            </div>
          </template>

          <el-table :data="endpointList" v-loading="loading" empty-text="暂无接口数据">
            <el-table-column label="请求方法" width="100">
              <template #default="{ row }">
                <MethodTag :method="row.method" />
              </template>
            </el-table-column>
            <el-table-column prop="path" label="路径" show-overflow-tooltip>
              <template #default="{ row }">
                <el-link type="primary" @click="goToDetail(row)">{{ row.path }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="summary" label="摘要" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button type="primary" link size="small" @click="handleEditEndpoint(row)">编辑</el-button>
                <el-button type="danger" link size="small" @click="handleDeleteEndpoint(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <GroupDialog
      v-model="groupDialogVisible"
      :group="currentGroup"
      :project-id="projectId"
      @success="loadGroups"
    />

    <EndpointDialog
      v-model="endpointDialogVisible"
      :endpoint="currentEndpoint"
      :project-id="projectId"
      :group-id="selectedGroup?.id"
      @success="loadEndpoints"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { apiGroupApi, apiEndpointApi } from '../api'
import GroupDialog from '../components/GroupDialog.vue'
import EndpointDialog from '../components/EndpointDialog.vue'
import MethodTag from '../components/MethodTag.vue'

const router = useRouter()
const route = useRoute()

const projectId = parseInt(route.params.id)
const groupList = ref([])
const endpointList = ref([])
const loading = ref(false)
const selectedGroup = ref(null)

const groupDialogVisible = ref(false)
const currentGroup = ref(null)

const endpointDialogVisible = ref(false)
const currentEndpoint = ref(null)

const loadGroups = async () => {
  try {
    const res = await apiGroupApi.listByProjectId(projectId)
    groupList.value = res.data || []
  } catch (error) {
    console.error('Failed to load groups:', error)
  }
}

const loadEndpoints = async () => {
  if (!selectedGroup.value) {
    endpointList.value = []
    return
  }

  loading.value = true
  try {
    const res = await apiEndpointApi.listByProjectId(projectId)
    endpointList.value = (res.data || []).filter(e => e.groupId === selectedGroup.value.id)
  } catch (error) {
    console.error('Failed to load endpoints:', error)
  } finally {
    loading.value = false
  }
}

const handleGroupClick = (data) => {
  selectedGroup.value = data
  loadEndpoints()
}

const handleAddGroup = () => {
  currentGroup.value = null
  groupDialogVisible.value = true
}

const handleEditGroup = (group) => {
  currentGroup.value = { ...group }
  groupDialogVisible.value = true
}

const handleDeleteGroup = async (group) => {
  try {
    await ElMessageBox.confirm(`确定删除分组"${group.name}"吗？删除后将级联删除该分组下的所有接口。`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await apiGroupApi.delete(group.id)
    ElMessage.success('删除成功')
    if (selectedGroup.value?.id === group.id) {
      selectedGroup.value = null
      endpointList.value = []
    }
    loadGroups()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete group:', error)
    }
  }
}

const handleAddEndpoint = () => {
  currentEndpoint.value = null
  endpointDialogVisible.value = true
}

const handleEditEndpoint = (endpoint) => {
  currentEndpoint.value = { ...endpoint }
  endpointDialogVisible.value = true
}

const handleDeleteEndpoint = async (endpoint) => {
  try {
    await ElMessageBox.confirm(`确定删除接口"${endpoint.path}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await apiEndpointApi.delete(endpoint.id)
    ElMessage.success('删除成功')
    loadEndpoints()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete endpoint:', error)
    }
  }
}

const goToDetail = (endpoint) => {
  router.push(`/apis/${endpoint.id}`)
}

const goBack = () => {
  router.push('/projects')
}

const getStatusType = (status) => {
  const types = { 1: 'success', 2: 'info', 3: 'warning' }
  return types[status] || 'info'
}

const getStatusLabel = (status) => {
  const labels = { 1: '已完成', 2: '开发中', 3: '已废弃' }
  return labels[status] || '未知'
}

onMounted(() => {
  loadGroups()
})
</script>

<style scoped>
.api-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.tree-node-actions {
  display: none;
}

.tree-node:hover .tree-node-actions {
  display: inline;
}
</style>
