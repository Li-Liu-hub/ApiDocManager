<template>
  <div class="project-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目管理</span>
          <el-button type="primary" @click="handleAdd">新增项目</el-button>
        </div>
      </template>

      <el-table :data="projectList" v-loading="loading" empty-text="暂无项目数据">
        <el-table-column prop="name" label="项目名称">
          <template #default="{ row }">
            <el-link type="primary" @click="goToApiList(row)">{{ row.name }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="baseUrl" label="基础URL" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <ProjectDialog
      v-model="dialogVisible"
      :project="currentProject"
      @success="loadProjects"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { projectApi } from '../api'
import ProjectDialog from '../components/ProjectDialog.vue'

const router = useRouter()
const projectList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const currentProject = ref(null)

const loadProjects = async () => {
  loading.value = true
  try {
    const res = await projectApi.list()
    projectList.value = res.data || []
  } catch (error) {
    console.error('Failed to load projects:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  currentProject.value = null
  dialogVisible.value = true
}

const handleEdit = (project) => {
  currentProject.value = { ...project }
  dialogVisible.value = true
}

const handleDelete = async (project) => {
  try {
    await ElMessageBox.confirm(`确定删除项目"${project.name}"吗？删除后将级联删除该项目的所有分组和接口。`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await projectApi.delete(project.id)
    ElMessage.success('删除成功')
    loadProjects()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete project:', error)
    }
  }
}

const goToApiList = (project) => {
  router.push(`/projects/${project.id}/apis`)
}

const getStatusType = (status) => {
  const types = { 1: 'success', 2: 'info', 3: 'warning' }
  return types[status] || 'info'
}

const getStatusLabel = (status) => {
  const labels = { 1: '已完成', 2: '开发中', 3: '已废弃' }
  return labels[status] || '未知'
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

onMounted(() => {
  loadProjects()
})
</script>

<style scoped>
.project-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
