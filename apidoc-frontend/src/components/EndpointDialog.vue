<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑接口' : '新增接口'"
    width="700px"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="请求方法" prop="method">
        <el-select v-model="form.method" placeholder="请选择请求方法" style="width: 100%">
          <el-option label="GET" value="GET" />
          <el-option label="POST" value="POST" />
          <el-option label="PUT" value="PUT" />
          <el-option label="DELETE" value="DELETE" />
        </el-select>
      </el-form-item>
      <el-form-item label="路径" prop="path">
        <el-input v-model="form.path" placeholder="请输入接口路径，如 /user/{id}" />
      </el-form-item>
      <el-form-item label="摘要" prop="summary">
        <el-input v-model="form.summary" placeholder="请输入接口摘要" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入接口描述" />
      </el-form-item>
      <el-form-item label="请求头">
        <el-input v-model="form.requestHeaders" type="textarea" :rows="3" placeholder="请求头 JSON 格式" />
      </el-form-item>
      <el-form-item label="请求参数">
        <el-input v-model="form.requestParams" type="textarea" :rows="4" placeholder="请求参数 JSON 格式" />
      </el-form-item>
      <el-form-item label="请求体">
        <el-input v-model="form.requestBody" type="textarea" :rows="4" placeholder="请求体 JSON 格式" />
      </el-form-item>
      <el-form-item label="响应体">
        <el-input v-model="form.responseBody" type="textarea" :rows="4" placeholder="响应体 JSON 格式" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
          <el-option label="已完成" :value="1" />
          <el-option label="开发中" :value="2" />
          <el-option label="已废弃" :value="3" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { apiEndpointApi } from '../api'

const props = defineProps({
  modelValue: Boolean,
  endpoint: Object,
  projectId: {
    type: Number,
    required: true
  },
  groupId: {
    type: Number,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const formRef = ref(null)
const submitting = ref(false)

const form = ref({
  method: 'GET',
  path: '',
  summary: '',
  description: '',
  requestHeaders: '',
  requestParams: '',
  requestBody: '',
  responseBody: '',
  status: 1
})

const rules = {
  method: [{ required: true, message: '请选择请求方法', trigger: 'change' }],
  path: [{ required: true, message: '请输入接口路径', trigger: 'blur' }],
  summary: [{ required: true, message: '请输入接口摘要', trigger: 'blur' }]
}

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const isEdit = computed(() => !!props.endpoint?.id)

watch(() => props.endpoint, (newVal) => {
  if (newVal) {
    form.value = {
      id: newVal.id,
      method: newVal.method || 'GET',
      path: newVal.path || '',
      summary: newVal.summary || '',
      description: newVal.description || '',
      requestHeaders: newVal.requestHeaders || '',
      requestParams: newVal.requestParams || '',
      requestBody: newVal.requestBody || '',
      responseBody: newVal.responseBody || '',
      status: newVal.status || 1
    }
  } else {
    form.value = {
      method: 'GET',
      path: '',
      summary: '',
      description: '',
      requestHeaders: '',
      requestParams: '',
      requestBody: '',
      responseBody: '',
      status: 1
    }
  }
}, { immediate: true })

const handleClose = () => {
  visible.value = false
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    const data = {
      ...form.value,
      projectId: props.projectId,
      groupId: props.groupId
    }

    if (isEdit.value) {
      await apiEndpointApi.update(data.id, data)
      ElMessage.success('更新成功')
    } else {
      await apiEndpointApi.create(data)
      ElMessage.success('创建成功')
    }

    emit('success')
    handleClose()
  } catch (error) {
    console.error('Submit failed:', error)
  } finally {
    submitting.value = false
  }
}
</script>
